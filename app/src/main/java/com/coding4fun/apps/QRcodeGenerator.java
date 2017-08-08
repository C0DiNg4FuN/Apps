package com.coding4fun.apps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by coding4fun on 30-Dec-16.
 */

public class QRcodeGenerator extends AppCompatActivity {

    android.webkit.WebView wv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_generator);

        wv = (android.webkit.WebView) findViewById(R.id.qr_webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("file:///android_asset/qr.html");
    }


    public void generateQR(View view) {
        String qrString = ((EditText)findViewById(R.id.qr_string)).getText().toString();
        wv.loadUrl("javascript:makeCode('"+qrString+"');");
    }
}