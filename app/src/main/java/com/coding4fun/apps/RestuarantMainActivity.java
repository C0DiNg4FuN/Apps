package com.coding4fun.apps;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.coding4fun.adapters.ViewPagerAdapter;

/**
 * Created by coding4fun on 25-Oct-16.
 */

public class RestuarantMainActivity extends AppCompatActivity {

    Toolbar tb;
    ViewPager vp;
    ViewPagerAdapter vAdapter;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_main_activity);

        initToolbar();
        setupViewPager();
        initTabLayout();
        //customizeTabs();
    }

    private void customizeTabs() {
        TextView t1 = (TextView) LayoutInflater.from(this).inflate(R.layout.tab,null);
        t1.setText("RESTAURANTS");
        tabLayout.getTabAt(0).setCustomView(t1);
        TextView t2 = (TextView) LayoutInflater.from(this).inflate(R.layout.tab,null);
        t2.setText("FOOD ITEMS");
        tabLayout.getTabAt(1).setCustomView(t2);
    }

    private void initToolbar(){
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
    }


    private void setupViewPager() {
        vp = (ViewPager) findViewById(R.id.viewPager);
        vAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        vAdapter.addFrag(new RestaurantFragment(), "T1");
        vAdapter.addFrag(new CategoryFragment(), "CATEGORIES");
        vAdapter.addFrag(new IngredientFragment(), "INGREDIENTS");
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(vAdapter);
        //vp.setCurrentItem(1);
    }

    private void initTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(vp);
        vp.setCurrentItem(1);
    }

}