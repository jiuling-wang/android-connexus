package com.jiuling.connexus;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewNearbyImageActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_nearby_image);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		GridView gridView = (GridView)findViewById(R.id.nearby_gridview);
		Intent intent = getIntent();
		double longitude = intent.getDoubleExtra(ViewAllStreamsActivity.LONGITUDE, 0);
		double latitude = intent.getDoubleExtra(ViewAllStreamsActivity.LATITUDE, 0);
		ArrayList<ConnexusImage> result = WebUtility.getImages("nearby", Long.valueOf(0), longitude, latitude);
		gridView.setAdapter(new ImageDistanceAdapter(this,result,latitude,longitude));
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(ViewNearbyImageActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        }

	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_nearby_image, menu);
		return true;
	}
	
	//TODO implement button handler
	
	public void viewMoreNearby(View view){
		Intent intent = new Intent(this,ViewNearbyImageActivity.class);
		startActivity(intent);
	}
	
	public void gotoViewAllStreams(View view){
		Intent intent = new Intent(this,ViewAllStreamsActivity.class);
		startActivity(intent);
	}
	
	

}
