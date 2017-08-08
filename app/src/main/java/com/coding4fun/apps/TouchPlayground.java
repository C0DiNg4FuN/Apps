package com.coding4fun.apps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coding4fun.models.TouchPoint;
import com.coding4fun.services.SimulateTouch;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coding4fun on 03-Sep-16.
 */

public class TouchPlayground extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    EditText x,y;
    TextView coordinates;
    Button simulate,test,record;
    LinearLayout ll;
    List<TouchPoint> touchPoints = new ArrayList<>();
    boolean recording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_playground);

        ll = (LinearLayout) findViewById(R.id.tLL);
        x = (EditText) findViewById(R.id.XET);
        y = (EditText) findViewById(R.id.YET);
        coordinates = (TextView) findViewById(R.id.coordinates);
        simulate = (Button) findViewById(R.id.simulateBTN);
        test = (Button) findViewById(R.id.testSimulateBTN);
        record = (Button) findViewById(R.id.recordTouchesBTN);

        ll.setOnTouchListener(this);
        simulate.setOnClickListener(this);
        test.setOnClickListener(this);
        record.setOnClickListener(this);
    }

    void simulateTouch(float x,float y){
        long downTime = SystemClock.uptimeMillis();
        long upTime = SystemClock.uptimeMillis() + 100;
        int metaState = 0;
        MotionEvent me = MotionEvent.obtain(downTime,upTime,MotionEvent.ACTION_UP,x,y,metaState);
        ll.dispatchTouchEvent(me);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.simulateBTN:
                float x = Float.parseFloat(this.x.getText().toString());
                float y = Float.parseFloat(this.y.getText().toString());
                simulateTouch(x,y);
                break;
            case R.id.testSimulateBTN:
                Toast.makeText(this, "Button Clicked...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.recordTouchesBTN:
                if(!recording){
                    recording = true;
                    touchPoints = new ArrayList<>();
                    record.setText("stop recording touches");
                    this.x.setVisibility(View.GONE);
                    this.y.setVisibility(View.GONE);
                    this.simulate.setVisibility(View.GONE);
                    this.test.setVisibility(View.GONE);
                    this.coordinates.setVisibility(View.GONE);
                } else {
                    recording = false;
                    record.setText("start recording touches");
                    try {
                        JSONArray ja = new JSONArray();
                        for(TouchPoint tp : touchPoints){
                            JSONObject jo = new JSONObject();
                            jo.put("x",(double)tp.getX());
                            jo.put("y",(double)tp.getY());
                            ja.put(jo.toString());
                        }
                        final String json = ja.toString();
                        Log.e("ja",json);
                        //start service and sent ja to it...
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(TouchPlayground.this, SimulateTouch.class);
                                i.putExtra("touchesJSON",json.toString());
                                startService(i);
                            }
                        },10000);
                    } catch (Exception e) {
                        Log.e("end recording exception",e.getMessage());
                    }
                }
                break;
            default:
                return;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(recording){
            if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                touchPoints.add(new TouchPoint(motionEvent.getX(),motionEvent.getY()));
            }
        } else {
            float y = motionEvent.getY();
            float x = motionEvent.getX();
            coordinates.setText("X: "+x+"\nY: "+y);
        }
        return true;
    }
}