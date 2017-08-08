package com.coding4fun.utils;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.List;

public class Utils {
	
	Context context;
	
	public Utils(Context context) {
		this.context = context;
	}
	
	
	public boolean isOnline () {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting())
			return true;
		return false;
	}
	
	public boolean isOnline_WIFI () {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			if (netInfo.getType() == ConnectivityManager.TYPE_WIFI)
				return true;
		}
		return false;
	}
	
	public void toast_short (String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public void toast_long (String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	
	public void hideKeyboard(View view) {
	    //View view = getCurrentFocus();
	    if (view != null) {
	        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).
	            hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
	
	public void alertErrorAndExit(String msg) {
		AlertDialog.Builder d = new AlertDialog.Builder(context);
		d.setCancelable(false);
		d.setTitle("Oppps!");
		d.setMessage(msg + "!\n"+"App will be terminated!");
		d.setPositiveButton("EXIT", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//exit
				//System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		d.show();
	}
	
	public void removeLocalPreferences () {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefs.edit().clear().commit();
	}

	public void turnWIFI(boolean enable){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		boolean isEnabled = wifiManager.isWifiEnabled();
		if((enable && !isEnabled) || (!enable && isEnabled)) {
			wifiManager.setWifiEnabled(enable);
		}
	}

	public void turnBluetooth(boolean enable){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		boolean isEnabled = bluetoothAdapter.isEnabled();
		if(enable && !isEnabled){
			bluetoothAdapter.enable();
		} else if(!enable && isEnabled){
			bluetoothAdapter.disable();
		}
	}

	public Camera turnFlash(Camera cam, boolean enable){
		boolean hasFlash = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		if(!hasFlash){ //device does not support flashlight
			return null;
		}
		if(cam == null){
			cam = Camera.open();
		}
		if(!enable){
			cam.stopPreview();
			cam.release();
			return null;
		}
		Camera.Parameters p = cam.getParameters();
		List<String> modes = p.getSupportedFlashModes();
		if(modes.contains(Camera.Parameters.FLASH_MODE_TORCH)){
			p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
		} else if(modes.contains(Camera.Parameters.FLASH_MODE_ON)){
			p.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
		} else {
			cam.release();
			return null;
		}
		cam.setParameters(p);
		cam.startPreview();
		return cam;
	}
	
}