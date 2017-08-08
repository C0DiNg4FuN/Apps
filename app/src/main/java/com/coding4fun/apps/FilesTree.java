package com.coding4fun.apps;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

/**
 * Created by coding4fun on 11-Sep-16.
 */

public class FilesTree extends AppCompatActivity {

    Button listBTN;
    TextView listTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_tree);

        listBTN = (Button) findViewById(R.id.fileTreeBTN);
        listTV = (TextView) findViewById(R.id.fileTreeTV);
        listTV.setMovementMethod(new ScrollingMovementMethod());

        listBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String filesTree = listFilesTree(Environment.getRootDirectory().getAbsoluteFile(),"");
                String filesTree = listFilesTree(Environment.getExternalStorageDirectory().getAbsoluteFile(),"");
                listTV.setText(filesTree);
            }
        });
    }

    String listFilesTree(File root, String append){
        append += "__";
        String tree = "";
        for(File f : root.listFiles()){
            tree += (f.isDirectory()) ? "|"+append+f.getName()+":\n"+listFilesTree(f,append) : "|"+append+f.getName()+"\n";
        }
        return tree;
    }
}