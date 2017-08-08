package com.coding4fun.apps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.coding4fun.models.Resturant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddResturantToFBDB extends AppCompatActivity {

    AppCompatCheckBox delivery,takeAway, bookTable,cash,visa;
    EditText resturantName, email, password, ownerName, phoneNumber, address;
    TextView results;
    Toolbar tb;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_resturant_to_fbdb);

        resturantName = (EditText) findViewById(R.id.resturantName);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        ownerName = (EditText) findViewById(R.id.ownerName);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        address = (EditText) findViewById(R.id.address);
        delivery = (AppCompatCheckBox) findViewById(R.id.checkbox_delivery);
        takeAway = (AppCompatCheckBox) findViewById(R.id.checkbox_takeAway);
        bookTable = (AppCompatCheckBox) findViewById(R.id.checkbox_bookTable);
        cash = (AppCompatCheckBox) findViewById(R.id.checkbox_cash);
        visa = (AppCompatCheckBox) findViewById(R.id.checkbox_visa);
        results = (TextView) findViewById(R.id.resultsTV);

        initToolbar();

    }

    private void initToolbar(){
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
        //tb.setBackgroundColor(this.getResources().getColor(R.color.green));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Signup");
    }

    public void signup(View v){
        showProgressDialog("Sign up ...");
        Resturant r = getObject();
        FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
        DatabaseReference ref = fbdb.getReference("resturants");
        String key = ref.push().getKey();
        ref.child(key).setValue(r, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                hideProgressDialog();
                if(databaseError == null) Log.e("AddResturantToFBDB","done :)");
                else Log.e("AddResturantToFBDB",databaseError.getMessage());
            }
        });
    }

    Resturant getObject(){
        String name = resturantName.getText().toString();
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        String ownerName = this.ownerName.getText().toString();
        String phoneNumber = this.phoneNumber.getText().toString();
        String address = this.address.getText().toString();
        boolean delivery = this.delivery.isChecked();
        boolean takeAway = this.takeAway.isChecked();
        boolean bookTable = this.bookTable.isChecked();
        boolean cash = this.cash.isChecked();
        boolean visa = this.visa.isChecked();
        Resturant r = new Resturant(name,email,password,ownerName,phoneNumber,address,delivery,takeAway,bookTable,cash,visa);
        return r;
    }

    public void getDeliveryOnly(View v){
        FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
        //fbdb.getReference("resturants").orderByChild("name").equalTo("obeida").addListenerForSingleValueEvent(new ValueEventListener() {
        fbdb.getReference("resturants").orderByChild("services/delivery").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String msg = "";
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Log.e("getDeliveryOnly", ds.getValue().toString());
                    Resturant r = ds.getValue(Resturant.class);
                    msg += r.getOwnerName()+"\n"+r.getPhoneNumber()+"\n\n";
                }
                results.setText(msg);
                Log.e("getDeliveryOnly","done :)");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void moveObjectsToActivity(View view) {
        Resturant r = getObject();
        Intent i = new Intent(this, MoveObjectsBetweenActivities1.class);
        i.putExtra("resturantObject",r);
        startActivity(i);
    }

    public void clicked_cash(View view) {
        cash.setChecked(!cash.isChecked());
    }

    public void clicked_visa(View view) {
        visa.setChecked(!visa.isChecked());
    }

    public void clicked_delivery(View view) {
        delivery.setChecked(!delivery.isChecked());
    }

    public void clicked_takeAway(View view) {
        takeAway.setChecked(!takeAway.isChecked());
    }

    public void clicked_bookTable(View view) {
        bookTable.setChecked(!bookTable.isChecked());
    }

    private void showProgressDialog(String title) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(title);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}