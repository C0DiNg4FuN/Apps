package com.coding4fun.apps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by coding4fun on 11-Oct-16.
 */

public class Dummy_1 extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "WHATEVER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_1);
    }

    public void dummy_1_BTN_clicked(View view){
        EditText et = (EditText) findViewById(R.id.dummy_ET);
        Intent i = new Intent(this, Dummy_1_2.class);
        i.putExtra(EXTRA_MESSAGE,et.getText().toString());
        startActivity(i);
    }

}