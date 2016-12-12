package com.izv.dam.newquip;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.izv.dam.newquip.pojo.Mapas;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private double longitud;
    private double latitud;
    private ArrayList<Mapas> mapas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();



    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void init() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        Bundle b = getIntent().getExtras();
        mapas = b.getParcelableArrayList("mapas");
    }

    //Metodos de las interfaces
    // OnMapReadyCallback
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        for ( Mapas m : mapas ) {

            LatLng punto = new LatLng(m.getLatitud(), m.getLongitud());
            mMap.addMarker(new MarkerOptions().position(punto).title("Punto"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(Granada));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punto, 18));

        }


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "Conectado", Toast.LENGTH_SHORT).show();

         if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation =  LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Toast.makeText(this,mLastLocation.getLatitude()+ " " + mLastLocation.getLongitude() , Toast.LENGTH_SHORT).show();
            latitud=mLastLocation.getLatitude();
            longitud=mLastLocation.getLongitude();
        }else{
            Toast.makeText(this, "sin datos", Toast.LENGTH_SHORT).show();
        }
        LocationRequest mLocationRequest = new LocationRequest();
        //mLocationRequest.setInterval(10000);
        //mLocationRequest.setFastestInterval(5000);
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY); //es el mas recmendado


        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onLocationChanged(Location location) {
        /*LatLng punto = new LatLng(location.getLatitude(), location.getLongitude()); //objeto que guarda la latitud y longitud.
        mMap.addMarker(new MarkerOptions().position(punto).title("Granada, España"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Granada));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punto, 7));*/

    }


}
