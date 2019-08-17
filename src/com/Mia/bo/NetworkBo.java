package com.Mia.bo;

import java.util.List;
import java.util.StringTokenizer;

import com.Mia.po.Network;


public class NetworkBo {

	PubBo pubBo = new PubBo();
	
	/**
	 * read data from Network.csv file
	 * @return information about Network
	 */
	public Network[] getBasicData(){
		//get the data from Network.csv file
		String fileName = "data/Network.csv";
		List<StringTokenizer> lstToken = pubBo.readCSV(fileName);
		
		//put the data into data structure Network
		if(lstToken.size()==0) return null;
		Network[] networks = new Network[lstToken.size()-1];
		for(int i=1;i<lstToken.size();i++){
			StringTokenizer token = lstToken.get(i);
			networks[i-1] = new Network();
			networks[i-1].setSegId(Integer.parseInt(token.nextToken()));
			networks[i-1].setSourceId(Integer.parseInt(token.nextToken().substring(8)));
			networks[i-1].setDestId(Integer.parseInt(token.nextToken().substring(8)));
			networks[i-1].setTravelTime(Integer.parseInt(token.nextToken()));
		}
		return networks;
	}
}
