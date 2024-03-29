package com.jiuling.connexus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.IOUtils;

import com.google.common.*;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UploadImageActivity extends Activity {

	private static int RESULT_LOAD_IMAGE = 1;
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
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_image);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10,
    			mLocationListener01);
    	
    	locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10,
    			mLocationListener01);
    	
    	
    	
        Intent intent = getIntent();
		final Long streamId= intent.getLongExtra(ViewSingleStreamActivity.STREAMID, 0);
	    final String streamName = intent.getStringExtra(ViewSingleStreamActivity.STREAMNAME);
	    
       
        Button uploadFromCameraButton = (Button) findViewById(R.id.upload_from_camera_button);
        uploadFromCameraButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			    
			    Intent newIntent = new Intent(v.getContext(),UseCameraActivity.class);
			    newIntent.putExtra(STREAMID, streamId);
			    newIntent.putExtra(STREAMNAME, streamName);
				startActivity(newIntent);
			}
		});
        
		Button uploadFromLibraryButton = (Button) findViewById(R.id.upload_from_library_button);
		uploadFromLibraryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		TextView textView = (TextView)findViewById(R.id.upload_textview);
		textView.setText("Stream : " + streamName);
		
		Button uploadImageButton = (Button)findViewById(R.id.upload_image_button);
		uploadImageButton.setEnabled(false);
		
		uploadImageButton.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				if (WebUtility.path != null){
					FileInputStream fileStream = null;
					try {
					    fileStream = new FileInputStream(WebUtility.path);
					    final byte[] imageData = IOUtils.toByteArray(fileStream);
					    Intent intent = getIntent();
					    Long streamId= intent.getLongExtra(ViewSingleStreamActivity.STREAMID, 0);
					    String streamName = intent.getStringExtra(ViewSingleStreamActivity.STREAMNAME);
					    Location mLocation = getLocation(v.getContext());
					    double lat = 0;
				        double lng = 0;
				    	if (mLocation!=null){
				    		lat = mLocation.getLatitude();
				    		lng = mLocation.getLongitude();
				    		
				    	}
				    	MobileImage image = new MobileImage(streamId, streamName, lng,lat, imageData);
					    WebUtility.uploadImage(image);
					    
					} catch (FileNotFoundException e) {
					    e.printStackTrace();
					} catch (IOException e) {
					    e.printStackTrace();
					} finally {
					    IOUtils.closeQuietly(fileStream);
					}
					
				}
				
			}
		});
	}
	protected void onStart(){
		super.onStart();
		if (WebUtility.picAvailable){
		    Button uploadImageButton = (Button)findViewById(R.id.upload_image_button);
			uploadImageButton.setEnabled(true);
		}
	}
	protected void onStop(){
		super.onStop();
		WebUtility.picAvailable = false;
		WebUtility.path = new String();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			WebUtility.path = cursor.getString(columnIndex);
			WebUtility.needRotate="no";
			cursor.close();
			WebUtility.picAvailable = true;
			Button uploadImageButton = (Button)findViewById(R.id.upload_image_button);
			uploadImageButton.setEnabled(true);
			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload_image, menu);
		return true;
	}



}
