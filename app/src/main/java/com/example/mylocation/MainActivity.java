package com.example.mylocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_LOCATION = 123;
    private TextView tv;
    private LocationManager manager;
    private LocationListener listener;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_PERMISSION_LOCATION);
                    }else{
                        doIt();
                    }
                }else{
                    doIt();
                }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_PERMISSION_LOCATION && grantResults.length>0 &&
                grantResults[0]== PackageManager.PERMISSION_GRANTED){
            doIt();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        manager.requestLocationUpdates(provider,10000,0.5F,listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.removeUpdates(listener);
    }

    private void doIt(){
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        List<String> providers = manager.getAllProviders();
        for(String name : providers){
            tv.append("Name: " + name + " isEnabled: " + manager.isProviderEnabled(name) + "\n");
            LocationProvider locationProvider = manager.getProvider(name);
            tv.append("requires Cell: " + locationProvider.requiresCell() + "\n");
            tv.append("requires Network: " + locationProvider.requiresNetwork() + "\n");
            tv.append("requires Satellite: " + locationProvider.requiresSatellite() + "\n");
        }
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        provider = manager.getBestProvider(criteria,true);
        tv.append("Verwendet: " + provider + "\n");

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                tv.append("Neuer Standort: ");
                if(location!=null){
                    tv.append("Breite: " + location.getLatitude() + " Länge: " + location.getLongitude() + "\n");
                    Geocoder gc = new Geocoder(MainActivity.this);
                    if(gc.isPresent()){
                        try {
                           List<Address> adr = gc.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            Address address = adr.get(0);
                            tv.append(address.getAddressLine(0));
                        } catch (IOException e) {
                            Log.e("Error", "", e);
                        }
                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

    }
}
