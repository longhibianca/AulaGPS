package br.edu.iff.pooa20181.aulagps.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import br.edu.iff.pooa20181.aulagps.R;

public class UltimaLocalizacao2Activity extends AppCompatActivity {

    private TextView latitude;
    private TextView longitude;
    private TextView altitude;
    private TextView accuracy;
    private TextView speed;
    private TextView sensorType;
    private TextView updatesOnOff;
    private ToggleButton switchGpsBalanced;
    private ToggleButton locationOnOff;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean updatesOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultima_localizacao2);

        //initialuse View
        latitude = (TextView) findViewById(R.id.tvLatitude);
        longitude = (TextView) findViewById(R.id.tvLongitude);
        altitude = (TextView) findViewById(R.id.tvAltitude);
        accuracy = (TextView) findViewById(R.id.tvAccuracy);
        speed = (TextView) findViewById(R.id.tvSpeed);
        sensorType = (TextView) findViewById(R.id.tvSensor);
        updatesOnOff = (TextView) findViewById(R.id.tvUpdates);
        switchGpsBalanced = (ToggleButton) findViewById(R.id.tbGps_Balanced);
        locationOnOff = (ToggleButton) findViewById(R.id.tvLocationOnOff);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(7500); //use a value fo about 10 to 15s for a real app
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        switchGpsBalanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchGpsBalanced.isChecked()) {
                    //using GPS only
                    sensorType.setText("GPS");
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                } else {
                    //using balanced power accuracy
                    sensorType.setText("Cell Tower and WiFi");
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                }

            }
        });

        locationOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationOnOff.isChecked()) {
                    //location updates on
                    updatesOnOff.setText("On");
                    updatesOn = true;
                    startLocationUpdates();
                } else {
                    //location updates off
                    updatesOnOff.setText("Off");
                    updatesOn = false;
                    stopLocationUpdates();
                }

            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitude.setText(String.valueOf(location.getLatitude()));
                        longitude.setText(String.valueOf(location.getLongitude()));
                        accuracy.setText(String.valueOf(location.getAccuracy()));
                        if (location.hasAltitude()) {
                            altitude.setText(String.valueOf(location.getAltitude()));
                        } else {
                            altitude.setText("No altitude available");
                        }
                        if (location.hasSpeed()) {
                            speed.setText(String.valueOf(location.getSpeed()) + "m/s");
                        } else {
                            speed.setText("No speed available");
                        }

                    }
                }
            });
        } else {
            // request permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
        }

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    //Update UI with location data
                    if (location != null) {
                        latitude.setText(String.valueOf(location.getLatitude()));
                        longitude.setText(String.valueOf(location.getLongitude()));
                        accuracy.setText(String.valueOf(location.getAccuracy()));
                        if (location.hasAltitude()) {
                            altitude.setText(String.valueOf(location.getAltitude()));
                        } else {
                            altitude.setText("No altitude available");
                        }
                        if (location.hasSpeed()) {
                            speed.setText(String.valueOf(location.getSpeed()) + "m/s");
                        } else {
                            speed.setText("No speed available");
                        }

                    }
                }
            }
        };

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted do nothing and carry on

                } else {
                    Toast.makeText(getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_SHORT).show();
                    finish();
                }

                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (updatesOn) startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}