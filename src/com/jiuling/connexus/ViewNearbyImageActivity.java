package com.jiuling.connexus;

import java.util.ArrayList;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewNearbyImageActivity extends Activity {

	public final static String STREAMID="com.jiuling.connexus.STREAMID";
	public final static String STREAMNAME="com.jiuling.connexus.STREAMNAME";
	
	public final LocationListener mLocationListener01 = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {}

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
	};
	
	public static Location getLocation(Context context) {

		LocationManager locMan = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		Location location = locMan
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location==null){
			location = locMan
			.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		return location;
	}

	private static final String TAG = "ViewNearbyImage";  
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_nearby_image);
		
		LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10,
    			mLocationListener01);
    	
    	locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10,
    			mLocationListener01);
    	
    	Location mLocation = getLocation(this);
	    double lat = 0;
        double lng = 0;
    	if (mLocation!=null){
    		lat = mLocation.getLatitude();
    		lng = mLocation.getLongitude();
    	}

    	
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		GridView gridView = (GridView)findViewById(R.id.nearby_gridview);
		
		WebUtility.resultImages = WebUtility.getImages("nearby", Long.valueOf(0), lng, lat);
		Log.d(TAG, "size is " + WebUtility.resultImages.size());
		
		ArrayList<ConnexusImage> result = new ArrayList<ConnexusImage>();
		Button viewLessButton = (Button)findViewById(R.id.nearby_less_button);
		viewLessButton.setEnabled(false);
		
		if (WebUtility.resultImages.size() < 16) {
			Button button = (Button) findViewById(R.id.nearby_more_button);
			button.setEnabled(false);
			result = WebUtility.resultImages;
		} else {
			for (int i = 0; i < 16; i++ ){
				result.add(WebUtility.resultImages.get(i+WebUtility.offsetOfImages));
			}
			WebUtility.offsetOfImages += 16;
		}

		gridView.setAdapter(new ImageDistanceAdapter(this,result,lat,lng));
		final ArrayList<ConnexusImage> sharedResult = result;
		gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            //Toast.makeText(ViewAllStreamsActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        	Intent intent = new Intent(v.getContext(),ViewSingleStreamActivity.class);
	        	Long streamId = sharedResult.get(position).streamId;
	        	String streamName = sharedResult.get(position).streamName;
	        	intent.putExtra(STREAMID, streamId);
	        	intent.putExtra(STREAMNAME, streamName);
	        	startActivity(intent);
	        }

	    });
		
	}

	@Override
	protected void onStop() {
		super.onStop();
		WebUtility.offsetOfImages = 0;
		WebUtility.resultImages = new ArrayList<ConnexusImage>();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_nearby_image, menu);
		return true;
	}
	
	//TODO implement button handler
	
	public void viewMoreNearby(View view){
		Location mLocation = getLocation(this);
	    double lat = 0;
        double lng = 0;
    	if (mLocation!=null){
    		lat = mLocation.getLatitude();
    		lng = mLocation.getLongitude();
    	}
    	
		int remainNum = WebUtility.resultImages.size()
				- WebUtility.offsetOfImages;
		int displayNum = remainNum>16?16:remainNum;
		ArrayList<ConnexusImage> result = new ArrayList<ConnexusImage>();
		for (int i = 0; i < displayNum; i++ ){
			result.add(WebUtility.resultImages.get(i+WebUtility.offsetOfImages));
		}
		GridView gridView = (GridView) findViewById(R.id.nearby_gridview);
		gridView.setAdapter(new ImageDistanceAdapter(this,result,lat,lng));
		WebUtility.offsetOfImages += 16;
		if (WebUtility.offsetOfImages >= WebUtility.resultImages.size()){
			Button viewMoreButton = (Button)findViewById(R.id.nearby_more_button);
			viewMoreButton.setEnabled(false);
		}
		Button viewLessButton = (Button)findViewById(R.id.nearby_less_button);
		viewLessButton.setEnabled(true);
	}
	
	public void viewLessNearby(View view){
		Location mLocation = getLocation(this);
	    double lat = 0;
        double lng = 0;
    	if (mLocation!=null){
    		lat = mLocation.getLatitude();
    		lng = mLocation.getLongitude();
    	}
		int displayNum = 16;
		WebUtility.offsetOfImages -= 16;
		ArrayList<ConnexusImage> result = new ArrayList<ConnexusImage>();
		for (int i = 0; i < displayNum; i++ ){
			result.add(WebUtility.resultImages.get(WebUtility.offsetOfImages-(displayNum-i)));
		}
		GridView gridView = (GridView) findViewById(R.id.nearby_gridview);
		gridView.setAdapter(new ImageDistanceAdapter(this,result,lat,lng));
		if (WebUtility.offsetOfImages <= 0){
			Button viewLessButton = (Button)findViewById(R.id.nearby_less_button);
			viewLessButton.setEnabled(false);
			
		}
		Button viewMoreButton = (Button)findViewById(R.id.nearby_more_button);
		viewMoreButton.setEnabled(true);
	}
	
	public void gotoViewAllStreams(View view){
		Intent intent = new Intent(this,ViewAllStreamsActivity.class);
		startActivity(intent);
	}
	
	

}
