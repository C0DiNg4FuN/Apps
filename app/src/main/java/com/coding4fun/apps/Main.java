package com.coding4fun.apps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.coding4fun.adapters.MainRVAdapter;
import com.coding4fun.models.MainRVRowModel;

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
        list.add(new MainRVRowModel("GIF in WebView","display animated GIF imside a WebView (needs internet!)",R.drawable.list_view,WebView.class));

        adapter = new MainRVAdapter(this,list);
        rv.setAdapter(adapter);

    }

    private void initToolbar() {
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
    }

    private void initRV() {
        rv = (RecyclerView) findViewById(R.id.mainRV);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        DefaultItemAnimator anim = new DefaultItemAnimator();
        anim.setAddDuration(500);
        anim.setRemoveDuration(500);
        rv.setItemAnimator(anim);
    }

}