package com.coding4fun.apps;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by coding4fun on 23-Jan-17.
 */

public class KeyBoardFragment extends Fragment implements KeyEvent.Callback {

    private PController controller;
    private EditText et;
    private boolean isKeyboardShown;

    public KeyBoardFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pcontroller_keyboard_fragment,container,false);
        et = (EditText) v.findViewById(R.id.keyboard_fragment_ET);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = PController.getInstance();
        setHasOptionsMenu(true);
        //showKeyboard();
        //toggleKeyboard();
        isKeyboardShown = true;
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //todo: KeyEvent.Callback & setOnKeyListener are not working. try this...
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                char pressedKey = (char) keyEvent.getUnicodeChar();
                String s = String.valueOf(pressedKey);
                Log.e(PController.TAG,"keyboard: " + s);
                return true;
            }
        });
    }

    private void showKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et,InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(),0);
    }

    private void toggleKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.keyboard_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.keyboard){
            /*if(isKeyboardShown){
                hideKeyboard();
                isKeyboardShown = false;
                item.setIcon(ContextCompat.getDrawable(getContext(),R.drawable.keyboard));
            } else {
                showKeyboard();
                isKeyboardShown = true;
                item.setIcon(ContextCompat.getDrawable(getContext(),R.drawable.keyboard_hide));
            }*/
            toggleKeyboard();
            return true;
        }
        return false;
    }

    private void sendCommand(String cmd){
        if(controller!= null && controller.isReady()){
            controller.sendUdpBroadcast(cmd);
        }
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        char pressedKey = (char) keyEvent.getUnicodeChar();
        String s = String.valueOf(pressedKey);
        Log.e(PController.TAG,"keyboard: " + s);
        return false;
    }

    @Override
    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        char pressedKey = (char) keyEvent.getUnicodeChar();
        String s = String.valueOf(pressedKey);
        Log.e(PController.TAG,"keyboard: " + s);
        String cmd = "coding4fun keyboard " + s;
        sendCommand(cmd);
        return true;
    }

    @Override
    public boolean onKeyMultiple(int i, int i1, KeyEvent keyEvent) {
        return false;
    }
}