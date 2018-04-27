/**
 * 
 */
package com.tmghealth.log.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.apache.log4j.Logger;

import com.tmghealth.log.bean.TraceBean;
import com.tmghealth.log.dao.GreenplumDAOImp;


/**
 * @author Haojie Ma
 * @date Dec 28, 2015
 */
public class MySink extends AbstractSink implements Configurable {

	private static Logger log= Logger.getLogger(MySink.class);
	
	private String hostName;
	private int port;
	private List<TraceBean> list;
	private String path;
	private final int insertThreshold=5000;
	private final int transactionThreshold=5000;
	private int count=0;
	private String table="gf_trace";
	private String delimiter="$";
	private String prefix;
	 @Override
	 public void configure(Context context) {
	    try{
	    	String key1="hostName";
	    	String key2="port";
	    	String key3="gpfdistDir";
	    	String key4="prefix";
	    	hostName=context.getString(key1);
	    	port=Integer.valueOf(context.getString(key2));
	    	path=context.getString(key3);
	    	prefix=context.getString(key4);
	    }catch(Exception e){
	    	log.info("",e);
	    	
	    }
	    	
	   }

	

	  @Override
	  public void start() {
		  
		  
		  
		if(StringUtils.isEmpty(hostName)){
				log.error("hostName cannot be null");
				stop();
		}
		
		if(!checkSocket()){
			log.error("gpfdisk on port "+port+" is not open!");
			stop();
		}
		 
		File file= new File(path);
		if(!file.exists()){
			log.error(path+" doesn't exist");
			stop();
		}
		
		  list= new ArrayList<TraceBean>();
		  final int timerInterval=5;
		  Thread t = new Thread(new Runnable() {
              public void run() {
                  while (true) {
                      try {
                          Thread.sleep(timerInterval * 1000);
                      } catch (InterruptedException ex) {
                    	  log.info("My god, I cannot sleep",ex);
                      }
                      cleanup();  
             		 if(count>=insertThreshold){
             			  load2DB();
             			  synchronized(list){
             				 count=0;  
             			  } 
             		  }
                  }
              }
          });

          t.setDaemon(true);
          t.start();
	  }

	  @Override
	  public void stop () {
		  cleanup();
		  load2DB();
	  }

	  @Override
	  public Status process() throws EventDeliveryException {
		  	
	    Status status = null;
	    String regex=".*(\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,4}).*(TraceExecution|TraceAuthentication|TraceDDLReplay) <DRDAConnThread_([0-9]{1,})> tid=0x([0-9a-f]+).*((executing|Sending|Persisting|Authentication|CALL|alter|truncate|delete|update|insert|drop|select)\\s.*)";
	    Pattern p=Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	    

	    Channel ch = getChannel();
	    Transaction txn = ch.getTransaction();
	    Event event=null;
	    txn.begin();
	    try {	
	    int eventCount=0;
	      for(eventCount=0;eventCount<transactionThreshold;eventCount++){
	    	  event = ch.take();
		      if(event!=null){
		    	  byte[] body=event.getBody();
		    	  if(body.length>=2048){	 
		    		  //String line1=new String(body,StandardCharsets.UTF_8);
		    		  //log.info("too long"+line1);
		    		  continue;
		    	  }
		    		  
		    	  String line=new String(body,StandardCharsets.UTF_8);
		    	  //String line=EventHelper.dumpEvent(event,event.getBody().length);
		    	  //System.out.println(Thread.currentThread().getName()+"+++++"+line);
		    	  Matcher m = p.matcher(line);
		    	  if(m.find()){
		    		  //System.out.println(Thread.currentThread().getName()+"****************matched");
		    		  TraceBean trace= new TraceBean();
		    		  //group(0) is the whole string
		    		  String time=m.group(1);
		    		  String action=m.group(2);
		    		  int threadId=Integer.valueOf(m.group(3));
		    		  //System.out.println(m.group(3)+":"+threadId);
		    		  int tid=Integer.parseInt(m.group(4), 16);
		    		  //System.out.println(m.group(4)+":"+tid);
		    		  String sql=m.group(5);
		    		  trace.setHost(hostName);
		    		  trace.setTime(time);
		    		  trace.setAction(action);
		    		  trace.setThreadId(threadId);
		    		  trace.setTid(tid);
		    		  trace.setSql(sql);
		    		  synchronized(list){
		    			  list.add(trace);
		    			  count++;
		    		  } 
		    	  } 
		      }else
		    	  break;
	      }
	     
	      txn.commit();
	      
	      if(eventCount<1)
	    	  return Status.BACKOFF;
	      else
	    	  return Status.READY;
	    	  
	      
	    } catch (Exception e) {
	    	txn.rollback();
	    	status = Status.BACKOFF;
	    	log.info("",e);
	    }finally{
	    	txn.close();
	    }
	    return status;
	  }
	  
	  
	  private void cleanup(){
		 // System.out.println(Thread.currentThread().getName()+"++in clean up");
		  BufferedWriter writer=null;
		  //String delimiter="$";
		  try{
			  writer= new BufferedWriter(new FileWriter(new File(path+"/"+prefix+table+".txt"),true));
			  synchronized(list){
				  //System.out.println("++++write to file");
				  Iterator<TraceBean> iterator=list.iterator();
				  while(iterator.hasNext()){
					  TraceBean trace=iterator.next();
					  writer.write(trace.getHost());
					  writer.write(delimiter);
					  writer.write(trace.getTime());
					  writer.write(delimiter);
					  writer.write(trace.getAction());
					  writer.write(delimiter);
					  writer.write(String.valueOf(trace.getThreadId()));
					  //System.out.println("threadId="+trace.getThreadId());
					  writer.write(delimiter);
					  writer.write(String.valueOf(trace.getTid()));
					  //System.out.println("tid="+trace.getTid());
					  writer.write(delimiter);
					  writer.write(trace.getSql());
					  writer.write("\n");
				  }
				  list.clear();
			  }
			  
		  }catch(Exception e){
			  
			  log.info("",e);
		  }finally{
			  try{
				  writer.close();  
			  }catch(Exception e){
				  log.info("close file error",e);
			  }
		  }

		  
	  }
	  
	  private void load2DB(){
		  log.info("++++in load to DB");
		  
		  	String schema="odm";
		  	//String table="gf_trace";
		  	String ext=".txt";
		  	int random=(int)(Math.random()*1000);
		  	String extTable=table+random;
		  	
		  	GreenplumDAOImp gpDaoImp= new GreenplumDAOImp();
			
			StringBuilder sb= new StringBuilder("CREATE EXTERNAL TABLE ").append(schema).append(".").append(extTable);;
			sb.append(" (like ").append(schema).append(".").append(table).append(")\n");
			sb.append("LOCATION ('gpfdist://").append(hostName).append(":").append(port).append("/").append(prefix).append(table).append(ext);
			sb.append("')\n FORMAT 'TEXT' ( DELIMITER '");
			sb.append(delimiter).append("' NULL as 'null');");
			
			//load data from flat file into gp table
			sb.append("insert into ").append(schema).append(".").append(table).append(" select * from ").append(schema).append(".").append(extTable).append(";");
			
			//drop the externerl table
			sb.append("drop external table ").append(schema).append(".").append(extTable).append(";");
			
			long start=System.currentTimeMillis();
			int[] results=gpDaoImp.executeMultipleQuery(sb.toString());
			long end=System.currentTimeMillis();
			long used=end-start;
			StringBuilder resultSb= new StringBuilder();
			if(results!=null){
				for(int i=0;i<results.length;i++){
					resultSb.append(results[i]).append(",");
				}
			}
			log.info("Load Table finished,using time "+used+",results:"+resultSb);
			
			File file= new File(path+"/"+prefix+table+".txt");
			file.delete();
			
		}
		  
	 
	  private boolean checkSocket(){
		  
		    Socket s = null;
		    boolean open=false;
		    try {
		        s = new Socket(hostName, port);
		        open=true;
		    } catch (Exception e) {
		        log.info("port:"+port+" is not opened");
		    } finally {
		        if( s != null){
		            try {
		                s.close();
		            } catch (Exception e) {
		                log.info("close socket error",e);
		            }
		        }
		    }
		  return open;
	  }
}

