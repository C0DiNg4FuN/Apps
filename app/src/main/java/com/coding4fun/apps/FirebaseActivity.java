package com.coding4fun.apps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by coding4fun on 06-Aug-16.
 */

public class FirebaseActivity extends AppCompatActivity {

    Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firebase_activity);

        initToolbar();
        final EditText subscribeET = (EditText) findViewById(R.id.subscribeET);
        Button subscribeBTN = (Button) findViewById(R.id.subscribeBTN);
        TextView fcmTV = (TextView) findViewById(R.id.fcmTV);

        subscribeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().subscribeToTopic(subscribeET.getText().toString());
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String r="";
            for(String key : bundle.keySet())
                r += key+" : "+bundle.getString(key);
            fcmTV.setText(r);
        }

    }

    private void initToolbar(){
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
    }

}