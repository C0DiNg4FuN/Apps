package com.coding4fun.apps;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by coding4fun on 30-May-17.
 */

public class PowerpointFragment extends Fragment {

    private PController controller;
    private TextView next, previous;
    private Mouse mouseCallback;

    public PowerpointFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pcontroller_ppt_fragment,container,false);
        next = (TextView) v.findViewById(R.id.ppt_next);
        previous = (TextView) v.findViewById(R.id.ppt_previous);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = PController.getInstance();
        setHasOptionsMenu(true);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("coding4fun ppt next");
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("coding4fun ppt previous");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mouseCallback = (Mouse) context;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_mouse, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_mouse){
            mouseCallback.onMouse();
            return true;
        }
        return false;
    }

    private void sendCommand(String cmd){
        if(controller!= null && controller.isReady()){
            controller.sendUdpBroadcast(cmd);
        }
    }

    public interface Mouse{
        void onMouse();
    }
}