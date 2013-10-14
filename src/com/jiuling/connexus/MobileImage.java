package com.jiuling.connexus;

public class MobileImage {
	
	public Long streamId;
	public String streamName;
	public double latitude;
	public double longitude;
	public byte[] iamgeData;
	
	public MobileImage(Long streamId, String streamName, byte[] imageData){
		this.iamgeData = imageData;
		this.streamId = streamId;
		this.streamName = streamName;
		this.longitude = 0;
		this.latitude = 0;
			
	}
	
	public MobileImage(Long streamId, String streamName, double longitude, double latitude, byte[] imageData){
		this.iamgeData = imageData;
		this.streamId = streamId;
		this.streamName = streamName;
		this.longitude = longitude;
		this.latitude = latitude;
			
	}

}
