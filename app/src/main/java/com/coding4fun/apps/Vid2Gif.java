package com.coding4fun.apps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.coding4fun.utils.AnimatedGifEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by coding4fun on 21-Aug-16.
 */

public class Vid2Gif extends AppCompatActivity implements View.OnClickListener {

    Toolbar tb;
    Button pickVid, gifify;
    WheelPicker fpsWheel;
    android.webkit.WebView wv;
    List<String> l;
    String path,duration;
    boolean ready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vid2gif);

        initToolbar();
        pickVid = (Button) findViewById(R.id.v2gPick);
        gifify = (Button) findViewById(R.id.v2gGifify);
        fpsWheel = (WheelPicker) findViewById(R.id.v2gWHEEL);
        wv = (android.webkit.WebView) findViewById(R.id.v2gWV);

        pickVid.setOnClickListener(this);
        gifify.setOnClickListener(this);

        l = new ArrayList<>();
        for(int i=1;i<=11;i+=2)
            l.add(i+"");
        fpsWheel.setData(l);
        fpsWheel.setVisibleItemCount(3);
        fpsWheel.setIndicator(true);
        fpsWheel.setIndicatorColor(Color.parseColor("#883D2175"));
        fpsWheel.setItemTextColor(Color.parseColor("#1e103a"));
        fpsWheel.setCurved(true);
        //wp.setAtmospheric(true);
        fpsWheel.setBackgroundColor(Color.parseColor("#55ffffff"));
        fpsWheel.setSelectedItemPosition(1);

        /*android.webkit.WebView wv = (android.webkit.WebView) findViewById(R.id.web_view);
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.loadUrl(url);*/

    }

    private void initToolbar(){
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.v2gPick:
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("video/*");
                startActivityForResult(Intent.createChooser(i,"Pick a video"),99);
                break;
            case R.id.v2gGifify:
                if(ready){new Vid2GifTask().execute();}
                else {Toast.makeText(this, "Not Ready yet !", Toast.LENGTH_SHORT).show();}
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 99){
                ready = false;
                Uri uri = data.getData();
                String mPath = getAbsolutePathFromUri(uri);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
                    String s="";
                    MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
                    mRetriever.setDataSource(mPath);
                    s=mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
                    if (s != null && !s.equals("") && s.startsWith("video")){
                        path = mPath;
                        s=mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        if (s != null && !s.equals("")){
                            duration = s;
                            ready = true;
                        }
                    } else{
                        Toast.makeText(this, "Unsupported video!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Your API version is "+android.os.Build.VERSION.SDK_INT+"\nIt should be >= 10", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    class Vid2GifTask extends AsyncTask<Void,Void,Boolean> {

        List<Bitmap> bitmaps = new ArrayList<>();
        long wheelValue;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            wheelValue = Long.parseLong(l.get(fpsWheel.getCurrentItemPosition()));
            pd = new ProgressDialog(Vid2Gif.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setMessage("Converting video to gif ...");
            pd.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
                MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
                mRetriever.setDataSource(path);
                long d = Long.parseLong(duration);
                long inc = 1000 / wheelValue;
                Log.e("path", path);
                Log.e("duration", d + " ms");
                for (long i = inc; i <= d; i += inc) {
                    Log.e("getting frame at ", (i*1000) + " micro second");
                    Bitmap b = mRetriever.getFrameAtTime(i*1000);
                    bitmaps.add(b);
                    //write frame
                    try {
                        FileOutputStream outStream = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Gify/vid2gif_"+i+".gif");
                        b.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        outStream.flush();
                        outStream.close();
                    } catch (IOException e) {
                        Log.e("Exception",e.getMessage());
                    }
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                AnimatedGifEncoder encoder = new AnimatedGifEncoder();
                encoder.setRepeat(0);
                //encoder.setFrameRate(5);
                encoder.setDelay(200);
                encoder.start(bos);
                for (Bitmap b : bitmaps){
                    encoder.addFrame(b);
                }
                encoder.finish();
                byte[] bytes = bos.toByteArray();
                //Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                try {
                    File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Gify/vid2gif.gif");
                    FileOutputStream fos = new FileOutputStream(f);
                    //bos.writeTo(fos);
                    fos.write(bytes);
                    fos.flush();
                    fos.close();
                    //bos.close();
                    return true;
                } catch (Exception e) {
                    Log.e("Exception",e.getMessage());
                    return false;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            pd.dismiss();
            Toast.makeText(Vid2Gif.this, (b) ? "Done" : "Error!", Toast.LENGTH_LONG).show();
        }
    }

}