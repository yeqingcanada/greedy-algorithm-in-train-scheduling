package com.Mia.vo;

import java.util.Date;

public class TimeList {

	/**
	 * the arrival/departure time
	 */
	public Date time;
	/**
	 * the statue of the time(arrival or departure)
	 */
	public int statu;
	
	//Constructors
	public TimeList(){
		super();
	}
	public TimeList(Date time, int statu) {
		super();
		this.time = time;
		this.statu = statu;
	}
}
