package pucp.telecom.moviles.incidencias.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

import pucp.telecom.moviles.incidencias.R;
import pucp.telecom.moviles.incidencias.entities.Incidence;

public class ViewIncidenceActivity extends AppCompatActivity {

    String incidenceNameSelected, incidenceDescriptionSelected, incidenceIdSelected, incidenceStatusSelected, incidenceCommentSelected;
    DatabaseReference databaseReference;
    EditText editTextIncidenceComment;
    Switch switchAttendIncidence;
    String incidenceStatus;
    String incidenceLocation;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();


    String incidenceOwnerSelected;




    private static final int MAPS_VIEW_INCIDENCE = 10;
    Button abrirMaps;
    String latitud;
    String longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_incidence);

        String rol;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Obtener el extra enviado del Activity que ha abierto esta actividad (ListChildElementsActivity, CreateFolderActivity y CreateBookmarkActivity)
            incidenceIdSelected = extras.getString("incidenceIdSelected");
            incidenceNameSelected = extras.getString("incidenceNameSelected");
            incidenceDescriptionSelected = extras.getString("incidenceDescriptionSelected");
            incidenceStatusSelected = extras.getString("incidenceStatusSelected");
            incidenceCommentSelected = extras.getString("incidenceCommentSelected");
            incidenceLocation = extras.getString("incidenceLocation");

            String[]ubicacion= incidenceLocation.split(",");
            latitud = ubicacion[0];
            longitud = ubicacion[1];

            rol = extras.getString("rol");
            incidenceOwnerSelected = extras.getString("incidenceOwnerSelected");
            if (rol.equalsIgnoreCase("U")) {
                //textViewTitleIncidenceComment
                //editTextIncidenceComment
                //switchAttendIncidence
                //buttonSolveIncidence
                findViewById(R.id.textViewTitleIncidenceComment).setVisibility(View.GONE);
                findViewById(R.id.editTextIncidenceComment).setVisibility(View.GONE);
                findViewById(R.id.switchAttendIncidence).setVisibility(View.GONE);
                findViewById(R.id.buttonSolveIncidence).setVisibility(View.GONE);
            }
        }

        setIncidenceValues(); // Configurar valores pasados de ListIncidencesActivity
        switchStatusIncidence(); // Switch para cambiar estado de incidencia entre "registrado" y "atendido"
        databaseReference = FirebaseDatabase.getInstance().getReference();


        //Se abrirá el mapa con la ubicación de la incidencia como marker y la ubicación que se actualiza automáticamente
        abrirMaps = (Button)findViewById(R.id.abrirMaps);
        abrirMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMapaView = new Intent (ViewIncidenceActivity.this,MapitaActivity.class);
                intentMapaView.putExtra("latitud",latitud);
                intentMapaView.putExtra("longitud",longitud);

                startActivityForResult(intentMapaView,MAPS_VIEW_INCIDENCE);
            }
        });

    }

    // Configurar valores pasados de ListIncidencesActivity
    public void setIncidenceValues() {
        TextView textViewIncidenceName = findViewById(R.id.textViewIncidenceName);
        TextView textViewIncidenceDescription = findViewById(R.id.textViewIncidenceDescription);
        editTextIncidenceComment = findViewById(R.id.editTextIncidenceComment);
        ImageView imageView = findViewById(R.id.imageViewCheckPhoto);

        textViewIncidenceName.setText(incidenceNameSelected);
        textViewIncidenceDescription.setText(incidenceDescriptionSelected);

        //Aplicamos glide para cargar las imagen de la incidencia en memoria.
        Log.d("verificar",incidenceIdSelected);
        StorageReference imageRef = storageRef.child("incidences/"+incidenceIdSelected);
        Glide.with(this)
                .load(imageRef)
                .into(imageView);


        incidenceStatus = incidenceStatusSelected; // inicializamos el valor del estado de la incidencia según lo que se haya recibido de ListIncidencesActivity

        // Configurar estado inicial de la incidencia
        switchAttendIncidence = (Switch) findViewById(R.id.switchAttendIncidence);
        if (incidenceStatusSelected.equals("atendido")) {
            switchAttendIncidence.setChecked(true);
            editTextIncidenceComment.setEnabled(true);
            editTextIncidenceComment.setText(incidenceCommentSelected);
        } else {
            switchAttendIncidence.setChecked(false);
            editTextIncidenceComment.setEnabled(false);
        }
    }

    // Switch para cambiar estado de incidencia entre "registrado" y "atendido"
    public void switchStatusIncidence() {
        switchAttendIncidence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editTextIncidenceComment.setEnabled(true);
                    editTextIncidenceComment.setHint("Ingresar comentario");
                    incidenceStatus = "atendido";
                    Toast.makeText(ViewIncidenceActivity.this, "isChecked", Toast.LENGTH_SHORT).show();
                } else {
                    editTextIncidenceComment.setEnabled(false);
                    editTextIncidenceComment.setHint("Atender incidencia para comentar");
                    incidenceStatus = "registrado";
                    Toast.makeText(ViewIncidenceActivity.this, "isChecked false", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Atender incidencia
    public void attendIncidence(View view) {
        // Configuración de parámetros de la Incidencia
        String incidenceComment;

        if (incidenceStatus.equals("atendido")) { // ERROR DE INICIALIZACIÓN DEL ESTADO DEL SWITCH
            incidenceComment = editTextIncidenceComment.getText().toString();
        } else {
            incidenceComment = "";
        }

        // Configurar comentario ingresado por el personal de infra
        databaseReference.child(incidenceOwnerSelected + "/incidences/" + incidenceIdSelected + "/comment").setValue(incidenceComment);
        databaseReference.child(incidenceOwnerSelected + "/incidences/" + incidenceIdSelected + "/status").setValue(incidenceStatus);

        // Regresar a ListIncidencesActivity
        intentListIncidences();
    }

    // Volver al Activity que abrió esta Activity mediante un Intent
    public void intentListIncidences() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == MAPS_VIEW_INCIDENCE){

        }

    }

}