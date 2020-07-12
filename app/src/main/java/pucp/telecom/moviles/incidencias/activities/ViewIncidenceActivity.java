package pucp.telecom.moviles.incidencias.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import pucp.telecom.moviles.incidencias.R;
import pucp.telecom.moviles.incidencias.entities.Incidence;

public class ViewIncidenceActivity extends AppCompatActivity {

    String incidenceNameSelected, incidenceDescriptionSelected, incidenceIdSelected, incidenceStatusSelected, incidenceCommentSelected;
    DatabaseReference databaseReference;
    EditText editTextIncidenceComment;
    Switch switchAttendIncidence;
    String incidenceStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_incidence);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            // Obtener el extra enviado del Activity que ha abierto esta actividad (ListChildElementsActivity, CreateFolderActivity y CreateBookmarkActivity)
            incidenceIdSelected = extras.getString("incidenceIdSelected");
            incidenceNameSelected = extras.getString("incidenceNameSelected");
            incidenceDescriptionSelected = extras.getString("incidenceDescriptionSelected");
            incidenceStatusSelected = extras.getString("incidenceStatusSelected");
            incidenceCommentSelected = extras.getString("incidenceCommentSelected");
        }

        setIncidenceValues(); // Configurar valores pasados de ListIncidencesActivity
        switchStatusIncidence(); // Switch para cambiar estado de incidencia entre "registrado" y "atendido"
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    // Configurar valores pasados de ListIncidencesActivity
    public void setIncidenceValues(){
        TextView textViewIncidenceName = findViewById(R.id.textViewIncidenceName);
        TextView textViewIncidenceDescription = findViewById(R.id.textViewIncidenceDescription);
        editTextIncidenceComment = findViewById(R.id.editTextIncidenceComment);

        textViewIncidenceName.setText(incidenceNameSelected);
        textViewIncidenceDescription.setText(incidenceDescriptionSelected);

        // Configurar estado inicial de la incidencia
        switchAttendIncidence = (Switch) findViewById(R.id.switchAttendIncidence);
        if(incidenceStatusSelected.equals("atendido")){
            switchAttendIncidence.setChecked(true);
            editTextIncidenceComment.setEnabled(true);
            editTextIncidenceComment.setText(incidenceCommentSelected);
        }else{
            switchAttendIncidence.setChecked(false);
            editTextIncidenceComment.setEnabled(false);
        }
    }

    // Switch para cambiar estado de incidencia entre "registrado" y "atendido"
    public void switchStatusIncidence(){
        switchAttendIncidence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editTextIncidenceComment.setEnabled(true);
                    editTextIncidenceComment.setHint("Ingresar comentario");
                    incidenceStatus = "atendido";
                    Toast.makeText(ViewIncidenceActivity.this, "isChecked", Toast.LENGTH_SHORT).show();
                }else{
                    editTextIncidenceComment.setEnabled(false);
                    editTextIncidenceComment.setHint("Atender incidencia para comentar");
                    incidenceStatus = "registrado";
                    Toast.makeText(ViewIncidenceActivity.this, "isChecked false", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Atender incidencia
    public void attendIncidence(View view){
        // Configuración de parámetros de la Incidencia
        String incidenceComment;

        if (incidenceStatus.equals("atendido")){ // ERROR DE INICIALIZACIÓN DEL ESTADO DEL SWITCH
            incidenceComment = editTextIncidenceComment.getText().toString();
        } else {
            incidenceComment = "";
        }

        String userid = "userid2";

        // Configurar comentario ingresado por el personal de infra
        databaseReference.child(userid + "/incidences/" + incidenceIdSelected + "/comment").setValue(incidenceComment);
        databaseReference.child(userid + "/incidences/" + incidenceIdSelected + "/status").setValue(incidenceStatus);

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