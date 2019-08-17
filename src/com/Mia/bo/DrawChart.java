package com.Mia.bo;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import com.Mia.po.Schedule;
import com.Mia.vo.PubData;

public class DrawChart {

	private ChartPanel frame1;  
	
	public DrawChart(Schedule[] schedules){  
		XYDataset xydataset = createDataset(schedules);  
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("The Schedule of Trains", "Time", "Station",xydataset, true, true, true);  
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();  
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();  
		dateaxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));  
		frame1=new ChartPanel(jfreechart,true);  
		dateaxis.setLabelFont(new Font("Courier New",Font.BOLD,14));       
		dateaxis.setTickLabelFont(new Font("Courier New",Font.BOLD,12)); 
		NumberAxis dominAxis = (NumberAxis) xyplot.getRangeAxis();
		dominAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		ValueAxis rangeAxis=xyplot.getRangeAxis(); 
		rangeAxis.setLabelFont(new Font("Courier New",Font.BOLD,15));  
		jfreechart.getLegend().setItemFont(new Font("Courier New", Font.BOLD, 15));  
		jfreechart.getTitle().setFont(new Font("Courier New",Font.BOLD,20));
	}   

	@SuppressWarnings("deprecation")
	private static XYDataset createDataset(Schedule[] schedules) {
		
		PubBo pubBo = new PubBo();
		
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(); 
		for(int i=0;i<schedules.length;i++){
			Schedule temp_s = schedules[i];
			TimeSeries timeseries = new TimeSeries("Train"+temp_s.getTrainId(), org.jfree.data.time.Minute.class);  
			for(int j=0;j<temp_s.getDepartTime().length;j++){
				Date temp_A = temp_s.getArriveTime()[j];
				Date temp_D = temp_s.getDepartTime()[j];
				if(temp_A!=null && pubBo.CmpTime(temp_A,"00:00")!=0){
					try{
						timeseries.add(new Minute(temp_A.getMinutes(),temp_A.getHours(),temp_A.getDay(),1,2013), j+1);
					}catch(Exception e){
						System.out.print(e.getMessage());
					}
				}
				if(temp_D!=null && pubBo.CmpTime(temp_D,PubData.UNLIMIT)!=0 && pubBo.CmpTime(temp_D, temp_A)!=0){
					try{
						timeseries.add(new Minute(temp_D.getMinutes(),temp_D.getHours(),temp_D.getDay(),1,2013), j+1);
					}catch(Exception e){
						System.out.print(e.getMessage());
					}
				}
			}
			timeseriescollection.addSeries(timeseries);
		}  
		return timeseriescollection;  
	}  

	public ChartPanel getChartPanel(){  
		return frame1;  
	}  
}
