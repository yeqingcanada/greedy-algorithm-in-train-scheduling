package com.Mia.po;

import java.util.Date;


public class Schedule {

	private int trainId;               //the train's id which the schedule for
	private Date[] departTime;		   //The array save the train's departure time from each station
	private Date[] ArriveTime;         //The array save the train's arrive time from each station
	
	public Schedule(){
		super();
	}
	/**
	 * Constructor
	 * @param trainId
	 * @param departTime
	 * @param arriveTime
	 */
	public Schedule(int trainId, Date[] departTime, Date[] arriveTime) {
		super();
		this.trainId = trainId;
		this.departTime = departTime;
		ArriveTime = arriveTime;
	}

	public int getTrainId() {
		return trainId;
	}

	public void setTrainId(int trainId) {
		this.trainId = trainId;
	}

	public Date[] getDepartTime() {
		return departTime;
	}

	public void setDepartTime(Date[] departTime) {
		this.departTime = departTime;
	}

	public Date[] getArriveTime() {
		return ArriveTime;
	}

	public void setArriveTime(Date[] arriveTime) {
		ArriveTime = arriveTime;
	}
	
}
