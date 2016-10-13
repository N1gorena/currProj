package com.guango.society;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.HashMap;

public class OpeningActivity extends AppCompatActivity {
    private final int FIN = 100;
    //Loc
    private LocationManager locationManager;
    private EstablishLocationListener myLocationListener;
    private ProgressBar locationBar;
    //Inv
    private HashMap<String,Integer> Inventory;
    private ProgressBar inventoryBar;

    //Char
    private HashMap<String,Object> playerCharacter;
    private ProgressBar characterStatisticsBar;
    private ProgressBar characterModificationsBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        //User Info from Login.
        Intent creationIntent = getIntent();
        Bundle userLoggingInInformation = creationIntent.getExtras();
        Integer userID = userLoggingInInformation.getInt(AppLanding.UID);
        //Loc
        locationBar = (ProgressBar)findViewById(R.id.LocationProgressBar);
        locationBar.setProgress(FIN);
        //Inv
        inventoryBar = (ProgressBar)findViewById(R.id.InventoryProgressBar);
        EstablishInventory inventoryGetter = new EstablishInventory(this);
        inventoryGetter.execute(userID);
        //CharacterStats and Modifications
        characterStatisticsBar = (ProgressBar)findViewById(R.id.StatsProgressBar);
        characterModificationsBar = (ProgressBar)findViewById(R.id.ModificationsProgressBar);
        EstablishCharacter characterBuilder = new EstablishCharacter(this);
        characterBuilder.execute(userID);
    }

    public void setCharacter(HashMap<String, Object> characterMap) {
        playerCharacter = characterMap;
        characterStatisticsBar.setProgress(FIN);
        characterModificationsBar.setProgress(FIN);
    }
    public void setInventory(HashMap<String,Integer> inv){
        Inventory = inv;
        inventoryBar.setProgress(FIN);

    }

    public void BeginPlaying(View v){
        //Log.d("ALPHA","BEGIN");
        Character characterToBegin = new Character(playerCharacter,Inventory);
        ProgressBar characterBar = (ProgressBar)findViewById(R.id.CharacterProgressBar);
        characterBar.setProgress(100);
        Intent letsPlay = new Intent(this,PlayActivity.class);
        letsPlay.putExtra("CharacterObject",characterToBegin);
        startActivity(letsPlay);
        //Log.d("OMEGA","End");
    }


}
