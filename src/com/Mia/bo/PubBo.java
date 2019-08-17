package com.Mia.bo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.Mia.vo.TimeList;

public class PubBo {

	/**
	 * compare time1 and time2
	 * @param time1
	 * @param time2
	 * @return 1:if time1 is larger; 0: equal; -1: time1 is smaller
	 */
	public int CmpTime(Date time1,Date time2){
		return time1.compareTo(time2);
	}
	
	/**
	 * compare time1 and time2
	 * @param time1 
	 * @param time2
	 * @return 1:if time1 is larger; 0: equal; -1: time1 is smaller
	 */
	public int CmpTime(Date time1,String time2){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String time = sdf.format(time1);
		String[] time1_str = time.split(":");
		String[] time2_str = time2.split(":");
		if(Integer.parseInt(time1_str[0])>Integer.parseInt(time2_str[0])){
			return 1;
		}
		else if(Integer.parseInt(time1_str[0])<Integer.parseInt(time2_str[0])){
			return -1;
		}
		else{
			if(Integer.parseInt(time1_str[1])>Integer.parseInt(time2_str[1])) return 1;
			else if(Integer.parseInt(time1_str[1])<Integer.parseInt(time2_str[1])) return -1;
			else return 0;
		}
	}
	
	/**
	 * Add operation of time
	 * @param time1 The time need to be added
	 * @param minutes The add minutes
	 * @return The time after add operation
	 */
	public Date AddTime(Date time1,int minutes){
		long time = time1.getTime();
		time = time + minutes*60*1000;
		return new Date(time);
	}
	
	/**
	 * Subtraction operation of time: time1 - time2
	 * @param time1 the minuend the time
	 * @param time2 the subtracter
	 * @return minutes
	 */
	public int SubTime(Date time1,Date time2){
		long t = time1.getTime() - time2.getTime();
		int minutes = (int)t/1000/60;
		return minutes;
	}
	
	/**
	 * Merge a arrival/departure time and statue into the listTime
	 * @param listTime An ordered list stored a set of TimeList
	 * @param time The time need to be merged
	 * @param statu The statue of the time(0:Arrival time;1:Departure time)
	 * @return The position of the new element
	 */
	public int mergeTime(List<TimeList> listTime, Date time, int statu){
		TimeList tlst = new TimeList(time,statu);
		for(int i=0;i<listTime.size();i++){
			if(this.CmpTime(listTime.get(i).time, time)>0){
				listTime.add(i, tlst);
				return i;
			}
		}
		listTime.add(tlst);
		return listTime.size()-1;
	}
	
	/**
	 * Parse a string to date 
	 * @param strDate The string need to be parsed, the format of string must satisfy "HH:mm"
	 * @return The date after parsed. If the string's format is wrong, will return null
	 */
	public Date perseDate(String strDate){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Read the csv file
	 * @param fileName The name of the file
	 * @return The List stored all the data in the file
	 */
	public List<StringTokenizer> readCSV(String fileName){
		List<StringTokenizer> lstToken = new ArrayList<StringTokenizer>();
		try { 
			File csv = new File(fileName); // CSV file
			BufferedReader br = new BufferedReader(new FileReader(csv));
			
			// read until the last line
			String line = "";
			while ((line = br.readLine()) != null) { 
				// split the tokens
				StringTokenizer st = new StringTokenizer(line, ",");
				lstToken.add(st);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		return lstToken;
	}
}
