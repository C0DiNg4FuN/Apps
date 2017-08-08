package com.coding4fun.apps;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by coding4fun on 13-Jan-17.
 */

public class MouseFragment extends Fragment implements View.OnTouchListener {

    private int lastX=-1,lastY=-1;
    private boolean click, rightClick;
    private RelativeLayout rl;
    private PController controller;
    private PPT powerpointCallback;

    public MouseFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pcontroller_mouse_fragment,container,false);
        rl = (RelativeLayout) v.findViewById(R.id.mouse_RL);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = PController.getInstance();
        setHasOptionsMenu(true);
        rl.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.getId() == R.id.mouse_RL){
            String cmd = "coding4fun mouse ";
            //int action = MotionEventCompat.getActionMasked(motionEvent);
            int action = motionEvent.getActionMasked();
            if(action == MotionEvent.ACTION_DOWN) {
                lastX = (int) motionEvent.getRawX();
                lastY = (int) motionEvent.getRawY();
                click = true;
                Log.e(PController.TAG,"1st down.. index = " + motionEvent.getActionIndex());
            } else if (action == MotionEvent.ACTION_POINTER_DOWN){
                rightClick = true;
                click = false;
                Log.e(PController.TAG,"pointer down.. index = " + motionEvent.getActionIndex());
            } else if(action == MotionEvent.ACTION_MOVE) {
                int dx = (int) motionEvent.getRawX() - lastX;
                int dy = (int) motionEvent.getRawY() - lastY;
                if (dx != 0 && dy != 0) {
                    lastX = (int) motionEvent.getRawX();
                    lastY = (int) motionEvent.getRawY();
                    cmd += ((motionEvent.getPointerCount()>1) ? "scroll" : "move") + " " + dx + " " + dy;
                    click = false; //bcz it is not clicking. it is moving mouse cursor
                    rightClick = false;
                    sendCommand(cmd);
                    Log.e(PController.TAG,(motionEvent.getPointerCount()>1) ? "scroll .. dy = " + dy : "move");
                }
                //Log.e("PController","1st down");
            } else if(action == MotionEvent.ACTION_POINTER_UP) {
                if(rightClick){
                    cmd += "click right";
                    sendCommand(cmd);
                }
                Log.e(PController.TAG,"pointer up.. index = " + motionEvent.getActionIndex());
            } else if(action == MotionEvent.ACTION_UP){
                if(click){
                    cmd += "click";
                    sendCommand(cmd);
                }
                Log.e(PController.TAG,"1st up");
            }
            return true;
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        powerpointCallback = (PPT) context;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ppt, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_ppt){
            powerpointCallback.onPPT();
            return true;
        }
        return false;
    }

    private void sendCommand(String cmd){
        if(controller!= null && controller.isReady()){
            controller.sendUdpBroadcast(cmd);
        }
    }

    public interface PPT{
        void onPPT();
    }

}