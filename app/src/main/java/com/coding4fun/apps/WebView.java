package com.coding4fun.apps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by coding4fun on 16-Jul-16.
 */

public class WebView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_gif);

        String url = null;
        if(getIntent().getExtras() == null)
            url = "http://www.coding4fun.96.lt/gif/f.gif";
        //url = "http://www.google.com/";
        else
            url = getIntent().getExtras().getString("url");

        android.webkit.WebView wv = (android.webkit.WebView) findViewById(R.id.web_view);
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.loadUrl(url);

        /*Intent openIntent = new Intent();
        openIntent.setAction(Intent.ACTION_VIEW);
        openIntent.setData(Uri.parse(url));
        startActivity(openIntent);*/

    }
}