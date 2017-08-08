package com.coding4fun.apps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.realtime.ChannelStateListener;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.Message;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by coding4fun on 07-Aug-17.
 */

public class AblyTest extends AppCompatActivity {

    private Toolbar tb;
    private LinearLayout linearLayout;
    private TextView textView;
    private final String KEY = "s5JxjA.2BCWJw:LGn1yP48uyzGQ7F6";
    private final String CHANNEL = "channel_test";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ably_test);
        initToolbar();
        linearLayout = (LinearLayout) findViewById(R.id.ably_test_layout);
        textView = (TextView) findViewById(R.id.ably_test_text_view);
        try {
            initAbly();
        } catch (AblyException e) {
            Toast.makeText(this, "Oppsss! Ably Error!\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initToolbar(){
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setTitle("ABLY TEST");
    }

    private void initAbly() throws AblyException {
        AblyRealtime ably = new AblyRealtime(KEY);
        //get channel you can subscribe to
        Channel channel = ably.channels.get(CHANNEL);
        channel.on(new ChannelStateListener() {
            @Override
            public void onChannelStateChanged(ChannelStateChange stateChange) {
                Toast.makeText(AblyTest.this, "Channel State Changed >> " + stateChange.current.name(), Toast.LENGTH_SHORT).show();
                Log.e("Ably","Channel State Changed >> " + stateChange.current.name());
            }
        });
        channel.subscribe(new Channel.MessageListener() {
            @Override
            public void onMessage(Message messages) {
                Log.e("Ably","Message received >> " + messages.name + " >> " + messages.data);
                messageReceived_appendText(messages.name + " >> " + messages.data);
            }
        });
    }

    private void messageReceived_appendText(String txt){
        Observable.just(txt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        textView.append("\n" + s);
                        Log.e("Ably","Text >> " + textView.getText().toString());
                    }
                });
    }
}