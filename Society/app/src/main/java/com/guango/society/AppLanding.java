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
    public static final String UID = "USEIDENT";
    public static URL SERVERURL;
    static {
        try {
            SERVERURL = new URL("http://192.168.0.100/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_landing);
    }

    public void Enter(View v){

        AsyncTask<String,Void,Integer> signInCommunique = new AsyncTask<String, Void, Integer>() {
            @Override
            protected Integer doInBackground(String... params) {
                String username = params[0];
                String password = params[1];

                Integer toReturn = Integer.valueOf(0);

                try {
                    java.net.URLConnection dbEndPoint = new URL(SERVERURL,"Login.php").openConnection();
                    dbEndPoint.setDoOutput(true);
                    OutputStreamWriter toEndpoint = new OutputStreamWriter(dbEndPoint.getOutputStream());
                    toEndpoint.write("Username="+username);
                    toEndpoint.write("&");
                    toEndpoint.write("Password="+password);
                    toEndpoint.close();

                    BufferedReader fromEndpoint = new BufferedReader(new InputStreamReader(dbEndPoint.getInputStream()));
                    String responseLine;
                   if((responseLine = fromEndpoint.readLine())!=null){
                       //Log.d("RESPONSE",responseLine);
                       if(!responseLine.equals("FALSE")){
                           fromEndpoint.close();
                           toReturn = Integer.valueOf(Integer.parseInt(responseLine));
                           return toReturn;
                       }
                   }
                    fromEndpoint.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return toReturn;
            }

            @Override
            protected void onPostExecute(Integer result){
                if(result > 0 ) {
                    //Log.d("RESULT","POSITIVE");
                    Intent loggedInIntent = new Intent(getApplicationContext(), OpeningActivity.class);
                    loggedInIntent.putExtra(UID,result);
                    startActivity(loggedInIntent);

                }
                //Log.d("RESULT","NEGATIVE");
            }
        };

        EditText passwordField = (EditText)findViewById(R.id.passwordEditText);
        EditText usernameField = (EditText) findViewById(R.id.usernameEditText);
        //signInCommunique.execute(usernameField.getText().toString(),passwordField.getText().toString());
        signInCommunique.execute("guangore@usc.edu","Gabrindo7");
    }

    public void SignUp(View v){

    }
}
