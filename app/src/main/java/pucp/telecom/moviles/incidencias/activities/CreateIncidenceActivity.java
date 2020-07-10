package pucp.telecom.moviles.incidencias.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.UUID;

import pucp.telecom.moviles.incidencias.R;
import pucp.telecom.moviles.incidencias.entities.Incidence;

public class CreateIncidenceActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_incidence);

        // Variable con conexión a rama raíz (proyecto-grupal-apps/)
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    // Crear nueva incidencia
    public void createIncidence(View view){
        Incidence incidence = new Incidence();

        /*
        proyecto-grupal-apps/
            - userID1/
                - userinfo/ (mail, password, role, name, code)
                - incidences/
                    - incidenceID1/ (incidenceId, incidenceName, description, status, image, date, comment, location)
                    - incidenceID2/
                    - incidenceID3/
            - userID2/
                - userinfo/
                - incidences/
                    - incidenceID4/
            - userID3/
                - userinfo/
                - incidences/
            - userID4/
                - userinfo/
                - incidences/
                    - incidenceID5/
                    - incidenceID6/
                    - incidenceID7/
                    - incidenceID8/
         */

        // Se deberá cambiar por el Id pasado por Auth (id del usuario logueado)
        String userid = "userid2";

        // Configuración de parámetros de la Incidencia
        incidence.setIncidenceName(((EditText) findViewById(R.id.editTextIncidenceName)).getText().toString());
        incidence.setDescription(((EditText) findViewById(R.id.editTextIncidenceDescription)).getText().toString());
        incidence.setImage("image1");

        // Configurar Fecha
        Calendar calendar = Calendar.getInstance();
        incidence.setDate(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR));

        incidence.setComment("Se encontró que el foco estaba quemado por tiempo de uso");
        incidence.setLocation("xyz o latitud+longitud");

        // Guardar incidencia en DB
        DatabaseReference path = databaseReference.child(userid + "/incidences/").push(); // configurar la ruta para imprimir en DB
        String incidenceId = path.getKey(); // obtenemos el instanceId del push
        incidence.setIncidenceId(incidenceId);
        path.setValue(incidence)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("incidenceSaveSuccess","Guardado de incidencia exitoso"); // si se guardó exitosamente
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("incidenceSaveFail","Guardado de incidencia fallido",e.getCause()); // si hubo error al guardar
                }
            });
        // Regresar a ListIncidencesActivity
        intentListIncidences();
    }

    // Volver al Activity que abrió esta Activity mediante un Intent
    public void intentListIncidences(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}