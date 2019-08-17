package com.Mia.po;

public class Station {
	
	private int stationId;          //the station's id
	private int capacity;           //the number of truck of the station
	
	public Station(){
		super();
	}
	/**
	 * Constructor
	 * @param stationId
	 * @param capacity
	 */
	public Station(int stationId, int capacity) {
		super();
		this.stationId = stationId;
		this.capacity = capacity;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	
}
