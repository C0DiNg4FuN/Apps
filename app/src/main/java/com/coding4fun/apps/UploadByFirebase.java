package com.coding4fun.apps;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/*
    Firebase storage: upload & download
      1. compile 'com.google.firebase:firebase-storage:9.2.0'
      2. Set up Firebase Storage by creating an instance of FirebaseStorage
      3. Create a Storage Reference
      4. upload
      5. download
 */

public class UploadByFirebase extends AppCompatActivity {

    private static final int SELECT_PICTURE = 11;
    FirebaseStorage storage;
    StorageReference ref, gifsRef, newGifReg;
    ProgressDialog mProgressDialog;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_by_firebase);

        // 2
        storage = FirebaseStorage.getInstance();

        // 3
        ref = storage.getReferenceFromUrl("gs://apps-76724.appspot.com");
        // Create a child reference
        // imagesRef now points to "images"
        //gifsRef = ref.child("gifs");
    }

    public void fab_upload(View view) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i,"Pick a gif"),SELECT_PICTURE);
        Toast.makeText(this, "Pick a gif", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                Uri uri = data.getData();
                String type = getMimeType(uri);
                if (type == null)
                    Toast.makeText(this, "Unknown Error!", Toast.LENGTH_LONG).show();
                else if (type.toLowerCase().equals("image/gif"))
                    uploadFileUsingFirebase(uri); // 4
                else
                    Toast.makeText(this, "Only GIFs are supported!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadFileUsingFirebase(Uri uri) {
        Log.e("LastPathSegment",uri.getLastPathSegment());
        showProgressDialog("Uploading...");
        File f = new File(getAbsolutePathFromUri(uri));
        Uri u = Uri.fromFile(f);
        //newGifReg = ref.child("gifs/"+uri.getLastPathSegment());
        newGifReg = ref.child("gifs/"+uri.getLastPathSegment());
        StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/gif").build();
        UploadTask uploadTask = newGifReg.putFile(uri,metadata); // metadata is optional
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UploadByFirebase.this, "Done Uploading", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(UploadByFirebase.this, "ERROR uploading!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                mProgressDialog.setMessage("Uploading... "+(int)progress+"%");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UploadByFirebase.this, "Uploading paused!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 5
    public void downloadFileUsingFirebase(View view) {
        showProgressDialog("downloading...");
        String path = ((EditText) findViewById(R.id.filePathET)).getText().toString();
        StorageReference downloadRef = ref.child(path);
        /*downloadRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Uri.Builder b = uri.buildUpon();
                String url = b.toString();
                Log.e("download URL 2",url);
                Log.e("download URL",uri.getEncodedPath());
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, url);
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
            }
        });*/

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + path.substring(path.lastIndexOf("/")+1,path.length()));
        downloadRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                hideProgressDialog();
                Toast.makeText(UploadByFirebase.this, "Done downloading", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                Toast.makeText(UploadByFirebase.this, "Error!\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                mProgressDialog.setMessage("Downloading...\n"+(int)progress+"%");
            }
        });
    }

    // 5
    public void downloadBitmapUsingFirebase(View view) {
        showProgressDialog("downloading...");
        String path = ((EditText) findViewById(R.id.filePathET)).getText().toString();
        StorageReference downloadRef = ref.child(path);

        //getBytes has no onProgress & onPaused, but getFile & getStream have...

        /*
        downloadRef.getStream(new StreamDownloadTask.StreamProcessor() {
            @Override
            public void doInBackground(StreamDownloadTask.TaskSnapshot taskSnapshot, InputStream inputStream) throws IOException {
                Log.e("download","doInBackground");
                Log.e("getBytesTransferred", taskSnapshot.getBytesTransferred()+"");
                b = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            }
        }).addOnSuccessListener(new OnSuccessListener<StreamDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(StreamDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("download","onSuccess");
                ((ImageView) findViewById(R.id.firebaseIV)).setImageBitmap(b);
            }
        }).addOnProgressListener(new OnProgressListener<StreamDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(StreamDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("download","onProgress: " + taskSnapshot.getBytesTransferred());
            }
        });
         */

        downloadRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap b = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                ((ImageView) findViewById(R.id.firebaseIV)).setImageBitmap(b);
                hideProgressDialog();
                bitmap = b;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                hideProgressDialog();
                Toast.makeText(UploadByFirebase.this, "ERROR uploading!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
                bitmap = null;
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

    private String getMimeType(Uri uri) {
        String mimeType = null;
        ContentResolver cr = getContentResolver();
        mimeType = cr.getType(uri);
        if (mimeType==null || mimeType.equals("")) {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private void showProgressDialog(String title) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(title);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}