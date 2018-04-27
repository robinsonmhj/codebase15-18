/**
 * 
 */
package com.tmxxx.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;





import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.context.support.GenericXmlApplicationContext;














import com.tmxxx.Bean.AttributeType;
import com.tmxxx.Bean.Container;
import com.tmxxx.Bean.Host;
import com.tmxxx.Bean.ServerType;
import com.tmxxx.DAO.GemfireXDDAOImp;
import com.tmxxx.core.Properties;
import com.tmxxx.thread.GpfdistLoad;
import com.tmxxx.thread.ResourceCollectionThread;
import com.tmxxx.thread.TableStatisticsThread;
import com.tmxxx.thread.dataStoreThread;
/**
 * @author Haojie Ma
 * @date Jun 5, 2015
 */
public class Main {


	private static Logger log = Logger.getLogger(Main.class);
	
	
	
	
	public static void main(String[] args) {
		GenericXmlApplicationContext ctx=new GenericXmlApplicationContext(); 
		ctx.load("classpath:applicationContext.xml");  
		ctx.refresh(); 
		GemfireXDDAOImp gfxd=(GemfireXDDAOImp)ctx.getBean("gfxd");
		
		String intervalString=Properties.getProperty("tmg.get.interval");
		if(intervalString==null||intervalString.length()==0){
			System.out.println("tmg.get.interval cannot be null or blank");
			return;
		}
		String threadNoString=Properties.getProperty("tmg.jmx.threadNo");
		if(intervalString==null||intervalString.length()==0){
			System.out.println("tmg.jmx.threadNo cannot be null or blank");
			return;
		}
		
		int no=Integer.valueOf(threadNoString);
		
		int interval=Integer.valueOf(intervalString);
		
		/*
		List<String> attributesList= new ArrayList<String>();
		attributesList.add("UsedMemory");
		attributesList.add("CpuUsage");
		attributesList.add("TotalDiskUsage");
		attributesList.add("TotalBytesOnDisk");
		attributesList.add("NumberOfRows");
		attributesList.add("ActiveConnectionCount");
		attributesList.add("TotalEntriesOnlyOnDisk");
		*/
		
		List<String> networkAttributes= new ArrayList<String>();
		networkAttributes.add("ActiveConnectionCount");
		
		
		List<String> diskAttributes= new ArrayList<String>();
		diskAttributes.add("TotalBytesOnDisk");
		
		
		List<String> vmAttributes= new ArrayList<String>();
		vmAttributes.add("TotalDiskUsage");
		//vmAttributes.add("UsedMemory");
		//vmAttributes.add("CpuUsage");
		
		List<String> regionAttributes= new ArrayList<String>();
		regionAttributes.add("TotalEntriesOnlyOnDisk");
		
		List<String> tableAttributes= new ArrayList<String>();
		tableAttributes.add("NumberOfRows");
		
		
		Map<AttributeType,List<String>> attributemap= new HashMap<AttributeType,List<String>>();
		attributemap.put(AttributeType.NETWORK, networkAttributes);
		attributemap.put(AttributeType.DISK, diskAttributes);
		attributemap.put(AttributeType.VM, vmAttributes);
		attributemap.put(AttributeType.REGION, regionAttributes);
		attributemap.put(AttributeType.TABLE, tableAttributes);
		
		
		/*
		List<String> vmAttributes= new ArrayList<String>();
		vmAttributes.add("CurrentTime");
		vmAttributes.add("UsedMemory");
		vmAttributes.add("GetsRate");
		vmAttributes.add("PutsRate");
		vmAttributes.add("TotalRegionEntryCount");
		vmAttributes.add("MemberUpTime");
		vmAttributes.add("TotalDiskUsage");
		vmAttributes.add("CpuUsage");
		vmAttributes.add("DiskReadsRate");
		vmAttributes.add("DiskWritesRate");
		
		
		List<String> diskStoreAttributes= new ArrayList<String>();
		diskStoreAttributes.add("TotalBytesOnDisk");
		diskStoreAttributes.add("DiskReadsRate");
		diskStoreAttributes.add("DiskWritesRate");
		
		
		List<String> tableAttributes= new ArrayList<String>();
        tableAttributes.add("Updates");
        tableAttributes.add("Deletes");
        tableAttributes.add("Inserts");
        tableAttributes.add("NumberOfRows");
        
        */
        List<Host> hostList=gfxd.getServers(ServerType.DATASTORE);
        //Container<Host> hostContainer4vm= new Container<Host>();
        //Container<Host> hostContainer4Table= new Container<Host>();
        Container<Host> hostContainer4DataStore= new Container<Host>();
        for(Host h:hostList){
        	//hostContainer4vm.add2Queue(h);
        	//hostContainer4Table.add2Queue(h);
        	hostContainer4DataStore.add2Queue(h);
        }
      
        //for production
        //List<String> tableList=gfxd.getTableList("GEMFIREALL");

        //for uat
        List<String> tableList=gfxd.getTableList("");
        
        /*
        Container<String> fileContainer= new Container<String>();
        
       
       final int gpNo=2;
       
       
       ResourceCollectionThread[] vmCollectors= new ResourceCollectionThread[no];
 	   Thread[] vmThreads= new Thread[no];
 	   
 	   for(int i=0;i<no;i++){
 		  vmCollectors[i]=ctx.getBean("vmCollector", ResourceCollectionThread.class);
 		  vmCollectors[i].setFileContainer(fileContainer);
 		  vmCollectors[i].setDiskAttributes(diskStoreAttributes);
 		  vmCollectors[i].setHostContainer(hostContainer4vm);
 		  vmCollectors[i].setVmAttributes(vmAttributes);
 		  vmThreads[i]=new Thread(vmCollectors[i],"vmCollector"+i);
 	   }
 	
 	  TableStatisticsThread[] tableCollectors= new TableStatisticsThread[no];
	  Thread[] tableThreads= new Thread[no];
	   
	   for(int i=0;i<no;i++){
		   tableCollectors[i]=ctx.getBean("tableCollector", TableStatisticsThread.class);
		   tableCollectors[i].setHostContainer(hostContainer4Table);
		   tableCollectors[i].setFileContainer(fileContainer);
		   tableCollectors[i].setTableAttributes(tableAttributes);
		   tableCollectors[i].setTableList(tableList);
		   tableThreads[i]=new Thread(tableCollectors[i],"tableCollector"+i);
	   }

	  
 	   for(int i=0;i<no;i++){
 		  vmThreads[i].start();
 	   	  tableThreads[i].start();
 	   }
 	
 	   GpfdistLoad[] gpLoads= new GpfdistLoad[gpNo];
	   Thread[] gpLoadThreads= new Thread[gpNo];
	   
	   for(int i=0;i<gpNo;i++){
		  gpLoads[i]=ctx.getBean("gpLoader", GpfdistLoad.class);
		  gpLoads[i].setContainer(fileContainer);
		  gpLoadThreads[i]=new Thread(gpLoads[i],"gpLoader"+i);
		  gpLoadThreads[i].start();
	   }
	   */
        
       final int threadNo=10;
	   ExecutorService executor=Executors.newFixedThreadPool(threadNo);
	   for(int i=0;i<threadNo;i++){
			dataStoreThread ds= new dataStoreThread(hostContainer4DataStore,attributemap,tableList);
			executor.submit(ds);
		}
		
	   final int count=3600000/interval;
	   
	   int i=1;
       long lastRun=System.currentTimeMillis();
 	   while(true){
 		   long current=System.currentTimeMillis();
 		   if(current-lastRun<interval){
 			   try{
 				   Thread.sleep(interval);
 				   continue;
 			   }catch(Exception e){
 				   log.info("I cannot sleep",e);
 			   }
 		   }
 		  lastRun=current;
 		  //sometimes some server can be 
 		  if(i>=count){
 			 List<Host> tmpList=gfxd.getServers(ServerType.DATASTORE);
 			 if(tmpList!=null&&!tmpList.isEmpty()){
 				hostList=tmpList;
 			 }
 			i=1;
 		  }
 		  /* 
 		  //trigger another run
 		  synchronized(hostContainer4vm){
 			 for(Host h:hostList)
  	        	hostContainer4vm.add2Queue(h);
 			 try{
 				hostContainer4vm.notifyAll();
 			 }catch(Exception e){
 				 log.info("",e);
 			 }
 		  }
 		  
 		 synchronized(hostContainer4Table){
 			 for(Host h:hostList)
	        	hostContainer4Table.add2Queue(h);
 			 try{
 				hostContainer4Table.notifyAll();
 			 }catch(Exception e){
 				 log.info("",e);
 			 }
		  }
		  */
 		  
 		 synchronized(hostContainer4DataStore){
 			 for(Host h:hostList)
 				hostContainer4DataStore.add2Queue(h);
 			 try{
 				hostContainer4DataStore.notifyAll();
 			 }catch(Exception e){
 				 log.info("",e);
 			 }
 		  }
 		  i++;
 	   }
        
	}

}