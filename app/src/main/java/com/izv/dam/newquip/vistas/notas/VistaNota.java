package com.izv.dam.newquip.vistas.notas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Permission;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.izv.dam.newquip.MapsActivity;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.basedatos.AyudanteOrm;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.pojo.Mapas;
import com.izv.dam.newquip.pojo.Nota;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VistaNota extends AppCompatActivity implements ContratoNota.InterfaceVista,
                                                            GoogleApiClient.ConnectionCallbacks,
                                                            GoogleApiClient.OnConnectionFailedListener,
                                                            LocationListener{

    private EditText editTextTitulo, editTextNota;
    private Nota nota = new Nota();
    private PresentadorNota presentador;
    private Mapas map = new Mapas();
    private AyudanteOrm a = new AyudanteOrm(this);
    private GoogleApiClient mGoogleApiClient;
    private Button btMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        try {
            presentador = new PresentadorNota(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        editTextTitulo = (EditText) findViewById(R.id.etTitulo);
        editTextNota = (EditText) findViewById(R.id.etNota);
        btMap= (Button) findViewById(R.id.btmapa);

        if (savedInstanceState != null) {
            nota = savedInstanceState.getParcelable("nota");
        } else {
            Bundle b = getIntent().getExtras();
            if(b != null ) {
                nota = b.getParcelable("nota");
            }
        }
        mostrarNota(nota);

        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        btMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                List<Mapas> lista = presentador.onGetMapas(nota.getId());

                ArrayList<Mapas> listaAux = new ArrayList();
                for ( Mapas m : lista ) {

                    listaAux.add(m);
                }
                if ( lista != null ) {

                    Intent i = new Intent(VistaNota.this, MapsActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelableArrayList("mapas", listaAux);

                    i.putExtras(b);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        saveNota();
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentador.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("nota", nota);
    }

    @Override
    public void mostrarNota(Nota n) {
        editTextTitulo.setText(nota.getTitulo());
        editTextNota.setText(nota.getNota());
    }

    private void saveNota() {
        nota.setTitulo(editTextTitulo.getText().toString());
        nota.setNota(editTextNota.getText().toString());
        long r = presentador.onSaveNota(nota);
        if(r > 0 & nota.getId() == 0){
            nota.setId(r);
        }

        //Guardar localizacion
        map.setIdNota(nota.getId());
        presentador.onSaveMap(map);
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Toast.makeText(this, "Conectado", Toast.LENGTH_SHORT).show();

        iniGeoLocalizacion();
    }



    private void iniGeoLocalizacion() {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{  android.Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            map.setLatitud(mLastLocation.getLatitude());
            map.setLongitud(mLastLocation.getLongitude());
        }else{
            Toast.makeText(this, "sin datos", Toast.LENGTH_SHORT).show();
        }

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //es el mas recmendado
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        map.setLatitud(location.getLatitude());
        map.setLongitud(location.getLongitude());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if ( requestCode == 0 ) {

            if ( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {

                iniGeoLocalizacion();

            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}