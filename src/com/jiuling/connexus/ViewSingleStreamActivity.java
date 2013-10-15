package com.jiuling.connexus;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewSingleStreamActivity extends Activity {
	public final static String STREAMID = "com.jiuling.connexus.STREAMID";
	public final static String STREAMNAME = "com.jiuling.connexus.STREAMNAME";
	private static final String TAG = "ViewSingleStream";  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_single_stream);
		Intent intent = getIntent();
		final Long streamId = intent.getLongExtra(
				ViewAllStreamsActivity.STREAMID, 0);
		final String streamName = intent
				.getStringExtra(ViewAllStreamsActivity.STREAMNAME);
		WebUtility.increaseStreamView(streamId, streamName);
		
		TextView textView = (TextView) findViewById(R.id.single_stream_textview);
		textView.setText(textView.getText() + streamName);
		GridView gridView = (GridView) findViewById(R.id.single_stream_gridview);

		
		WebUtility.resultImages = WebUtility
				.getImages("stream", streamId, 0, 0);
		Log.d(TAG, "size is " + WebUtility.resultImages.size());
		
		ArrayList<ConnexusImage> result = new ArrayList<ConnexusImage>();
		Button viewLessButton = (Button)findViewById(R.id.single_stream_less_button);
		viewLessButton.setEnabled(false);
		
		if (WebUtility.resultImages.size() < 16) {
			Button button = (Button) findViewById(R.id.single_stream_more_button);
			button.setEnabled(false);
			result = WebUtility.resultImages;
		} else {
			for (int i = 0; i < 16; i++ ){
				result.add(WebUtility.resultImages.get(i+WebUtility.offsetOfImages));
			}
			WebUtility.offsetOfImages += 16;
		}

		gridView.setAdapter(new ImageAdapter(this, result));
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(ViewSingleStreamActivity.this, "" + position,
						Toast.LENGTH_SHORT).show();

			}

		});

		

		Button uploadImageButton = (Button) findViewById(R.id.single_stream_upload_button);
		uploadImageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						UploadImageActivity.class);
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
		getMenuInflater().inflate(R.menu.view_single_stream, menu);
		return true;
	}

	// TODO implement button handler
	public void viewMorePictures(View view) {
		int remainNum = WebUtility.resultImages.size()
				- WebUtility.offsetOfImages;
		int displayNum = remainNum>16?16:remainNum;
		ArrayList<ConnexusImage> result = new ArrayList<ConnexusImage>();
		for (int i = 0; i < displayNum; i++ ){
			result.add(WebUtility.resultImages.get(i+WebUtility.offsetOfImages));
		}
		GridView gridView = (GridView) findViewById(R.id.single_stream_gridview);
		gridView.setAdapter(new ImageAdapter(this,result));
		WebUtility.offsetOfImages += 16;
		if (WebUtility.offsetOfImages >= WebUtility.resultImages.size()){
			Button viewMoreButton = (Button)findViewById(R.id.single_stream_more_button);
			viewMoreButton.setEnabled(false);
		}
		Button viewLessButton = (Button)findViewById(R.id.single_stream_less_button);
		viewLessButton.setEnabled(true);
		
	}
	
	public void viewLessPictures(View view){
		int displayNum = 16;
		WebUtility.offsetOfImages -= 16;
		ArrayList<ConnexusImage> result = new ArrayList<ConnexusImage>();
		for (int i = 0; i < displayNum; i++ ){
			result.add(WebUtility.resultImages.get(WebUtility.offsetOfImages-(displayNum-i)));
		}
		GridView gridView = (GridView) findViewById(R.id.single_stream_gridview);
		gridView.setAdapter(new ImageAdapter(this,result));
		if (WebUtility.offsetOfImages <= 0){
			Button viewLessButton = (Button)findViewById(R.id.single_stream_less_button);
			viewLessButton.setEnabled(false);
			
		}
		Button viewMoreButton = (Button)findViewById(R.id.single_stream_more_button);
		viewMoreButton.setEnabled(true);
		
		
	}

	public void gotoViewAllStreams(View view) {
		Intent intent = new Intent(this, ViewAllStreamsActivity.class);
		startActivity(intent);
	}

}
