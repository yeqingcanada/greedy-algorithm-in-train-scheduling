package com.Mia.bo;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.Mia.po.Station;
import com.Mia.vo.TimeList;
import com.Mia.vo.TimeStatu;

public class StationBo {

	PubBo pubBo = new PubBo();
	
	/**
	 * Initial the data of stations
	 * @param size The total number of stations
	 * @return The Information of all stations
	 */
	public Station[] initialData(int size){
		//get the data from Capacity.csv file
		String fileName = "data/Capacity.csv";
		List<StringTokenizer> lstToken = pubBo.readCSV(fileName);
		
		//put the data into data structure Station
		if(lstToken.size()==0) return null;
		
		Station[] stations = new Station[lstToken.size()-1];
		
		for(int i=1;i<lstToken.size();i++){
			stations[i-1] = new Station();
//			stations[i].setStationId(i+1);
//			if(i==0||i==stations.length-1) stations[i].setCapacity(40);
//			else if(i==19||i==39) stations[i].setCapacity(4);
//			else if(i==4||i==9||i==20||i==38||i==49||i==54) stations[i].setCapacity(3);
//			else stations[i].setCapacity(2);
						
			StringTokenizer token = lstToken.get(i);
			stations[i-1].setStationId(i);
			token.nextToken();
			stations[i-1].setCapacity(Integer.parseInt(token.nextToken()));
			
		}
		
		return stations;
	}
	
	/**
	 * get available time to arrive the station
	 * @param index index of the station
	 * @param time the compare time, available time should bigger than this time
	 * @return The index of the available time
	 */
	private int getAvailbleTime(List<TimeList> listTime, Date time,int position){
		for(int i=0;i<listTime.size();i++){
			if(pubBo.CmpTime(listTime.get(i).time, time)>0 && i<position && listTime.get(i).statu==TimeStatu.DEPART){
				return i;
			}
		}
		return position;
	}
	
	/**
	 * check the capacity of the station, give an feasible arrival schedule
	 * @param listTime The List stored all the trains arrival and departure time from the station
	 * @param station Information of the station
	 * @param position The position of the new train's arrival time
	 * @return The index of time that the new train should arrive
	 */
	public int checkCapacity(List<TimeList> listTime,Station station,int position_A,int position_D){
		int sum = 0;
		for(int i=0;i<listTime.size();i++){
			if(listTime.get(i).statu==TimeStatu.ARRAY) {
				sum += 1;
				if(sum > station.getCapacity()) {
					Date time = listTime.get(position_A).time;
					listTime.remove(position_A);
					position_A = this.getAvailbleTime(listTime, time,position_D-1);
					listTime.add(position_A+1, new TimeList(listTime.get(position_A).time,TimeStatu.ARRAY));
					return checkCapacity(listTime,station,position_A+1,position_D);
				}
			}
			else sum -= 1;
		}
		return position_A;
	}
	
}
