package com.tmxxx.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;




import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;

import com.tmghealth.monitor.Sender;
import com.tmxxx.Bean.AttributeType;
import com.tmxxx.Bean.Container;
import com.tmxxx.Bean.Host;
import com.tmxxx.core.Properties;

public class dataStoreThread implements Runnable{

	private static Logger log=Logger.getLogger(dataStoreThread.class);
	private Container<Host> hostContainer;
	private Map<AttributeType,List<String>> attributeMap;
	private List<String> tableList;
	
	public dataStoreThread(){
		
		
		
	}
	
	
	public dataStoreThread(Container<Host> hostContainer,Map<AttributeType,List<String>> attributeMap,List<String> tableList){
		
		this.hostContainer=hostContainer;
		this.attributeMap=attributeMap;
		this.tableList=tableList;
		
	}
	
	
	public void run(){
		
		String jmxUser=Properties.getProperty("tmg.jmx.user");
		String jmxPassword=Properties.getProperty("tmg.jmx.password");
		Host h=null;
		Sender sender=Sender.getInstance();
		while(true){
			synchronized(hostContainer){
				try{
					h=hostContainer.get();
					if(h==null){
						hostContainer.wait();
						continue;
					}	
				}catch(Exception e){
					log.info("",e);
				}
			}
			String host=h.getHost();
			String id=h.getId();
			JMXConnector jmxc=null;
			try{
				JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://"+host+":12099/jndi/rmi://"+host+":11099/jmxrmi");
			    String[] credentials = new String[] {jmxUser,jmxPassword};
			    Map<String,Object> env = new HashMap<String,Object>();
			    env.put(JMXConnector.CREDENTIALS, credentials);
			    jmxc = JMXConnectorFactory.connect(url, env);
			    MBeanServerConnection mbeanServerConn = jmxc.getMBeanServerConnection();
			    ObjectName bean=null;
			    List<String> attributeList=null;
			    attributeList=attributeMap.get(AttributeType.TABLE);
			    List<String> regionList=attributeMap.get(AttributeType.REGION);
			    for(String table:tableList){
			    	// for production
			    	//bean = new ObjectName("GemFireXD:group=GEMFIREALL,service=Table,type=Member,member="+id+",table="+table);
			    	
			    	//for uat
			    	bean = new ObjectName("GemFireXD:group=DEFAULT,service=Table,type=Member,member="+id+",table="+table);
			    	/*
			    	MBeanInfo info = mbeanServerConn.getMBeanInfo(bean);
			        MBeanAttributeInfo[] attributes = info.getAttributes();
			        for (MBeanAttributeInfo attr : attributes)
			        {
			        	String desc=attr.getDescription();
			            if(attributeList.contains(desc)){
			            	long value=(long)mbeanServerConn.getAttribute(bean,attr.getName());
			            	//sender.send(host,System.currentTimeMillis()/1000,table+"_"+desc,value,"");
			            }
			        }
			        */
			    	
			    	for(String attribute:attributeList){
			    		long value=(long)mbeanServerConn.getAttribute(bean,attribute);
		            	sender.send(host,System.currentTimeMillis(),table+"_"+attribute,value,"");
			    	}
			    	
			        
			        //TotalEntriesOnlyOnDisk
			        //GemFire:service=Region,name=/ODS/ADJUSTMENT,type=Member,member=10.66.13.153(15394)<v292>-44001
			        bean = new ObjectName("GemFire:service=Region,name=/"+table.replace(".","/")+",type=Member,member="+id);
			        //System.out.println("GemFire:service=Region,name=/"+table.replace(".","/")+",type=Member,member="+id);
			    	for(String attribute:regionList){
			    		long value=(long)mbeanServerConn.getAttribute(bean,attribute);
			    		//System.out.println(table+"_"+attribute+":"+value);
		            	sender.send(host,System.currentTimeMillis(),table+"_"+attribute,value,"");
			    	}

			    }
			    
			    //vm
			    bean = new ObjectName("GemFire:type=Member,member="+id);
			    attributeList=attributeMap.get(AttributeType.VM);
		    	for(String attribute:attributeList){
		    		long value=(long)mbeanServerConn.getAttribute(bean,attribute);
	            	sender.send(host,System.currentTimeMillis(),attribute,value,"");
		    	}
		    	
		    	//Disk Store
		        bean = new ObjectName("GemFire:service=DiskStore,name=DISK_STORE,type=Member,member="+id);
		        attributeList=attributeMap.get(AttributeType.DISK);
		    	for(String attribute:attributeList){
		    		long value=(long)mbeanServerConn.getAttribute(bean,attribute);
	            	sender.send(host,System.currentTimeMillis(),attribute,value,"");
		    	}
		        
		        //Disk store for event listener
		        bean = new ObjectName("GemFire:service=DiskStore,name=ODS_EVENTLISTENER,type=Member,member="+id);
		        attributeList=attributeMap.get(AttributeType.DISK);
		    	for(String attribute:attributeList){
		    		long value=(long)mbeanServerConn.getAttribute(bean,attribute);
	            	sender.send(host,System.currentTimeMillis(),attribute,value,"");
		    	}

		       //connection ActiveConnectionCount
		        Set<ObjectName> set=mbeanServerConn.queryNames(new ObjectName("com.pivotal.gemfirexd.internal:type=NetworkServer,system=*"), null);
		        for(ObjectName object:set){
		        	attributeList=attributeMap.get(AttributeType.NETWORK);
			    	for(String attribute:attributeList){
			    		int value=(int)mbeanServerConn.getAttribute(object,attribute);
		            	sender.send(host,System.currentTimeMillis(),attribute,value,"");
			    	}
		        }
			}catch(Exception e){
				
				log.info("",e);
				
			}finally{
				try{
					if(jmxc!=null)
						jmxc.close();
				}catch(Exception e){
					log.error("close connection error",e);
				}
			}
		}
		
		
		
	}
	
	
}
