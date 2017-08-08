package com.coding4fun.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by coding4fun on 09-Sep-16.
 */

public class UploadFile {

    public static boolean uploadFile(Context context, String path){
        Log.e("GPA","start uploading");
        //this.server = "http://www.coding4fun.96.lt";
        //String server = "http://172.29.23.90";
        String response = HttpManager.getData(getRequestPackage("http://"+getIP(context),path));
        Log.e("GPA",response);
        Log.e("GPA","Done uploading");
        try {
            JSONObject jo = new JSONObject(response);
            if(jo.getString("status").equals("OK"))
                return true;
            else {
                Log.e("GPA","response error: "+jo.getString("reason"));
                return false;
            }
        } catch (JSONException e) {
            Log.e("GPA","Upload Exception: "+e.getMessage());
            return false;
        }
    }

    private static RequestPackage getRequestPackage(String server, String path){
        RequestPackage p = new RequestPackage();
        p.setMethod_POST();
        p.setUrl(server+"/fake_gpa/main.php");
        p.addParam("what","upload_file");
        p.addParam("name",getFileNameWithoutExtension(path));
        p.addParam("ext",getFileExtension(path));
        p.addParam("encoded_string",file2base64(path));
        return p;
    }

    private static String file2base64(String path) {
        String base64 = null;
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fis.read(bytes);
            base64 = Base64.encodeToString(bytes,Base64.DEFAULT);
        } catch (IOException e) {
            //Toast.makeText(UploadPics.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Exception in base 64",e.getMessage());
        }
        return base64;
    }

    private static String getFileNameWithoutExtension(String path){
        File f = new File(path);
        String name = f.getName().substring(0, f.getName().lastIndexOf("."));
        return name;
    }

    private static String getFileExtension(String path){
        String ext = path.substring(path.lastIndexOf('.')+1, path.length());
        return ext;
    }

    public static String getIP(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String ip = prefs.getString("ip","");
        return ip;
    }

    public static boolean writeToFile(Context context,String fileName, String content){
        RequestPackage p = new RequestPackage();
        p.setMethod_POST();
        Log.e("GPA","IP: " + getIP(context));
        p.setUrl("http://"+getIP(context)+"/fake_gpa/main.php");
        p.addParam("what","write_to_file");
        p.addParam("name",fileName);
        p.addParam("content",content);
        String response = HttpManager.getData(p);
        Log.e("GPA","response: " + response);
        try {
            JSONObject jo = new JSONObject(response);
            if(jo.getString("status").equals("OK"))
                return true;
            else {
                Log.e("GPA","response error" + jo.getString("reason"));
                return false;
            }
        } catch (JSONException e) {
            Log.e("GPA","Upload exception: " + e.getMessage());
            return false;
        }
    }

}