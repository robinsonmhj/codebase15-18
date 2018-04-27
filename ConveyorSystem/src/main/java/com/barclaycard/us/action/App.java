package com.barclaycard.us.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.barclaycard.us.model.Bag;
import com.barclaycard.us.model.Departure;
import com.barclaycard.us.model.Path;
import com.barclaycard.us.service.PathService;
import com.barclaycard.us.service.PathServiceImpl;
import com.barclaycard.us.util.AuxiliaryUtil;
import com.barclaycard.us.util.PathUtil;

/**
 * Hello world!
 *
 */
public class App 
{
	
	public static List<String>  fileList=new ArrayList<String>();
    public static void main( String[] args )
    {
    	fileList.add("gates.properties");
		fileList.add("flights.properties");
		fileList.add("conveyor.properties");
		fileList.add("bags.txt");
		fileList.add("departures.txt");
    	String sourceDir="SampleData/";	
    	String targetDir="";
    	
    	//move data from sample to current folder
    	moveData(sourceDir,targetDir);
    	
    	
    	List<Bag> bags= AuxiliaryUtil.getBags();
    	PathService pathService = new PathServiceImpl();
    	
    	for(Bag b:bags){
    		long bagId=b.getId();
    		String path=pathService.getPath(bagId);
    		System.out.println(path);
    	}
    	
    	//after done, move the sample data back to sample folder
    	moveData(targetDir,sourceDir);
    	
    	
    	
    	
    }
    
    
    
    public static void moveData(String sourDir,String target){
    	
    	for(String file:fileList){
			File source= new File(sourDir+file);
			source.renameTo(new File(target+file));
		}
    }
    
    
    
    
}
