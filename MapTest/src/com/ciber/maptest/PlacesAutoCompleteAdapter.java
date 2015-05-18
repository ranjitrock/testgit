package com.ciber.maptest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements
		Filterable {

	private ArrayList<String> resultList;
	private Context mContext;
	private LayoutInflater inflater;
	private ViewHolder holder;
	
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";

	private static final String API_KEY = "AIzaSyA8mUo1QOJlFFhTtlJ4_2C4trBUxz90YP8";
	
	public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.mContext = context;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public String getItem(int position) {
		return resultList.get(position);
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_list_item, parent,
					false);
			holder = new ViewHolder();
			holder.title = (TextView) convertView
					.findViewById(R.id.textViewItem);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		try {
			holder.title.setText(resultList.get(position).toString());

		} catch (Exception e) {

		}
		return convertView;
	}

	public static class ViewHolder {
		private TextView title;
	}

	@Override
	public Filter getFilter() {

		Filter mFilter = new Filter() {

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}

			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {

					// Retrieve the autocomplete results.
					if (constraint.toString().length() > 3) {
						resultList = autocomplete(constraint.toString());

						// Assign the data to the FilterResults
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}
				}
				return filterResults;
			}
		};
		return mFilter;
	}

	protected ArrayList<String> autocomplete(String input) {

		ArrayList<String> resultList = new ArrayList<String>();

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			String myUrl = "https://api.mobijibe.com/androidcontent/search/findByTitleContainingIgnoreCase?title="
					+ input;
			URL url = new URL(myUrl);
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			return resultList;
		} catch (IOException e) {
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonResults.toString());
			if (jsonObject.has("_embedded")) {
				JSONObject embeddedObj = jsonObject.getJSONObject("_embedded");
				JSONArray mJsonArray = embeddedObj
						.getJSONArray("androidcontent");
				for (int i = 0; i < mJsonArray.length(); i++) {
					JSONObject mJsonObject = mJsonArray.getJSONObject(i);
					String suggestion = mJsonObject.getString("title");
					resultList.add(suggestion);
				}
			}
		} catch (JSONException e) {
		}

		return resultList;
	}

}