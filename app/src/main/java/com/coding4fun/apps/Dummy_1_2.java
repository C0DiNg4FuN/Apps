package com.coding4fun.apps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by coding4fun on 11-Oct-16.
 */

public class Dummy_1_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_1_2);

        String msg = getIntent().getStringExtra(Dummy_1.EXTRA_MESSAGE);
        TextView tv = new TextView(this);
        tv.setTextSize(40);
        tv.setText(msg);

        ViewGroup vg = (ViewGroup) findViewById(R.id.dummy_1_2_LL);
        vg.addView(tv);
    }
}