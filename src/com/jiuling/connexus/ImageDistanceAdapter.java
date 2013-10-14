package com.jiuling.connexus;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageDistanceAdapter extends BaseAdapter {
	private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<ConnexusImage> images;
    private double longitude;
    private double latitude;
    
    public ImageDistanceAdapter(Context c) {
        mContext = c;
        layoutInflater = LayoutInflater.from(mContext);
    }
    
    public ImageDistanceAdapter(Context c,ArrayList<ConnexusImage> images, double latitude, double longitude) {
        mContext = c;
        layoutInflater = LayoutInflater.from(mContext);
        this.images = images;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    
    public int getCount() {
        return images.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	View grid;
    	if (convertView == null){
    		grid = new View(mContext);
    	    grid = layoutInflater.inflate(R.layout.grid_layout, null); 
    	}else{
    		grid = (View)convertView;
    	}
    	ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
    	URL newurl = null;
		try {
			newurl = new URL(images.get(position).bkUrl);
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		} 
    	Bitmap mIcon_val = null;
		try {
			mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
		} catch (IOException e) {	
			e.printStackTrace();
		} 
    	imageView.setImageBitmap(mIcon_val);
    	TextView textView = (TextView)grid.findViewById(R.id.grid_text);
    	double lat = images.get(position).latitude;
    	double lon = images.get(position).longitude;
    	double distance = Math.sqrt((lat-latitude)*(lat-latitude)+ (lon-longitude)*(lon-longitude));
    	textView.setText(Double.toString(distance));
    	
    	return grid;
    }
}
