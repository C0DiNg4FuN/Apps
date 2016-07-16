package com.coding4fun.apps;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.coding4fun.adapters.GifRVAdapter;
import com.coding4fun.models.GifRowModel;
import com.coding4fun.utils.HttpConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coding4fun on 14-Jul-16.
 */

public class RVwithProgressBar extends AppCompatActivity {

    Toolbar tb;
    RecyclerView rv;
    GifRVAdapter adapter;
    List<GifRowModel> list = new ArrayList<>();
    private int offset;
    private boolean moreDate = true;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_with_progress_bar);

        CoordinatorLayout col = (CoordinatorLayout) findViewById(R.id.coLayout);
        initToolbar();
        initRV();

        //set scroll listener to detect when the last item is visible to show prpgress bar and get new data
        //src: https://codentrick.com/load-more-recyclerview-bottom-progressbar/
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && moreDate && totalItemCount-1 <= (lastVisibleItem + 0)) {
                    new GetGIFs().execute();
                }
            }
        });

        //list.add(null);
        adapter = new GifRVAdapter(this,list,col);
        rv.setAdapter(adapter);

        //get the first 20 gif from server
        offset = 0;
        new GetGIFs().execute();

    }

    private void initToolbar(){
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
    }

    private void initRV(){
        rv = (RecyclerView) findViewById(R.id.RVwithPB);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        DefaultItemAnimator anim = new DefaultItemAnimator();
        anim.setAddDuration(500);
        anim.setRemoveDuration(500);
        rv.setItemAnimator(anim);
    }



    class GetGIFs extends AsyncTask<Void,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list.add(null); //to show progress bar
            adapter.notifyItemInserted(list.size()-1);
            isLoading = true;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = "http://www.coding4fun.96.lt/gif/main.php?what=getNames&offset="+offset;
            String response = HttpConnection.readUrl(url);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                //remove the progress bar (the last item)
                list.remove(list.size()-1);
                isLoading = false;
                //parse json response
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                if(status.equals("OK")){
                    //update offset, and add the new gifs
                    offset += 20; //cz each request gets 20 item
                    JSONArray ja = jo.getJSONArray("names");
                    for(int i=0;i<ja.length();i++)
                        list.add(new GifRowModel(ja.getString(i)));
                } else {
                    String reason = jo.getString("reason");
                    if(reason.equals("No more data!")){
                        moreDate = false;
                        Toast.makeText(RVwithProgressBar.this, "No more data!", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(RVwithProgressBar.this, reason, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(RVwithProgressBar.this, "ERROR: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
        }
    }

}