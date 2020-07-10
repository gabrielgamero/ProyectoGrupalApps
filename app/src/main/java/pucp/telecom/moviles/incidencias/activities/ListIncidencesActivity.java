package pucp.telecom.moviles.incidencias.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
    ArrayList<Incidence> incidences = new ArrayList<>();
    private RecyclerView recyclerViewIncidences; // RecyclerView
    private ListIncidencesAdapter listIncidencesAdapter; // Adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_incidences);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        incidenceValueEventListener(); // Obtener lista completa de incidencias por usuario
        // incidenceChildEventListener(); // Obtener solo incidencias modificadas/creadas por usuario
        // buildIncidenceRecyclerView();
    }

    // Escucha por cambios en toda la rama
    public void incidenceValueEventListener(){
        databaseReference.child("userid1" + "/incidences/").addValueEventListener(new ValueEventListener() { // Se deberá cambiar por el Id pasado por Auth (id del usuario logueado)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // cada vez que hay un cambio en Firebase
                // dataSnapshot contiene el json (equivalente a gson.fromJson)
                Log.d("dataSnapshotJson",dataSnapshot.getValue().toString());

                // Iterar por todas las incidencias del JSON
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Incidence incidence = postSnapshot.getValue(Incidence.class);
                    Log.d("incidenceNamesFromSnap",incidence.getIncidenceName()); // imprimir desde el snapshot directamente
                    Log.d("incidenceKeys",postSnapshot.getKey()); // imprimir llaves de los elementos

                    incidences.add(incidence);  // agregar todas las incidencias a un arreglo
                    Log.d("incidenceNamesFromArray",incidences.get(incidences.indexOf(incidence)).getIncidenceName()); // imprimir desde un List
                }
                buildIncidenceRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { // si hay un error al obtener la información en Firebase

            }
        });
    }

    // Escucha por cambios solo en los hijos
    public void incidenceChildEventListener(){
        databaseReference.child("abcde01" + "/incidences/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Incidence incidence = dataSnapshot.getValue(Incidence.class);
                Log.d("incidenceAdded",incidence.getIncidenceName());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Incidence incidence = dataSnapshot.getValue(Incidence.class);
                Log.d("incidenceChanged",incidence.getIncidenceName());
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

    public void buildIncidenceRecyclerView(){
        listIncidencesAdapter = new ListIncidencesAdapter(incidences, ListIncidencesActivity.this);
        recyclerViewIncidences = findViewById(R.id.recyclerViewIncidences);
        recyclerViewIncidences.setAdapter(listIncidencesAdapter);
        recyclerViewIncidences.setLayoutManager(new LinearLayoutManager(ListIncidencesActivity.this));

        listIncidencesAdapter.setOnItemClickListener(new ListIncidencesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(ListIncidencesActivity.this, "to DetailIncidenceActivity", Toast.LENGTH_SHORT).show();
            }
        });
    }
}