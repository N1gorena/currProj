package com.guango.society;

import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created by NJG on 10/5/2016.
 */
public class EstablishLocationListener implements LocationListener {

    private double latestLat = 0.00000000;
    private double latestLong = 0.000000000;
    private boolean FRESH = true;
    private PlayActivity parentActivity;
    public EstablishLocationListener(PlayActivity mediator){
        parentActivity = mediator;
    }

    @Override
    public void onLocationChanged(Location location){
        latestLat = location.getLatitude();
        latestLong = location.getLongitude();
        if(FRESH){
            FRESH =false;
            parentActivity.characterListo();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){}

    @Override
    public void onProviderEnabled(String provider){
    }

    @Override
    public void onProviderDisabled(String provider){}

    public double getLatestLat(){
        return latestLat;
    }
    public double getLatestLong(){
        return latestLong;
    }
}
