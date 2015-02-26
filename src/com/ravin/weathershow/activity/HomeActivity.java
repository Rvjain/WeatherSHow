package com.ravin.weathershow.activity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ravin.weathershow.AppController;
import com.ravin.weathershow.R;
import com.ravin.weathershow.bean.WeatherBean;
import com.ravin.weathershow.util.Constant;
import com.ravin.weathershow.util.GPSUtil;
import com.ravin.weathershow.util.UtilityFunctions;
import com.ravin.weathershow.views.EditTextRoboto;
import com.ravin.weathershow.views.TextViewRoboto;

public class HomeActivity extends ActionBarActivity {

	private String TAG = HomeActivity.class.getSimpleName();
	private ProgressDialog pDialog;
	private String tag_json_obj = "jobj_req";
	String URL;
	// GPSTracker class
	GPSUtil gps;

	// UI elements
	LinearLayout ll_cards;
	TextViewRoboto tv_address;
	TextViewRoboto tv_date;
	TextViewRoboto tv_description;
	TextViewRoboto tv_temp;
	TextViewRoboto tv_min;
	TextViewRoboto tv_max;
	TextViewRoboto tv_morn;
	TextViewRoboto tv_night;
	TextViewRoboto tv_pressure;
	TextViewRoboto tv_wind;
	TextViewRoboto tv_humidity;
	EditTextRoboto et_place;
	UtilityFunctions ut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Getting weather for you :)");
		pDialog.setCancelable(false);
		ll_cards = (LinearLayout) findViewById(R.id.ll_card);
		et_place = (EditTextRoboto) findViewById(R.id.et_location);
		ut = new UtilityFunctions();
		if (ut.hasActiveInternetConnection(this)) {
			getLocation();
		} else {
			Toast.makeText(
					this,
					"Not internet connection please enable internet connection.",
					Toast.LENGTH_SHORT).show();
		}

	}

	// functions : gets location of the user using GPS
	private void getLocation() {
		gps = new GPSUtil(HomeActivity.this);
		if (gps.canGetLocation()) {
			double lat = gps.getLatitude();
			double lon = gps.getLongitude();
			makeQuery(lat, lon);
		} else {
			gps.showSettingsAlert();
		}
	}

	// onClick function: gets the input text of the city and calls the api
	public void onClickGo(View v) {

		Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrate.vibrate(50);
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		if (ut.hasActiveInternetConnection(this)) {
			String place = et_place.getText().toString().trim();
			if (!place.equalsIgnoreCase("")) {
				showProgressDialog();
				makeQuery(place);
			} else {
				Toast.makeText(this,
						"Please specify a city in the field above.",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(
					this,
					"Not internet connection please enable internet connection.",
					Toast.LENGTH_SHORT).show();
		}

	}

	// functions: creates query
	private void makeQuery(double lat, double lon) {
		URL = Constant.BASE_URL + "lat=" + lat + "&lon=" + lon
				+ "&cnt=5&mode=json";
		makeJsonObjReq();
	}

	private void makeQuery(String place) {
		URL = Constant.BASE_URL + "q=" + place + "&cnt=5&mode=json";
		makeJsonObjReq();
	}

	private void showProgressDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideProgressDialog() {
		if (pDialog.isShowing())
			pDialog.hide();
	}

	// function: calls the api an get data and creates the json object sets the
	// views with the data
	private void makeJsonObjReq() {
		showProgressDialog();
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, URL,
				null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());
						ArrayList<WeatherBean> listBean = parseJson(response);
						UtilityFunctions ut = new UtilityFunctions();
						ll_cards.removeAllViews();
						for (WeatherBean weatherBean : listBean) {
							View child = getLayoutInflater().inflate(
									R.layout.card_layout, ll_cards, false);
							tv_address = (TextViewRoboto) child
									.findViewById(R.id.tv_address);
							tv_date = (TextViewRoboto) child
									.findViewById(R.id.tv_date);
							tv_description = (TextViewRoboto) child
									.findViewById(R.id.tv_description);
							tv_temp = (TextViewRoboto) child
									.findViewById(R.id.tv_temp);
							tv_min = (TextViewRoboto) child
									.findViewById(R.id.tv_min_temp);
							tv_max = (TextViewRoboto) child
									.findViewById(R.id.tv_max_temp);
							tv_morn = (TextViewRoboto) child
									.findViewById(R.id.tv_sunrise);
							tv_night = (TextViewRoboto) child
									.findViewById(R.id.tv_sunset);
							tv_pressure = (TextViewRoboto) child
									.findViewById(R.id.tv_pressure);
							tv_wind = (TextViewRoboto) child
									.findViewById(R.id.tv_wind);
							tv_humidity = (TextViewRoboto) child
									.findViewById(R.id.tv_humidity);
							tv_address.setText(weatherBean.getAddress());
							String formattedDate = ut
									.timestampToTime(weatherBean.getDate());
							tv_date.setText(formattedDate);
							tv_description.setText(weatherBean.getDescription());
							tv_temp.setText(ut.kelvinToFahrenheit(weatherBean
									.getTemp()));
							tv_pressure.setText(weatherBean.getPressure()
									+ " mB");
							tv_wind.setText(weatherBean.getWind() + " mph");
							tv_humidity.setText(weatherBean.getHumidity()
									+ " %");
							tv_min.setText(ut.kelvinToFahrenheit(weatherBean
									.getMinTemp()));
							tv_max.setText(ut.kelvinToFahrenheit(weatherBean
									.getMaxTemp()));
							tv_morn.setText(ut.kelvinToFahrenheit(weatherBean
									.getMorning()));
							tv_night.setText(ut.kelvinToFahrenheit(weatherBean
									.getNight()));
							ll_cards.addView(child);
						}
						hideProgressDialog();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						hideProgressDialog();
						Toast.makeText(
								HomeActivity.this,
								"Some error occured while fetching data. Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				}) {
		};
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
	}

	// function: parse the response and give the json data in the list of
	// Weather bean
	protected ArrayList<WeatherBean> parseJson(JSONObject response) {
		ArrayList<WeatherBean> list = new ArrayList<WeatherBean>();
		try {
			if (response.getString("cod").equalsIgnoreCase("404")) {
				Toast.makeText(
						HomeActivity.this,
						"Some error occured while fetching data. Please try again.",
						Toast.LENGTH_SHORT).show();
			} else {
				String city = response.getJSONObject("city").getString("name");
				String country = response.getJSONObject("city").getString(
						"country");
				String address = city + ", " + country;
				JSONArray list_array = response.getJSONArray("list");
				for (int i = 0; i < list_array.length(); i++) {
					WeatherBean beanItem = new WeatherBean();
					JSONObject item = list_array.getJSONObject(i);
					beanItem.setAddress(address);
					beanItem.setDate(item.getString("dt"));
					JSONObject temp = item.getJSONObject("temp");
					beanItem.setTemp(temp.getString("day"));
					beanItem.setMinTemp(temp.getString("min"));
					beanItem.setMaxTemp(temp.getString("max"));
					beanItem.setMorning(temp.getString("morn"));
					beanItem.setNight(temp.getString("night"));
					beanItem.setPressure(item.getString("pressure"));
					beanItem.setHumidity(item.getString("humidity"));
					beanItem.setWind(item.getString("speed"));
					beanItem.setWindDegree(item.getString("deg"));
					JSONArray weather = item.getJSONArray("weather");
					beanItem.setDescription(weather.getJSONObject(0).getString(
							"description"));
					list.add(beanItem);
				}
			}
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
