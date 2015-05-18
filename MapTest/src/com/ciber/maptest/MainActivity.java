package com.ciber.maptest;

import java.lang.reflect.Field;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciber.maptest.CustomSearchView.OnClearListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;

public class MainActivity extends Activity {

	GoogleMap gMap;
	ActionBar actionbar;
	View actionBarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maphome);
		intializeGmap();
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");

			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		actionbar.setTitle(ActionBar.DISPLAY_SHOW_TITLE);
		actionbar = getActionBar();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
				| ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE);

		// Hide the action bar tit
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setTitle("Enter data");
		// actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.color_header));
		LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		actionBarView = inflator.inflate(R.layout.layout_actionbar, null);

		actionbar.setCustomView(actionBarView);
		final ImageView searchIcon = (ImageView) actionBarView
				.findViewById(R.id.imageView);
		final CustomSearchView customView = (CustomSearchView) actionBarView
				.findViewById(R.id.searchView);
		final TextView titleText = (TextView) actionBarView
				.findViewById(R.id.titleText);

		// customView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_search,
		// 0, 0, 0);

		customView.setHintTextColor(Color.WHITE);
		customView.setAdapter(new PlacesAutoCompleteAdapter(MainActivity.this,
				R.layout.activity_list_item));
		customView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});

		customView.setVisibility(View.INVISIBLE);
		searchIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleSearch(false);
				actionbar.setDisplayShowTitleEnabled(false);
				titleText.setVisibility(View.GONE);
			}
		});

		customView.setOnClearListener(new OnClearListener() {

			@Override
			public void onClear() {
				titleText.setVisibility(View.VISIBLE);
				actionbar.setDisplayShowTitleEnabled(false);
				toggleSearch(true);
			}
		});

		customView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				customView.setText(parent.getAdapter().getItem(position)
						.toString());
				;

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		actionbar.setCustomView(actionBarView);

		// drawPathOnMap();
	}

	private void drawPathOnMap() {
		MapAsyncTask mMapAsyncTask = new MapAsyncTask(MainActivity.this);
		mMapAsyncTask.execute();
	}

	private void intializeGmap() {

		FragmentManager fragmentManager = getFragmentManager();
		gMap = ((MapFragment) fragmentManager.findFragmentById(R.id.mapView))
				.getMap();
		UiSettings mGmapSettings = gMap.getUiSettings();
		mGmapSettings.setZoomControlsEnabled(true);
		mGmapSettings.setAllGesturesEnabled(true);
		mGmapSettings.setTiltGesturesEnabled(true);
		mGmapSettings.setScrollGesturesEnabled(true);
		gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		gMap.setTrafficEnabled(true);

	}

	// this toggles between the visibility of the search icon and the search box
	// to show search icon - reset = true
	// to show search box - reset = false
	protected void toggleSearch(boolean reset) {
		CustomSearchView searchBox = (CustomSearchView) actionBarView
				.findViewById(R.id.searchView);
		ImageView searchIcon = (ImageView) actionBarView.findViewById(R.id.imageView);
		if (reset) {
			// hide search box and show search icon
			searchBox.setText("");
			searchBox.setVisibility(View.GONE);
			searchIcon.setVisibility(View.VISIBLE);
			// hide the keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
		} else {
			// hide search icon and show search box
			searchIcon.setVisibility(View.GONE);
			searchBox.setVisibility(View.VISIBLE);
			searchBox.requestFocus();
			// show the keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
			//imm.getEnabledInputMethodList().add(arg0)
		}

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private static final String LOG_TAG = "MainActivity";
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
	private static final String TAG = "MainActivity";
	private static final String SERVER_KEY = "AIzaSyD-giiFJ3vFNWAKNjpyRFxfLUN2ODxyAb8";

}
