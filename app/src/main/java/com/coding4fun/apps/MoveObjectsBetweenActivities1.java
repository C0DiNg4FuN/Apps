package com.coding4fun.apps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.coding4fun.models.Resturant;

public class MoveObjectsBetweenActivities1 extends AppCompatActivity {

    TextView tv;
    Resturant r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_objects_between_activities1);

        tv = (TextView) findViewById(R.id.moveObjectTV);
        r = getIntent().getParcelableExtra("resturantObject");
        String msg = "";
        msg += r.getName()+"\n"+r.getEmail()+"\n"+r.getPassword()+"\n"+r.getOwnerName()+"\n"+r.getPhoneNumber()+"\n"+r.getAddress()+"\n";
        for(String key : r.getServices().keySet()){
            msg += key + " : " + String.valueOf(r.getServices().get(key)) + "\n";
        }
        for(String key : r.getPaymentMethods().keySet()){
            msg += key + " : " + String.valueOf(r.getPaymentMethods().get(key)) + "\n";
        }
        tv.setText(msg);
    }
}
