package pucp.telecom.moviles.incidencias.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.UUID;

import pucp.telecom.moviles.incidencias.R;
import pucp.telecom.moviles.incidencias.entities.Incidence;

public class CreateIncidenceActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    //Declaramos lo necesario para usar storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();





    //Definimos lo necesario para abrir la galería con el botón abrir  galería
    ImageView imageView;
    Button button;
    private static final int PICK_IMAGE = 100;
    private static final int MAPS_CODE=5;
    Uri imageUri;
    Button buttonMaps;
    String latitud;
    String longitud;



    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_incidence);
        Intent intent = getIntent(); // Get serializable intent data
        userId = (String) intent.getSerializableExtra("userId");
        // Variable con conexión a rama raíz (proyecto-grupal-apps/)
        databaseReference = FirebaseDatabase.getInstance().getReference();


        imageView = (ImageView) findViewById(R.id.imageViewAttachPhoto);
        button = (Button) findViewById(R.id.buttonGallery);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        //Definimos setOnClickListerner para el btn que abrirá maps
        buttonMaps = (Button)findViewById(R.id.buttonMaps);
        buttonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMaps = new Intent(CreateIncidenceActivity.this,MapsActivity.class);

                startActivityForResult(intentMaps,MAPS_CODE);

            }
        });

    }


    //FUNCION PARA GENERAR EL INTENT Y ABRIR LA GALERIA
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        } else if (resultCode==RESULT_OK && requestCode==MAPS_CODE){
            latitud = data.getStringExtra("latitud");
            longitud = data.getStringExtra("longitud");
        }

    }



    // Crear nueva incidencia
    public void createIncidence(View view){
        Incidence incidence = new Incidence();

        // Configuración de parámetros de la Incidencia
        incidence.setIncidenceName(((EditText) findViewById(R.id.editTextIncidenceName)).getText().toString());
        incidence.setDescription(((EditText) findViewById(R.id.editTextIncidenceDescription)).getText().toString());
        // Configurar Fecha
        Calendar calendar = Calendar.getInstance();
        incidence.setDate(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR));
        incidence.setStatus("registrado"); // Las incidencias creadas siempre inician con en el estado "registrado"
        incidence.setComment(""); // Al crear una incidencia este parámetro está vacío
        incidence.setUserId(userId);

        // PARAMETROS QUE FALTAN CONFIGURAR
        //incidence.setImage("image1");   //Esto se setea líneas más abajo.
        if (longitud != null && latitud != null) {
            incidence.setLocation("xyz o latitud+longitud");


            // Guardar incidencia en DB
            DatabaseReference path = databaseReference.child(userId + "/incidences/").push(); // configurar la ruta para imprimir en DB
            String incidenceId = path.getKey(); // obtenemos el instanceId del push
            incidence.setIncidenceId(incidenceId);
            path.setValue(incidence)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("incidenceSaveSuccess", "Guardado de incidencia exitoso"); // si se guardó exitosamente
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("incidenceSaveFail", "Guardado de incidencia fallido", e.getCause()); // si hubo error al guardar
                        }
                    });

            // Usaremos putBytes para subir nuestra imagen.
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmapImage = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();


            StorageReference imageIncidence = storageReference.child("incidences/" + incidenceId);
            StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("userId", userId).build();

            UploadTask uploadTask = imageIncidence.putBytes(data, metadata);

            // PENDIENTE AGREGAR PROGRESO DE SUBIDA!!!! OJOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("infoApp", "subido exitoso");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("infoApp", "subido erróneoooooooooooooooooooooo");
                }
            });


            // Regresar a ListIncidencesActivity
            intentListIncidences();
        } else {
            Toast.makeText(this, "Debe ingresar una ubicación", Toast.LENGTH_SHORT).show();
        }


    }

    // Volver al Activity que abrió esta Activity mediante un Intent
    public void intentListIncidences(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}