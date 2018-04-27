/**
 * 
 */
package com.tmxxx.thread;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tmxxx.Bean.Container;
import com.tmxxx.Bean.Host;
import com.tmxxx.core.Properties;


/**
 * @author Haojie Ma
 * @date Nov 2, 2015
 */


@Component("vmCollector")
@Scope("prototype")
public class ResourceCollectionThread implements Runnable{
	
	
	private static Logger log = Logger.getLogger(ResourceCollectionThread.class);
	
	private Container<String> fileContainer;
	private Container<Host> hostContainer;
	private List<String> vmAttributes;
	private List<String> diskAttributes;
	
	
	public ResourceCollectionThread(){
		
		
	}
	
	
	public ResourceCollectionThread(Container<String> fileContainer,Container<Host> hostContainer,List<String> vmAttributes,List<String> diskAttributes){
		this.hostContainer=hostContainer;
		this.vmAttributes=vmAttributes;
		this.diskAttributes=diskAttributes;
	}


	public void run(){
		String jmxUser=Properties.getProperty("tmg.jmx.user");
		String jmxPassword=Properties.getProperty("tmg.jmx.password");
		log.info("I am going to run");
		String delimiter=",";
		long lastTime=System.currentTimeMillis();
		long internal=3*60*60*1000L;
		String dir=Properties.getProperty("tmg.gpfdist.dir");
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Host h=null;
		String threadName=Thread.currentThread().getName();
		String fileName="vm"+threadName+lastTime;
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
			/*
			if(h==null){
				try{
					log.info("I am going to sleep");
					Thread.sleep(10000);
					continue;
				}catch(Exception e){
					log.info("I cannot sleep",e);
				}
			}
			*/
			String host=h.getHost();
			String id=h.getId();
			BufferedWriter writer=null;
			JMXConnector jmxc=null;
			
			try{
				long currentTime=System.currentTimeMillis();
				if(currentTime-lastTime>=internal){
					fileContainer.add2Queue(fileName);
					lastTime=currentTime;
					fileName="vm"+threadName+lastTime;
				}
				writer= new BufferedWriter(new FileWriter(dir+fileName,true));
				StringBuilder sb= new StringBuilder();
				sb.append(host).append(delimiter);
				
				JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://"+host+":12099/jndi/rmi://"+host+":11099/jmxrmi");
				String[] credentials = new String[] {jmxUser,jmxPassword};
			    Map<String,Object> env = new HashMap<String,Object>();
			    env.put(JMXConnector.CREDENTIALS, credentials);
				 jmxc = JMXConnectorFactory.connect(url, env);
			   
			    MBeanServerConnection mbeanServerConn =   jmxc.getMBeanServerConnection();
			    ObjectName bean = new ObjectName("GemFire:type=Member,member="+id); 
			    MBeanInfo info = mbeanServerConn.getMBeanInfo(bean);
		        MBeanAttributeInfo[] attributes = info.getAttributes();
		        Map<String,Object> tmpMap= new HashMap<String,Object>();
		        for (MBeanAttributeInfo attr : attributes)
		        {
		        	String desc=attr.getDescription();
		            if(vmAttributes.contains(desc)){
		            	Object value=mbeanServerConn.getAttribute(bean,attr.getName());
		            	if(desc.equals("CurrentTime")){
		            		value=formatter.format(new Date((Long)value));
		            	}
		            	tmpMap.put(desc,value);
		            	//sb.append(desc).append(":").append(value).append(delimiter);
		            }
		        }
		        
		        for(String key:vmAttributes){
		        	sb.append(tmpMap.get(key)).append(delimiter);
		        }
		        
		        
		        bean = new ObjectName("GemFire:service=DiskStore,name=DISK_STORE,type=Member,member="+id);
		        info = mbeanServerConn.getMBeanInfo(bean);
		        attributes = info.getAttributes();
		        for (MBeanAttributeInfo attr : attributes)
		        {
		        	String desc=attr.getDescription();
		            if(diskAttributes.contains(desc)){
		            	Object value=mbeanServerConn.getAttribute(bean,attr.getName()).toString();
		            	sb.append(value).append(delimiter);
		            }
		        }
		        
		        bean = new ObjectName("GemFire:service=DiskStore,name=ODS_EVENTLISTENER,type=Member,member="+id);
		        info = mbeanServerConn.getMBeanInfo(bean);
		        attributes = info.getAttributes();
		        for (MBeanAttributeInfo attr : attributes)
		        {
		        	String desc=attr.getDescription();
		            if(diskAttributes.contains(desc)){
		            	Object value=mbeanServerConn.getAttribute(bean,attr.getName()).toString();
		            	sb.append(value).append(delimiter);
		            }
		        } 
				//remove the last comma
		        sb.deleteCharAt(sb.length()-1);
		        sb.append("\n");
		        writer.write(sb.toString());
		        
				
			}catch(Exception e){
				
				log.info("",e);
				
			}finally{
				try{
					if(writer!=null)
						writer.close();
					if(jmxc!=null)
						jmxc.close();
				}catch(Exception e){
					
					log.error("lose file/connection error",e);
				}
			}
		}
		
		
		
	}

	
	

	public void setFileContainer(Container<String> fileContainer) {
		this.fileContainer = fileContainer;
	}


	public void setHostContainer(Container<Host> hostContainer) {
		this.hostContainer = hostContainer;
	}


	public void setVmAttributes(List<String> vmAttributes) {
		this.vmAttributes = vmAttributes;
	}


	public void setDiskAttributes(List<String> diskAttributes) {
		this.diskAttributes = diskAttributes;
	}
	
	
	
	
	

}


