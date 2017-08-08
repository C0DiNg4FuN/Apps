package com.coding4fun.apps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by coding4fun on 08-Feb-17.
 */

public class BuildVariantsTest extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.build_variants_test);
        FirebaseMessaging.getInstance().subscribeToTopic("BuildVariantsTest");
    }

    public void writeInFBDB(View v){
        FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
        DatabaseReference ref = fbdb.getReference().child("test");
        String key = ref.push().getKey();
        ref.child(key).setValue("bla").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(BuildVariantsTest.this, "DONE :)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BuildVariantsTest.this, "ERROR!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}