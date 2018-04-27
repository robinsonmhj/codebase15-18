/**
 * 
 */
package com.tmxxx.thread;

/**
 * @author Haojie Ma
 * @date Nov 2, 2015
 */

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


@Component("tableCollector")
@Scope("prototype")
public class TableStatisticsThread implements Runnable{
	
	private static Logger log=Logger.getLogger(TableStatisticsThread.class);
	
	private Container<String> fileContainer;
	private Container<Host> hostContainer;
	private List<String> tableAttributes;
	private List<String> tableList;
	
	/*
	@Resource(name="gfxd")
	private  GemfireXDDAOImp gfxd;
	
	
	public void setGfxdDaoImp(GemfireXDDAOImp gfxd) {
		this.gfxd = gfxd;
	}
*/
	public TableStatisticsThread(){
		
		
	}
	
	public TableStatisticsThread(Container<String> fileContainer,Container<Host> hostContainer,List<String> tableAttributes,List<String> tableList){
		this.fileContainer=fileContainer;
		this.hostContainer=hostContainer;
		this.tableAttributes=tableAttributes;
		this.tableList=tableList;
	}
	
	
	public void run(){
		
		//List<String> tableList=gfxd.getTableList();
		log.info("I am going to run");
		String delimiter=",";
		long lastTime=System.currentTimeMillis();
		long internal=60*60*1000L;
		String jmxUser=Properties.getProperty("tmg.jmx.user");
		String jmxPassword=Properties.getProperty("tmg.jmx.password");
		String dir=Properties.getProperty("tmg.gpfdist.dir");
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Host h=null;
		String threadName=Thread.currentThread().getName();
		String fileName="table"+threadName+lastTime;
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
					Thread.sleep(20000);
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
					fileName="table"+threadName+lastTime;
				}	
				writer= new BufferedWriter(new FileWriter(dir+fileName,true));
				
				
				JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://"+host+":12099/jndi/rmi://"+host+":11099/jmxrmi");
			    String[] credentials = new String[] {jmxUser,jmxPassword};
			    Map<String,Object> env = new HashMap<String,Object>();
			    env.put(JMXConnector.CREDENTIALS, credentials);
			    jmxc = JMXConnectorFactory.connect(url, env);
			    MBeanServerConnection mbeanServerConn = jmxc.getMBeanServerConnection();
			    ObjectName bean=null;
			    for(String table:tableList){
			    	//long start=System.currentTimeMillis();
			    	bean = new ObjectName("GemFireXD:group=GEMFIREALL,service=Table,type=Member,member="+id+",table=ODS."+table);
			    	//long end=System.currentTimeMillis();
			    	//log.info("get infor for "+table+" using "+(end-start));
			    	StringBuilder sb= new StringBuilder();
			    	sb.append(host).append(delimiter);
			    	sb.append(table).append(delimiter);
			    	MBeanInfo info = mbeanServerConn.getMBeanInfo(bean);
			        MBeanAttributeInfo[] attributes = info.getAttributes();
			        Map<String,Object> tmpMap= new HashMap<String,Object>();
			        for (MBeanAttributeInfo attr : attributes)
			        {
			        	String desc=attr.getDescription();
			            if(tableAttributes.contains(desc)){
			            	Object value=mbeanServerConn.getAttribute(bean,attr.getName());
			            	tmpMap.put(desc,value);
			            }
			        }
			        for(String key:tableAttributes){
			        	sb.append(tmpMap.get(key)).append(delimiter);
			        }
					long getTime=System.currentTimeMillis();
			        sb.append(formatter.format(new Date(getTime)));
			        sb.append("\n");
			        //log.info(sb);
			        writer.write(sb.toString());
			    }
			    
				
			}catch(Exception e){
				
				log.info("",e);
				
			}finally{
				try{
					if(writer!=null)
						writer.close();
					if(jmxc!=null)
						jmxc.close();
				}catch(Exception e){
					
					log.error("close file/connection error",e);
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

	public void setTableAttributes(List<String> tableAttributes) {
		this.tableAttributes = tableAttributes;
	}

	public void setTableList(List<String> tableList) {
		this.tableList = tableList;
	}


	
	

}


