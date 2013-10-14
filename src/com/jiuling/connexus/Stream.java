package com.jiuling.connexus;


import java.util.ArrayList;
import java.util.Date;

import com.google.common.base.Joiner;


public class Stream implements Comparable<Stream> {


	public Long id;
	public String name;
	public String tags;
	public Date createDate;
	public Date newImageDate;
	public int visitTime;
	public int pictureNum;
	public String coverImageUrl;
	public ArrayList<Date> visitQueue;
  
	// TODO: figure out why this is needed
	@SuppressWarnings("unused")
	private Stream() {
		visitTime = 0;
		pictureNum = 0;
		coverImageUrl = "http://www.sunipix.com/utaustin/UTAustin20.jpg";
		createDate = new Date();
		newImageDate = new Date();
		visitQueue = new ArrayList<Date>();
	}
	
	@Override
	public String toString() {
		Joiner joiner = Joiner.on(":");
		return joiner.join(id.toString(), name, coverImageUrl);
 	}

	public Stream(String name, String tags) {
		this.name = name;
		this.tags = tags;
		this.coverImageUrl = "http://www.sunipix.com/utaustin/UTAustin20.jpg";
		this.createDate = new Date();
		this.newImageDate = new Date();
		this.visitQueue = new ArrayList<Date>();
		visitTime = 0;
		pictureNum = 0;
	}
	public Stream(String name, String tags, String coverImageUrl) {
		this.name = name;
		this.tags = tags;
		this.coverImageUrl = coverImageUrl;
		this.createDate = new Date();
		this.newImageDate = new Date();
		this.visitQueue = new ArrayList<Date>();
		visitTime = 0;
		pictureNum = 0;
	}
	public int updateQueue(Date date){
		ArrayList<Date> tmpArrayList = new ArrayList<Date>();
		for (Date e:visitQueue ){
			if ((date.getTime()-e.getTime())/(1000*60*60) >= 1){
				tmpArrayList.add(e);
			}
		}
		for (Date e:tmpArrayList){
			visitQueue.remove(e);
		}
		return visitQueue.size();
	}

	
	public void updateNewImageDate(){
		newImageDate = new Date();
	}

	@Override
	public int compareTo(Stream other) {
		if (createDate.after(other.createDate)) {
			return -1;
		} else if (createDate.before(other.createDate)) {
			return 1;
		}
		return 0;
	}
}
