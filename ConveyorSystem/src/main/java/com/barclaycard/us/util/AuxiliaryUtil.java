/**
 * 
 */
package com.barclaycard.us.util;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.barclaycard.us.model.Bag;
import com.barclaycard.us.model.Conveyor;
import com.barclaycard.us.model.Departure;
import com.barclaycard.us.model.Flight;
import com.barclaycard.us.model.Gate;


/**
 * @author Haojie Ma Mar 25, 2018
 */
public class AuxiliaryUtil {
	private static Logger log=Logger.getLogger(AuxiliaryUtil.class);
	private static List<Flight> flights;
	private static List<Bag> bags;
	private static List<Departure> departures;
	private static List<Gate> gates;
	private static List<Conveyor> conveyors;
	private static final String[] fileArr= new String[]{"gates.properties","conveyor.properties","flights.properties","bags.txt","departures.txt"};
	
	static {
		prepareData();
	}
	
	private static void prepareData(){
		prepareGates();
		prepareFlights();
		prepareBags();
		prepareDepartures();
		prepareConveyors();
		
		for(String fileName:fileArr){
			
			prepare(fileName);
			
		}
		
	}

	public static void reLoad(){
		prepareData();
		
	}
	
	
	private static void prepare(String fileName){
		
		List<String> l = FileUtil.readFile(fileName);
		Iterator<String> ite = l.iterator();
		while (ite.hasNext()) {
			String line = ite.next();
			String[] lines = line.split(",");
			if(fileName.equals("")){
				if(lines.length!=3){
					log.info("bad record:"+line);
					continue;
				}
				
				
			}
			
			
			
		}
		
	}
	public static class Attribute{
		
		String className;
		Class<?>[] construc;
		TYPE[] parameterType;
		
		public Attribute(){
			
			
		}
		
		public Attribute(String className,Class<?>[] construc,TYPE[] parameterType){
			this.className=className;
			this.construc=construc;
			this.parameterType=parameterType;
			
			
		}
		
	}
	@SuppressWarnings("unchecked")
	public static<T> T getInstance(Attribute a,String line){
		
		try{
			Class<?> clazz= Class.forName(a.className);
			Constructor<?> constructor=clazz.getConstructor(a.construc);
			int len=a.parameterType.length;
			Object[] obj= new Object[len];
			
			String[] lines=line.split(",");
			for(int i=0;i<len;i++){
				TYPE type=a.parameterType[i];
				String value=lines[i];
				Object o=transform(type,value);
				obj[i]=o;
				
			}
			
			return (T)constructor.newInstance(obj);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
 		return null;
		
		
	}
	
	
	
	enum TYPE{
		INT,SHORT,LONG,FLOAT,BOOLEAN,CHAR,BYTE,DOUBLE
	}
	
	private static Object transform(TYPE type,String value){
		Object o=null;
		switch(type){
		
		case INT:o=Integer.valueOf(value);break;
		case SHORT:o=Short.valueOf(value);break;
		case LONG:o=Long.valueOf(value);break;
		case FLOAT:o=Float.valueOf(value);break;
		case BOOLEAN:o=Boolean.valueOf(value);break;
		case CHAR:o=value.charAt(0);break;
		case BYTE:o=Byte.valueOf(value);break;
		case DOUBLE:o=Double.valueOf(value);break;
		default:o=value;
		}
		
		return o;
		
	}
	
	
	private static void prepareGates() {
		final String fileName = "gates.properties";
		gates = new ArrayList<Gate>();
		List<String> l = FileUtil.readFile(fileName);
		Iterator<String> ite = l.iterator();
		while (ite.hasNext()) {
			String line = ite.next();
			String[] lines = line.split(",");
			if(lines.length!=2){
				log.info("bad record:"+line);
				continue;
			}
			Gate g = new Gate(Short.valueOf(lines[0]),lines[1]);
			gates.add(g);
		}
	}
	
	
	private static void prepareConveyors() {
		final String fileName = "conveyor.properties";
		conveyors = new ArrayList<Conveyor>();
		List<String> l = FileUtil.readFile(fileName);
		Iterator<String> ite = l.iterator();
		while (ite.hasNext()) {
			String line = ite.next();
			String[] lines = line.split(",");
			if(lines.length!=3){
				log.info("bad record:"+line);
				continue;
			}
			Conveyor c = new Conveyor(Short.valueOf(lines[0]),Short.valueOf(lines[1]), Float.valueOf(lines[2]));
			conveyors.add(c);
		}
	}
	private static void prepareFlights() {

		final String fileName = "flights.properties";
		flights = new ArrayList<Flight>();
		List<String> l = FileUtil.readFile(fileName);
		Iterator<String> ite = l.iterator();
		while (ite.hasNext()) {
			String line = ite.next();
			String[] lines = line.split(",");
			if(lines.length!=3){
				log.info("bad record:"+line);
				continue;
			}
			Flight f = new Flight(Integer.valueOf(lines[0]), lines[1]);
			flights.add(f);
		}
	}

	private static void prepareBags() {
		
		final String fileName = "bags.txt";
		bags = new ArrayList<Bag>();
		List<String> l = FileUtil.readFile(fileName);
		Iterator<String> ite = l.iterator();
		while (ite.hasNext()) {
			String line = ite.next();
			String[] lines = line.split(",");
			if(lines.length!=3){
				log.info("bad record:"+line);
				continue;
			}
			
			
			Bag b = new Bag(Long.valueOf(lines[0]), Short.valueOf(lines[1]),
					Integer.valueOf(lines[2]));
			bags.add(b);
		}
	}

	private static void prepareDepartures() {
		final String fileName = "departures.txt";
		departures = new ArrayList<Departure>();
		List<String> l = FileUtil.readFile(fileName);
		Iterator<String> ite = l.iterator();
		while (ite.hasNext()) {
			String line = ite.next();
			String[] lines = line.split(",");
			if(lines.length!=4){
				log.info("bad record:"+line);
				continue;
			}
			Departure d = new Departure(Integer.valueOf(lines[0]),
					Short.valueOf(lines[1]), Integer.valueOf(lines[2]));
			departures.add(d);
		}
	}
	
	
	

	public static List<Flight> getFlights() {
		return flights;
	}

	public static List<Bag> getBags() {
		return bags;
	}

	public static List<Departure> getDepartures() {
		return departures;
	}

	public static List<Gate> getGates() {
		return gates;
	}

	public static List<Conveyor> getConveyors() {
		return conveyors;
	}
	
	
	

}
