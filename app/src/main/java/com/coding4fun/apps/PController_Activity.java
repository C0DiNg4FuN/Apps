package com.coding4fun.apps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * Created by coding4fun on 23-Jan-17.
 */

public class PController_Activity extends AppCompatActivity
        implements MouseFragment.PPT, PowerpointFragment.Mouse {

    PController pController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pcontroller_activity);

        pController = PController.getInstance();
        initToolbar();

        MouseFragment mouseFragment = new MouseFragment();
        KeyBoardFragment keyBoardFragment = new KeyBoardFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.pcontroller_container,mouseFragment)
                .commit();
    }

    private void initToolbar(){
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setTitle("PController");
    }

    @Override
    protected void onDestroy() {
        //pController.stopThread();
        if(pController!= null && pController.isReady()){
            pController.sendUdpBroadcast("coding4fun bye");
            Log.e(PController.TAG,"bye..");
        }
        super.onDestroy();
    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.pcontroller_container,fragment)
                .commit();
    }

    @Override
    public void onPPT() {
        replaceFragment(new PowerpointFragment());
    }

    @Override
    public void onMouse() {
        replaceFragment(new MouseFragment());
    }
}