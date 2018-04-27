/**
 * 
 */
package com.tmg.gemfire.Action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sangupta.murmur.Murmur1;
import com.sangupta.murmur.Murmur2;
import com.sangupta.murmur.Murmur3;
import com.tmg.gemfire.Bean.Table;
import com.tmg.gemfire.DAOImp.GreenplumDAOImp;
import com.tmghealth.test.ConnectionPerformance;

/**
 * @author Haojie Ma
 * @date May 12, 2015
 */
public class Test {
	
	private static  ApplicationContext context=null;
	public static void main(String[] args){
		
		
		
		
		
		  context =  new ClassPathXmlApplicationContext("applicationContext.xml");
		  testConnectionPerformance();
		  /*
		  GreenplumDAOImp gpDAOImp = (GreenplumDAOImp) context.getBean("GreenplumDAO");
		
		  
		  Table t1=gpDAOImp.getTableMetaData("cdc", "ds_contract");
		  Table t2=gpDAOImp.getTableMetaData("sandbox", "ds_contract");
		  
		  
		  if(!t1.equals(t2)){
			  
			  System.out.println("They are different");
			  System.out.println(t1);
			  System.out.println("==================");
			  System.out.println(t2);
			  
		  }
			*/  
		
		
		  //murmurHash();
		/*
		Map<String,String> map = new HashMap<String,String>();
		map.put("clientId","123456");
		map.put("test","oiu89");
		try{
			generateKey(map);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		*/
		
		
		
		
	}
	
	
	
	/*
	public static void murmurHash(){
		
		
		String str = "Hello, World!";
		byte[] bytes=str.getBytes();
		long seed=1000l;
		long hash = Murmur1.hash(bytes, bytes.length, seed);
		System.out.println("Murmur1="+hash);
		

		hash = Murmur2.hash(bytes, bytes.length, seed);
		System.out.println("Murmur2="+hash);
		hash = Murmur2.hash64(bytes, bytes.length, seed);
		System.out.println("Murmur2.64="+hash);
		
		

		hash = Murmur3.hash_x86_32(bytes, bytes.length, seed); // 64-bit hash
		System.out.println("Murmur3.64="+hash);
		
		
		// returns 2 long values that contain the 128-bit hash
		long[] hashes = Murmur3.hash_x64_128(bytes, bytes.length, seed); // 128-bit hash
		
		
		
	}
	
	
	public static void generateKey(Map<String, String> args) throws Exception {
        long key = 0;
        if (args.containsKey("clientId")) {
               String argString = args.entrySet().stream()
                            .map(entry -> entry.getKey() + " - " + entry.getValue())
                            .collect(Collectors.joining(", ")); //Append using which character?

               System.out.println(argString);
               byte[] bytes = argString.getBytes();
               // long seed = 1000l;

               //key = Murmur3.hash64(bytes); //The 32 bit variant returns int not long. Other options of this method are by passing offset, length and seed params.

               //return key;
        } else {
               throw new Exception("Houston we have a problem"); 
        }
 }

	*/
	
	
	public static void testConnectionPerformance(){
		
		Map<Integer,String> sqls=new HashMap<Integer,String>();
		sqls.put(0, "select * from ods.procedure_code where PR_CD_ID=?");
		sqls.put(1, "select * from ods.reference_data where REF_DTA_ID=? and VER=?");
		sqls.put(2, "select * from ods.code_value where CD_VAL_ID=? and CD_TYP_REF_ID=? and CLIENT_ID=?");
		
		Map<Integer,List<String>> paras= new HashMap<Integer,List<String>>();
		List<String> l= new ArrayList<String>();
		l.add("8930843");
		l.add("8934572");
		l.add("8096790");
		l.add("9388945");
		l.add("8001870");
		l.add("9413353");
		l.add("8543818");
		l.add("8957963");
		l.add("9327021");
		l.add("8328666");
		l.add("8310247");
		l.add("8589244");
		l.add("7969213");
		l.add("8623144");
		l.add("8930504");
		l.add("8096564");
		l.add("8506415");
		l.add("8511161");
		l.add("8614330");
		l.add("9318094");
		l.add("8541558");
		l.add("8107073");
		l.add("8283692");
		l.add("8530032");
		l.add("8407879");
		l.add("8882253");
		l.add("8543140");
		l.add("8864625");
		l.add("8313185");
		l.add("8140069");
		l.add("7975767");
		l.add("8260414");
		l.add("8437146");
		l.add("8625178");
		l.add("8792192");
		l.add("9092659");
		l.add("8439632");
		l.add("8007407");
		l.add("8027295");
		l.add("8776598");
		l.add("8860896");
		l.add("8760100");
		l.add("8949488");
		l.add("8158714");
		l.add("9346457");
		l.add("8357255");
		l.add("9215038");
		l.add("8350475");
		l.add("8607324");
		l.add("9212326");
		l.add("8652524");
		l.add("8759196");
		l.add("8713996");
		l.add("9021921");
		l.add("8420987");
		l.add("9165544");
		l.add("9361825");
		l.add("8535908");
		l.add("8761456");
		l.add("8318609");
		l.add("8897847");
		l.add("9054578");
		l.add("8133628");
		l.add("8764281");
		l.add("8449915");
		l.add("8748913");
		l.add("8698289");
		l.add("8594329");
		l.add("9114807");
		l.add("8552632");
		l.add("8693430");
		l.add("8605064");
		l.add("9093224");
		l.add("9157521");
		l.add("8638060");
		l.add("8453418");
		l.add("8938640");
		l.add("9155148");
		l.add("8023340");
		l.add("9301596");
		l.add("9203964");
		l.add("8398387");
		l.add("8908356");
		l.add("8822024");
		l.add("9202043");
		l.add("8294992");
		l.add("9287697");
		l.add("7990570");
		l.add("8069218");
		l.add("8807334");
		l.add("9032317");
		l.add("8233520");
		l.add("8729025");
		l.add("9025989");
		l.add("8590035");
		l.add("8677158");
		l.add("9040905");
		l.add("8149222");
		l.add("8042663");
		l.add("9394030");
		paras.put(0, l);
		l=new ArrayList<String>();
		l.add("113,273");
		l.add("2486,273");
		l.add("5650,273");
		l.add("2825,273");
		l.add("1582,273");
		l.add("1000728,273");
		l.add("4972,273");
		l.add("2260,273");
		l.add("1356,273");
		l.add("2034,273");
		l.add("5311,273");
		l.add("1469,273");
		l.add("2599,273");
		l.add("5537,273");
		l.add("1000841,775091");
		l.add("4407,273");
		l.add("226,273");
		l.add("791,273");
		l.add("4746,273");
		l.add("1130,273");
		l.add("2147,273");
		l.add("452,273");
		l.add("678,273");
		l.add("5424,273");
		l.add("4294,273");
		l.add("2373,273");
		l.add("565,273");
		l.add("2938,273");
		l.add("1921,273");
		l.add("1000615,273");
		l.add("3955,273");
		l.add("5085,273");
		l.add("1017,273");
		l.add("904,273");
		l.add("4633,273");
		l.add("4181,273");
		l.add("5763,273");
		l.add("1808,273");
		l.add("339,273");
		l.add("5198,273");
		l.add("4068,273");
		l.add("4859,273");
		l.add("2712,273");
		l.add("1243,273");
		l.add("114,273");
		l.add("5425,273");
		l.add("1018,273");
		l.add("5312,273");
		l.add("2600,273");
		l.add("227,273");
		l.add("5764,273");
		l.add("2261,273");
		l.add("679,273");
		l.add("1696,273");
		l.add("1000842,775091");
		l.add("1470,273");
		l.add("5651,273");
		l.add("792,273");
		l.add("1809,273");
		l.add("4069,273");
		l.add("5199,273");
		l.add("1922,273");
		l.add("4408,273");
		l.add("4634,273");
		l.add("1244,273");
		l.add("1357,273");
		l.add("340,273");
		l.add("4860,273");
		l.add("1000729,273");
		l.add("453,273");
		l.add("2487,273");
		l.add("5086,273");
		l.add("4295,273");
		l.add("2713,273");
		l.add("2826,273");
		l.add("1000616,273");
		l.add("2035,273");
		l.add("1131,273");
		l.add("2374,273");
		l.add("566,273");
		l.add("4747,273");
		l.add("4182,273");
		l.add("1583,273");
		l.add("4521,273");
		l.add("2148,273");
		l.add("4973,273");
		l.add("3956,273");
		l.add("5538,273");
		l.add("905,273");
		l.add("1000730,273");
		l.add("4522,273");
		l.add("793,273");
		l.add("5087,273");
		l.add("4635,273");
		l.add("5765,273");
		l.add("567,273");
		l.add("2827,273");
		l.add("5426,273");
		l.add("1245,273");
		l.add("2488,273");
		paras.put(1, l);
		l=new ArrayList<String>();
		l.add("1806,962832,1806");
		l.add("3083,37815166,3083");
		l.add("1266,91695479,1266");
		l.add("1660,36799269,1660");
		l.add("1371,66292652,1371");
		l.add("1696,18318492,1696");
		l.add("1618,49983801,1618");
		l.add("2146,18578494,2146");
		l.add("2583,16123535,2583");
		l.add("4742,27255617,4742");
		l.add("1204,94730306,1204");
		l.add("1478,24117056,1478");
		l.add("1509,95962285,1509");
		l.add("2032,66797502,2032");
		l.add("2541,16512086,2541");
		l.add("2547,9378777,2547");
		l.add("834,17581884,834");
		l.add("4239,51309387,4239");
		l.add("4185,78401638,4185");
		l.add("2376,64424895,2376");
		l.add("2563,26783457,2563");
		l.add("1442,17688516,1442");
		l.add("3071,84957419,3071");
		l.add("2538,40428684,2538");
		l.add("1546,28691339,1546");
		l.add("1503,83283781,1503");
		l.add("4640,31504783,4640");
		l.add("2027,1076993,2027");
		l.add("4550,93993691,4550");
		l.add("860,98554235,860");
		l.add("1232,26024640,1232");
		l.add("4516,94237609,4516");
		l.add("3609,30946364,3609");
		l.add("3342,17498231,3342");
		l.add("3313,84849940,3313");
		l.add("4552,71490686,4552");
		l.add("553,97450393,553");
		l.add("1346,13384174,1346");
		l.add("4794,57087063,4794");
		l.add("2611,95986698,2611");
		l.add("2258,11659246,2258");
		l.add("3674,79637127,3674");
		l.add("2153,64068485,2153");
		l.add("838,51897935,838");
		l.add("2971,39382250,2971");
		l.add("3619,91747629,3619");
		l.add("648,66275095,648");
		l.add("1446,819309,1446");
		l.add("4122,7586234,4122");
		l.add("855,59599091,855");
		l.add("1734,49093541,1734");
		l.add("4782,38328772,4782");
		l.add("3911,96412133,3911");
		l.add("1399,33286061,1399");
		l.add("225,37861948,225");
		l.add("2755,35429460,2755");
		l.add("2505,12186634,2505");
		l.add("4201,60718221,4201");
		l.add("4209,40251528,4209");
		l.add("3861,13449783,3861");
		l.add("4453,16347729,4453");
		l.add("2581,72425181,2581");
		l.add("3962,37011552,3962");
		l.add("3734,94627490,3734");
		l.add("465,26879484,465");
		l.add("3527,85306166,3527");
		l.add("3361,35629926,3361");
		l.add("1432,87074905,1432");
		l.add("4112,20372486,4112");
		l.add("622,79434047,622");
		l.add("2014,93690507,2014");
		l.add("2450,87672301,2450");
		l.add("2517,62719717,2517");
		l.add("2627,85744079,2627");
		l.add("3318,38581398,3318");
		l.add("4052,42483982,4052");
		l.add("2205,69458069,2205");
		l.add("3983,38007168,3983");
		l.add("2443,6569785,2443");
		l.add("4565,70690949,4565");
		l.add("3163,98820396,3163");
		l.add("923,25104329,923");
		l.add("3299,58940994,3299");
		l.add("4639,24655919,4639");
		l.add("4807,5125621,4807");
		l.add("2054,27019317,2054");
		l.add("1375,19066057,1375");
		l.add("3945,9338689,3945");
		l.add("3240,97515574,3240");
		l.add("3462,5198351,3462");
		l.add("980,65611252,980");
		l.add("2908,24412790,2908");
		l.add("1466,50831596,1466");
		l.add("1350,46925830,1350");
		l.add("2659,91853097,2659");
		l.add("2091,65088268,2091");
		l.add("3291,9788332,3291");
		l.add("4467,2576753,4467");
		l.add("4417,72229478,4417");
		l.add("616,17996608,616");
		paras.put(2, l);
		
		final int threadNo=200;
		
		ConnectionPerformance[] cps= new ConnectionPerformance[threadNo];
		for(int i=0;i<threadNo;i++){
			//cps[i]= new ConnectionPerformance(sqls,paras);
			cps[i]=context.getBean("ConnectionPerformance", ConnectionPerformance.class);
			cps[i].setSqlMap(sqls);
			cps[i].setParameters(paras);
		}
		
		Thread[] threads= new Thread[threadNo];
		for(int i=0;i<threadNo;i++){
			threads[i]= new Thread(cps[i]);
			threads[i].start();
		}
		
		
		
		
		
		
	}
	

}


