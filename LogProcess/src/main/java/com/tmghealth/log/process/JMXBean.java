/**
 * 
 */
package com.tmghealth.log.process;

/**
 * @author Haojie Ma
 * @date Jul 25, 2016
 */



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.remote.JMXServiceURL;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
public class JMXBean {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://DEVRHGEMFIREV2.xxxx.com:12099/jndi/rmi://DEVRHGEMFIREV2.xxxxx.com:11099/jmxrmi");
		    // Assuming the following JMX credentials: 
		    //  username=controlRole, password=derby
		    String[] credentials = new String[] { "xxx" , "xxx" };
		    Map<String,Object> env = new HashMap<String,Object>();
		    // Set credentials (jmx.remote.credentials, 
		    //  see JMX Remote API 1.0 spec section 3.4)
		    env.put(JMXConnector.CREDENTIALS, credentials);
		    // if the server's RMI registry is protected with SSL/TLS (JDK 6)
		    //  (com.sun.management.jmxremote.registry.ssl=true), the following
		    //  entry must be included:
		    //env.put("com.sun.jndi.rmi.factory.socket", 
		    //    new SslRMIClientSocketFactory());  // uncomment if needed

		    // Connect to the server
		    JMXConnector jmxc = JMXConnectorFactory.connect(url, env);
		    MBeanServerConnection mbeanServerConn =   jmxc.getMBeanServerConnection();
		    
		    
		    ObjectName bean = new ObjectName("GemFire:type=Member,member=IP(18077)<v3>-44005");

	        MBeanInfo info = mbeanServerConn.getMBeanInfo(bean);
	        MBeanAttributeInfo[] attributes = info.getAttributes();
	        
	        
	        List<String> attributeList= new ArrayList<String>();
	        attributeList.add("CurrentTime");
	        attributeList.add("UsedMemory");
	        attributeList.add("GetsRate");
	        attributeList.add("PutsRate");
	        attributeList.add("TotalRegionEntryCount");
	        attributeList.add("MemberUpTime");
	        attributeList.add("TotalDiskUsage");
	        attributeList.add("CpuUsage");
	        attributeList.add("DiskReadsRate");
	        attributeList.add("DiskWritesRate");
	        for (MBeanAttributeInfo attr : attributes)
	        {
	        	String desc=attr.getDescription();
	            if(attributeList.contains(desc)){
	            	Object value=mbeanServerConn.getAttribute(bean,attr.getName());
	            	if(desc.equals("CurrentTime")){
	            		SimpleDateFormat formatter=new SimpleDateFormat("yyyy.MM.dd hh:mm:ss:SSS");

	            		System.out.println(formatter.format(new Date((Long)value)));
	            	}
	            		
	            	System.out.print(desc+":"+value+" ");
	            }
	        	
	        	//System.out.println(attr.getDescription() + " " + mbeanServerConn.getAttribute(bean,attr.getName()));
	        }
	        System.out.println();
	        
	        
	        bean = new ObjectName("GemFire:service=DiskStore,name=DISK_STORE,type=Member,member=IP(18077)<v3>-44005");
	        info=mbeanServerConn.getMBeanInfo(bean);
	        attributes = info.getAttributes();
	        List<String> diskAttributes= new ArrayList<String>();
	        diskAttributes.add("TotalBytesOnDisk");
	        diskAttributes.add("DiskReadsRate");
	        diskAttributes.add("DiskWritesRate");
	        
	        for (MBeanAttributeInfo attr : attributes)
	        {
	        	String desc=attr.getDescription();
	            if(diskAttributes.contains(desc)){
	            	Object value=mbeanServerConn.getAttribute(bean,attr.getName());
	            	System.out.print(desc+":"+value+" ");
	            }
	        	
	        }
	        System.out.println();
	        
	        // GemFire:service=DiskStore,name=ODS_EVENTLISTENER,type=Member,member=IP(18077)<v3>-44005 disk:TotalBytesOnDisk DiskReadsRate DiskWritesRate
	        
	        ObjectName beanELDisk = new ObjectName("GemFire:service=DiskStore,name=ODS_EVENTLISTENER,type=Member,member=IP(18077)<v3>-44005");
	        //GemFire:service=DiskStore,name=DISK_STORE,type=Member,member=IP(18077)<v3>-44005 disk:TotalBytesOnDisk DiskReadsRate DiskWritesRate
	        
	        
	        bean = new ObjectName("GemFireXD:group=GEMFIREALL,service=Table,type=Member,member=IP(18077)<v3>-44005,table=ODS.ADJUSTMENT");
	        info=mbeanServerConn.getMBeanInfo(bean);
	        attributes = info.getAttributes();
	        List<String> tableAttributes= new ArrayList<String>();
	        tableAttributes.add("Deletes");
	        tableAttributes.add("Inserts");
	        tableAttributes.add("Updates");
	        tableAttributes.add("NumberOfRows");
	        for (MBeanAttributeInfo attr : attributes)
	        {
	        	String desc=attr.getDescription();
	            if(tableAttributes.contains(desc)){
	            	Object value=mbeanServerConn.getAttribute(bean,attr.getName());
	            	System.out.print(desc+":"+value+" ");
	            }
	        	
	        }
	        
	        jmxc.close();
		}catch(Exception e){
			
			e.printStackTrace();
			
			
		}
		

	}

}


