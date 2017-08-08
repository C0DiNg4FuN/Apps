package com.coding4fun.apps;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by coding4fun on 13-Jan-17.
 */

public class PController {

    private static PController uniqueInstance;

    private PController(){
        Log.e(TAG,"PController constructed");
        ready = init();
    }

    public static PController getInstance(){
        if(uniqueInstance == null){
            uniqueInstance = new PController();
        }
        return uniqueInstance;
    }

    /************* client code here ***************/

    public static final String TAG = "PController";
    private DatagramSocket s;
    private List<NetworkInterface> networkInterfaces;
    private boolean ready;


    private boolean init(){
        try {
            //keepRunning = true;
            s = new DatagramSocket();
            s.setBroadcast(true);
            networkInterfaces = new ArrayList<>();
            Enumeration<NetworkInterface> is = NetworkInterface.getNetworkInterfaces();
            while (is.hasMoreElements()){
                NetworkInterface i = is.nextElement();
                if(!i.isLoopback() && i.isUp()){
                    networkInterfaces.add(i);
                    Log.e(TAG, "Display name: "+ i.getDisplayName() + " .. HardwareAddress: " + i.getHardwareAddress() + " .. InetAddresses: " + i.getInetAddresses());
                }
            }
            Log.e(TAG,"PController & socket are initialized successfully");
            return true;
        } catch (SocketException e) {
            return false;
        }
    }

    /*void stopThread(){
        keepRunning = false;
    }*/

    boolean isReady(){
        return ready;
    }

    void sendUdpBroadcast(String command){
        for(NetworkInterface i : networkInterfaces){
            for(InterfaceAddress ia : i.getInterfaceAddresses()){
                InetAddress broadcast = ia.getBroadcast();
                if(broadcast != null){
                    byte[] bytesToSend = command.getBytes();
                    DatagramPacket p = new DatagramPacket(bytesToSend,bytesToSend.length,broadcast,8765);
                    sendInBackground(p);
                }
            }
        }
    }

    private void sendInBackground(final DatagramPacket p){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    s.send(p);
                    //Log.e(TAG,"Packet sent :)");
                }
                catch (IOException e) {Log.e(TAG,"Error sending packet: " + e.getMessage());}
            }
        }).start();
    }

    /*@Override
    public void run() {
        if(init()){
            Log.e(TAG,"Thread started");
            running = true;
            while (keepRunning) {} // keep thread alive
            running = false;
            Log.e(TAG,"Thread stopped");
        }
    }*/
}
