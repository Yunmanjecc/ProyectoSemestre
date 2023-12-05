package twin.developers.projectmqtt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class ubicacion extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    GoogleMap gMap;
    Button volver, ubicacion;
    private static final int PERMISSION_REQUEST_LOCATION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        // Se inicializan los botones y mapa
        volver = findViewById(R.id.bttnVolver3);
        ubicacion = findViewById(R.id.bttnUbicacion);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Configuración del mapa una vez esté listo
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                gMap = map;

                // Se configura el botón de ubicación
                ubicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Verificar si se tienen permisos de ubicación
                        if (ActivityCompat.checkSelfPermission(ubicacion.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(ubicacion.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // Solicitar permisos si no se tienen
                            ActivityCompat.requestPermissions(ubicacion.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                            return;
                        }
                        // Habilitar la capa de ubicación en el mapa
                        gMap.setMyLocationEnabled(true);

                        // Obtener la ubicación actual y centrar el mapa en ella
                        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(ubicacion.this);
                        fusedLocationClient.getLastLocation().addOnSuccessListener(ubicacion.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
                                }
                            }
                        });
                    }
                });
            }
        });

        // Se configura el botón de volver
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), MainActivity.class
                );
                startActivity(intent);
            }
        });
    }

    // Manejo de la respuesta de permisos de ubicación
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Vuelve a llamar al método onClick del botón ubicación
                ubicacion.performClick();
            } else {
                // Muestra un mensaje o realiza alguna acción alternativa
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}