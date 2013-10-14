package com.jiuling.connexus;

import java.util.Date;

import com.google.common.base.Joiner;



public class ConnexusImage implements Comparable<ConnexusImage>{

	public Long id;
	public Long streamId;
	public String streamName;
	public String bkUrl;
	public Date createDate;
	public double latitude;
	public double longitude;
	
	public ConnexusImage(){
		this.createDate = new Date();
	}
	public ConnexusImage(Long streamId, String streamName, String bkUrl){
		this.streamId = streamId;
		this.streamName = streamName;
		this.bkUrl = bkUrl;
		this.createDate = new Date();
	}
	
	public ConnexusImage(Long streamId, String streamName, String bkUrl,double latitude, double longitude){
		this.streamId = streamId;
		this.streamName = streamName;
		this.bkUrl = bkUrl;
		this.latitude = latitude;
		this.longitude = longitude;
		this.createDate = new Date();
	}
	
	public ConnexusImage(MobileImage image, String bkUrl){
		this.streamId = image.streamId;
		this.streamName = image.streamName;
		this.bkUrl = bkUrl;
		this.latitude = image.latitude;
		this.longitude = image.longitude;
		this.createDate = new Date();
	}
	
	
	@Override
	public String toString() {
		// Joiner is from google Guava (Java utility library), makes the toString method a little cleaner
		Joiner joiner = Joiner.on(":");
		return joiner.join(id.toString(), streamId, streamName, bkUrl==null ? "null" : bkUrl);
	}
	
	@Override
	public int compareTo(ConnexusImage other) {
		if (createDate.after(other.createDate)) {
			return -1;
		} else if (createDate.before(other.createDate)) {
			return 1;
		}
		return 0;
	}

}
