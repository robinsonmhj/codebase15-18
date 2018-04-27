/**
 * 
 */
package com.barclaycard.us.util;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Haojie Ma
 * Mar 25, 2018
 */
public class PathUtilTest {

	static String dir="TestCase/UtilTestCase/PathUtilTestCase/";	
	static List<String>  fileList=new ArrayList<String>();
	static PathUtil pathUtil;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fileList.add("gates.properties");
		fileList.add("conveyor.properties");
		for(String file:fileList){
			File source= new File(dir+file);
			source.renameTo(new File(file));
		}
		
		//file must move to the destnation before creating the singleton instance
		AuxiliaryUtil.reLoad();
		pathUtil=PathUtil.getInstance();
		pathUtil.reCalculate();
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		for(String file:fileList){
			File target= new File(file);
			target.renameTo(new File(dir+file));
		}
		
	}

	//test if the function remove dups in the gates file
	//the mocked file above including duplicated gate info
	@Test
	public void testGateCount() {
		int count=pathUtil.getGateCount();
		//System.out.println("count="+count);
		assertTrue(count==11);
	}
	
	
	@Test
	public void testPathNoReachable(){
		
		String res=pathUtil.getShortTime(1, 10);
		System.out.println(res);
		assertTrue(res.equals("Not Reachable"));
		
	}
	
	
	@Test
	public void testPathNoSuchGateBiggerThanSize(){
		
		String res=pathUtil.getShortTime(1, 100);
		//System.out.println(res);
		assertTrue(res.equals("No such gate"));
		
	}
	
	
	@Test
	public void testPathNoSuchGateLessThanSmallest(){
		String res=pathUtil.getShortTime(-1, 9);
		//System.out.println(res);
		assertTrue(res.equals("No such gate"));
	}
	
	
	@Test
	public void testPathNormal1to2(){
		String res=pathUtil.getShortTime(1, 2);
		assertTrue(res.equals("1->2,TotalTime:24.0"));
	}
	
	
	/*special case, in the test case file, there are two records
	 * as below
	 * 1,2,24
	 * 2,1,60
	 * the code will choose 1,2,24 instead of 1,2,60 as 60 is larger than 24
	 */
	
	@Test
	public void testPathNormal2to1(){
		String res=pathUtil.getShortTime(2, 1);
		assertTrue(res.equals("2->1,TotalTime:24.0"));
	}
	
	//speical case, which larger node comes before smaller one
	//3,1,3
	@Test
	public void testPathNormal1to3(){
		String res=pathUtil.getShortTime(1, 3);
		assertTrue(res.equals("1->3,TotalTime:3.0"));
	}
	
	//speical case, which larger node comes before smaller one
		//3,1,3
		@Test
		public void testPathNormal3to1(){
			String res=pathUtil.getShortTime(3, 1);
			assertTrue(res.equals("3->1,TotalTime:3.0"));
		}

		@Test
		public void testPathNormal1to4(){
			String res=pathUtil.getShortTime(1, 4);
			//System.out.println(res);
			assertTrue(res.equals("1->3->4,TotalTime:15.0"));
		}
		
		@Test
		public void testPathNormal4to1(){
			String res=pathUtil.getShortTime(4, 1);
			assertTrue(res.equals("4->3->1,TotalTime:15.0"));
		}
		
		@Test
		public void testPathNormal2to3(){
			String res=pathUtil.getShortTime(2, 3);
			//System.out.println(res);
			assertTrue(res.equals("2->1->3,TotalTime:27.0"));
		}
		
		@Test
		public void testPathNormal3to2(){
			String res=pathUtil.getShortTime(3, 2);
			//System.out.println(res);
			assertTrue(res.equals("3->1->2,TotalTime:27.0"));
		}
		
		
		@Test
		public void testPathNormal2to4(){
			String res=pathUtil.getShortTime(2, 4);
			assertTrue(res.equals("2->1->3->4,TotalTime:39.0"));
		}
		
		@Test
		public void testPathNormal4to2(){
			String res=pathUtil.getShortTime(4, 2);
			assertTrue(res.equals("4->3->1->2,TotalTime:39.0"));
		}
		
		
		@Test
		public void testPathNormal3to4(){
			String res=pathUtil.getShortTime(3, 4);
			assertTrue(res.equals("3->4,TotalTime:12.0"));
		}
		
		/*
		 * special case
		 * in the test case, there are 2 records
		 * 4,3,12 and 4,3,50
		 * The code will pick 4,3,12 instead of 4,3,50 as 12 is smaller than 50
		 */
		@Test
		public void testPathNormal4to3(){
			String res=pathUtil.getShortTime(3, 4);
			assertTrue(res.equals("3->4,TotalTime:12.0"));
		}
		
		/*
		 * Test the path should be the same when the pair is the same
		 * for example, the length should be the same when traveling from 1 to 4
		 * and from 4 to 1, but the path should be reversed
		 * 
		 */
		@Test
		public void testPathSpecial(){
			String p1=pathUtil.getShortTime(1, 4);
			String p2=pathUtil.getShortTime(4, 1);
			assertFalse(p1.equals(p2));
			assertTrue(p1.substring(p1.indexOf("TotalTime:")).equals(p2.substring(p2.indexOf("TotalTime:"))));
			assertFalse(p1.substring(0,p1.indexOf("TotalTime:")).equals(p2.substring(0,p2.indexOf("TotalTime:"))));
		}
		
		/*
		 * 
		 * special case
		 * no need to move, arrive and leave at the same gate
		 * 
		 */
		
		@Test
		public void testPathSpecial1to1(){
			String res=pathUtil.getShortTime(1, 1);
			assertTrue(res.equals("1,TotalTime:0.0"));
			
		}
		
		@Test
		public void testPathSpecial4to4(){
			String res=pathUtil.getShortTime(4, 4);
			assertTrue(res.equals("4,TotalTime:0.0"));
			
		}
		
}
