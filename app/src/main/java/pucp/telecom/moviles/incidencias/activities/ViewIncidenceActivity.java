package pucp.telecom.moviles.incidencias.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import pucp.telecom.moviles.incidencias.R;

public class ViewIncidenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_incidence);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            // Obtener el extra enviado del Activity que ha abierto esta actividad (ListChildElementsActivity, CreateFolderActivity y CreateBookmarkActivity)
            String incidenceIdSelected = extras.getString("incidenceIdSelected");
            String incidenceDescriptionSelected = extras.getString("incidenceDescriptionSelected");

        }


    }
}