package com.guango.society;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

public class ListenActivity extends AppCompatActivity {
    private MediaRecorder microphone;
    private String audioPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);

        StringBuilder filePath = new StringBuilder();
        filePath.append(this.getCacheDir().getAbsolutePath());
        filePath.append("/communique");
        audioPath = filePath.toString();

        microphone = new MediaRecorder();
        if( PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)){
            microphone.setAudioSource(MediaRecorder.AudioSource.MIC);
            microphone.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            microphone.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            microphone.setOutputFile(audioPath);
        }
        else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},77);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults){
        switch (requestCode){
            case 77:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    microphone.setAudioSource(MediaRecorder.AudioSource.MIC);
                    microphone.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    microphone.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    microphone.setOutputFile(audioPath);

                }
        }
    }
    public void Start(View v){
        try {
            microphone.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        microphone.start();
    }

    public void Stop(View v){
        microphone.stop();
        microphone.setAudioSource(MediaRecorder.AudioSource.MIC);
        microphone.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        microphone.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        microphone.setOutputFile(audioPath);
    }

    public void Playback(View v){

        MediaPlayer tempPlayer = new MediaPlayer();
        try{
            tempPlayer.setDataSource(audioPath);
            tempPlayer.prepare();
            tempPlayer.setLooping(false);
            tempPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.release();
                }
            });
            tempPlayer.start();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
