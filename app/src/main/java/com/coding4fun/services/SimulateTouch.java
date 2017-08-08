package com.coding4fun.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.coding4fun.models.TouchPoint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by coding4fun on 15-Sep-16.
 */

public class SimulateTouch extends IntentService {

    List<TouchPoint> touchPoints;

    public SimulateTouch() {
        super("SimulateTouch");
        touchPoints = new ArrayList<>();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Log.e("SimulateTouch","service started");
            String json = intent.getExtras().getString("touchesJSON");
            JSONArray ja = new JSONArray(json);
            for(int i=0;i<ja.length();i++){
                JSONObject jo = new JSONObject(ja.get(i).toString());
                touchPoints.add(new TouchPoint((float)jo.getDouble("x"),(float)jo.getDouble("y")));
            }
            for(TouchPoint tp : touchPoints){
                executeRoot("input touchscreen tap "+tp.getX()+" "+tp.getY());
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            Log.e("SimulateTouch exception",e.getMessage());
        }
    }

    String executeRoot(String command){
        try {
            Log.e("shell commands","Start executing...");
            Process su = Runtime.getRuntime().exec("su",null,null);
            DataOutputStream os = new DataOutputStream(su.getOutputStream());
            os.writeBytes(command);
            os.flush();
            os.close();
            /*BufferedReader reader = new BufferedReader(new InputStreamReader(su.getInputStream()));
            String line = "", output = "";
            while ((line = reader.readLine()) != null) {
                output += line+"\n";
            }*/
            su.waitFor();
            Log.e("shell commands","DONE executing...");
            //return output;
            return "";
        } catch (Exception e) {
            Log.e("shell commands","ERROR!");
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return "";
        }
    }

    @Override
    public void onDestroy() {
        Log.e("SimulateTouch","service stopped");
        super.onDestroy();
    }
}