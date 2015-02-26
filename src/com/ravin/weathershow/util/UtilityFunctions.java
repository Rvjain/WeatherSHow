package com.ravin.weathershow.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UtilityFunctions {

	private String TAG = UtilityFunctions.class.getSimpleName();

	public String timestampToTime(String timestamp) {
		long unixSeconds = Long.parseLong(timestamp);
		Date date = new Date(unixSeconds * 1000L);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
		String formattedDate = sdf.format(date);
		return formattedDate;
	}

	public String kelvinToFahrenheit(String temp) {
		float ftemp_k = Float.parseFloat(temp);
		int ftemp_f = (int) (ftemp_k - 273.15f) * 9 / 5 + 32;
		return String.valueOf(ftemp_f);
	}

	// function: checks internet connectivity
	public boolean hasActiveInternetConnection(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

}
