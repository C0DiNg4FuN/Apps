package com.coding4fun.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection {
	
	public static String readUrl(String u) {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(u);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.connect();
			iStream = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			data = sb.toString();
			br.close();
			iStream.close();
			urlConnection.disconnect();
		} catch (Exception e) {
			//Log.e("HttpConnection util",e.getMessage());
			data = "ERROR";
		}
		return data;
	}
	//add post method cz long params (base64 encoded file) in GET url is not working!

}
