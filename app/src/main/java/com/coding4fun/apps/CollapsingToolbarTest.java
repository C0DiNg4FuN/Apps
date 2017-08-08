package com.coding4fun.apps;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by coding4fun on 06-Nov-16.
 */

public class CollapsingToolbarTest extends AppCompatActivity {

    Toolbar tb;
    CollapsingToolbarLayout ctb;
    FloatingActionButton fab;
    ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collapsing_toolbar_with_image);

        initToolbar();
        fab = (FloatingActionButton) findViewById(R.id.fab_collapse_upload);
        image = (ImageView) findViewById(R.id.collapsing_toolbar_image);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i,"Pick a pic"),99);
                Toast.makeText(CollapsingToolbarTest.this, "Pick a pic", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar(){
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ctb = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ctb.setTitle("TEEEEEST");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 99 &&resultCode == RESULT_OK){
            Uri uri = data.getData();
            String path = getAbsolutePathFromUri(uri);
            File f = new File(path);
            ctb.setTitle(f.getName());
            Bitmap b = BitmapFactory.decodeFile(path);
            image.setImageBitmap(b);
            //b.recycle();
            //b = null;
        }
    }

    private String getAbsolutePathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
}