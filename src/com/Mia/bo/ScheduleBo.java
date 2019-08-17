package com.Mia.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


import com.Mia.po.Network;
import com.Mia.po.Schedule;
import com.Mia.po.Station;
import com.Mia.po.Train;
import com.Mia.vo.Direction;
import com.Mia.vo.PubData;
import com.Mia.vo.TimeList;
import com.Mia.vo.TimeStatu;

public class ScheduleBo {

	PubBo pubBo = new PubBo();
	StationBo staBo = new StationBo();
	/**
	 * the list saving each train's arrival and departure time from each siding,
	 * used to check sidings' capacities
	 */
	private List<List<TimeList>> listTime = new ArrayList<List<TimeList>>();
	
	/**
	 * initial data for each trains' schedule
	 * @param trains
	 * @param networks
	 * @return The initialed schedule
	 */
	public Schedule[] initialSchedule(Network[] networks){
		
		//get the data from Capacity.csv file
		String fileName = "data/Schedule.csv";
		List<StringTokenizer> lstToken = pubBo.readCSV(fileName);
		
		Schedule[] schedules = new Schedule[lstToken.size()-1];
		
		for(int i=1;i<lstToken.size();i++){
			schedules[i-1] = new Schedule();
			StringTokenizer token = lstToken.get(i);
			
			schedules[i-1].setTrainId(Integer.parseInt(token.nextToken()));
			schedules[i-1].setDepartTime(new Date[networks.length+1]);
			schedules[i-1].setArriveTime(new Date[networks.length+1]);
			Date[] depDate = new Date[networks.length+1];
			Date[] arrDate = new Date[networks.length+1];
			
			Date depTime = pubBo.perseDate(token.nextToken());
			int direction = token.nextToken().equals("W")?Direction.WEST:Direction.EAST;
			int oriId = Integer.parseInt(token.nextToken().substring(8));
			int destId = Integer.parseInt(token.nextToken().substring(8));
			
			int[][] stop_stations = new int[3][2];
			for(int j=0;j<3;j++){
				for(int k=0;k<2;k++){
					try{
						stop_stations[j][k] = Integer.parseInt(token.nextToken());
					}catch(Exception e){
						stop_stations[j][k] = 0;
					}
				}
			}
			
			if(direction==Direction.EAST){
				for(int j=0;j<networks.length+1;j++){
					if(j<oriId-1||j>destId-1){
						depDate[j] = null;
						arrDate[j] = null;
					}
					else if(j==oriId-1){
						depDate[j] = depTime;
						arrDate[j] = pubBo.perseDate("00:00");
					}
					else if(j>oriId-1){
						arrDate[j] = pubBo.AddTime(depDate[j-1],networks[j-1].getTravelTime());
						depDate[j] = arrDate[j];
						for(int k=0;k<3;k++){
							if(j==stop_stations[k][0]-1){
								depDate[j] = pubBo.AddTime(depDate[j], stop_stations[k][1]);
								break;
							}
						}
					}
					else{
						depDate[j] = pubBo.perseDate(PubData.UNLIMIT);
						arrDate[j] = pubBo.AddTime(depDate[j-1],networks[j-1].getTravelTime());
					}
				}
			}
			else{
				for(int j=networks.length;j>=0;j--){
					if(j>oriId-1||j<destId-1){
						depDate[j] = null;
						arrDate[j] = null;
					}
					else if(j==oriId-1){
						depDate[j] = depTime;
						arrDate[j] = pubBo.perseDate("00:00");
					}
					else if(j==destId-1){
						depDate[j] = pubBo.perseDate(PubData.UNLIMIT);
						arrDate[j] = pubBo.AddTime(depDate[j+1],networks[j].getTravelTime());
					}
					else{
						arrDate[j] = pubBo.AddTime(depDate[j+1],networks[j].getTravelTime());
						depDate[j] = arrDate[j];
						for(int k=0;k<3;k++){
							if(j==stop_stations[k][0]-1){
								depDate[j] = pubBo.AddTime(depDate[j], stop_stations[k][1]);
								break;
							}
						}
					}
				}
			}
			schedules[i-1].setDepartTime(depDate);
			schedules[i-1].setArriveTime(arrDate);
		}
		
		return schedules;
	}
	
	/**
	 * Get a feasible schedule for all trains
	 * @param schedules The original schedule which need to be adjusted
	 * @param trains The array of trains' information
	 * @param stations The array of stations's information
	 */
	public void getFeasibleSchedule(Schedule[] schedules,Train[] trains,Station[] stations){
		for(int i=0;i<stations.length;i++){
			List<TimeList> lst = new ArrayList<TimeList>();
			listTime.add(lst);
		}
		for(int i=0;i<trains.length;i++){
			int delay = 0;
			if(trains[i].getDirection()==Direction.EAST){
				for(int j=trains[i].getOriId();j<trains[i].getDestId();j++){
					schedules[i].getDepartTime()[j-1] = pubBo.AddTime(schedules[i].getDepartTime()[j-1], delay);
					schedules[i].getArriveTime()[j] = pubBo.AddTime(schedules[i].getArriveTime()[j], delay);
					delay += this.adjustSchedule(schedules, trains, trains[i].getTrainId(), stations, j, j+1);
				}
			}
			else{
				for(int j=trains[i].getOriId();j>trains[i].getDestId();j--){
					schedules[i].getDepartTime()[j-1] = pubBo.AddTime(schedules[i].getDepartTime()[j-1], delay);
					schedules[i].getArriveTime()[j-2] = pubBo.AddTime(schedules[i].getArriveTime()[j-2], delay);
					delay += this.adjustSchedule(schedules, trains, trains[i].getTrainId(), stations, j, j-1);
				}
			}
		}
	}
	
	/**
	 * AdjustSchedule a train's schedule in a segment
	 * @param schedules the information of all the schedules in the set of schedule
	 * @param trains the information of all the trains in the set of train
	 * @param trainId the id of the train who's schedule is being modified
	 * @param segments the information of all the segments in the set of segment
	 * @param segmentId the id of the segment in which the being-modified train are
	 * @param stations the information of all the stations in the set of station
	 * @param stationId1 the id of the station which the train departed
	 * @param stationId2 the id of the station which the train will arrive
	 */
	private int adjustSchedule(Schedule[] schedules,Train[] trains,int trainId,Station[] stations,int stationId1,int stationId2){
		int oriId_this = trains[trainId-1].getOriId();
		int destId_this = trains[trainId-1].getDestId();
		Date oriDepart_time = schedules[trainId-1].getDepartTime()[stationId1-1];
		for(int i=0; i<trainId-1; i++){
			if(schedules[i].getDepartTime()[stationId1-1]==null||pubBo.CmpTime(schedules[i].getDepartTime()[stationId1-1], PubData.UNLIMIT)==0||pubBo.CmpTime(schedules[i].getArriveTime()[stationId1-1], "00:00")==0) continue;
			int oriId_cmp = trains[i].getOriId();
			int destId_cmp = trains[i].getDestId();
			//if the two trains are in opposite directions
			if ((oriId_this - destId_this)*(oriId_cmp - destId_cmp) < 0){
				//if two trains meet at the segment
				if (pubBo.CmpTime(schedules[i].getDepartTime()[stationId2-1], schedules[trainId-1].getArriveTime()[stationId2-1])<0 && pubBo.CmpTime(schedules[trainId-1].getDepartTime()[stationId1-1],schedules[i].getArriveTime()[stationId1-1])<0){
					//train trainId must wait until train i pass
					int delay = pubBo.SubTime(schedules[i].getArriveTime()[stationId1-1], schedules[trainId-1].getDepartTime()[stationId1-1]);
					schedules[trainId-1].getArriveTime()[stationId2-1] = pubBo.AddTime(schedules[trainId-1].getArriveTime()[stationId2-1], delay);
					schedules[trainId-1].getDepartTime()[stationId1-1] = schedules[i].getArriveTime()[stationId1-1];
				} 
			}
			//if the two trains are in the same directions
			else{
				/* if two trains meet in the segment or arrive the next siding 
				 * at the same time, put off the this train¡¯s arrive time to make 
				 * sure they won¡¯t meet in the segment and have a safety distance 
				 * with each other 
				 */
				if ((pubBo.SubTime(schedules[trainId-1].getDepartTime()[stationId1-1], schedules[i].getDepartTime()[stationId1-1])*pubBo.SubTime(schedules[trainId-1].getArriveTime()[stationId2-1], schedules[i].getArriveTime()[stationId2-1])) <= 0){
					int delay = pubBo.SubTime(schedules[i].getDepartTime()[stationId1-1], schedules[trainId-1].getDepartTime()[stationId1-1]) + PubData.SAFETY;
					schedules[trainId-1].getDepartTime()[stationId1-1] = pubBo.AddTime(schedules[i].getDepartTime()[stationId1-1], PubData.SAFETY);
					schedules[trainId-1].getArriveTime()[stationId2-1] = pubBo.AddTime(schedules[trainId-1].getArriveTime()[stationId2-1], delay);
					//adjustSchedule(schedules, trains, trainId, stations, stationId1, stationId2);
				}
			}
		}
		/*if the station stationId2 is the destination of the train, 
		 * we don't need to check capacity
		 */
		if(trains[trainId-1].getDestId()==stationId2){
			schedules[trainId-1].getDepartTime()[stationId2-1] = pubBo.perseDate(PubData.UNLIMIT);
			return pubBo.SubTime(schedules[trainId-1].getDepartTime()[stationId1-1],oriDepart_time);
		}
		/* check capacity of the station stationId2 */
		List<TimeList> listTemp = listTime.get(stationId2-1);
		int position_A = pubBo.mergeTime(listTemp, schedules[trainId-1].getArriveTime()[stationId2-1],TimeStatu.ARRAY);
		int position_D = pubBo.mergeTime(listTemp, schedules[trainId-1].getDepartTime()[stationId2-1],TimeStatu.DEPART);
		/* if doesn't over the station's capacity, merge departure time to 
		 * listTime; if not, let the train wait at the previous station until
		 * it can arrive when there is a train left this station
		 */
		int temp_p = staBo.checkCapacity(listTemp,stations[stationId2-1], position_A,position_D);
		
		if(temp_p==position_A){
			return pubBo.SubTime(schedules[trainId-1].getDepartTime()[stationId1-1],oriDepart_time);
		}
		else{
			Date tempTime = listTemp.get(temp_p).time;
			int delay = pubBo.SubTime(tempTime,schedules[trainId-1].getArriveTime()[stationId1-1]);
			schedules[trainId-1].getArriveTime()[stationId2-1] = tempTime;
			schedules[trainId-1].getDepartTime()[stationId1-1] = pubBo.AddTime(schedules[trainId-1].getDepartTime()[stationId1-1], delay);
			//adjust again
			this.adjustSchedule(schedules, trains, trainId, stations, stationId1-1, stationId2-1);
		}
		return pubBo.SubTime(schedules[trainId-1].getDepartTime()[stationId1-1],oriDepart_time);
	}
	
	
	public void outputInfo(Date[] oriSrc, Date[] oriDst,Train[] trains, Schedule[] newSche){
		int travelTime_sum = 0;
		int waitTime_sum = 0;
		int delaySrc_sum = 0;
		int delayDst_sum = 0;
		int t_length = trains.length;
		
		for(int i=0;i<t_length;i++){
			Date[] departTime = newSche[i].getDepartTime();
			Date[] arriveTime = newSche[i].getArriveTime();
			int travelTime = pubBo.SubTime(arriveTime[trains[i].getDestId()-1],departTime[trains[i].getOriId()-1]);
			travelTime_sum = travelTime_sum + travelTime;
			delaySrc_sum = delaySrc_sum + pubBo.SubTime(departTime[trains[i].getOriId()-1], oriSrc[i]);
			delayDst_sum = delayDst_sum + pubBo.SubTime(arriveTime[trains[i].getDestId()-1], oriDst[i]);
			if(trains[i].getDirection()==Direction.EAST){
				for(int j=trains[i].getOriId();j<trains[i].getDestId()-1;j++){
					waitTime_sum = waitTime_sum + pubBo.SubTime(departTime[j], arriveTime[j]);
				}
			}
			else{
				for(int j=trains[i].getOriId()-2;j>trains[i].getDestId()-1;j--){
					waitTime_sum = waitTime_sum + pubBo.SubTime(departTime[j], arriveTime[j]);
				}
			}
		}
		
		int travelTime_avg = travelTime_sum/t_length;
		int waitTime_avg = waitTime_sum/t_length;
		int delaySrc_avg = delaySrc_sum/t_length;
		int delayDst_avg = delayDst_sum/t_length;
		
		System.out.println("Average travel time: "+travelTime_avg +" minutes");
		System.out.println("Average waiting time: "+waitTime_avg+" minutes");
		System.out.println("Average tardiness for departure times: "+delaySrc_avg+" minutes");
		System.out.println("Average tardiness for arrival times: "+delayDst_avg+" minutes");
	}
	
	
}
