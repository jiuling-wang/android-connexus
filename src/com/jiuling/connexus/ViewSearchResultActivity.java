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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewSearchResultActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_search_result);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		TextView textView = (TextView)findViewById(R.id.search_result_textview);
		textView.setText("123 results for Lucknow,\nclick on an image to view stream");
		GridView gridView = (GridView)findViewById(R.id.search_result_gridview);
		Intent intent = getIntent();
		String keyword = intent.getStringExtra(ViewAllStreamsActivity.KEYWORD);
		ArrayList<Stream> result = WebUtility.getStreams("search", keyword, null);
		gridView.setAdapter(new ImageTextAdapter(this,result));
		gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(ViewSearchResultActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        }

	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_search_result, menu);
		return true;
	}
	
	//TODO implement button handler
	
	public void searchAgain(View view){
		
	}
		
	public void searchMore(View view){
		
	}

}
