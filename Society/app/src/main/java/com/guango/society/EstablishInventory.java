package com.guango.society;

import android.graphics.Path;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by nijeguan on 10/7/16.
 */

public class EstablishInventory extends AsyncTask<Integer,Void,String>{
    private HashMap<String,Integer> InventoryMap = new HashMap<>();
    private OpeningActivity establishingActivity;

    public EstablishInventory(OpeningActivity OpA){
        establishingActivity = OpA;
    }

    @Override
    protected String doInBackground(Integer... params) {
        String userID = String.valueOf( params[0].intValue() );
        try {
            URLConnection inventoryDBConn = new URL(AppLanding.SERVERURL,"Inventory.php").openConnection();
            inventoryDBConn.setDoOutput(true);
            OutputStreamWriter outputWriter = new OutputStreamWriter(inventoryDBConn.getOutputStream());
            outputWriter.write("UserID="+userID);
            outputWriter.close();

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(inventoryDBConn.getInputStream()));
            String responseLine;
            if ( (responseLine = inputReader.readLine()) != null){
                //Log.d("InvResponse",responseLine);
                return responseLine;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(String result){
        JSONObject returnedJSON = null;
        try {
            returnedJSON = new JSONObject(result);

            Iterator<String> itemKeyIterator = returnedJSON.keys();
            while (itemKeyIterator.hasNext()) {
                String currKey = itemKeyIterator.next();
                InventoryMap.put(currKey, returnedJSON.getInt(currKey));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        establishingActivity.setInventory(InventoryMap);
    }

    public HashMap<String,Integer> getInventory(){
        return InventoryMap;
    }
}
