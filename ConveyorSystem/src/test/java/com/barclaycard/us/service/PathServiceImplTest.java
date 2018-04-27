/**
 * 
 */
package com.barclaycard.us.service;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.barclaycard.us.util.AuxiliaryUtil;
import com.barclaycard.us.util.PathUtil;


/**
 * @author Haojie Ma
 * Mar 25, 2018
 */
public class PathServiceImplTest {

	static String dir="TestCase/ServiceTestCase/";	
	static List<String>  fileList=new ArrayList<String>();
	static PathService pathService;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fileList.add("gates.properties");
		fileList.add("flights.properties");
		fileList.add("conveyor.properties");
		fileList.add("bags.txt");
		fileList.add("departures.txt");
		for(String file:fileList){
			File source= new File(dir+file);
			source.renameTo(new File(file));
		}
		
		AuxiliaryUtil.reLoad();
		PathUtil.getInstance().reCalculate();
		pathService= new PathServiceImpl();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		for(String file:fileList){
			File source= new File(file);
			source.renameTo(new File(dir+file));
		}
	}

	
	
	@Test
	public void test() {
		long bagId=1000;
		String res=pathService.getPath(bagId);
		assertTrue("1000,1->2,TotalTime:24.0".equals(res));
	}
	
	
	@Test
	public void testSpecialNoSuchBag() {
		long bagId=99999;
		String res=pathService.getPath(bagId);
		assertTrue("99999,The bag doesn't exist".equals(res));
	}
	
	
	/*
	
	 * special case
	 * the bag arrive and leave at the same gate
	 */
	@Test
	public void testSameGate() {
		long bagId=102;
		String res=pathService.getPath(bagId);
		//System.out.println(res);
		assertTrue("102,2,TotalTime:0.0".equals(res));
	}
	
	
	@Test
	public void testSpecialStartGateNotExists() {
		long bagId=1;
		String res=pathService.getPath(bagId);
		//System.out.println(res);
		assertTrue("1,The bag doesn't exist".equals(res));
	}
	
	
	@Test
	public void testSpecialDepartureGateNotExists() {
		long bagId=2;
		String res=pathService.getPath(bagId);
		//System.out.println(res);
		assertTrue("2,departure gate -1 doesn't exist".equals(res));
	}
	
	
	/*
	 * speical case 
	 * 
	 * a bag without departure record and the flightId is arrival
	 * 
	 */
	@Test
	public void testSpecialArrival() {
		long bagId=88;
		String res=pathService.getPath(bagId);
		//System.out.println(res);
		assertTrue("88,1->10,TotalTime:2.0".equals(res));
	}
	
	
	
	/*
	 * speical case 
	 * 
	 * a bag which cannot transferd to the departure gate, as there is no connction
	 * 
	 */
	@Test
	public void testSpecialNotReachable() {
		long bagId=99;
		String res=pathService.getPath(bagId);
		assertTrue("99,Not Reachable".equals(res));
	}

}
