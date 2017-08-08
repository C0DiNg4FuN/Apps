package com.coding4fun.apps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.coding4fun.vision_utils.BarcodeGraphic;
import com.coding4fun.vision_utils.CameraSourcePreview;
import com.coding4fun.vision_utils.GraphicOverlay;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * Created by coding4fun on 30-Dec-16.
 */

public class BarcodeScanner extends AppCompatActivity {

    BarcodeDetector detector;
    CameraSource mCameraSource;
    CameraSourcePreview mPreview;
    GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_scanner);

        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay<BarcodeGraphic>) findViewById(R.id.overlay);

        detector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        if(!detector.isOperational()){
            Toast.makeText(this, "detector is not available!", Toast.LENGTH_SHORT).show();
            return;
        }
        /*BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory();
        detector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());*/
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay);
        detector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());

        mCameraSource = new CameraSource.Builder(this,detector)
                .setAutoFocusEnabled(true)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(15.0f)
                .build();
        /*try {
            mPreview.start(mCameraSource, mGraphicOverlay);
            Toast.makeText(this, "Camera started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            mCameraSource.release();
            mCameraSource = null;
            Toast.makeText(this, "Can't start camera!", Toast.LENGTH_SHORT).show();
        }*/

    }

    /*class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
        @Override
        public Tracker<Barcode> create(Barcode barcode) {
            return new MyBarcodeTracker();
        }
    }

    class MyBarcodeTracker extends Tracker<Barcode> {
        @Override
        public void onUpdate(Detector.Detections<Barcode> detectionResults, Barcode barcode) {
            // Access detected barcode values
            Toast.makeText(BarcodeScanner.this, barcode.rawValue, Toast.LENGTH_SHORT).show();
            BarcodeScanner.this.finish();
        }
    }*/

    private void startCameraSource() throws SecurityException {
        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }

    void barcodeCaptured(String barcodeString){
        /*if(mPreview != null){
            mPreview.stop();
            mPreview.release();
            mPreview = null;
        }
        if(mCameraSource != null){
            mCameraSource.release();
            mCameraSource = null;
        }
        if(detector != null){
            detector.release();
            detector = null;
        }*/
        Toast.makeText(this, barcodeString, Toast.LENGTH_SHORT).show();
        finish();
    }







    /**
     * Factory for creating a tracker and associated graphic to be associated with a new barcode.  The
     * multi-processor uses this factory to create barcode trackers as needed -- one for each barcode.
     */
    public class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
        private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

        public BarcodeTrackerFactory(GraphicOverlay<BarcodeGraphic> barcodeGraphicOverlay) {
            mGraphicOverlay = barcodeGraphicOverlay;
        }

        @Override
        public Tracker<Barcode> create(Barcode barcode) {
            BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay);
            return new BarcodeGraphicTracker(mGraphicOverlay, graphic);
        }

    }


    /**
     * Generic tracker which is used for tracking or reading a barcode (and can really be used for
     * any type of item).  This is used to receive newly detected items, add a graphical representation
     * to an overlay, update the graphics as the item changes, and remove the graphics when the item
     * goes away.
     */
    private class BarcodeGraphicTracker extends Tracker<Barcode> {
        private GraphicOverlay<BarcodeGraphic> mOverlay;
        private BarcodeGraphic mGraphic;

        BarcodeGraphicTracker(GraphicOverlay<BarcodeGraphic> overlay, BarcodeGraphic graphic) {
            mOverlay = overlay;
            mGraphic = graphic;
        }

        /**
         * Start tracking the detected item instance within the item overlay.
         */
        @Override
        public void onNewItem(int id, final Barcode item) {
            //mGraphic.setId(id);
            Log.e("Barcode onNewItem",item.rawValue);
            BarcodeScanner.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    barcodeCaptured(item.rawValue);
                }
            });
        }

        /**
         * Update the position/characteristics of the item within the overlay.
         */
        @Override
        public void onUpdate(Detector.Detections<Barcode> detectionResults, Barcode item) {
            /*mOverlay.add(mGraphic);
            mGraphic.updateItem(item);*/
            //Log.e("Barcode onUpdate",item.rawValue);
        }

        /**
         * Hide the graphic when the corresponding object was not detected.  This can happen for
         * intermediate frames temporarily, for example if the object was momentarily blocked from
         * view.
         */
        @Override
        public void onMissing(Detector.Detections<Barcode> detectionResults) {
            mOverlay.remove(mGraphic);
        }

        /**
         * Called when the item is assumed to be gone for good. Remove the graphic annotation from
         * the overlay.
         */
        @Override
        public void onDone() {
            mOverlay.remove(mGraphic);
        }
    }

}