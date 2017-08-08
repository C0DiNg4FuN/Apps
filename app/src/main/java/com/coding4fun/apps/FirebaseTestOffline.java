package com.coding4fun.apps;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coding4fun.models.Ingredient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by coding4fun on 05-Nov-16.
 */

public class FirebaseTestOffline extends AppCompatActivity {

    TextView tv;
    boolean online;
    ProgressDialog mProgressDialog;
    FirebaseDatabase fbdb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firebase_test_offline);

        fbdb = FirebaseDatabase.getInstance();
        fbdb.setPersistenceEnabled(false);
        tv = (TextView) findViewById(R.id.firebase_test_ingredients);
        online = true;
    }


    public void fetchIngredients(View view) {
        showProgressDialog("Wait a moment...");
        tv.setText("");
        fbdb.getReference("ingredients").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String msg = "";
                for (DataSnapshot ingredientSnapshot: dataSnapshot.getChildren()) {
                    Ingredient i = ingredientSnapshot.getValue(Ingredient.class);
                    i.setKey(ingredientSnapshot.getKey());
                    msg += i.getKey() + " : " + i.getName();
                }
                tv.setText(msg);
                hideProgressDialog();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
                Toast.makeText(FirebaseTestOffline.this, "ERROR!\n" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void offline_online(View view) {
        if(online){
            fbdb.goOffline();
            ((Button)view).setText("GO ONLINE");
        } else {
            fbdb.goOnline();
            ((Button)view).setText("GO OFFLINE");
        }
        online = !online;
    }

    private void showProgressDialog(String title) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(title);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}