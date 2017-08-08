package com.coding4fun.apps;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.coding4fun.utils.HttpManager;
import com.coding4fun.utils.RequestPackage;

/**
 * Created by coding4fun on 18-Jan-17.
 */

public class SMS extends AppCompatActivity {

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Sending SMS...");
        //mProgressDialog.show();
    }

    public void sendSMS(View view) {
        String phoneNumber = ((TextView)findViewById(R.id.sms_phone_number)).getText().toString();
        String body = ((TextView)findViewById(R.id.sms_body)).getText().toString();
        if(phoneNumber.isEmpty() || body.isEmpty()){
            Toast.makeText(this, "Some inputs are empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestPackage p = new RequestPackage();
        p.setMethod_GET();
        p.setUrl("http://212.36.222.10:1111/pullhttp2.aspx");
        p.addParam("uid","obeida");
        p.addParam("pwd","obeida@123");
        p.addParam("sid","Sho 3aBelak");
        p.addParam("lng","L");
        p.addParam("mnb","1");
        p.addParam("gsm",phoneNumber);
        p.addParam("msg",body);

        new SendSMS(p).execute();
    }


    private class SendSMS extends AsyncTask<Void,Void,Boolean>{

        RequestPackage p;

        public SendSMS(RequestPackage p) {
            this.p = p;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String r = HttpManager.getData(p);
            return r.startsWith("Done");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            Toast.makeText(SMS.this, (result) ? "DONE :)" : "ERROR! :(", Toast.LENGTH_LONG).show();
        }
    }
}