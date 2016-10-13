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
    private enum OBJECTIN {NODE,WAY,RELATION,NONE};
    private final int openingElement = 0;

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

        WorldEngine renderer = new WorldEngine();
        GWindow gWindow = (GWindow)findViewById(R.id.GraphicsView);
        gWindow.setEGLContextClientVersion(3);
        gWindow.setRenderer(renderer);
        //CURR
            //Get osm.xml folder. The assets folder
        AssetManager mappingAssets = getAssets();
        try {
            BufferedReader mapFile = new BufferedReader( new InputStreamReader(mappingAssets.open("map.osm")) );
                //The String to read full lines </>  into
            String xmlToken;
                //The flag for what XML object is being built while parsing.
            OBJECTIN currentXMLObject = OBJECTIN.NONE;
                //Storage
            ArrayList<GNode> nodeList = new ArrayList<>();
            ArrayList<GWay> wayList = new ArrayList<>();
            ArrayList<GRelation> relationList = new ArrayList<>();
                //Interfaced xml object being built.
            Object XMLObj = null;

            //Parsing loop
            /**/
            while( (xmlToken = mapFile.readLine()) != null){
                //Removes leading and trailing whitespace.
                xmlToken = xmlToken.trim();

                //Splits XML into elements that are either a KV pair or  a opening '<' statement.
                String[] tokenElements = xmlToken.split(" ");

                //Check opening '<' statement for closing of an XML Obj.
                if( ( tokenElements[openingElement].charAt(1) ) == '/'){
                    //Close object by storing it.
                    switch (currentXMLObject){
                        case NODE:
                            ((GXMLObject)XMLObj).dump();
                            nodeList.add((GNode)XMLObj);
                            break;
                        case WAY:
                            ((GXMLObject)XMLObj).dump();
                            wayList.add((GWay)XMLObj);
                            break;
                        case RELATION:
                           ((GXMLObject)XMLObj).dump();
                            relationList.add((GRelation)XMLObj);
                            break;
                        default:
                            break;
                    }
                    //Reset object and its marker.
                    currentXMLObject = OBJECTIN.NONE;
                    XMLObj = null;
                    continue;
                }
                else{
                    //Drop < character from opening tag and find what obj we are working with.
                   String openingTag = tokenElements[openingElement].substring(1);
                    //For any of the three objects in the xml...
                    //Create an object to build, and set the marker.
                    if(openingTag.equals("node") || openingTag.equals("way") || openingTag.equals("relation")){
                        switch(openingTag){
                            case "node":
                                XMLObj = new GNode();
                                currentXMLObject = OBJECTIN.NODE;
                                break;
                            case "way":
                                XMLObj = new GWay();
                                currentXMLObject = OBJECTIN.WAY;
                                break;
                            case "relation":
                                XMLObj = new GRelation();
                                currentXMLObject = OBJECTIN.RELATION;
                            default:
                                break;
                        }

                        //Original xmlToken is split into elements based on blankspace betweek KV pairs.
                        //This StringBuilder builds KV pairs that were broken due to having spaces in either the K or V.
                        StringBuilder keyValItem = new StringBuilder();

                        //Go over all the elements, fixing and processing KV pairs.
                        for(int i = openingElement+1; i < tokenElements.length;i++){
                            //Build String of first KV pair.
                            keyValItem.append(tokenElements[i]);
                            String currElement = keyValItem.toString();

                            //Full element vs partial. Due to the blankspace-split above splitting up values with spaces in them.
                            //If the element does not end in a " or a > then it has been broken by a space, so skip KV processing
                            //And allow StringBuilder to repair the blankspace-split
                            if(currElement.endsWith("\"") || currElement.endsWith(">")){
                                //Create array to hold Key Value pair by splitting on `"/`or `=`. This will make a 2 or 3 element array depending on if
                                //this element is the final one of the object.
                                String[] itemPair = currElement.split("(\"/|=)");
                                ((GXMLObject)XMLObj).attachInfo(itemPair[0],itemPair[1].replaceAll("[>\"]",""));
                                //If the KV array has a third element then this obj should be closed since that third element indicates that the KV pair had a `/>` trailing.
                                if(itemPair.length > 2){
                                    switch (currentXMLObject){
                                        case NODE:
                                            nodeList.add((GNode)XMLObj);
                                            break;
                                        case WAY:
                                            wayList.add((GWay)XMLObj);
                                        case RELATION:
                                            relationList.add((GRelation)XMLObj);
                                        default:
                                            break;
                                    }
                                    currentXMLObject = OBJECTIN.NONE;
                                    XMLObj = null;
                                }
                                //Reset String builder since we finished a KV pair process.
                                keyValItem.setLength(0);
                            }
                        }
                    }
                    else if(openingTag.equals("nd")){
                        //Update the xml object being built.
                        String[] nodeRef = tokenElements[openingElement+1].split("[/=]");
                        ((GXMLObject)XMLObj).addNode(nodeRef[1]);
                    }
                    else if(openingTag.equals("tag")){
                        //Update the xml object being built.
                        StringBuilder keyValTag = new StringBuilder();
                        String key = "ERROR";
                        String val = "UNSET";
                        for(int i = openingElement + 1; i < tokenElements.length;i++){
                            keyValTag.append(tokenElements[i]);
                            String currElement = keyValTag.toString();
                            if(currElement.endsWith("\"") || currElement.endsWith(">")){
                                String[] KorV = currElement.split("(\"/|=)");
                                if(KorV[0].equals("k")){
                                    key = KorV[1].replace("\"","");
                                    keyValTag.setLength(0);
                                }
                                else{
                                    val = KorV[1].replace("\"","");
                                }
                            }
                        }
                        ((GXMLObject)XMLObj).addTag(key, val);
                    }
                    else if(openingTag.equals("member")){
                        //Update the xml object being built.
                        StringBuilder keyValTag = new StringBuilder();
                        String type = "ERROR";
                        String ref = "UNSET";
                        String role ="NONE";
                        for(int i = openingElement + 1; i < tokenElements.length;i++){
                            keyValTag.append(tokenElements[i]);
                            String currElement = keyValTag.toString();

                            if(currElement.endsWith("\"") || currElement.endsWith(">")){
                                String[] memberPair = currElement.split("(\"/|=)");
                                memberPair[1] =  memberPair[1].replaceAll("[>\"]","");
                                if(memberPair[0].equals("type")){
                                    type = memberPair[1];
                                }
                                else if(memberPair[0].equals("ref")){
                                    ref = memberPair[1];

                                }
                                else{
                                    role = memberPair[1];
                                }
                                keyValTag.setLength(0);
                            }
                        }
                        ((GXMLObject)XMLObj).addMember(type,ref,role);
                    }
                }

            }
            mapFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


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
}
