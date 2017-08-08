package com.coding4fun.apps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.coding4fun.adapters.MainRVAdapter;
import com.coding4fun.models.MainRVRowModel;
import com.coding4fun.services.MyFirebaseMessagingService;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {

    Toolbar tb;
    RecyclerView rv;
    MainRVAdapter adapter;
    List<MainRVRowModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initRV();

        list.add(new MainRVRowModel("Recycler View","with progress bar at the bottom appearing when loading data",R.drawable.list_view,RVwithProgressBar.class));
        list.add(new MainRVRowModel("GIF in WebView","display animated GIF inside a WebView (needs internet!)",R.drawable.list_view,WebView.class));
        list.add(new MainRVRowModel("Upload to server","upload any image to an online server",android.R.drawable.ic_menu_upload,UploadPics.class));
        list.add(new MainRVRowModel("Download pic","download a GIF preview in jpg and view it",android.R.drawable.stat_sys_download,DownloadPics.class));
        list.add(new MainRVRowModel("Firebase","Firebase Cloud Messaging demo",R.drawable.firebase_logo,FirebaseActivity.class));
        list.add(new MainRVRowModel("Wheel","Wheel picker demo",R.drawable.list_view,Wheel.class));
        list.add(new MainRVRowModel("Extract MetaData","Extract MetaData demo",R.drawable.list_view,ExtractMetaData.class));
        list.add(new MainRVRowModel("Vid 2 GIF","Convert video to animated gif",R.drawable.list_view,Vid2Gif.class));
        list.add(new MainRVRowModel("TouchPlayground","get touch coordinates and simulate touch",R.drawable.list_view,TouchPlayground.class));
        list.add(new MainRVRowModel("Mouse","Control pc mouse using smart phone",R.drawable.mouse,Mouse.class));
        list.add(new MainRVRowModel("FilesTree","list files tree",R.drawable.list_view,FilesTree.class));
        list.add(new MainRVRowModel("CallMsgLogs","Show calls & SMSs logs",R.drawable.list_view,CallMsgLogs.class));
        list.add(new MainRVRowModel("ServiceTest","test intent service",R.drawable.list_view,ServiceTest.class));
        list.add(new MainRVRowModel("ShellCommands","testing shell commands with and without root",R.drawable.list_view,ShellCommands.class));
        list.add(new MainRVRowModel("Dummy_1","",R.drawable.list_view,Dummy_1.class));
        list.add(new MainRVRowModel("Firebase Upload","",R.drawable.firebase_logo,UploadByFirebase.class));
        list.add(new MainRVRowModel("Firebase Database","",R.drawable.firebase_logo,FirebaseDB.class));
        list.add(new MainRVRowModel("AddResturantToFBDB","",R.drawable.firebase_logo,AddResturantToFBDB.class));
        list.add(new MainRVRowModel("FireBaseAuthTest1","User creating using email & password + auth listener",R.drawable.firebase_logo,FireBaseAuthTest1.class));
        list.add(new MainRVRowModel("FirebaseTestOffline","Testing fetching data offline from local cache",R.drawable.firebase_logo,FirebaseTestOffline.class));
        list.add(new MainRVRowModel("RestuarantMainActivity","",R.drawable.driver,RestuarantMainActivity.class));
        list.add(new MainRVRowModel("CollapsingToolbarTest","Testing Collapsing Toolbar + parallax + fab",R.drawable.list_view,CollapsingToolbarTest.class));
        list.add(new MainRVRowModel("QR code generator","Generate QR code based on particular string",R.drawable.qr_code,QRcodeGenerator.class));
        list.add(new MainRVRowModel("BarcodeScanner","Scan barcode (with different formats such as QR...)",R.drawable.qr_code,BarcodeScanner.class));
        list.add(new MainRVRowModel("Firebase DB","testing real-time firebase database feature",R.drawable.list_view,FBDB_listenners.class));
        list.add(new MainRVRowModel("Notification","customize notifications",R.drawable.qr_code,Notif.class));
        list.add(new MainRVRowModel("FullScreenDialog","Show customized full screen dialog showing date & time pickers...",R.drawable.qr_code,FullScreenDialog.class));
        list.add(new MainRVRowModel("SMS","Testing sms API...",R.drawable.list_view,SMS.class));
        list.add(new MainRVRowModel("Navigation Drawer","Navigation Drawer Demo....",R.drawable.list_view,Activity_item_main_product_details.class));
        list.add(new MainRVRowModel("PController","Control PC mouse,keyboard, etc wirelessly...",R.drawable.mouse,PController_Activity.class));
        list.add(new MainRVRowModel("Build Variant","Testing build variants",R.drawable.list_view,BuildVariantsTest.class));
        list.add(new MainRVRowModel("Voice Commands","Testing Voice Commands to control the app",R.drawable.list_view,VoiceCommands.class));
        list.add(new MainRVRowModel("Truck Requests",".....",R.drawable.list_view,TruckRequestActivity.class));
        list.add(new MainRVRowModel("Ably Test",".....",R.drawable.ably_logo,AblyTest.class));

        adapter = new MainRVAdapter(this,list);
        rv.setAdapter(adapter);

        Intent i = new Intent(this, MyFirebaseMessagingService.class);
        startService(i);

    }

    private void initToolbar() {
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
    }

    private void initRV() {
        rv = (RecyclerView) findViewById(R.id.mainRV);
        //rv.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        rv.setLayoutManager(llm);

        rv.setHasFixedSize(true);
        DefaultItemAnimator anim = new DefaultItemAnimator();
        anim.setAddDuration(500);
        anim.setRemoveDuration(500);
        rv.setItemAnimator(anim);
    }

}