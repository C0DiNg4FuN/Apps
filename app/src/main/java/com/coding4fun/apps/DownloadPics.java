package com.coding4fun.apps;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.coding4fun.utils.RequestPackage;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by coding4fun on 24-Jul-16.
 */

public class DownloadPics extends AppCompatActivity {

    Toolbar tb;
    EditText imgName;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_pics);

        initToolbar();

        imgName = (EditText) findViewById(R.id.download_et);
        Button download = (Button) findViewById(R.id.download_btn);
        img = (ImageView) findViewById(R.id.download_img);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadPic().execute();
            }
        });

    }

    private void initToolbar(){
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
    }


    class DownloadPic extends AsyncTask<Void,Void,Bitmap>{

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(DownloadPics.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setMessage("Downloading...");
            pd.show();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                RequestPackage p = getRequestPackage();
                URL u = new URL(p.getUrl()+"?"+p.getEncodedParams());
                InputStream in = (InputStream) u.getContent();
                Bitmap b = BitmapFactory.decodeStream(in);
                in.close();
                return b;
            } catch (Exception ex){
                //return "{'status':'ERROR','reason':"+ex.getMessage()+"}";
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            pd.dismiss();
            if(result == null)
                Toast.makeText(DownloadPics.this, "Error!", Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(DownloadPics.this, "Pic downloaded successfully...", Toast.LENGTH_SHORT).show();
                img.setImageBitmap(result);
            }
        }

        RequestPackage getRequestPackage(){
            RequestPackage p = new RequestPackage();
            p.setMethod_GET();
            p.setUrl("http://www.coding4fun.96.lt/gif/gif2jpg.php");
            p.addParam("img",imgName.getText().toString());
            return p;
        }

    }

}