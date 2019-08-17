package com.Mia.po;

public class Network {
	
	private int segId;          //the segment id 
	private int sourceId;       //the source station id
	private int destId;         //the destination station id
	private int travelTime;     //the travel time through the segment
	
	
	public Network(){
		super();
	}
	/**
	 * Constructor
	 * @param segId the segment id 
	 * @param sourceId the source station id
	 * @param destId the destination station id
	 * @param travelTime the travel time through the segment
	 */
	public Network(int segId, int sourceId, int destId, int travelTime) {
		super();
		this.segId = segId;
		this.sourceId = sourceId;
		this.destId = destId;
		this.travelTime = travelTime;
	}
	
	public int getSegId() {
		return segId;
	}
	public void setSegId(int segId) {
		this.segId = segId;
	}
	public int getSourceId() {
		return sourceId;
	}
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	public int getDestId() {
		return destId;
	}
	public void setDestId(int destId) {
		this.destId = destId;
	}
	public int getTravelTime() {
		return travelTime;
	}
	public void setTravelTime(int travelTime) {
		this.travelTime = travelTime;
	}
	
}
