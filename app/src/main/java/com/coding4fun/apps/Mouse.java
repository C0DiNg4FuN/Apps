package com.coding4fun.apps;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by coding4fun on 19-Sep-16.
 */

public class Mouse extends AppCompatActivity implements View.OnTouchListener {

    //RelativeLayout rl;
    View rl;
    Button leftClick,rightClick,connect;
    EditText serverIpET;
    InetAddress serverIP;
    int serverPort = 1234;
    boolean connected = false;
    int lastX=-1,lastY=-1;
    DatagramPacket p;
    DatagramSocket s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mouse);

        //rl = (RelativeLayout) findViewById(R.id.mouseLayout);
        rl = findViewById(R.id.mouseLayout);
        leftClick = (Button) findViewById(R.id.leftClickBTN);
        rightClick = (Button) findViewById(R.id.rightClickBTN);
        //connect = (Button) findViewById(R.id.connectBTN);
        //serverIpET = (EditText) findViewById(R.id.serverIpET);

        rl.setOnTouchListener(this);
        leftClick.setOnTouchListener(this);
        rightClick.setOnTouchListener(this);

        try {
            s = new DatagramSocket(1234);
            serverIP = InetAddress.getByName("172.29.16.136"); //192.168.43.252
            connected = true;
        } catch (SocketException e) {
            Toast.makeText(this, "DatagramSocket ERROR: "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (UnknownHostException e) {
            Toast.makeText(this, "DatagramSocket ERROR: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }

        /*connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send hi, receive ack, connected = true, init serverIP
                try {
                    if (!connected) {
                        Log.e("mouse","start connecting");
                        //InetAddress server = InetAddress.getByName(serverIpET.getText().toString());
                        serverIP = InetAddress.getByName(serverIpET.getText().toString());
                        Log.e("mouse","server ready");
                        String hi = "coding4fun";
                        //p = new DatagramPacket(hi.getBytes(),hi.length(),server,serverPort);
                        //Log.e("mouse","sending hi");
                        //s.send(p);
                        //sendUDP(hi);
                        new SendUDP(hi,serverIP,serverPort).execute();
                        Log.e("mouse","hi sent");
                        byte[] message = new byte[256];
                        p = new DatagramPacket(message, message.length);
                        Log.e("mouse","waiting for ack");
                        s.receive(p);
                        String ack = new String(p.getData(),0,p.getLength());
                        Log.e("mouse","ack received");
                        Log.e("mouse","ack: " + ack);
                        if(ack.equals("ACK: "+hi)){
                            connect.setText("DISCONNECT");
                            serverIpET.setEnabled(false);
                            connected = true;
                            //serverIP = server;
                        }
                    } else {
                        String hi = "bye";
                        p = new DatagramPacket(hi.getBytes(),hi.length(),serverIP,serverPort);
                        s.send(p);
                        connect.setText("CONNECT");
                        serverIpET.setEnabled(true);
                        connected = false;
                        serverIP = null;
                    }
                } /*catch (Exception e) {
                    Log.e("mouse error",e.getMessage());
                    Toast.makeText(Mouse.this, "ERROR: "+e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (UnknownHostException e) {
                    Log.e("mouse error",e.getMessage());
                    Toast.makeText(Mouse.this, "ERROR: "+e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Log.e("mouse error",e.getMessage());
                    Toast.makeText(Mouse.this, "ERROR: "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(!connected) return false;
        /*if(motionEvent.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){
            Log.e("mouse","new finger");
            Log.e("mouse","Index: " + motionEvent.getActionIndex());
            Log.e("mouse","ID: " + motionEvent.getPointerId(motionEvent.getActionIndex()));
        }
        if(motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE && view.getId()==R.id.mouseLayout){
            Log.e("mouse","Moving...");
        }*/
        switch (view.getId()){
            case R.id.leftClickBTN:
                mouseClick(motionEvent,view,true);
                return true;
            case R.id.rightClickBTN:
                mouseClick(motionEvent,view,false);
                return true;
            case R.id.mouseLayout:
                mouseMove(motionEvent);
                return true;
            default:
                return false;
        }
    }

    void mouseClick(MotionEvent motionEvent, View v, boolean left){
        String msg = "mouse " + ((left) ? "left" : "right");
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            msg += " pressed";
            new SendUDP(msg,serverIP,serverPort).execute();
            //Log.e("mouse","pressed " + ((left) ? "left" : "right"));
        } else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
            v.setBackgroundColor(getResources().getColor(R.color.colorAccentTransparent));
            msg += " released";
            new SendUDP(msg,serverIP,serverPort).execute();
            //Log.e("mouse","released " + ((left) ? "left" : "right"));
        }
        //sendUDP(msg);
        //new SendUDP(msg,serverIP,serverPort).execute();
    }

    void mouseMove(MotionEvent motionEvent){
        String msg = "mouse move ";
        if(motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN || motionEvent.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            //c = 0;
            lastX = (int) motionEvent.getRawX();
            lastY = (int) motionEvent.getRawY();
        } else if(motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE){
            int x = (int) motionEvent.getRawX() - lastX;
            int y = (int) motionEvent.getRawY() - lastY;
            lastX = (int) motionEvent.getRawX();
            lastY = (int) motionEvent.getRawY();
            msg += x + " " + y;
            //Log.e("mouse move","move mouse");
            //sendUDP(msg);
            new SendUDP(msg,serverIP,serverPort).execute();
        }
    }


    void sendUDP(String msg){
        final String m = msg;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    p = new DatagramPacket(m.getBytes(),m.length(),serverIP,serverPort);
                    s.send(p);
                } catch (IOException e) {}
            }
        }).start();
    }

    class SendUDP extends AsyncTask<Void,Void,Void>{

        String msg;
        InetAddress serverAddress;
        int serverPort;

        public SendUDP(String msg,InetAddress serverAddress,int serverPort) {
            this.msg = msg;
            this.serverAddress = serverAddress;
            this.serverPort = serverPort;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.e("mouse","sending udp: " + msg);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                p = new DatagramPacket(msg.getBytes(),msg.length(),serverAddress,serverPort);
                s.send(p);
            } catch (IOException e) {}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Log.e("mouse","udp sent");
        }
    }

}