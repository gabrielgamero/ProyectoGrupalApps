package pucp.telecom.moviles.incidencias.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pucp.telecom.moviles.incidencias.R;
import pucp.telecom.moviles.incidencias.entities.Incidence;

public class CreateIncidenceActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_incidence);

        // Variable con conexión a rama raíz
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    // Crear nueva incidencia
    public void createIncidence(View view) {
        Incidence incidence = new Incidence();

        incidence.setIncidenceId("1");
        incidence.setIncidenceName(((EditText) findViewById(R.id.editTextIncidenceName)).getText().toString());
        incidence.setDescription(((EditText) findViewById(R.id.editTextIncidenceDescription)).getText().toString());
        incidence.setImage("image1");
        incidence.setDate("27/05/2020");
        incidence.setComment("Se encontró que el foco estaba quemada por tiempo de uso");
        incidence.setLocation("xyz o latitud+longitud");
        incidence.setUserId("abcde01");

        // Guardar incidencia en DB
        databaseReference.setValue(incidence)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // si se guardó exitosamente
                        Log.d("incidenceSaveSuccess", "Guardado de incidencia exitoso");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // si hubo error al guardar
                        Log.e("incidenceSaveFail", "Guardado de incidencia fallido", e.getCause());
                    }
                });
    }
}