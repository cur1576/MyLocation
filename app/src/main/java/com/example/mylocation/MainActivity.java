package com.example.mylocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

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

    }
}
