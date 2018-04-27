/**
 * 
 */
package com.tmg.Action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;






import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;










import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.tmg.Bean.Cache;
import com.tmg.Bean.Column;
import com.tmg.Bean.LightTable;
import com.tmg.Bean.MapContainer;
import com.tmg.Bean.Row;
import com.tmg.Bean.Table;
import com.tmg.Bean.TableContainer;
import com.tmg.Util.FileUtil;
import com.tmg.core.Properties;
import com.tmg.greenplum.DAOImp.GreenplumDAOImp;
import com.tmg.thread.CombineFile;
import com.tmg.thread.ExportThread;
import com.tmg.thread.FileList;
import com.tmg.thread.FlatFileCombine;
import com.tmg.thread.GpfdistLoad;
import com.tmg.thread.ImportThread;
import com.tmg.thread.GenerateTableDefinition;

/**
 * @author Haojie Ma
 * @date Jun 5, 2015
 */
public class Main {


	private static Logger log = Logger.getLogger(Main.class);
	private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
			"applicationContext.xml");
	private static GreenplumDAOImp gpDAOImp = (com.tmg.greenplum.DAOImp.GreenplumDAOImp) context
			.getBean("GreenplumDAO");
	
	
	

	public static void main(String[] args) {
		
		List<String> list=gpDAOImp.getTables("cdc");
		System.out.println(list.size());
		for(String table:list)
			System.out.println(table);
		//testLoadFlatFile();
		//testProcessFile();//used to test convert flat file to gp formated file
		//readFile();

		//testCache();

		/*
		gpDAOImp.getDistributedKey("cdc", "s_fac_dbo_cmc_umum_util_mgt_ps");
		//compressFile("cdc.ds_transaction_register");
		
		int len=args.length;
		if(len<1){
			return;
		}
		
		
		for(int i=0;i<len;i++)
			log.info(i+":"+args[i]);
		
		String appName=args[0];
		if(!appName.equals("GenerateDefinition")&&!appName.equals("DR")){
			System.out.println("choose which app you want to run\n AppName:GenerateDefinition or DR");
			return;
		}
			
		
		int threadNo=1;
		if(appName.equals("GenerateDefinition")){
			
			if(len!=2){
				System.out.println("Usage:java -jar jarName GenerateDefinition threadNo");
				return;
			}
			
			try{
				threadNo=Integer.valueOf(args[1]);
			}catch(Exception e){
				System.out.println("Bad parameter:"+args[1]+",using the default threadNo 1");
			}
			
			
			testGenerateTableDefinition(threadNo);
		}
		
		
		if(appName.equals("DR")){
		
			if(args.length!=4){
			
				System.out.println("============================================================================");
				System.out.println("Usage: java -jar DR threadNo from to");
				System.out.println("============================================================================");
				return;
			
			}
		
			try{
				threadNo=Integer.valueOf(args[1]);
			}catch(Exception e){
				System.out.println("Bad parameter:"+args[1]+",using the default threadNo 1");
			}
			
			
			int from=0;
			int to=0;
			try{
				from=Integer.valueOf(args[2]);
				to=Integer.valueOf(args[3]);
			}catch(Exception e){
			
				System.out.println("============================================================================");
				System.out.println("Usage: java -jar DR threadNo from to");
				System.out.println("============================================================================");
				return;
			
			}
		
			System.out.println("from="+from+",to="+to);
			testTableDR(threadNo,from,to);
		}
		*/
		
		
		
		
	    

		

		//context.close();

	}

	
	public static void testLoadFlatFile(){
		String schema=Properties.getProperty("tmg.flatfile.schma.name");
		if(StringUtils.isEmpty(schema)){
			log.error("the value for tmg.flatfile.schma.name must be set");
			return;
		}
		
		String table;
		
		//tableList, used by multiple thread to get the file List
		List<String> tableList=gpDAOImp.getTables(schema);
		Iterator<String> iterator=tableList.iterator();
		LinkedList<String> linkList=new LinkedList<String>();
		while(iterator.hasNext()){
			table=iterator.next();
			linkList.add(table);
			//log.info("added table "+table);
		}
		
		TableContainer container= new TableContainer(linkList);
		MapContainer<String,String[]> mapContainer= new MapContainer<String,String[]>();
		
		//multiple thread to get file list for each table
		final int listFileNo=5;
		Thread[] getFileListThread= new Thread[listFileNo];
		FileList[] FileList= new FileList[listFileNo];
		
		for(int i=0;i<listFileNo;i++){
			FileList[i]=new FileList(container,mapContainer);
			getFileListThread[i]=new Thread(FileList[i],"getFileList"+i);
			getFileListThread[i].start();
			
		}
		
		for(int i=0;i<listFileNo;i++){
			try{
				getFileListThread[i].join();
			}catch(Exception e){
				log.info("Cannot join thread,"+getFileListThread[i].getName(),e);
				
			}
			
		}
		
		
		//format the original flat file  to gpfdist file
		TableContainer tableContainer= new TableContainer();//stored the formated flag file
		
		// multiple thread to format flat file
		final int formatFileThreadNo = 5;
		Thread[] formatFileThread = new Thread[formatFileThreadNo];
		FlatFileCombine[] formatFileList = new FlatFileCombine[formatFileThreadNo];

		for (int i = 0; i < formatFileThreadNo; i++) {
			formatFileList[i] = (com.tmg.thread.FlatFileCombine)context.getBean("FormatFlatFile");
			formatFileList[i].setMap(mapContainer);
			formatFileList[i].setTableContainer(container);
			formatFileThread[i] = new Thread(formatFileList[i], "formatFileList" + i);
			formatFileThread[i].start();

		}

		for (int i = 0; i < formatFileThreadNo; i++) {
			try {
				formatFileThread[i].join();
			} catch (Exception e) {
				log.info("Cannot join thread," + formatFileThread[i].getName(),e);

			}

		}
		
		/*
		//multiple threads code to load data into gp
		final int gpLoadThreadNo = 5;
		Thread[] gpLoadThread = new Thread[gpLoadThreadNo];
		GpfdistLoad[] gpLoadArray = new GpfdistLoad[gpLoadThreadNo];

		for (int i = 0; i < gpLoadThreadNo; i++) {
			gpLoadArray[i] = new GpfdistLoad(schema,tableContainer);
			gpLoadThread[i] = new Thread(gpLoadArray[i], "gpLoadArray" + i);
			gpLoadThread[i].start();

		}

		for (int i = 0; i < gpLoadThreadNo; i++) {
			try {
				gpLoadThread[i].join();
			} catch (Exception e) {
				log.info("Cannot join thread," + gpLoadThread[i].getName(),e);

			}

		}
		*/
		
	}
	
	
	public static void testMatch(){
		
		String table="ds_transaction_register_1_prt_fsr";
		//String table="cdc.claim";
	    if(table.matches("ds_transaction_register_[0-9]{1,}_prt_.*"))
	    	System.out.println("matches");
	    else
	    	System.out.println("doesn't match");
	    String[] tables=table.split("_[0-9]{1,}_prt_");
	    System.out.println(tables[0]);
	    System.out.println(table.substring(0, table.indexOf("_")));
		
		
	}
	
	
	public static void testProcessFile(){
		
		String fileName="organizations_1450305159234.txt";
		List<Row> list=FileUtil.processFile(fileName);
		
		String schemaName="ods";
		String tableName="organizations";
		
		LightTable table=gpDAOImp.getLightTable(schemaName, tableName);
		
		List<String> columnList=table.getColumnList();
		
		Iterator<Row> itera=list.iterator();
		
		while(itera.hasNext()){	
			Row row=itera.next();
			String operator=row.getOperation();
			Map<String,String> map=row.getMap();
			for(int i=0;i<columnList.size();i++){
				String key=columnList.get(i);
				String value=map.get(key);
				if(key.equals("ver")&&operator.equalsIgnoreCase("DELETE"))
					value="-"+value;
				else if(key.equals("del"))
					value="false";
				System.out.print(value+",");		
			}
			
			System.out.println();
			
			
		}
		
		
		
	}
	
	
	
	public static void getTableWithoutTS(){
		

		
		String schema="cdc";
		List<String> tableList = gpDAOImp.getTables(schema);
		Iterator<String> iterator = tableList.iterator();

		String tableName;
		
		
		
		while (iterator.hasNext()) {
			tableName = iterator.next();
			
			
			Table table = gpDAOImp.getTableMetaData(schema, tableName);
			List<Column> columnList = table.getCloumns();
			Iterator<Column> iterator1 = columnList.iterator();
			int count = 0;
			while (iterator1.hasNext()) {
				Column column = iterator1.next();
				String columnName = column.getName();
				if (columnName.contains("insrt_ts")	|| columnName.contains("etl_insrt_ts")) {
					break;
				}else
					count++;
			}

			if (columnList.size() == count) {
				System.err.println(tableName);
			}
			
		}
		
		 
		 

		context.close();

		
		
	}
	
	
	public static void testTableDR(int threadNo,int fromInteger,int toInteger){
		


		String schema = "cdc",fullTableName,tmp,url;
		String protocol=Properties.getProperty("tmg.gpfdist.protocol");
		String server=Properties.getProperty("tmg.gpfdist.server");
		String port=Properties.getProperty("tmg.gpfdist.port");
		
		if(StringUtils.isEmpty(protocol)||StringUtils.isEmpty(server)||StringUtils.isEmpty(port)){
			
			log.error("one of the values for protocol, sever and port is invalid!");
			return;
		}
		
		String dir=Properties.getProperty("tmg.gpfdist.dir");
		String backupDir=Properties.getProperty("tmg.gpfdist.processed.dir");
		
		File file= new File(dir);
		if(!file.exists()||!file.isDirectory()){
			log.error("no such directory:"+dir);
			return;
		}
		
		file=new File(backupDir);
		if(!file.exists()||!file.isDirectory()){
			log.error("no such directory:"+dir);
			return;
		}
		
		
		StringBuilder urlsb=new StringBuilder(protocol).append("://");
		urlsb.append(server).append(":").append(port).append("/");
		url=urlsb.toString();
		
		
		Cache<String,Table> gpCache= new Cache<String,Table>(0,0,100);
		Cache<String,Table> drCache= new Cache<String,Table>(0,0,100);
		
		
		//date parameters
		String from=FileUtil.getDate(fromInteger);
		String to=FileUtil.getDate(toInteger);
		
		
		log.info("from="+from+",to="+to);
		long start=System.currentTimeMillis();
		List<String> tableList = gpDAOImp.getPartitionedTable(schema);
		long end=System.currentTimeMillis();
		long used=end-start;
		log.debug("get table list using time:"+used);
		Iterator<String> iterator = tableList.iterator();
		TableContainer fullNameTableConsumer = new TableContainer();//consumed by export thread,generated by main thread
		TableContainer tableProducer = new TableContainer();//produced by Combine thread and consumed by import thread
		TableContainer tableConsumer = new TableContainer();//consumed by Combine thread,generated by export  thread
		//used by combine thread, generated by main thread
		//Map<String,List<String>> map= new HashMap<String,List<String>>();
		//used by export thread and combine thread, key:schema+tableName,value:partitionedTableName
		//only export thread write, combine thread read 
		Map<String,List<String>> processedMap= new HashMap<String,List<String>>();
		int count = 0;
		while (iterator.hasNext()) {
			tmp = iterator.next();
			//fullTableName=schema+"."+tmp;
			fullTableName = new StringBuilder(schema).append(".").append(tmp).toString();
			/*
			tables=fullTableName.split(regex);
			tableName=tables[0];
			
			if(map.containsKey(tableName)){
				list=map.get(tableName);	
			}else{
				list= new ArrayList<String>();
			}
			list.add(fullTableName);
			map.put(tableName, list);			
			*/
			fullNameTableConsumer.add2Queue(fullTableName);
			count++;
			}
		
		/*
		for(String key:map.keySet()){
			tableConsumer.add2Queue(key);
			
		}
        */
		log.info("There are " + count + " tables");

		ExportThread[] producerArray = new ExportThread[threadNo];
		CombineFile[] combineFileArray= new CombineFile[threadNo];
		ImportThread[] consumerArray = new ImportThread[threadNo];
		

		Thread[] producerThreadArray = new Thread[threadNo];
		Thread[] combineFileThreadArray = new Thread[threadNo];
		Thread[] consumerThreadArray = new Thread[threadNo];

		for (int i = 0; i < threadNo; i++) {

			producerArray[i] = (com.tmg.thread.ExportThread) context
					.getBean("Export");
			producerArray[i].setTableConsumer(fullNameTableConsumer);
			producerArray[i].setTableProducer(tableConsumer);
			producerArray[i].setUrl(url);
			producerArray[i].setDrCache(drCache);
			producerArray[i].setGpCache(gpCache);
			producerArray[i].setFrom(from);
			producerArray[i].setTo(to);
			producerArray[i].setMap(processedMap);

			
			combineFileArray[i] = (com.tmg.thread.CombineFile) context
					.getBean("Combine");
			combineFileArray[i].setTableConsumer(tableConsumer);
			combineFileArray[i].setTableProducer(tableProducer);
			combineFileArray[i].setProcessedMap(processedMap);
			
			consumerArray[i] = (com.tmg.thread.ImportThread) context
					.getBean("Import");
			consumerArray[i].setTableContainer(tableProducer);
			consumerArray[i].setUrl(url);
			consumerArray[i].setDrCache(drCache);

			producerThreadArray[i] = new Thread(producerArray[i], "ExportThread"
					+ i);
			combineFileThreadArray[i]= new Thread(combineFileArray[i],"CombineFileThread"+i);
			consumerThreadArray[i] = new Thread(consumerArray[i], "ImportThread"
					+ i);

			producerThreadArray[i].start();

		}
		
		//make sure that producer are all finished before combining file
		
		for (int i = 0; i < threadNo; i++) {
			try {
				producerThreadArray[i].join();
			} catch (Exception e) {
				log.info("",e);
			}
		}
		
		for (int i = 0; i < threadNo; i++) {
			combineFileThreadArray[i].start();	
		}
		
		/*
		for (int i = 0; i < threadNo; i++) {
			try {
				combineFileThreadArray[i].join();
			} catch (Exception e) {
				log.info("",e);
			}
			consumerThreadArray[i].start();
		}
		
		
		
		//context cannot be executed until all the consumer is done
		for (int i = 0; i < threadNo; i++) {
			try {
				consumerThreadArray[i].join();
			} catch (Exception e) {
				log.info("",e);
			}
			context.close();
		}
		*/
		
	}
	
	
	
	

	public static void readFile() {

		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {

			reader = new BufferedReader(new FileReader(new File(
					"C:/work/gpfdist/cdc.ds_transaction_register.txt")));
			File file = new File(
					"C:/work/gpfdist/cdc.ds_transaction_register_afx.txt");
			writer = new BufferedWriter(new FileWriter(file));
			final String dir = "C:\\Users\\hma\\workspace\\GPDisaster\\logs\\";
			String line = reader.readLine();
			List<StringBuilder> list=new ArrayList<StringBuilder>();
			int i=0;
			while (line != null) {
			
			String[] lines=line.split(",");
			/*if(lines.length!=8){
				
				line = reader.readLine();
				System.out.println("error"+line);
				continue;
				
			}*/
			
			if(i==5379||i==5378)
				System.out.println(i+"hahha"+line);
			
			/*
			if(lines[4].equals("AFX")){
				StringBuilder sb= new StringBuilder();
				for(int j=0;j<5;j++)
					sb.append(lines[j]);
				
				
				if(list.contains(sb))
					System.out.println("duplicate:"+sb);
				
				list.add(sb);
				
				
				writer.write(line);
				writer.write("\n");
				i++;
			}
			*/
			i++;
			line = reader.readLine();
			
			
			}

			System.out.println("Total:"+i);
			System.out.println("Size:"+list.size());
			/*
			int i = 1;
			int j = 1;
			while (line != null) {

				if (i % 1000000 == 0) {

					i = 0;
					j++;
					file = new File(dir + "info" + j + ".log");
					writer = new BufferedWriter(new FileWriter(file));

				}

				writer.write(line + "\n");

				line = reader.readLine();
				i++;
			}
			*/

		} catch (Exception e) {

			e.printStackTrace();

		}finally{
			
			try{
				
				writer.close();
				
				
			}catch(Exception e){
				
				e.printStackTrace();
			}
			
			
		}

	}

	public static void testSql() {

		String fileName = "C:/project/Greenplum/DisasterRecovery/Logs/gpdb-2015-05-02_000000.csv";
		List<String> sqlArray = FileUtil.getSql(fileName);

		System.out.println(sqlArray.size());

		Iterator<String> iterator = sqlArray.iterator();
		String sql = null;
		String operation = null;
		String delimiter = "into";

		int i = 0;
		while (iterator.hasNext()) {
			sql = iterator.next().toLowerCase();

			if (sql.contains("select") && !sql.contains("into"))
				continue;
			if (sql.startsWith("insert") && !sql.contains("."))// filter insert
																// without
																// schema name
				continue;

			i++;

		}

		System.out.print(i);

	}

	
	public static void testGenerateTableDefinition(int threadNo){
		
		String schema="cdc";
		String tableName,baseTableName,partition;
		String regx=".*_[0-9]{1,}_prt_.*";
		List<String> list=null;
		Map<String,List<String>> map= new HashMap<String,List<String>>();
		
		
		List<String> tableList=gpDAOImp.getPartitionedTable(schema);
		
		
		
		Iterator<String> tableItera= tableList.iterator();
		while(tableItera.hasNext()){
			list=null;
			tableName=tableItera.next();
			if(tableName.matches(regx)){
				String[] tmp=tableName.split("_[0-9]{1,}_prt_");
				baseTableName=schema+"."+tmp[0];
				partition=tmp[1];
				//others is the default, which is processed in the generateTableDefiniton class
				if(partition.equals("others"))
					continue;
				list=map.get(baseTableName);
				if(StringUtils.isEmpty(list))
					list= new ArrayList<String>();
				list.add(partition);
			}else
				baseTableName=schema+"."+tableName;
			map.put(baseTableName, list);
		}
		
		
		
		
		TableContainer container= new TableContainer();
		
		
		//put all the tables without partition key to the container
		for(String key:map.keySet()){
			container.add2Queue(key);
			/*list=map.get(key);
			System.out.print(key+":");
			if(list==null)
				System.out.print("null");
			else
			{
			for(int i=0;i<list.size();i++)
				System.out.print(list.get(i)+",");
			}
			System.out.println("");
			*/	
		}
			
		
		GenerateTableDefinition[] definitionArray= new GenerateTableDefinition[threadNo];
		Thread[] threadArray= new Thread[threadNo];
		for(int i=0;i<threadNo;i++){
			
			definitionArray[i]=(com.tmg.thread.GenerateTableDefinition) context.getBean("Definition");
			definitionArray[i].setContainer(container);
			definitionArray[i].setMap(map);
			
			
			threadArray[i]= new Thread(definitionArray[i],"DefinitonThread"+i);
			
			threadArray[i].start();
			
		}
		
		
		
		
		
		
	}
	
	
	public static void compressFile(String tableName){
		
		byte[] buffer = new byte[1024];
		ZipOutputStream zout=null;
		FileInputStream fin=null;
		FileOutputStream fout=null;
		String ext=Properties.getProperty("tmg.gpfdist.file.extension");
		String path=Properties.getProperty("tmg.gpfdist.dir");
		String backupPath=Properties.getProperty("tmg.gpfdist.processed.dir");
		String zipExt=Properties.getProperty("tmg.gpfdist.zip.extension");
		try{
			String date=FileUtil.getDate(0);
			fout = new FileOutputStream(backupPath+tableName+"."+date+zipExt);
			zout=new ZipOutputStream(fout);
			ZipEntry ze= new ZipEntry(tableName+ext);
			zout.putNextEntry(ze);
			fin = new FileInputStream(path+tableName+ext);
			int len;
			while ((len = fin.read(buffer)) > 0) {
				zout.write(buffer, 0, len);
    		}
		}catch(Exception e){
			
			log.info("compress file error",e);
			
		}finally{
			try{
				fin.close();
				zout.closeEntry();
				zout.close();
				fout.close();
			}catch(Exception e){
				
				log.error("close file error",e);
				
			}

		}
		
		
		
	}
	
	public static void truncate(){
		
		String schema="cdc";
		String env="DR";
		String table;
		gpDAOImp.setEnv(env);
		List<String> tableList = gpDAOImp.getTables(schema);
		
		Iterator<String> iterator=tableList.iterator();
		
		StringBuilder sb=null;
		while(iterator.hasNext()){
			table=iterator.next();
			sb= new StringBuilder("truncate table ");
			sb.append(schema).append(".").append(table);
			
			int[] result=gpDAOImp.executeMultipleQuery(sb.toString());
			
			int len=result.length;
			for(int i=0;i<len;i++)
				System.out.println(table+":"+result);
		}
		
		
		
		
	}
	
}
