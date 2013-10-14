package com.jiuling.connexus;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewSingleStreamActivity extends Activity {
	public final static String STREAMID="com.jiuling.connexus.STREAMID";
	public final static String STREAMNAME="com.jiuling.connexus.STREAMNAME";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_single_stream);
		Intent intent = getIntent();
		final Long streamId = intent.getLongExtra(ViewAllStreamsActivity.STREAMID,0);
		final String streamName = intent.getStringExtra(ViewAllStreamsActivity.STREAMNAME);
		TextView textView = (TextView)findViewById(R.id.single_stream_textview);
		textView.setText(textView.getText()+streamName);
		GridView gridView = (GridView)findViewById(R.id.single_stream_gridview);	
		ArrayList<ConnexusImage> result = WebUtility.getImages("stream", streamId, 0, 0);
		gridView.setAdapter(new ImageAdapter(this,result));	
		gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(ViewSingleStreamActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	            
	        }

	    });
		
		Button uploadImageButton = (Button)findViewById(R.id.single_stream_upload_button);
		uploadImageButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),UploadImageActivity.class);
				intent.putExtra(STREAMID, streamId);
				intent.putExtra(STREAMNAME, streamName);
				startActivity(intent);
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_single_stream, menu);
		return true;
	}
	//TODO implement button handler
	public void viewMorePictures(View view){
		Intent intent = new Intent(this,ViewSingleStreamActivity.class);
		startActivity(intent);
	}
	
	
	public void gotoViewAllStreams(View view){
		Intent intent = new Intent(this,ViewAllStreamsActivity.class);
		startActivity(intent);
	}

}
