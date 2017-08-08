package com.coding4fun.apps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by coding4fun on 21-Oct-16.
 */

public class FirebaseDB extends AppCompatActivity {

    FirebaseDatabase fbdb;
    DatabaseReference fbdbRef;
    EditText reference, key, value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firebase_database);

        reference = (EditText) findViewById(R.id.fbdb_ref_ET);
        key = (EditText) findViewById(R.id.fbdb_key_ET);
        value = (EditText) findViewById(R.id.fbdb_value_ET);

        fbdb = FirebaseDatabase.getInstance();

    }

    public void fbdb_read (View v) {

    }

    public void fbdb_write (View v) {
        DatabaseReference myRef = fbdb.getReference("message/time/mins");
        myRef.setValue("25");
        //fbdbRef = fbdb.getReference(reference.getText().toString());
        //myRef.child("time").child("mins").setValue("2");
    }

}