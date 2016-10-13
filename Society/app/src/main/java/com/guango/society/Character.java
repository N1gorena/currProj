package com.guango.society;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by nijeguan on 10/7/16.
 */

public class Character implements Parcelable{

    private String mName;
    private HashMap<String,Integer> mStats;
    private HashMap<String,Integer> mItems;
    private EstablishLocationListener locationHolder;
    private boolean LISTENING = false;

    public Character(Parcel in){
        Bundle parcelBundle = in.readBundle();
        mName = parcelBundle.getString("Name");
        mStats = (HashMap<String,Integer>)parcelBundle.getSerializable("Stats");
        mItems = (HashMap<String,Integer>)parcelBundle.getSerializable("Items");

    }

    public Character(HashMap<String,Object> me, HashMap<String,Integer> myItems){
        mStats = new HashMap<>();
        Set<String> meKeys = me.keySet();
        for(String s : meKeys){
            Object meValue = me.get(s);
            if(meValue.getClass() == Integer.class){
                mStats.put(s,(Integer)me.get(s));
            }
            else{
                mName = (String) me.get(s);
            }
        }
        mItems = myItems;

    }

    public Pair<Double,Double> getLocation(){
        if(LISTENING) {
            return new Pair<>(locationHolder.getLatestLat(),locationHolder.getLatestLong());

        }
        return new Pair<>(0.0,0.0);
    }

    public void setLocationListener(EstablishLocationListener locationEar){
        locationHolder = locationEar;
        LISTENING = true;
    }

    public static final Parcelable.Creator<Character> CREATOR = new Parcelable.Creator<Character>(){
        public Character createFromParcel(Parcel in){
            return new Character(in);
        }
        public Character[] newArray(int size){
            return new Character[size];
        }
    };
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle tBundle = new Bundle();
        tBundle.putString("Name",mName);
        tBundle.putSerializable("Stats",mStats);
        tBundle.putSerializable("Items",mItems);
        tBundle.writeToParcel(dest,flags);

    }

    @Override
    public String toString(){
        return mName;
    }
}
