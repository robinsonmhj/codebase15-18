package com.tmghealth.log.process;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	 String regex=".*(\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,4}).*(TraceExecution|TraceAuthentication|TraceDDLReplay) <DRDAConnThread_([0-9]{1,})> tid=0x([0-9a-f]+).*((executing|Sending|Persisting|Authentication|CALL|alter|truncate|delete|update|insert|drop|select).*)";
 	     Pattern p=Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
 	     
 	     //Matcher m=p.matcher("2015/12/29 13:07:56.205 CST GFXD:TraceExecution <DRDAConnThread_268012> tid=0x44b5d] EmbedStatement#executeStatement: executing alter table ODS.\"CLAIM_COSHARE_TRACKING\" add column adj_to_clm_id bigint");
 	   // Matcher m=p.matcher("[info 2015/12/29 13:07:56.204 CST GFXD:TraceAuthentication <DRDAConnThread_268012> tid=0x44b5d] alter table ODS.\"CLAIM_COSHARE_TRACKING\" add column adj_to_clm_id bigint");
 	   Matcher m=p.matcher("[info 2015/12/02 23:17:12.989 CST GFXD:TraceExecution <DRDAConnThread_185735> tid=0x3893f] EmbedStatement#executeStatement: executing delete from ODS.PERSONS WHERE PRSN_ID=? and CLIENT_ID=?;, args: ;value=815386,type=0;value=8011,type=0");
 	     
 	     if(m.find()){
 	    	 int len=m.groupCount();
 	    	 
 	      String time=m.group(0);
  		  String action=m.group(1);
  		  int threadId=Integer.valueOf(m.group(2));
  		  int tid=Integer.parseInt(m.group(3), 16);
  		  String sql=m.group(4);
  		 // System.out.println();
 	    	 
 	    	/* 
 	    	 for(int i=1;i<len;i++){
 	    		 String value=m.group(i);
 	    		 
 	    		 if(i==4){
 	    			 int tid=Integer.parseInt(value, 16);
 	    			 System.out.println(tid+":"+value);
 	    		 }else
 	    			 System.out.println(value);
 	    			 
 	    	 }
 	    	*/	
 	    	
 	    	 
 	     }
 	    /*System.out.println("====================================");
 	    int tid=Integer.parseInt("11111", 16);
 	    System.out.println(tid);*/
       
    }
    
 
    
}
