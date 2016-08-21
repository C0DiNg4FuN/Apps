package com.coding4fun.apps;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coding4fun on 14-Aug-16.
 */

public class Wheel extends AppCompatActivity implements View.OnClickListener {

    WheelPicker wp,dialog_wp;
    List<String> list;
    Dialog d;
    Button showDialpg,pickGIF,upload;
    TextView gifName;
    Uri uri;
    boolean ready2upload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wheel);

        wp = (WheelPicker) findViewById(R.id.wheel1);
        list = new ArrayList<>();
        list.add("computer");
        list.add("SiliconValley");
        list.add("Mr.Robot");
        list.add("coding");
        list.add("movies");
        list.add("math");
        list.add("whatever");
        wp.setData(list);

        d = new Dialog(this);
        d.setContentView(R.layout.dialog);
        d.setTitle("Pick GIF & category");

        showDialpg = (Button) findViewById(R.id.show_dialog);
        gifName = (TextView) d.findViewById(R.id.dialog_tv);
        pickGIF = (Button) d.findViewById(R.id.dialog_btn);
        upload = (Button) d.findViewById(R.id.dialog_upload);
        dialog_wp = (WheelPicker) d.findViewById(R.id.dialog_wheel);
        dialog_wp.setData(list);

        showDialpg.setOnClickListener(this);
        pickGIF.setOnClickListener(this);
        upload.setOnClickListener(this);

        wp.setVisibleItemCount(5);
        wp.setIndicator(true);
        wp.setIndicatorColor(Color.parseColor("#883D2175"));
        wp.setItemTextColor(Color.parseColor("#1e103a"));
        //wp.setCurtain(true);
        //wp.setCurtainColor(Color.parseColor("#"));
        wp.setCurved(true);
        //wp.setAtmospheric(true);
        wp.setBackgroundColor(Color.parseColor("#55ffffff"));
        wp.setSelectedItemPosition(2);

        wp.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                Toast.makeText(Wheel.this, list.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        ContentResolver cr = getContentResolver();
        mimeType = cr.getType(uri);
        if (mimeType==null || mimeType.equals("")) {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private String getFileNameFromUri(Uri uri) {
        Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        return returnCursor.getString(nameIndex);
    }

    private long getFileSizeFromUri(Uri uri) {
        Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        return returnCursor.getLong(sizeIndex);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.show_dialog:
                d.show();
                break;
            case R.id.dialog_btn:
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/gif");
                startActivityForResult(Intent.createChooser(i,"Pick a pic"),11);
                Toast.makeText(this, "Pick a pic", Toast.LENGTH_SHORT).show();
                break;
            case R.id.dialog_upload:
                if(ready2upload){
                    ready2upload = false;
                    d.dismiss();
                    String name = getFileNameFromUri(uri);
                    long fileSize = getFileSizeFromUri(uri);
                    String size = (fileSize >= (1024*1024)) ? (fileSize/(1024*1024d))+" MB" : (fileSize/(1024d))+" KB";
                    Toast.makeText(this, "Uploading "+name+" ("+size+")\nCategory: "+list.get(dialog_wp.getCurrentItemPosition()), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Pick a valid GIF", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 11){
                Uri uri = data.getData();
                String type = getMimeType(uri);
                if (type == null){
                    Toast.makeText(this, "Unknown Error!", Toast.LENGTH_LONG).show();
                    ready2upload = false;
                }
                else if (type.toLowerCase().equals("image/gif")){
                    Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();
                    ready2upload = true;
                    this.uri = uri;
                    gifName.setText(getFileNameFromUri(this.uri));
                }
                else{
                    Toast.makeText(this, "Only GIFs are supported!", Toast.LENGTH_LONG).show();
                    ready2upload = false;
                }
            }
        }
    }

}