package com.jiuling.connexus;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
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

	private static final String TAG = "ViewNearbyImage";  
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
		WebUtility.resultImages = WebUtility.getImages("nearby", Long.valueOf(0), longitude, latitude);
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

		gridView.setAdapter(new ImageDistanceAdapter(this,result,latitude,longitude));
		
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
		int remainNum = WebUtility.resultImages.size()
				- WebUtility.offsetOfImages;
		int displayNum = remainNum>16?16:remainNum;
		ArrayList<ConnexusImage> result = new ArrayList<ConnexusImage>();
		for (int i = 0; i < displayNum; i++ ){
			result.add(WebUtility.resultImages.get(i+WebUtility.offsetOfImages));
		}
		GridView gridView = (GridView) findViewById(R.id.nearby_gridview);
		gridView.setAdapter(new ImageAdapter(this,result));
		WebUtility.offsetOfImages += 16;
		if (WebUtility.offsetOfImages >= WebUtility.resultImages.size()){
			Button viewMoreButton = (Button)findViewById(R.id.nearby_more_button);
			viewMoreButton.setEnabled(false);
		}
		Button viewLessButton = (Button)findViewById(R.id.nearby_less_button);
		viewLessButton.setEnabled(true);
	}
	
	public void viewLessNearby(View view){
		int displayNum = 16;
		WebUtility.offsetOfImages -= 16;
		ArrayList<ConnexusImage> result = new ArrayList<ConnexusImage>();
		for (int i = 0; i < displayNum; i++ ){
			result.add(WebUtility.resultImages.get(WebUtility.offsetOfImages-(displayNum-i)));
		}
		GridView gridView = (GridView) findViewById(R.id.nearby_gridview);
		gridView.setAdapter(new ImageAdapter(this,result));
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
