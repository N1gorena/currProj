package com.guango.society;

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
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by nijeguan on 10/7/16.
 */
public class EstablishCharacter extends AsyncTask<Integer,Void,String> {

    private OpeningActivity establishingActivity;

    public EstablishCharacter(OpeningActivity OpA){
        establishingActivity = OpA;
    }

    @Override
    protected String doInBackground(Integer... params) {
        String userId = String.valueOf(params[0].intValue());
        try {
            URLConnection characterConnection = new URL(AppLanding.SERVERURL, "Character.php").openConnection();
            characterConnection.setDoOutput(true);
            OutputStreamWriter outputWriter = new OutputStreamWriter(characterConnection.getOutputStream());
            outputWriter.write("UserID="+userId);
            outputWriter.close();

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(characterConnection.getInputStream()));
            String responseLine;
            if((responseLine = inputReader.readLine()) != null){
                Log.d("CharResponse", responseLine);
                return responseLine;
            }

        }catch (IOException e){
            Log.d("IOEXCEPTION:Char:",e.getMessage());
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result){
        if(result != null) {
            try {
                JSONObject character = new JSONObject(result);
                HashMap<String, Object> characterMap = new HashMap<>();
                Iterator<String> characterKeys = character.keys();
                while (characterKeys.hasNext()) {
                    String currKey = characterKeys.next();
                    characterMap.put(currKey, character.get(currKey));
                }
                establishingActivity.setCharacter(characterMap);
            } catch (JSONException e) {
                Log.d("JSON:CHAR:", e.getMessage());
            }
        }
    }
}
