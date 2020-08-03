package pucp.telecom.moviles.incidencias.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pucp.telecom.moviles.incidencias.R;
import pucp.telecom.moviles.incidencias.adapters.ListIncidencesAdapter;
import pucp.telecom.moviles.incidencias.entities.Incidence;

public class ListIncidencesActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    private MenuItem item;
    ArrayList<Incidence> incidences = new ArrayList<>();
    private RecyclerView recyclerViewIncidences; // RecyclerView
    private ListIncidencesAdapter listIncidencesAdapter; // Adapter
    private String userId;
    private String rol;

    int LAUNCH_CREATE_INCIDENCE_ACTIVITY = 1;
    int LAUNCH_VIEW_INCIDENCE_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_incidences);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent(); // Get serializable intent data
        userId = (String) intent.getSerializableExtra("userId");
        rol = (String) intent.getSerializableExtra("rol");
        Log.d("msgxd", userId + rol);
        Log.d("msgxd", "rol!!!!" + rol);
        if (rol.equalsIgnoreCase("A")) {
            // Obtener lista completa de todas las incidencias.
            incidenceValueEventListenerAdmin();
        } else if (rol.equalsIgnoreCase("U")) {
            incidenceValueEventListener(userId); // Obtener lista completa de incidencias por usuario
        }
        // incidenceChildEventListener(userId); // Obtener solo incidencias modificadas/creadas por usuario
        // buildIncidenceRecyclerView();
    }

    // Escucha cambios en toda la rama
    public void incidenceValueEventListenerAdmin() {
        databaseReference.getRoot().addValueEventListener(new ValueEventListener() { //
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // cada vez que hay un cambio en Firebase
                if (dataSnapshot.getValue() != null) {
                    // dataSnapshot contiene el json (equivalente a gson.fromJson)
                    //Log.d("msgxd", dataSnapshot.getValue().toString());
                    Log.d("msgxd", "estoyacanagana");
                    incidences.clear();
                    // Iterar por todas las incidencias del JSON
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        // Log.d("msgxd", "estoyacanagana");
                        DataSnapshot incidencesRef = postSnapshot.getChildren().iterator().next();
                        for (DataSnapshot postUserSnapshot : incidencesRef.getChildren()) {
                            //  Log.d("msgxd", "acas2333illega");

                            Incidence incidence = postUserSnapshot.getValue(Incidence.class);
                            //      Log.d("msgxd", incidence.getIncidenceName()); // imprimir desde el snapshot directamente
                            //    Log.d("msgxd", postSnapshot.getKey()); // imprimir llaves de los elementos

                            incidences.add(incidence);  // agregar todas las incidencias a un arreglo
                            // Log.d("msgxd", incidences.get(incidences.indexOf(incidence)).getIncidenceName()); // imprimir desde un List
                        }
                    }
                    Log.d("msgxd", "aebcede");
                    Log.d("msgxd", incidences.toString());
                    Log.d("msgxd", String.valueOf(incidences.size()));
                    buildIncidenceRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { // si hay un error al obtener la información en Firebase

            }
        });
    }


    // Escucha por cambios en toda la rama
    public void incidenceValueEventListener(String userId) {
        databaseReference.child(userId + "/incidences/").addValueEventListener(new ValueEventListener() { // Se deberá cambiar por el Id pasado por Auth (id del usuario logueado)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // cada vez que hay un cambio en Firebase
                // dataSnapshot contiene el json (equivalente a gson.fromJson)
                if (dataSnapshot.getValue() != null) {
                    Log.d("msgxd", dataSnapshot.getValue().toString());

                    incidences.clear();
                    // Iterar por todas las incidencias del JSON
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Incidence incidence = postSnapshot.getValue(Incidence.class);
                        Log.d("msgxd", incidence.getIncidenceName()); // imprimir desde el snapshot directamente
                        Log.d("msgxd", postSnapshot.getKey()); // imprimir llaves de los elementos

                        incidences.add(incidence);  // agregar todas las incidencias a un arreglo
                        Log.d("incidenceNamesFromArray", incidences.get(incidences.indexOf(incidence)).getIncidenceName()); // imprimir desde un List
                    }
                    buildIncidenceRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { // si hay un error al obtener la información en Firebase

            }
        });
    }

    // Escucha por cambios solo en los hijos
    public void incidenceChildEventListener(String userId) {
        databaseReference.child(userId + "/incidences/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Incidence incidence = dataSnapshot.getValue(Incidence.class);
                Log.d("incidenceAdded", incidence.getIncidenceName());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Incidence incidence = dataSnapshot.getValue(Incidence.class);
                Log.d("incidenceChanged", incidence.getIncidenceName());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void buildIncidenceRecyclerView() {

        Log.d("msgxd", "acasillega");
        listIncidencesAdapter = new ListIncidencesAdapter(incidences, ListIncidencesActivity.this);
        recyclerViewIncidences = findViewById(R.id.recyclerViewIncidences);
        recyclerViewIncidences.setAdapter(listIncidencesAdapter);
        recyclerViewIncidences.setLayoutManager(new LinearLayoutManager(ListIncidencesActivity.this));

        listIncidencesAdapter.setOnItemClickListener(new ListIncidencesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Incidence incidenceSelected = incidences.get(position);
                String incidenceIdSelected = incidenceSelected.getIncidenceId();
                String incidenceNameSelected = incidenceSelected.getIncidenceName();
                String incidenceDescriptionSelected = incidenceSelected.getDescription();
                String incidenceStatusSelected = incidenceSelected.getStatus();
                String incidenceCommentSelected = incidenceSelected.getComment();
                String incidenceOwnerSelected = incidenceSelected.getUserId();
                String incidenceLocation = incidenceSelected.getLocation();

                Intent intent = new Intent(ListIncidencesActivity.this, ViewIncidenceActivity.class);
                intent.putExtra("incidenceIdSelected", incidenceIdSelected);
                intent.putExtra("incidenceNameSelected", incidenceNameSelected);
                intent.putExtra("incidenceDescriptionSelected", incidenceDescriptionSelected);
                intent.putExtra("incidenceStatusSelected", incidenceStatusSelected);
                intent.putExtra("incidenceCommentSelected", incidenceCommentSelected);
                intent.putExtra("incidenceLocation",incidenceLocation);

                intent.putExtra("rol", rol);
                intent.putExtra("incidenceOwnerSelected", incidenceOwnerSelected);

                startActivityForResult(intent, LAUNCH_VIEW_INCIDENCE_ACTIVITY);
            }
        });
    }

    // Inflar appbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu);
        return true;
    }

    // Al hacer clic en el botón '+' de appbar abrir CreateIncidenceActivity
    public void actionAddIncAppBar(MenuItem item) {
        Intent i = new Intent(this, CreateIncidenceActivity.class);
        i.putExtra("userId", userId);
        // i.putExtra("loggedusername",nombre); // extra del nombre del usuario logueado
        startActivityForResult(i, LAUNCH_CREATE_INCIDENCE_ACTIVITY);
    }

    // Appbar: logout
    public void actionLogoutAppBar(MenuItem item) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    // Al regresar del Activity CreateIncidenceActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_CREATE_INCIDENCE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                if (rol.equalsIgnoreCase("U")) {

                    incidenceValueEventListener(userId);
                } else {

                    incidenceValueEventListenerAdmin();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Toast.makeText(this, "onActivityResult RESULT_CANCELED", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == LAUNCH_VIEW_INCIDENCE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                if (rol.equalsIgnoreCase("U")) {

                    incidenceValueEventListener(userId);
                } else {

                    incidenceValueEventListenerAdmin();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Toast.makeText(this, "onActivityResult RESULT_CANCELED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}