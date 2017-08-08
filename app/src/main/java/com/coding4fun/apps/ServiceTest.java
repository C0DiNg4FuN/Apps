package com.coding4fun.apps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.coding4fun.services.RecordAudio;

import java.io.DataOutputStream;

/**
 * Created by coding4fun on 09-Sep-16.
 */

public class ServiceTest extends AppCompatActivity {

    Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_test);

        initToolbar();
        Button startService = (Button) findViewById(R.id.startService);
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ServiceTest.this, RecordAudio.class);
                ServiceTest.this.startService(i);
            }
        });

        Button root = (Button) findViewById(R.id.root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String command = "screencap -p /sdcard/fakeGPAcalc/SS.png";
                    Process su = Runtime.getRuntime().exec("su",null,null);
                    //OutputStream os = su.getOutputStream();
                    //os.write(("/system/bin/screencap -p /sdcard/fakeGPAcalc/SSSS.png").getBytes("ASCII"));
                    DataOutputStream os = new DataOutputStream(su.getOutputStream());
                    os.writeBytes("/system/bin/screencap -p /sdcard/fakeGPAcalc/SSSS.png");
                    os.flush();
                    os.close();
                    //Process su = Runtime.getRuntime().exec("su -c ls");
                    /*BufferedReader reader = new BufferedReader(new InputStreamReader(su.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        Log.e("su line",line);
                    }*/
                    su.waitFor();
                    Toast.makeText(ServiceTest.this, "exitValue" + su.exitValue(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(ServiceTest.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initToolbar(){
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
    }

}