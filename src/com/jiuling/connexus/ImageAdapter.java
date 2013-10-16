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
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ConnexusImage> images;
    private LayoutInflater layoutInflater;
    
    public ImageAdapter(Context c) {
        mContext = c;
        layoutInflater = LayoutInflater.from(mContext);
    }
    public ImageAdapter(Context c,ArrayList<ConnexusImage> images) {
        mContext = c;
        layoutInflater = LayoutInflater.from(mContext);
        this.images = images;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	Bitmap mIcon_val = null;
		try {
			mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		imageView.setImageBitmap(mIcon_val);
        return grid;
    }

    
}