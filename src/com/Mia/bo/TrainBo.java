package com.Mia.bo;

import java.util.List;
import java.util.StringTokenizer;

import com.Mia.po.Train;
import com.Mia.vo.Direction;

public class TrainBo {
	
	PubBo pubBo = new PubBo();
	
	/**
	 * read data from Train.csv file
	 * @return information about trains
	 */
	public Train[] getBasicData(){
		//get the data from Network.csv file
		String fileName = "data/Train.csv";
		List<StringTokenizer> lstToken = pubBo.readCSV(fileName);
		
		//put the data into data structure Network
		if(lstToken.size()==0) return null;
		Train[] trains = new Train[lstToken.size()-1];
		for(int i=1;i<lstToken.size();i++){
			StringTokenizer token = lstToken.get(i);
			trains[i-1] = new Train();
			trains[i-1].setTrainId(Integer.parseInt(token.nextToken()));
			trains[i-1].setDepTime(pubBo.perseDate(token.nextToken()));
			trains[i-1].setDirection(token.nextToken().equals("W")?Direction.WEST:Direction.EAST);
			trains[i-1].setOriId(Integer.parseInt(token.nextToken().substring(8)));
			trains[i-1].setDestId(Integer.parseInt(token.nextToken().substring(8)));
		}
		return trains;
	}
}
