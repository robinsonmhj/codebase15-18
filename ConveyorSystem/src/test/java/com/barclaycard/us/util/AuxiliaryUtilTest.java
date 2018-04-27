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

import com.barclaycard.us.model.Bag;
import com.barclaycard.us.model.Conveyor;
import com.barclaycard.us.model.Departure;
import com.barclaycard.us.model.Flight;
import com.barclaycard.us.model.Gate;

/**
 * @author Haojie Ma
 * Mar 25, 2018
 */
public class AuxiliaryUtilTest {
	
	static String dir="TestCase/UtilTestCase/";	
	static List<String>  fileList=new ArrayList<String>();
	
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
		
		//reload configuration file
		AuxiliaryUtil.reLoad();
		
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

	

	@Test
	public void testGate() {
		List<Gate> l= new ArrayList<Gate>();
		l.add(new Gate((short)1,"A1"));
		l.add(new Gate((short)2,"A2"));
		assertTrue(l.equals(AuxiliaryUtil.getGates()));
		
	}
	
	
	@Test
	public void testFlight() {
		List<Flight> l= new ArrayList<Flight>();
		l.add(new Flight(3,"UA3"));
		l.add(new Flight(6,"AA6"));
		assertTrue(l.equals(AuxiliaryUtil.getFlights()));
		
	}
	
	
	@Test
	public void testBag() {
		List<Bag> l= new ArrayList<Bag>();
		l.add(new Bag(1l,(short)17,1));
		l.add(new Bag(6l,(short)6,6));
		List<Bag> res=AuxiliaryUtil.getBags();
		assertTrue(l.equals(res));
		
	}
	
	
	@Test
	public void testConveyor() {
		List<Conveyor> l= new ArrayList<Conveyor>();
		l.add(new Conveyor((short)1,(short)7,12));
		l.add(new Conveyor((short)10,(short)11,18));
		assertTrue(l.equals(AuxiliaryUtil.getConveyors()));
		
	}
	
	@Test
	public void testDeparture() {
		List<Departure> l= new ArrayList<Departure>();
		l.add(new Departure(1,(short)1,1));
		l.add(new Departure(4,(short)1,2));
		assertTrue(l.equals(AuxiliaryUtil.getDepartures()));
		
	}

}
