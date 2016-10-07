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

    public EstablishLocationListener(){

    }

    @Override
    public void onLocationChanged(Location location){
        Log.d("LOCATION",location.getLatitude()+"<Lat Long>"+location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){}

    @Override
    public void onProviderEnabled(String provider){}

    @Override
    public void onProviderDisabled(String provider){}


}
