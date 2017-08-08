package com.coding4fun.apps;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by coding4fun on 16-Sep-16.
 */

public class CallMsgLogs extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    Button callLogs,msgInboxLogs,msgSentLogs,contacts;
    TextView logsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_msg_logs);

        initToolbar();

        callLogs = (Button) findViewById(R.id.callLogs);
        msgInboxLogs = (Button) findViewById(R.id.msgInboxLogs);
        msgSentLogs = (Button) findViewById(R.id.msgSentLogs);
        contacts = (Button) findViewById(R.id.contacts);
        logsTV = (TextView) findViewById(R.id.logsTV);
        logsTV.setMovementMethod(new ScrollingMovementMethod());

        callLogs.setOnClickListener(this);
        msgInboxLogs.setOnClickListener(this);
        msgSentLogs.setOnClickListener(this);
        contacts.setOnClickListener(this);
    }

    private void initToolbar(){
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.callLogs:
                logsTV.setText(getCallsLogs());
                break;
            case R.id.msgInboxLogs:
                logsTV.setText(getSMSsLogs(true));
                break;
            case R.id.msgSentLogs:
                logsTV.setText(getSMSsLogs(false));
                break;
            case R.id.contacts:
                logsTV.setText(getContacts());
                break;
            default:
                break;
        }
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