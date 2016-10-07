package com.guango.society;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

public class AppLanding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_landing);
    }

    public void Enter(View v){

        AsyncTask<String,Void,Boolean> signInCommunique = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                String username = params[0];
                String password = params[1];

                try {
                    java.net.URLConnection dbEndPoint = new URL("http://192.168.0.109/Login.php").openConnection();
                    dbEndPoint.setDoOutput(true);
                    OutputStreamWriter toEndpoint = new OutputStreamWriter(dbEndPoint.getOutputStream());
                    toEndpoint.write("Username="+username);
                    toEndpoint.write("&");
                    toEndpoint.write("Password="+password);
                    toEndpoint.close();

                    BufferedReader fromEndpoint = new BufferedReader(new InputStreamReader(dbEndPoint.getInputStream()));
                    String responseLine;
                   if((responseLine = fromEndpoint.readLine())!=null){
                       Log.d("RESPONSE",responseLine);
                       return (responseLine.equals("1"));
                   }
                    fromEndpoint.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            protected void onPostExecute(Boolean result){
                if(result) {
                    //Log.d("RESULT","POSITIVE");
                    Intent loggedInIntent = new Intent(getApplicationContext(), OpeningActivity.class);
                    startActivity(loggedInIntent);

                }
                //Log.d("RESULT","NEGATIVE");
            }
        };

        EditText passwordField = (EditText)findViewById(R.id.passwordEditText);
        signInCommunique.execute("guangore@usc.edu",passwordField.getText().toString());
    }

    public void SignUp(View v){

    }
}
