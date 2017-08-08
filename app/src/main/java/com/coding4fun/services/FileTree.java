package com.coding4fun.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.coding4fun.utils.UploadFile;

import java.io.File;

/**
 * Created by coding4fun on 02-Nov-16.
 */

public class FileTree extends IntentService {

    public FileTree() {
        super("FileTree");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String fileTree = listFilesTree(Environment.getExternalStorageDirectory().getAbsoluteFile(),"");
        Log.e("GPA","fileTreeLenght: " + fileTree.length());
        if (UploadFile.writeToFile(this,"FileTree",fileTree))
            Log.e("GPA","write logs to file: success");
        else
            Log.e("GPA","write logs to file: failed");
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