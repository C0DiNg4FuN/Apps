package com.coding4fun.apps;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by coding4fun on 21-Aug-16.
 */

public class ExtractMetaData extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extract_metadata);

        tv = (TextView) findViewById(R.id.extractTV);
        Button btn = (Button) findViewById(R.id.extractBTN);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i,"Pick a gif"),99);
            }
        });
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 99){
                Uri uri = data.getData();
                String path = getAbsolutePathFromUri(uri);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
                    String msg = "",s="";
                    MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
                    mRetriever.setDataSource(path);
                    s=mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
                    if (s != null && !s.equals("")){msg += "Date: "+s;}
                    s=mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    if (s != null && !s.equals("")){msg += "Duration: "+s;}
                    s=mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
                    if (s != null && !s.equals("")){msg += "Mimetype: "+s;}
                    s=mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
                    if (s != null && !s.equals("")){msg += "GENRE: "+s;}
                    s=mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO);
                    if (s != null && !s.equals("")){msg += "Has Audio: "+s;}
                    s=mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
                    if (s != null && !s.equals("")){msg += "Has Video: "+s;}
                    s=mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                    if (s != null && !s.equals("")){msg += "Video Width: "+s;}
                    s=mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                    if (s != null && !s.equals("")){msg += "Video Height: "+s;}
                    tv.setText(msg);
                } else {
                    tv.setText("Your API version is "+android.os.Build.VERSION.SDK_INT+"\nIt should be >= 10");
                }
            }
        }
    }

}