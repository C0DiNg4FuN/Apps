package com.coding4fun.services;

import android.app.IntentService;
import android.content.Intent;
import android.hardware.Camera;
import android.util.Log;

import com.coding4fun.utils.Utils;

/**
 * Created by coding4fun on 16-Sep-16.
 */

public class FlashLight extends IntentService {

    public FlashLight() {
        super("FlashLight");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("FlashLight","started");
        Utils utils = new Utils(this);
        int time = intent.getExtras().getInt("time",10);
        Camera cam = utils.turnFlash(null,true);
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {}
        utils.turnFlash(cam,false);
    }

    @Override
    public void onDestroy() {
        Log.e("FlashLight","DONE");
        super.onDestroy();
    }
}