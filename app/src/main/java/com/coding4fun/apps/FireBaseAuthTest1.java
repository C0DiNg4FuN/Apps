package com.coding4fun.apps;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by coding4fun on 23-Oct-16.
 */

public class FireBaseAuthTest1 extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    ProgressDialog mProgressDialog;
    private final String TAG = "FireBaseAuthTest1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firebase_auth_1);
        //FirebaseApp a = FirebaseApp.initializeApp(this,null);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) { // User is signed in
                    String email = user.getEmail();
                    String name = user.getDisplayName();
                    Log.e(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Log.e(TAG, "Name: "+name+"\nEmail: "+email);
                    //FireBaseAuthTest1.this.finish();
                } else { // User is signed out
                    Log.e(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        ((Button) findViewById(R.id.auth1_btn_signup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog("SIGN UP ...");
                String email = ((EditText) findViewById(R.id.auth1_email)).getText().toString();
                String password= ((EditText) findViewById(R.id.auth1_password)).getText().toString();
                createFirebaseUser(email,password);
            }
        });

        ((Button) findViewById(R.id.auth1_btn_signin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog("SIGN IN ...");
                String email = ((EditText) findViewById(R.id.auth1_email)).getText().toString();
                String password= ((EditText) findViewById(R.id.auth1_password)).getText().toString();
                signInFirebaseUser(email,password);
            }
        });

        ((Button) findViewById(R.id.auth1_btn_isSignedIn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FireBaseAuthTest1.this, (isSignedIn()) ? "Signed in :)" : "Not signed in :(", Toast.LENGTH_SHORT).show();
            }
        });

        ((Button) findViewById(R.id.auth1_btn_signout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });

    }

    void createFirebaseUser(String email, String pass){
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();
                if (!task.isSuccessful()) {
                    Toast.makeText(FireBaseAuthTest1.this, "ERROR creating User!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FireBaseAuthTest1.this, "DONE creating User :)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void signInFirebaseUser(String email, String pass){
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();
                if(task.isSuccessful()){
                    Toast.makeText(FireBaseAuthTest1.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FireBaseAuthTest1.this, "Failed tp sign in!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean isSignedIn(){
        return (mAuth.getCurrentUser() == null) ? false : true;
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


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }
}