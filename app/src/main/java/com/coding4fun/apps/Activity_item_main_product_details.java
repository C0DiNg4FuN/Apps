package com.coding4fun.apps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by coding4fun on 22-Jan-17.
 */

public class Activity_item_main_product_details extends AppCompatActivity {

    private Toolbar tb;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_main_product_details);

        initToolbar();
        initNavigationDrawer();

        //the rest of the code here.....
    }

    private void initToolbar(){
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setTitle("whatever title");
    }

    private void initNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // passing toolbar is required to show up hamburger icon
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,tb,R.string.openDrawerContentDescRes,R.string.closeDrawerContentDescRes){
            @Override
            public void onDrawerOpened(View drawerView) {}//invalidateOptionsMenu();}
            @Override
            public void onDrawerClosed(View drawerView) {}//invalidateOptionsMenu();}
        };
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState(); // otherwise, hamburger icon wont show up! (animation won't work)
    }

}