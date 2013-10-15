package com.jiuling.connexus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.R.bool;
import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



public class WebUtility {
	
	
	public static String path = new String();
	public static String needRotate = "no";
	public static ArrayList<ConnexusImage> resultImages = null;
	public static int offsetOfImages = 0;
	
	public static ArrayList<Stream> resultStreams = null;
	public static int offsetOfStreams = 0;
	
	
	public static void makeHTTPPOSTRequest(String apiUrl, String tstJson) {
        try {
            HttpClient c = new DefaultHttpClient();        
            HttpPost p = new HttpPost(apiUrl);        
            p.setEntity(new StringEntity(tstJson));
            HttpResponse r = c.execute(p);
        }
      
        catch(IOException e) {
            System.out.println(e);
        }                        
    }    
	public static void uploadImage(MobileImage uploadImage){
		String apiUrl = "http://jiuling-connexus.appspot.com/mobileUploadImage?rotate="+WebUtility.needRotate;
		
		Gson gson = new Gson();
		String tstJson = gson.toJson(uploadImage);
		makeHTTPPOSTRequest(apiUrl, tstJson);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void increaseStreamView(Long streamId, String streamName){
		String apiUrl = "http://jiuling-connexus.appspot.com/incStreamViews?streamId="+streamId+"&streamName="+streamName;
		makeHTTPPOSTRequest(apiUrl, null);
	}
	public static ArrayList<Stream> getStreams(String type,String keyword, String username){
		String apiUrl = "http://jiuling-connexus.appspot.com/mobileGetStreams?type="+type+"&keyword="+keyword+"&username="+username;
		String returnedJson = null;
		ArrayList<Stream> result = new ArrayList<Stream>();
		
		System.out.print("SHIT");
	    try {
	      returnedJson = Resources.toString( new URL(apiUrl), Charsets.UTF_8 );
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    if (returnedJson != null){
	    	Gson gson = new Gson();
	    	Type listType = new TypeToken<List<Stream>>(){}.getType();
	        // In this test code i just shove the JSON here as string.
	        result = gson.fromJson(returnedJson, listType);
	    }
		return result;
	}

	public static ArrayList<ConnexusImage> getImages(String type,Long streamId, double longitude, double latitude){
		ArrayList<ConnexusImage> result = new ArrayList<ConnexusImage>();
		String apiUrl = "http://jiuling-connexus.appspot.com/mobileGetImage?type="+type+"&streamId="
				+streamId+"&longitude="+longitude+"&latitude="+latitude;
		String returnedJson = null;
		try {
		      returnedJson = Resources.toString( new URL(apiUrl), Charsets.UTF_8 );
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		    if (returnedJson != null){
		    	Gson gson = new Gson();
		    	Type listType = new TypeToken<List<ConnexusImage>>(){}.getType();
		        // In this test code i just shove the JSON here as string.
		        result = gson.fromJson(returnedJson, listType);
		    }
		return result;
	}

}
