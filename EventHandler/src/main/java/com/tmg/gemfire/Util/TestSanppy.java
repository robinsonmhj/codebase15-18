/**
 * 
 */
package com.tmg.gemfire.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import net.sourceforge.jtds.jdbc.Driver;
/**
 * @author Haojie Ma
 * May 27, 2017
 */
public class TestSanppy {
	public static void main(String[] args){
		  System.out.println("Hello1-----");
		try {
	        
	        //Connection conn =   DriverManager.getConnection("jdbc:snappydata://DEVRHSANDBOXV1:1527/");
			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        //Connection conn =   DriverManager.getConnection("jdbc:sqlserver://DEVCDCDBV1:1433;integratedSecurity=true;databaseName=FACETS_ETL_METADATA;user=dev_ssis_svc;password=5razuChE");
	        //Connection conn =   DriverManager.getConnection("jdbc:sqlserver://DEVCDCDBV1:1433;user=xfile;password=xfile","xfile","trustn00ne");
	        
	        Class.forName("net.sourceforge.jtds.jdbc.Driver");
	        Connection conn =   DriverManager.getConnection("jdbc:jtds:sqlserver://DEVCDCDBV1:1433;databaseName=bcbstxcaidrpt;domain=FACETS;useNTLMv2=true;user=dev_ssis_svc;password=5razuChE");
	        System.out.println("Hello2");
	        //PreparedStatement ps=conn.prepareStatement("select * from app.test");
	        PreparedStatement ps=conn.prepareStatement(" select * from cdc.dbo_CDS_IDIN_ID_INDIC_CT");
	       
	        ResultSet rs=ps.executeQuery();
	        System.out.println("Hello");
	        while(rs.next()){
	        	System.out.println("hello "+rs.getString(1)+","+rs.getString(2)+","+rs.getString(3));
	        	
	        	
	        	//System.out.println("hello "+rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5));
	        	
	        	
	        }

	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
		
		
	}
	
	
	
	
	
}
