package com.coding4fun.services;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by coding4fun on 11-Sep-16.
 */

public class Wallpaper extends IntentService {

    public Wallpaper() {
        super("Wallpaper");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Log.e("Wallpaper","start");
            //get pic name
            String picName = intent.getExtras().getString("wallpaper");
            //Download pic
            URL u = new URL("http://192.168.1.76/fake_gpa/" + picName);
            Log.e("Wallpaper","start Downloading...");
            InputStream in = (InputStream) u.getContent();
            Log.e("Wallpaper","done Downloading...");
            //create bitmap
            Bitmap b = BitmapFactory.decodeStream(in);
            in.close();
            //change wallpaper
            WallpaperManager wm = WallpaperManager.getInstance(this);
            wm.setBitmap(b);
            Log.e("Wallpaper","Wallpaper set...");
        } catch (Exception e) {
            Log.e("Wallpaper Exception",e.getMessage());
        }
    }

}