package com.coding4fun.apps;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by coding4fun on 31-Dec-16.
 */

public class Notif extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notif);
    }


    public void showNotification(View view) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder n = new NotificationCompat.Builder(this)
                    .setContentTitle("test")
                    .setContentText("test test test test test test test test test test test test test test test");
            n.setDefaults(Notification.DEFAULT_SOUND);	//default sound, vibration, light...
        n.setVibrate(new long[]{500,1000,500,1000});
        n.setLights(0xff00ff00,500,1000);
            n.setAutoCancel(true);	//to cancel it after clicking it
            n.setOngoing(false);
            n.setTicker("test...");
            Intent i = new Intent(this,Notif.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            n.addAction(R.drawable.qr_code,"Scan QR",pIntent);
            int id = 789;
            n.setSmallIcon(R.drawable.qr_code);
            nm.notify(id, n.build());
    }
}