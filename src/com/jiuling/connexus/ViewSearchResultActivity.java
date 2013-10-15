package com.jiuling.connexus;

import java.util.ArrayList;

import com.google.common.base.FinalizablePhantomReference;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewSearchResultActivity extends Activity {

	public final static String STREAMID="com.jiuling.connexus.STREAMID";
	public final static String STREAMNAME="com.jiuling.connexus.STREAMNAME";
	public final static String KEYWORD="com.jiuling.connexus.KEYWORD";
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_search_result);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		
		GridView gridView = (GridView)findViewById(R.id.search_result_gridview);
		Intent intent = getIntent();
		String keyword = intent.getStringExtra(ViewAllStreamsActivity.KEYWORD);
		
		WebUtility.resultStreams= WebUtility.getStreams("search", keyword, null);
		
		
		TextView textView = (TextView)findViewById(R.id.search_result_textview);
		textView.setText(WebUtility.resultStreams.size() + " results for " + keyword + "\nclick on an image to view stream");
		
		ArrayList<Stream> result = new ArrayList<Stream>();
		Button viewLessButton = (Button)findViewById(R.id.search_result_less);
		viewLessButton.setEnabled(false);
		
		if (WebUtility.resultStreams.size() < 8) {
			Button button = (Button) findViewById(R.id.search_result_more);
			button.setEnabled(false);
			result = WebUtility.resultStreams;
		} else {
			for (int i = 0; i < 8; i++ ){
				result.add(WebUtility.resultStreams.get(i+WebUtility.offsetOfStreams));
			}
			WebUtility.offsetOfStreams += 8;
		}
		
		final ArrayList<Stream> newResult = result;
		gridView.setAdapter(new ImageTextAdapter(this,newResult));
		gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            //Toast.makeText(ViewSearchResultActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        	Intent intent = new Intent(v.getContext(),ViewSingleStreamActivity.class);
	        	Long streamId = newResult.get(position).id;
	        	String streamName = newResult.get(position).name;
	        	intent.putExtra(STREAMID, streamId);
	        	intent.putExtra(STREAMNAME, streamName);
	        	startActivity(intent);
	        }

	    });
	}

	
	@Override
	protected void onStop() {
		super.onStop();
		WebUtility.offsetOfStreams = 0;
		WebUtility.resultStreams = new ArrayList<Stream>();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_search_result, menu);
		return true;
	}
	
	//TODO implement button handler
	
	public void searchAgain(View view){
		EditText editText = (EditText)findViewById(R.id.search_result_edittext);
		String keyword = editText.getText().toString();
		if (keyword!=null){
			Intent intent = new Intent(this,ViewSearchResultActivity.class);
			intent.putExtra(KEYWORD, keyword);
			startActivity(intent);
		}
	}
		
	public void searchMore(View view){
		int remainNum = WebUtility.resultStreams.size()
				- WebUtility.offsetOfStreams;
		int displayNum = remainNum>8?8:remainNum;
		ArrayList<Stream> result = new ArrayList<Stream>();
		for (int i = 0; i < displayNum; i++ ){
			result.add(WebUtility.resultStreams.get(i+WebUtility.offsetOfStreams));
		}
		
		final ArrayList<Stream> newResult = result;
		GridView gridView = (GridView) findViewById(R.id.single_stream_gridview);
		gridView.setAdapter(new ImageTextAdapter(this,newResult));
		gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            //Toast.makeText(ViewSearchResultActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        	Intent intent = new Intent(v.getContext(),ViewSingleStreamActivity.class);
	        	Long streamId = newResult.get(position).id;
	        	String streamName = newResult.get(position).name;
	        	intent.putExtra(STREAMID, streamId);
	        	intent.putExtra(STREAMNAME, streamName);
	        	startActivity(intent);
	        }

	    });
		
		
		
		WebUtility.offsetOfStreams += 8;
		if (WebUtility.offsetOfStreams >= WebUtility.resultStreams.size()){
			Button viewMoreButton = (Button)findViewById(R.id.search_result_more);
			viewMoreButton.setEnabled(false);
		}
		Button viewLessButton = (Button)findViewById(R.id.search_result_less);
		viewLessButton.setEnabled(true);
	}
	
	
	public void searchLess(View view){
		int displayNum = 8;
		WebUtility.offsetOfStreams -= 8;
		ArrayList<Stream> result = new ArrayList<Stream>();
		for (int i = 0; i < displayNum; i++ ){
			result.add(WebUtility.resultStreams.get(WebUtility.offsetOfStreams-(displayNum-i)));
		}
		
		final ArrayList<Stream> newResult = result;
		GridView gridView = (GridView) findViewById(R.id.search_result_gridview);
		gridView.setAdapter(new ImageTextAdapter(this,newResult));
		gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            //Toast.makeText(ViewSearchResultActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        	Intent intent = new Intent(v.getContext(),ViewSingleStreamActivity.class);
	        	Long streamId = newResult.get(position).id;
	        	String streamName = newResult.get(position).name;
	        	intent.putExtra(STREAMID, streamId);
	        	intent.putExtra(STREAMNAME, streamName);
	        	startActivity(intent);
	        }

	    });
		
		
		if (WebUtility.offsetOfStreams <= 0){
			Button viewLessButton = (Button)findViewById(R.id.search_result_less);
			viewLessButton.setEnabled(false);
			
		}
		Button viewMoreButton = (Button)findViewById(R.id.search_result_more);
		viewMoreButton.setEnabled(true);
	}

}
