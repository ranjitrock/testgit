package com.ciber.maptest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class MapAsyncTask  extends AsyncTask<Void, Void, String> {
  String URL="https://maps.googleapis.com/maps/api/directions/json?origin=Chicago,IL&destination=Los+Angeles,CA&waypoints=Joplin,MO|Oklahoma+City,OK&key=AIzaSyBrl152L7rDSRK_ld7FuPr2LkWvmemqmTw";
	Context context ;
  
  public MapAsyncTask(Context mContext) {
	  context=mContext;
  }
	// TODO Auto-generated constructor stub
 
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		//parse(result);
	}
	@Override
	protected String doInBackground(Void... arg0) {
		
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(URL);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.connect();
			iStream = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			data = sb.toString();
			br.close();
		} catch (Exception e) {
			Log.d("Exception while reading url", e.toString());
		} finally {
			try {
				iStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			urlConnection.disconnect();
		}
		return data;
		
	}

	
	/*public List<Route> parse(String routesJSONString) throws Exception {
	    try {
	        List<Route> routeList = new ArrayList<Route>();
	        final JSONObject jSONObject = new JSONObject(routesJSONString);
	        JSONArray routeJSONArray = jSONObject.getJSONArray(ROUTES);
	        Route route;
	        JSONObject routesJSONObject;
	        for (int m = 0; m < routeJSONArray.length(); m++) {
	            route = new Route(context);
	            routesJSONObject = routeJSONArray.getJSONObject(m);
	            JSONArray legsJSONArray;
	            route.setSummary(routesJSONObject.getString(SUMMARY));
	            legsJSONArray = routesJSONObject.getJSONArray(LEGS);
	            JSONObject legJSONObject;
	            Leg leg;
	            JSONArray stepsJSONArray;
	            for (int b = 0; b < legsJSONArray.length(); b++) {
	                leg = new Leg();
	                legJSONObject = legsJSONArray.getJSONObject(b);
	                leg.setDistance(new Distance(legJSONObject.optJSONObject(DISTANCE).optString(TEXT), legJSONObject.optJSONObject(DISTANCE).optLong(VALUE)));
	                leg.setDuration(new Duration(legJSONObject.optJSONObject(DURATION).optString(TEXT), legJSONObject.optJSONObject(DURATION).optLong(VALUE)));
	                stepsJSONArray = legJSONObject.getJSONArray(STEPS);
	                JSONObject stepJSONObject, stepDurationJSONObject, legPolyLineJSONObject, stepStartLocationJSONObject, stepEndLocationJSONObject;
	                Step step;
	                String encodedString;
	                LatLng stepStartLocationLatLng, stepEndLocationLatLng;
	                for (int i = 0; i < stepsJSONArray.length(); i++) {
	                    stepJSONObject = stepsJSONArray.getJSONObject(i);
	                    step = new Step();
	                    JSONObject stepDistanceJSONObject = stepJSONObject.getJSONObject(DISTANCE);
	                    step.setDistance(new Distance(stepDistanceJSONObject.getString(TEXT), stepDistanceJSONObject.getLong(VALUE)));
	                    stepDurationJSONObject = stepJSONObject.getJSONObject(DURATION);
	                    step.setDuration(new Duration(stepDurationJSONObject.getString(TEXT), stepDurationJSONObject.getLong(VALUE)));
	                    stepEndLocationJSONObject = stepJSONObject.getJSONObject(END_LOCATION);
	                    stepEndLocationLatLng = new LatLng(stepEndLocationJSONObject.getDouble(LATITUDE), stepEndLocationJSONObject.getDouble(LONGITUDE));
	                    step.setEndLocation(stepEndLocationLatLng);
	                    step.setHtmlInstructions(stepJSONObject.getString(HTML_INSTRUCTION));
	                    legPolyLineJSONObject = stepJSONObject.getJSONObject(POLYLINE);
	                    encodedString = legPolyLineJSONObject.getString(POINTS);
	                    step.setPoints(decodePolyLines(encodedString));
	                    stepStartLocationJSONObject = stepJSONObject.getJSONObject(START_LOCATION);
	                    stepStartLocationLatLng = new LatLng(stepStartLocationJSONObject.getDouble(LATITUDE), stepStartLocationJSONObject.getDouble(LONGITUDE));
	                    step.setStartLocation(stepStartLocationLatLng);
	                    leg.addStep(step);
	                }
	                route.addLeg(leg);
	            }
	            routeList.add(route);
	        }
	        return routeList;
	    } catch (Exception e) {
	        throw e;
	    }*/
	
}
