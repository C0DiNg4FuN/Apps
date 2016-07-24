package com.coding4fun.apps;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.coding4fun.utils.HttpManager;
import com.coding4fun.utils.RequestPackage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by coding4fun on 17-Jul-16.
 */

public class UploadPics extends AppCompatActivity {

    Button upload;
    Toolbar tb;
    private static final int SELECT_PICTURE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_pics);

        initToolbar();
        upload = (Button) findViewById(R.id.upload_btn);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i,"Pick a pic"),SELECT_PICTURE);
                Toast.makeText(UploadPics.this, "Pick a pic", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar(){
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                Uri uri = data.getData();
                //get the selected file type. ex: image/png
                String type = getMimeType(uri);
                if (type == null)
                    Toast.makeText(UploadPics.this, "Unknown Error!", Toast.LENGTH_LONG).show();
                else if ( type.startsWith("image"))
                    new UploadFile(uri).execute();
                else
                    Toast.makeText(UploadPics.this, "Only Images are supported!", Toast.LENGTH_LONG).show();
                //Log.e("type",type);
            }
        }
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





    class UploadFile extends AsyncTask<Void,Void,String> {

        Uri uri;
        ProgressDialog pd;
        String path;
        File file;

        public UploadFile(Uri uri) {
            this.uri = uri;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //get image path, name, and size
            path = getAbsolutePathFromUri(uri);
            // advise for self >> never say: File f = new File(uri.getPath())
            file = new File(path);
            String fileName = file.getName();
            long fileSize = file.length();
            String size = (fileSize >= (1024*1024)) ? (fileSize/(1024*1024d))+" MB" : (fileSize/(1024d))+" KB";

            //initialize progress dialog, and set its msg to file name & size
            pd = new ProgressDialog(UploadPics.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setMessage("Uploading "+fileName+" ("+size+") ...");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            /*String path = uri.getPath();
            String fileName = getFileNameFromUri(uri);
            long fileSize = getFileSizeFromUri(uri);
            String size = (fileSize >= (1024*1024)) ? (fileSize/(1024*1024))+" MB" : (fileSize/(1024))+" KB";
            Log.e("fileName from uri",fileName);
            Log.e("fileSize from uri",fileSize+"");
            Log.e("fileSize with KB or MB",size);

            File f = new File(path);
            Log.e("fileName from File",f.getName());
            Log.e("fileSize from File",f.length()+"");

            File ff = new File(getAbsolutePathFromUri(uri));
            Log.e("fileName from abs",ff.getName());
            Log.e("fileSize from abs",ff.length()+"");*/

            //String response = HttpConnection.readUrl(getUrl());
            String response = HttpManager.getData(getRequestPackage());
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            Log.e("result",result);
            JSONObject jo = null;
            try {
                jo = new JSONObject(result);
                if(jo.getString("status").equals("OK"))
                    Toast.makeText(UploadPics.this, "Picture uploaded successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(UploadPics.this, "Error uploading picture !", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Toast.makeText(UploadPics.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


        String getUrl(){
            String encodedFile = file2base64(path);
            Log.e("encoded string",encodedFile);
            String URIencoded = Uri.encode(encodedFile);
            Log.e("string (uri encoded)",URIencoded);
            try {
                String URLencoded = URLEncoder.encode(encodedFile,"UTF-8");
                Log.e("string (url encoded)",URLencoded);
            } catch (UnsupportedEncodingException e) {}
            String name = getFileNameWithoutExtension(file);
            name = Uri.encode(name);
            String url = "http://www.coding4fun.96.lt/gif/main.php?what=uploadGIF&name="+name+"&encoded_string="+encodedFile;
            Log.e("url","http://www.coding4fun.96.lt/gif/main.php?what=uploadGIF&name="+name+"&encoded_string=");
            return url;
        }

        RequestPackage getRequestPackage(){
            RequestPackage p = new RequestPackage();
            p.setMethod_POST();
            p.setUrl("http://www.coding4fun.96.lt/gif/main.php");
            p.addParam("what","uploadGIF");
            p.addParam("name",getFileNameWithoutExtension(file));
            p.addParam("encoded_string",file2base64(path));
            return p;
        }

        String file2base64(String path) {
            String base64 = null;
            try {
                File file = new File(path);
                FileInputStream fis = new FileInputStream(file);
                byte[] bytes = new byte[(int)file.length()];
                fis.read(bytes);
                base64 = Base64.encodeToString(bytes,Base64.DEFAULT);
            } catch (IOException e) {
                Toast.makeText(UploadPics.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return base64;
        }

        String getFileNameWithoutExtension(File f){
            String name = f.getName().substring(0, f.getName().lastIndexOf("."));
            return name;
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

    }

}