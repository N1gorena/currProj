package com.guango.society;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Stack;

public class PlayActivity extends Activity {
    private Character player;
    private final int LOCATION_REQUEST_CODE = 962;
    private boolean PLAY = false;
    private GWindow gWindow;
    private WorldEngine renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Intent characterGetter = getIntent();
        Bundle character = characterGetter.getExtras();
        player = character.getParcelable("CharacterObject");
        EstablishLocationListener myLocationListener = new EstablishLocationListener(this);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
            player.setLocationListener(myLocationListener);
        }

        gWindow = (GWindow)findViewById(R.id.GraphicsView);
        gWindow.setEGLContextClientVersion(3);

        RenderTask testTask = new RenderTask(this);
        testTask.execute("map.osm");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    EstablishLocationListener myLocationListener = new EstablishLocationListener(this);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
                    player.setLocationListener(myLocationListener);
                }
                else{
                }
            }
            return;
        }
    }

    public void characterListo() {
        PLAY = true;
        //Pair<Double,Double> location = player.getLocation();
        //Log.d("TEST",""+location.first+location.second);
    }

    public void renderableDataPass(ArrayList<GXMLObject> result) {
        renderer = new WorldEngine(result);
        gWindow.setRenderer(renderer);
    }
}
