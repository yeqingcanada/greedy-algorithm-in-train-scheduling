package com.Mia.po;

import java.util.Date;

public class Train {
	
	private int trainId;                 //the id number of the train
	private Date depTime;                //the departure time of the train
	private int direction;               //the direction of the train,W or E
	private int oriId;                   //the origin station of the train
	private int destId;                   //the destination station of the train
	
	public Train(){
		super();
	}
	/**
	 * Constructor
	 * @param trainId
	 * @param depTime
	 * @param direction
	 * @param oriId
	 * @param destId
	 */
	public Train(int trainId,Date depTime,int direction,int oriId,int destId){
		super();
		this.trainId = trainId;
		this.depTime = depTime;
		this.direction = direction;
		this.oriId = oriId;
		this.destId = destId;
	}
	
	public int getTrainId() {
		return trainId;
	}

	public void setTrainId(int trainId) {
		this.trainId = trainId;
	}

	public Date getDepTime() {
		return depTime;
	}

	public void setDepTime(Date depTime) {
		this.depTime = depTime;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getOriId() {
		return oriId;
	}

	public void setOriId(int oriId) {
		this.oriId = oriId;
	}

	public int getDestId() {
		return destId;
	}

	public void setDestId(int destId) {
		this.destId = destId;
	}

	
}
