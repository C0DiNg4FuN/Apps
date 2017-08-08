package com.coding4fun.apps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.coding4fun.models.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by coding4fun on 31-Dec-16.
 */

public class FBDB_listenners extends AppCompatActivity {

    DatabaseReference ref,customizedRef;
    Query customizedQuery;
    ChildEventListener childEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fbdb_listenners);

        ref = FirebaseDatabase.getInstance().getReference("test");
        customizedQuery = FirebaseDatabase.getInstance().getReference("test").orderByChild("orderLocation/place").equalTo("place");
        /*ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Log.e("app","onDataChange: " + ds.getValue().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });*/

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.e("app","onChildAdded: " + dataSnapshot.getValue().toString());
                Log.e("app","onChildAdded: previousChildName: " + previousChildName);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.e("app","onChildChanged: " + dataSnapshot.getValue().toString());
                Log.e("app","onChildChanged: previousChildName: " + previousChildName);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e("app","onChildRemoved: " + dataSnapshot.getValue().toString());
                Log.e("app","onChildRemoved: KEY: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.e("app","onChildMoved: " + dataSnapshot.getValue().toString());
                Log.e("app","onChildMoved: previousChildName: " + previousChildName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        customizedQuery.addChildEventListener(childEventListener);
    }

    public void addDummyData(View view) {
        Order order = new Order("x","restID");
        ref.push().setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(FBDB_listenners.this, task.isSuccessful() ? "added successfully" : "Failed!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ref.removeEventListener(childEventListener);
    }
}