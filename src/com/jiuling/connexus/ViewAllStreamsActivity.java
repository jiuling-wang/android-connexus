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
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class ViewAllStreamsActivity extends Activity {
	private LocationManager manager;  
	public final static String STREAMID="com.jiuling.connexus.STREAMID";
	public final static String STREAMNAME="com.jiuling.connexus.STREAMNAME";
	public final static String KEYWORD="com.jiuling.connexus.KEYWORD";
	public final static String LATITUDE="com.jiuling.connexus.LATITUDE";
	public final static String LONGITUDE="com.jiuling.connexus.LONGITUDE";
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_all_streams);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        GridView gridView = (GridView)findViewById(R.id.all_stream_gridview);
		
		final ArrayList<Stream> result = WebUtility.getStreams("all", null, null);
		
		
		
		gridView.setAdapter(new ImageTextAdapter(this,result));
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            //Toast.makeText(ViewAllStreamsActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        	Intent intent = new Intent(v.getContext(),ViewSingleStreamActivity.class);
	        	Long streamId = result.get(position).id;
	        	String streamName = result.get(position).name;
	        	intent.putExtra(STREAMID, streamId);
	        	intent.putExtra(STREAMNAME, streamName);
	        	startActivity(intent);
	        }

	    });
	
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_all_streams, menu);
		return true;
	}
	//TODO implement button handler
	public void searchSpecified(View view){
		Intent intent = new Intent(this,ViewSearchResultActivity.class);
		EditText edittext = (EditText)findViewById(R.id.all_stream_search_edittext);
		String keyword = edittext.getText().toString();
		intent.putExtra(KEYWORD, keyword);
		startActivity(intent);
	}
	
	
	public Location getLocation(Context context) {  
        LocationManager locMan = (LocationManager) context  
                .getSystemService(Context.LOCATION_SERVICE);  
        Location location = locMan  
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);  
        if (location == null) {  
            location = locMan  
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);  
        }  
        return location;  
    }  
	
	public void searchNearby(View view){
		Intent intent = new Intent(this,ViewNearbyImageActivity.class);
		
        double lat = 0;
        double lng = 0;
        Location mLocation = getLocation(this); 
        if(mLocation!=null){
            lat=mLocation.getLatitude();
            lng=mLocation.getLongitude();
        }

		intent.putExtra(LATITUDE, lat);
        intent.putExtra(LONGITUDE, lng);
		
		
		startActivity(intent);
	}
	
	public void gotoSubscribed(View view){
		if (!WebUtility.userAvailable){
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
		}else{
			GridView gridView = (GridView)findViewById(R.id.all_stream_gridview);
			final ArrayList<Stream> result = WebUtility.getStreams("subscribed", null, WebUtility.userName);

			gridView.setAdapter(new ImageTextAdapter(this,result));
			
			gridView.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		            //Toast.makeText(ViewAllStreamsActivity.this, "" + position, Toast.LENGTH_SHORT).show();
		        	Intent intent = new Intent(v.getContext(),ViewSingleStreamActivity.class);
		        	Long streamId = result.get(position).id;
		        	String streamName = result.get(position).name;
		        	intent.putExtra(STREAMID, streamId);
		        	intent.putExtra(STREAMNAME, streamName);
		        	startActivity(intent);
		        }

		    });

		}
	
	}
	
	
}
