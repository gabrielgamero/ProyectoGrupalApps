package pucp.telecom.moviles.incidencias.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import pucp.telecom.moviles.incidencias.R;

public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMarkerDragListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker markerUbicacion;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        button = findViewById(R.id.buttonSendMarker);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng pucp = new LatLng(-12.0696, -77.079335);
        //mMap.addMarker(new MarkerOptions().position(pucp).title("PUCP").snippet("Esta será la ubicación de tu incidencia").draggable(true));

        LatLng ubicacion = new LatLng(-12.0696, -77.079335);
        markerUbicacion = googleMap.addMarker(new MarkerOptions().position(ubicacion).title("Esta será la ubicación de la incidencia").draggable(true));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 18));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMarkerDragListener(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String latitudMarker = Double.toString(markerUbicacion.getPosition().latitude);
                String longitudMarker = Double.toString(markerUbicacion.getPosition().longitude);

                intent.putExtra("latitud", latitudMarker);
                intent.putExtra("longitud",longitudMarker);
                setResult(RESULT_OK,intent);
                finish();

            }
        });



    }



    @Override
    public boolean onMarkerClick(Marker marker) {
         if (marker.equals(markerUbicacion)){

             String lat,lng;
             lat = Double.toString(marker.getPosition().latitude);
             lng = Double.toString(marker.getPosition().longitude);

             Toast.makeText(this,lat + " , " + lng,Toast.LENGTH_SHORT).show();

         }

        return false;
    }

    //Drag Listener
    @Override
    public void onMarkerDragStart(Marker marker) {
        if (marker.equals(markerUbicacion)){
            Toast.makeText(this,"Estás moviendo el marcador de la incidencia",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        if (marker.equals(markerUbicacion)){
            String newTitle = String.format(Locale.getDefault(),
                                            getString(R.string.marker_detail_latlng),
                                            marker.getPosition().latitude,
                                            marker.getPosition().longitude);

            setTitle(newTitle);
        }


    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (marker.equals(markerUbicacion)){
            Toast.makeText(this,"Has terminado el movimiento  del marcador de la incidencia",Toast.LENGTH_SHORT).show();
            setTitle(R.string.app_name);


        }


    }
}