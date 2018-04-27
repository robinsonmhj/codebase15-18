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

public class locatorThread implements Runnable{
	
	private static Logger log=Logger.getLogger(locatorThread.class);
	private Container<Host> hostContainer;
	private Map<AttributeType,List<String>> attributeMap;
	
	public locatorThread(){
		
		
		
	}
	
	
	public locatorThread(Container<Host> hostContainer,Map<AttributeType,List<String>> attributeMap){
		
		this.hostContainer=hostContainer;
		this.attributeMap=attributeMap;
		
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
			JMXConnector jmxc=null;
			try{
				JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://"+host+":12099/jndi/rmi://"+host+":11099/jmxrmi");
			    String[] credentials = new String[] {jmxUser,jmxPassword};
			    Map<String,Object> env = new HashMap<String,Object>();
			    env.put(JMXConnector.CREDENTIALS, credentials);
			    jmxc = JMXConnectorFactory.connect(url, env);
			    MBeanServerConnection mbeanServerConn = jmxc.getMBeanServerConnection();
			    List<String> attributeList=null;
			    attributeList=attributeMap.get(AttributeType.NETWORK);
		       //connection ActiveConnectionCount
		        Set<ObjectName> set=mbeanServerConn.queryNames(new ObjectName("com.pivotal.gemfirexd.internal:type=NetworkServer,system=*"), null);
		        for(ObjectName object:set){
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
