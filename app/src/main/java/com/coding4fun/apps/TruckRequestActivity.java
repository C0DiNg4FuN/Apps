package com.coding4fun.apps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.coding4fun.adapters.TruckRequestRVadapter;
import com.coding4fun.models.TruckRequest;
import com.coding4fun.models.TruckSubRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by coding4fun on 03-Jul-17.
 */

public class TruckRequestActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar tb;
    @BindView(R.id.truck_request_activity_RV) RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truck_request_activity);
        ButterKnife.bind(this);

        initToolbar();
        initRV();

        TruckRequestRVadapter adapter = new TruckRequestRVadapter(this, getTruckRequests());
        rv.setAdapter(adapter);
    }

    private void initToolbar(){
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setTitle("Truck Requests");
    }

    private void initRV(){
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv.setHasFixedSize(true);
        DefaultItemAnimator anim = new DefaultItemAnimator();
        anim.setAddDuration(500);
        anim.setRemoveDuration(500);
        rv.setItemAnimator(anim);
        //rv.setNestedScrollingEnabled(true);
        /*rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return onInterceptTouchEvent(e);
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });*/
    }

    private List<TruckRequest> getTruckRequests() {
        List<TruckRequest> requests = new ArrayList<>();

        List<TruckSubRequest> sub1 = new ArrayList<>();
        sub1.add(new TruckSubRequest());
        sub1.add(new TruckSubRequest());
        sub1.add(new TruckSubRequest());

        List<TruckSubRequest> sub2 = new ArrayList<>();
        sub2.add(new TruckSubRequest());

        List<TruckSubRequest> sub3 = new ArrayList<>();
        sub3.add(new TruckSubRequest());
        sub3.add(new TruckSubRequest());

        requests.add(new TruckRequest(sub1));
        requests.add(new TruckRequest(sub2));
        requests.add(new TruckRequest(sub3));

        return requests;
    }
}