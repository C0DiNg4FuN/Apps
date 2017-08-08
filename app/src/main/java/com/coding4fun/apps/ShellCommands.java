package com.coding4fun.apps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

/**
 * Created by coding4fun on 15-Sep-16.
 */

public class ShellCommands extends AppCompatActivity implements View.OnClickListener {

    Button executeRoot,execute;
    EditText commandET;
    TextView outputTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shell_commands);

        executeRoot = (Button) findViewById(R.id.executeRootBTN);
        execute = (Button) findViewById(R.id.executeBTN);
        commandET = (EditText) findViewById(R.id.commandET);
        outputTV = (TextView) findViewById(R.id.commandOutputTV);

        execute.setOnClickListener(this);
        executeRoot.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String command = commandET.getText().toString();
        switch (view.getId()){
            case R.id.executeBTN:
                outputTV.setText(execute(command));
                break;
            case R.id.executeRootBTN:
                outputTV.setText(executeRoot(command));
                break;
        }
    }

    String execute(String command){
        try {
            Log.e("shell commands","start executing...");
            Process shell = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(shell.getInputStream()));
            String line = "", output = "";
            while ((line = reader.readLine()) != null) {
                output += line+"\n";
            }
            shell.waitFor();
            Log.e("shell commands","DONE executing...");
            return output;
        } catch (Exception e) {
            Log.e("shell commands","ERROR!");
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return "ERROR!";
        }
    }

    String executeRoot(String command){
        try {
            Log.e("shell commands","Start executing...");
            Process su = Runtime.getRuntime().exec("su",null,null);
            DataOutputStream os = new DataOutputStream(su.getOutputStream());
            os.writeBytes(command);
            os.flush();
            os.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(su.getInputStream()));
            String line = "", output = "";
            while ((line = reader.readLine()) != null) {
                output += line+"\n";
            }
            su.waitFor();
            Log.e("shell commands","DONE executing...");
            return output;
        } catch (Exception e) {
            Log.e("shell commands","ERROR!");
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return "";
        }
    }

}