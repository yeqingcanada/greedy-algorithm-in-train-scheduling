package com.Mia.bo;

import java.util.Date;

import javax.swing.JFrame;

import com.Mia.po.Network;
import com.Mia.po.Schedule;
import com.Mia.po.Station;
import com.Mia.po.Train;


public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime=System.currentTimeMillis(); 
		
		ScheduleBo scheduleBo = new ScheduleBo();
		
		Train[] trains = new TrainBo().getBasicData();
		Network[] networks = new NetworkBo().getBasicData();
		Station[] stations = new StationBo().initialData(networks.length+1);
		
		
		Schedule[] schedules = scheduleBo.initialSchedule(networks);
		
		//get the original departure and arrival time of each train
		Date[] oriSrc = new Date[schedules.length];
		Date[] oriDst = new Date[schedules.length];
		for(int i=0;i<schedules.length;i++){
			oriSrc[i] = schedules[i].getDepartTime()[trains[i].getOriId()-1];
			oriDst[i] = schedules[i].getArriveTime()[trains[i].getDestId()-1];
		}
		
		scheduleBo.getFeasibleSchedule(schedules, trains, stations);
		
		Schedule[] sche_out = new Schedule[5];
		for(int i=0;i<5;i++){
			sche_out[i] = schedules[i];
		}
		JFrame frame=new JFrame("JavaChart");  
		frame.add(new DrawChart(sche_out).getChartPanel());    //add string chart  
		frame.setBounds(10, 10, 1000, 800);  
		frame.setVisible(true);
		
		sche_out = new Schedule[10];
		for(int i=0;i<10;i++){
			sche_out[i] = schedules[i];
		}
		frame=new JFrame("JavaChart");  
		frame.add(new DrawChart(sche_out).getChartPanel());    //add string chart  
		frame.setBounds(10, 10, 1000, 800);  
		frame.setVisible(true);
		
		sche_out = new Schedule[20];
		for(int i=0;i<20;i++){
			sche_out[i] = schedules[i];
		}
		frame=new JFrame("JavaChart");  
		frame.add(new DrawChart(sche_out).getChartPanel());    //add string chart  
		frame.setBounds(10, 10, 1000, 800);  
		frame.setVisible(true);
		
		sche_out = new Schedule[30];
		for(int i=0;i<30;i++){
			sche_out[i] = schedules[i];
		}
		frame=new JFrame("JavaChart");  
		frame.add(new DrawChart(sche_out).getChartPanel());    //add string chart  
		frame.setBounds(10, 10, 1000, 800);  
		frame.setVisible(true);
		
		sche_out = new Schedule[40];
		for(int i=0;i<40;i++){
			sche_out[i] = schedules[i];
		}
		frame=new JFrame("JavaChart");  
		frame.add(new DrawChart(sche_out).getChartPanel());    //add string chart  
		frame.setBounds(10, 10, 1000, 800);  
		frame.setVisible(true);
		
		sche_out = new Schedule[50];
		for(int i=0;i<50;i++){
			sche_out[i] = schedules[i];
		}
		frame=new JFrame("JavaChart");  
		frame.add(new DrawChart(sche_out).getChartPanel());    //add string chart  
		frame.setBounds(10, 10, 1000, 800);  
		frame.setVisible(true);
		
		//get some information
		scheduleBo.outputInfo(oriSrc, oriDst, trains, schedules);
		
		long endTime=System.currentTimeMillis();   
		System.out.println("running time "+(endTime-startTime)+"ms");
	}

}
