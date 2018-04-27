package com.tmxxxx.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateUtil {
	
	private static Logger log=Logger.getLogger(DateUtil.class);
	
	public static long Date2Mil(String date){
		
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			Date d = sdf.parse(date);
			return d.getTime();
		}catch(Exception e){
			log.info("invalid date",e);
		}
		
		return 0;
		
		
	}
	
	

}
