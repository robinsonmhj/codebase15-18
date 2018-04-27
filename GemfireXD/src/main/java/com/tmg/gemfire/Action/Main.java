package com.tmg.gemfire.Action;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;












import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.tmg.gemfire.Bean.Column;
import com.tmg.gemfire.Bean.Table;
import com.tmg.gemfire.DAOImp.GemfireDAOImp;
import com.tmg.gemfire.DAOImp.GreenplumDAOImp;
import com.tmg.gemfire.DAOImp.MSDAOImp;
import com.tmg.gemfire.Util.FileUtil;
import com.tmg.gemfire.Util.TableUtil;
import com.tmg.core.Container;
import com.tmg.core.Mapper;
import com.tmg.core.Properties;


public class Main {

	private static Logger log = Logger.getLogger(Main.class);
	private static GenericXmlApplicationContext context;
	private static GreenplumDAOImp gpDAOImp;
	private static GemfireDAOImp gfDAOImp;
	private static MSDAOImp msDAOImp;
	private static TableUtil tableUtil;

	public static void main(String[] args) {
	
	
		context=new GenericXmlApplicationContext();  
		context.load("classpath:applicationContext.xml");  
		context.refresh();
		
		gpDAOImp = (GreenplumDAOImp) context.getBean("GPDAO");
		gfDAOImp = (GemfireDAOImp) context.getBean("GFDAO");
		msDAOImp = (MSDAOImp) context.getBean("MsSqlImp");
		//tableUtil = (TableUtil) context.getBean("TableUtil");
		
		
		try{
			
			
			
		//testTransaction();
		/*
		if(args.length==1){
			
			String argu=args[0];
			
			if(argu.equals("1"))
				transferData();
			else if(argu.equals("2"))	
				copySchema();	
			
		}else if (args.length==4){
			tableUtil.insertFromFile(args[0], args[1], args[2], Integer.valueOf(args[3]));
			
		}else if(args.length==3){
			
			String[] clientArray=args[1].split(",");
			int clientCount=clientArray.length;
			for(int i=0;i<clientCount;i++){
				String client_id=clientArray[i];
				bulkInsert(args[0], client_id, Integer.valueOf(args[2]));
			}
			
			
			
		}else{
			
			
			System.out.println("Usage: java -jar Gemfire-2.0-SNAPSHOT-jar-with-dependencies.jar [1-2]");
			System.out.println("1: transferData");
			System.out.println("2: copySchema");
			System.out.println("**************************\nInsert Data from File\n");
			System.out.println("Usage: java -jar Gemfire-2.0-SNAPSHOT-jar-with-dependencies.jar schemaName, tableName, file, batch");
			System.out.println("Usage: java -jar Gemfire-2.0-SNAPSHOT-jar-with-dependencies.jar mode, client_id, threadNo");
			System.out.println("**************************");
			
		}
		*/
		
		//test();
		//transferData();
		
		//generateGrant();
			
			/*
		List<String> tableList= new ArrayList<String>();
		
		
		
		tableList.add("dim_class");
		tableList.add("dim_client");
		tableList.add("dim_diagnosis_code");
		tableList.add("dim_eligibility_reason");
		tableList.add("dim_explanation_code");
		tableList.add("dim_group");
		tableList.add("dim_member");
		tableList.add("dim_provider");
		tableList.add("dim_placeofservice");
		tableList.add("dim_plan");
		tableList.add("dim_procedure_code");
		tableList.add("dim_product");
		tableList.add("dim_subclient");
		tableList.add("dim_subgroup");
		tableList.add("fact_claim");
		tableList.add("fact_claim_detail");
		tableList.add("fact_member_address_history");
		tableList.add("fact_provider_address_history");
		*/
			
			/*
		List<String> tableList= new ArrayList<String>();
		tableList.add("procedure_price");
		generateTableDefinition(tableList);
		*/
	
//		Container<String> container= new Container<String>();
//		container.put("dim_class");
//		container.put("dim_diagnosis_code");
//		container.put("dim_eligibility_reason");
//		container.put("dim_explanation_code");
//		container.put("dim_group");
//		container.put("dim_member");
//		container.put("dim_provider");
//		container.put("dim_placeofservice");
//		container.put("dim_plan");
//		container.put("dim_procedure_code");
//		container.put("dim_product");
//		container.put("dim_subgroup");
//		container.put("dim_client");
//		container.put("dim_subclient");
//		container.put("fact_claim");
//		container.put("fact_claim_detail");
//		container.put("fact_member_address_history");
//		container.put("fact_provider_address_history");

		/*	
		final int threadNo=3;
		MoveData[] moves= new MoveData[threadNo];
		Thread[] threads= new Thread[threadNo];
		
		for(int i=0;i<threadNo;i++){
			moves[i]=(com.tmg.gemfire.Action.MoveData)context.getBean("MoveData");
			moves[i].setContainer(container);
			threads[i]=new Thread(moves[i]);
		}
			
		
		for(int i=0;i<threadNo;i++){
			threads[i].start();
		}
		
	*/	
		/*
		final int threadNo=3;
		populateDimTable[] moves= new populateDimTable[threadNo];
		Thread[] threads= new Thread[threadNo];
		
		for(int i=0;i<threadNo;i++){
			moves[i]=(com.tmg.gemfire.Action.populateDimTable)context.getBean("populateDimTable");
			moves[i].setContainer(container);
			threads[i]=new Thread(moves[i]);
		}
			
		
		for(int i=0;i<threadNo;i++){
			threads[i].start();
		}
		*/
			
		//createBucket();
		
			
		/*
		String schema="ods";
		String table="check_status";
		compareTable(schema,table,schema,table.toUpperCase());
		*/
		//getTotal();
		//bulkExport();
		//bulkInsert();
		//createTableFromMS2Gf();
		//generateDMLfromMSsql();
		//getTableDistribution();
		//getTablesWithoutPK();
		//copySchema();
		generateTableDefinitonGF();
		//details();
		//getTotal();
		//deleteData();
		//getDataDistribution("ODS");
		//prepareAccess();
		//getIndex("ODS");
		//removeTables();
		//generatedImportTableList();
		//findIllegalTables();
			
		//testTransaction();	
		//generateSelect("ods","person_event");
		}catch(Exception e){
			log.error("Error in Main",e);
			
		}

		
	}
	
	//this is used for generated table definition from GP to GP
	public static void generateTableDefinition(List<String> tableList){
		
		/*
		List<String> dsKeyList= new ArrayList<String>();
		dsKeyList.add("grp_id");
		Table table=gfDAOImp.getTableMetaData("ODS","GROUP_RELATION");
		 System.out.println(gfDAOImp.generateSchema("ODS", table, dsKeyList));
		*/
		String schema="ods";
		for(String tableName:tableList){
			
			StringBuilder sql= new StringBuilder("create table ");
			sql.append(schema).append(".").append(tableName).append("\n");
			sql.append("(\n");
			Table table=gpDAOImp.getTableMetaData(schema, tableName);
			List<Column> columnList=table.getCloumns();
			List<String> pkList=new ArrayList<String>();
			List<String> dsKeyList=gpDAOImp.getDistributedKey(schema, tableName);
			List<String> dsKeyListNew= new ArrayList<String>();
			for(String column:dsKeyList){
				//if(column.equalsIgnoreCase("client_id")||column.equalsIgnoreCase("subclient_id"))
					//continue;
				dsKeyListNew.add(column);
			}
			StringBuilder dsKey= new StringBuilder("DISTRIBUTED BY (");
			if(dsKeyListNew==null||dsKeyListNew.isEmpty())
				log.info("table "+tableName+" doesn't have any distributed key");
			else{
				for(String column:dsKeyListNew)
					dsKey.append(column).append(",");
				dsKey.deleteCharAt(dsKey.length()-1);
				dsKey.append(")\n");
			}
			
			
			for(Column c:columnList){
				String columnName=c.getName();
				sql.append("  ").append(columnName);
				String datatype=c.getType();
				sql.append(" ").append(datatype);
				int type=Mapper.getParameter(datatype);
				if(datatype.equals("numeric")&&c.getLen()==131089)
					type=0;
				if(type==1){
					sql.append("(");
					sql.append(c.getLen());
					sql.append(")");
					
				}else if(type==2){
					sql.append("(");
					sql.append(c.getLen());
					sql.append(",");
					sql.append(c.getDecimalLen());
					sql.append(")");
					
					
				}
				String nullable=c.getFlag();
				if(nullable.equals("NO")){
					
					sql.append(" NOT NULL");
					String dValue=c.getDefaultValue();
					if(!StringUtils.isEmpty(dValue)){
						
						sql.append(" default ").append(dValue);
					}
					
					
				}
				sql.append(",\n");
				if(c.isPrimary())
					pkList.add(columnName);
				
				
				
				
			}
			
			if(!pkList.isEmpty()){
				
				sql.append("  CONSTRAINT ").append(tableName).append("_pkey ").append("PRIMARY KEY (");
				
				//distributed must be the left part of primary key
				for(String column:dsKeyListNew)
					sql.append(column).append(",");
				for(String column:pkList){
					if(dsKeyListNew.contains(column))
						continue;
					sql.append(column).append(",");
				}
				//remove the last comma
				sql.deleteCharAt(sql.length()-1);
				sql.append(")\n");
			}else
				sql.deleteCharAt(sql.length()-1);//delete the last commar when there is no primary keys
			
			sql.append(")\n");
			
			
			
			if(dsKey.length()<=16)
					log.error("table "+tableName+" only have distributed key client_id and subClient_id");
			else{
					sql.append(dsKey);
			}
					
				
			
			
			List<String> partitionList=gpDAOImp.getPartitions(schema, tableName);
			if(partitionList.isEmpty()){
				sql.append(";");
			}else{
				sql.append("PARTITION BY LIST(subclient_id)\n");
				sql.append("         (\n");
				int i=1;
				for(String partition:partitionList){
					if(partition.equals("others"))
						continue;
					sql.append("         PARTITION ");
					sql.append(partition);
					sql.append(" VALUES(").append(i).append("),\n");
					i++;
				}
				sql.append("         DEFAULT PARTITION others\n");
				sql.append("         )\n;\n");
			}
				
			sql.append("ALTER TABLE ").append(schema).append(".").append(tableName).append("  OWNER TO dev_edw_etl;\n");
			sql.append("GRANT ALL ON TABLE ").append(schema).append(".").append(tableName).append("  TO dev_edw_etl;\n");
			sql.append("GRANT SELECT ON TABLE ").append(schema).append(".").append(tableName).append("  TO dev_odm_read;\n");
			
			
			System.out.println(sql);
			log.info(sql);
			
			
			
			
			
		}
		
		
		
		
	}
	
	public static void moveData(List<String> tableList){
		
		
		
		
		
		
	}
	public static void generatedImportTableList(){
		
	
		List<String> tableList=gfDAOImp.getTables("ODS");
		
		
		StringBuilder sb= new StringBuilder();
		
		Iterator<String> iter= tableList.iterator();
		
		while(iter.hasNext()){
			String table=iter.next();
			sb.append(table.toLowerCase()).append(",");
		}
		
		sb.deleteCharAt(sb.length()-1);
		
		System.out.println(sb);
		
		
		
	}
	
	
	
	
	//this is used to generate table definiton from GP to GF
	public static void copySchema(){
		try{
			
			String source="ods";
			
			//List<String> tableList=gpDAOImp.getTables(source);//get all the tables in GP
			List<String> tableList=new ArrayList<String>();
			tableList.add("billing_entity");
			tableList.add("billing_schedule");
			tableList.add("billing_entity_schedule");
			tableList.add("invoice");
			tableList.add("charge_item");
			tableList.add("billing_source");
			tableList.add("invoice_status");
			tableList.add("invoice_detail");
			tableList.add("receipt");
			tableList.add("unapplied_cash");
			/*
			StringBuilder sb22= new StringBuilder();
			
			Iterator<String> iterator=tableList.iterator();
			
			while(iterator.hasNext()){
				
				sb22.append(iterator.next()).append(",");
				
				
			}
			
			System.out.println(sb22);
			*/
			Map<String,String> dsKeyTableMap= TableUtil.getDistributedKeyTableMap();
			/*
			//commented out when generating tables partitioned by client_id and colocate with client_id
			tableList.remove("clients");
			*/
			
			String ignoreTable=Properties.getProperty("tmg.gp.ignore.file");
			//System.out.println(ignoreTable);
			List<String> ignoreTableList=FileUtil.string2List(ignoreTable);
			
			
			
			List<String> auxODSList= new ArrayList<String>();
			//auxODSList.add("moop_balance");
			//auxODSList.add("moop_balance_exceptions");
			//auxODSList.add("moop_accumulator");
			//auxODSList.add("claim_coshare_tracking");
			//auxODSList.add("reference_data_1");
			
			StringBuilder sb= new StringBuilder();
			StringBuilder sbDepent= new StringBuilder();
			//StringBuilder sb_1= new StringBuilder();
			//StringBuilder sb_1Depent= new StringBuilder();
			for(int i=0;i<tableList.size();i++){
				String tableName=tableList.get(i);
				if(ignoreTableList!=null&&ignoreTableList.contains(tableName)){
					System.out.println(tableName+" ignored!");
					continue;
				}
				
				
				//if(!auxODSList.contains(tableName))
					//continue;
				
				Table table=gpDAOImp.getTableMetaData(source,tableName);
				String schema=gpDAOImp.generateSchema(source,table,false);
				if(dsKeyTableMap.containsValue(tableName.toUpperCase()))
					sb.append(schema);
				else
					sbDepent.append(schema);
				
				/*
				String schema_1=gpDAOImp.generateSchema(source,table, dsKeyList,true);
				if(dsKeyTableMap.containsValue(tableName.toUpperCase()))
					sb_1.append(schema_1);
				else
					sb_1Depent.append(schema_1);
				*/
			}
			
			sb.append(sbDepent);
			
			log.info(sb);
			System.out.println(sb);
			//log.info(sb_1);
			
			
			//tableUtil.copySchema2GF(source, tableList, dsKeyList);
		}catch(Exception e){
			
			
			log.info("",e);
			
		}
		
		
		
		
	}
	
	
	
	public static void removeTables(){
		
		try{
			
			String source="ODS";
			
			List<String> tableList=gfDAOImp.getTables(source);//get all the tables in Gf
			//key:distributed key,value tableName all uppercase
			Map<String,String> dsKeyTableMap= TableUtil.getDistributedKeyTableMap();
			Iterator<String> iter= tableList.iterator();
			//tables depend on the other table
			
			while(iter.hasNext()){
				StringBuilder sb= new StringBuilder("TRUNCATE TABLE ODS.");
				String tableName=iter.next();
				sb.append("\"").append(tableName).append("\"");
				sb.append(";");
				/*
				tableName=tableName.toUpperCase();
				if(dsKeyTableMap.values().contains(tableName))
				//if(!tableName.endsWith("_1"))
					sb.append("drop table ").append(source).append(".\"").append(tableName).append("\";\n");
				else
					sb1.append("drop table ").append(source).append(".\"").append(tableName).append("\";\n");
				*/
				/*
				sb.append("insert into ").append(source).append(".").append(tableName).append("1 select * from ").append(source).append(".").append(tableName).append(";\n");
				sb1.append("drop table ").append(source).append(".").append(tableName).append(";\n");
				sb2.append("alter table ").append(source).append(".").append(tableName).append("1 RENAME TO ").append(source).append(".").append(tableName).append(";\n");
				*/
				
				/*
				sb.append("ALTER TABLE ").append(source).append(".").append(tableName).append(" OWNER TO dev_edw_etl;\n");
				sb.append("GRANT ALL ON TABLE ").append(source).append(".").append(tableName).append(" TO dev_edw_etl;\n");
				sb.append("GRANT SELECT ON TABLE ").append(source).append(".").append(tableName).append(" TO dev_odm_eval_read;\n");
				*/
				
				
				
				
				
				System.out.println(sb);
				
			}		
			
		}catch(Exception e){
			log.info("",e);
			
		}
		
		
		
	}
	
	//used to generate bulk insert statement
	public static void bulkInsert(String mode,String clientId,int threadNo){
		
	
		try{
			
			String source="ods";
			
			List<String> tableList=gpDAOImp.getTables(source);//get all the tables in GP
			
			
			Iterator<String> iter= tableList.iterator();
			String dir=Properties.getProperty("tmg.gf.local.dir");
			dir="/GFXD_Bulk_File_Load/20171102_8006/";
			String targetSchema=Properties.getProperty("tmg.gf.target.schema");
			String noIdTablesString=Properties.getProperty("tmg.gf.noId.file");
			
		
			List<String> noIdTables=FileUtil.string2List(noIdTablesString);
			
			
			while(iter.hasNext()){
				String tableName=iter.next();
				StringBuilder file=new StringBuilder(dir);
				file.append("/").append(tableName.toUpperCase());
				if(!noIdTables.contains(tableName.toUpperCase())&&!noIdTables.contains(tableName.toLowerCase()))
						file.append("_").append(clientId);
				
				file.append(".sql");
				File f= new File(file.toString());
				
				if(!f.exists()){
					log.info(file+" doesn't exists,escape");
					continue;
				}
					
				
				Table table=gpDAOImp.getTableMetaData(source, tableName);
				List<Column> list=table.getCloumns();
				Iterator<Column> it=list.iterator();
				StringBuilder sb=new StringBuilder("CALL SYSCS_UTIL.IMPORT_DATA_EX(\n");
				sb.append("'").append(targetSchema).append("',\n");//schema name
				sb.append("'").append(tableName).append("',\n");//schema name
				sb.append("'");
				while(it.hasNext()){
					String column=it.next().getName();
					if(column.equalsIgnoreCase("del"))
						continue;
					//columnName
					sb.append(column.toUpperCase()).append(",");
			
				}
				
				
				sb.deleteCharAt(sb.length()-1);
				sb.append("',\n");
				sb.append("null,\n");//columnIndex
				sb.append("'").append(file).append("',\n");//file localtion
				sb.append("',',\n");//column delimiter
				sb.append("null,\n");
				sb.append("null,\n");
				sb.append(mode).append(",\n");
				sb.append("0,\n");
				sb.append(threadNo).append(",\n");
				sb.append("0,\n");
				sb.append("null,\n");
				sb.append("null\n");
				sb.append(");\n");
				
				log.info(sb);
				
				long start=System.currentTimeMillis();
				gfDAOImp.callProcedure(sb.toString());
				long end=System.currentTimeMillis();
				log.info("Bulk insert "+tableName+" done!Using time:"+(end-start));
				
			}
			
			
			
		}catch(Exception e){
			
			
			log.info("",e);
			
		}
		
		
	}
	
	public static void bulkExport(){
		
		//Map<String,Map<Integer,Integer>> map=getTotal();
		Map<String,Map<Integer,Integer>> map= new HashMap<String,Map<Integer,Integer>>();
		//Map<Integer,Integer> total= new HashMap<Integer,Integer>();
		//total.put(8006, 100);
		//map.put("CODE_VALUE", total);
		//map.put("CLAIM_DETAIL", total);
		//map.put("CLAIM_STATUS", total);
		//map.put("CLIENT_REFERENCE_DATA", total);
		String dir="/GFXD_Bulk_File_Load/20171102_8006/";
		for(Entry<String,Map<Integer,Integer>> entry:map.entrySet()){
			String tableName=entry.getKey();
			Map<Integer,Integer> m=entry.getValue();
			for(int clientId:m.keySet()){
				if(m.get(clientId)>0){
					String fileName=tableName+"_"+clientId;
					StringBuilder sb= new StringBuilder();
					sb.append("CALL SYSCS_UTIL.EXPORT_QUERY('select * from ods.\"");
					sb.append(tableName.toUpperCase()).append("\"");
					sb.append(" where client_id=").append(clientId).append("',");
					sb.append("'").append(dir).append(fileName).append("', null, null, null);\n");
					
					log.info(sb);
					log.info("started "+fileName);
					gfDAOImp.callProcedure(sb.toString());
					log.info("ended "+fileName);
				}
			}

		}

	}
	
	
	
	public static void bulkInsert(){
		
		//Map<String,Map<Integer,Integer>> map=getTotal();
		String dirString="/GFXD_Bulk_File_Load/20171102_8006/";
		String networkShare="//UATJOBMGRV1/ClientData/MultiClients/GFXD_Bulk_File_Load/20171102_8006/";
		File dir= new File(networkShare); 
		//String[] files=dir.list();
		String[] files=new String[]{"CLAIM_DETAIL_8006","CLAIM_8006","PERSONS_8006"};
		String schemaName="ODS";
		for(String file:files){
			String tableName = file.substring(0, file.lastIndexOf("_"));
			StringBuilder sb = new StringBuilder();
			sb.append("CALL SYSCS_UTIL.IMPORT_TABLE_EX(");
			sb.append("'").append(schemaName).append("',");
			sb.append("'").append(tableName.toUpperCase()).append("',");
			sb.append("'").append(dirString).append(file).append("',");
			sb.append("null, null, null,1,0,4,0,null,");
			sb.append("'../../../../../").append(dirString).append("log/");
			log.info(sb);
			log.info("started " + file);
			gfDAOImp.callProcedure(sb.toString());
			log.info("ended " + file);
		}
		
		
	}
	public static void generateSelect(String schema,String tableName){
		

		Table table=gfDAOImp.getTableMetaData(schema.toUpperCase(),tableName.toUpperCase());
		
		List<Column> columnList=table.getCloumns();
		StringBuilder sb= new StringBuilder("select ");
		
		for(Column column:columnList)
			sb.append(column.getName()).append(",");
		
		sb.deleteCharAt(sb.length()-1);
		
		sb.append(" from ").append(schema).append(".").append(tableName);	
		
		System.out.println(sb);
		
	}
	
	
	//generate table definition from gf to gf
	public static void generateTableDefinitonGF(){
		
		try{
			
			String source="ods";
			
			//List<String> tableList=gpDAOImp.getTables(source);//get all the tables in GP
			List<String> tableList=new ArrayList<String>();
			//tableList.add("clients");
			/*
			tableList.add("BENEFIT_PACKAGE_ATTRIBUTE");
			tableList.add("CLAIM_COB");
			tableList.add("MOOP_FILE_LOG");
			tableList.add("REVENUE_CODE");
			tableList.add("ORGANIZATIONS");
			tableList.add("CLAIM_COSHARE_TRACKING");
			tableList.add("CLAIM_LINE_ATTRIBUTE");
			tableList.add("FDI_TX_IDCARD");
			tableList.add("FDI_CORRESPONDENCE");
			tableList.add("PERSON_PAYMENT_OPTIONS");
			tableList.add("MOOP_BALANCE");
			tableList.add("MOOP_ACCUMULATOR");
			tableList.add("SERVICE_CODE");
			tableList.add("AGREEMENT");
			tableList.add("CLAIM_HOSPITAL");
			*/
			tableList.add("BENEFIT_PACKAGE");
			//tableList.add("fdi_tx_idcard");
			
			Map<String,String> dsKeyTableMap= TableUtil.getDistributedKeyTableMap();
			
			String ignoreTable=Properties.getProperty("tmg.gp.ignore.file");
			List<String> ignoreTableList=FileUtil.string2List(ignoreTable);


			
			StringBuilder sb= new StringBuilder();
			StringBuilder sbDepent= new StringBuilder();

			for(int i=0;i<tableList.size();i++){
				String tableName=tableList.get(i);
				if(ignoreTableList!=null&&ignoreTableList.contains(tableName)){
					System.out.println(tableName+" ignored!");
					continue;
				}
				
				Table table=gfDAOImp.getTableMetaData(source,tableName);
				//System.out.println(table);
				String schema=gfDAOImp.generateSchema1(source,table,Properties.getTableType());
				if(dsKeyTableMap.containsValue(tableName.toUpperCase()))
					sb.append(schema);
				else
					sbDepent.append(schema);
				
				
			}
			
			sb.append(sbDepent);
			
			log.info(sb);
			System.out.println(sb);
		}catch(Exception e){
			
			
			log.info("",e);
			
		}
		
		
	}
	public static void transferData(){
		try{
			
			String target=Properties.getProperty("tmg.gf.target.schema");
			
			if(target==null||target.trim().equals("")){
				log.error("tmg.gf.target.schema is null,value is " +target);
				return;
			}
				
			String source="ods";
				
			
			String dir=Properties.getProperty("tmg.gf.local.dir");
			File fDir=new File(dir);
			
			if(!fDir.exists()||fDir.isFile()){
				
				boolean success=fDir.mkdirs();
				
				if(!success){
					log.error("Failed to create directory "+dir);
					return;
				}
					
			
			}
			
			
		
			List<String> List=gfDAOImp.getTables(target);//get all the tables in GF
			
			
			log.debug("currentpath:main function"+System.getProperty("user.dir"));
			
			//List<String> ignoreTables= new ArrayList<String>();
			List<String> noIdTables= new ArrayList<String>();
			List<String> clientList= new ArrayList<String>();
			
			//String ignoreTablesString=Properties.getProperty("tmg.gf.ignore.file");
			String noIdTablesString=Properties.getProperty("tmg.gf.noId.file");
			String clientListString=Properties.getProperty("tmg.gf.clientId");
			
			//ignoreTables=FileUtil.string2List(ignoreTablesString);
			noIdTables=FileUtil.string2List(noIdTablesString);
			clientList=FileUtil.string2List(clientListString);
			
			if(clientList==null){
				log.error("tmg.gf.clientId cannot be null");
				return ;
			}
				
			
			Iterator<String> iterClient=clientList.iterator();
			boolean first=true;//flag indicating the first client in client list
			while(iterClient.hasNext()){
				StringBuilder suffixSql=new StringBuilder("client_id=");
				String clientId=iterClient.next();
				log.info("client "+clientId+" begin");
				if(clientId==null||clientId.trim().equals("")){
					log.info("clientId error,clientId is "+clientId);
					continue;
				}
					
				suffixSql.append(clientId);
				Iterator<String> iter=List.iterator();
				while(iter.hasNext()){
					String tableName=iter.next();
					//ignore the tables end with _1
					if(tableName.endsWith("_1"))
						continue;
					log.info("Main:Begin table "+tableName);
					if(noIdTables!=null&&(noIdTables.contains(tableName.toLowerCase())||noIdTables.contains(tableName.toUpperCase()))){
						
						if(first){
							tableUtil.copyData2GF(source, tableName, null);
						}else
							log.info(tableName+" is ignored, as it is not the first client to get it");
							
					}else
						tableUtil.copyData2GF(source, tableName, suffixSql.toString());
					
				}
				log.info("client "+clientId+" ends");
				//make sure the table without client_id are updated only once if there are multiple client
				if(first){
					first=false;
				}
			}
			
			
			
			
			
			

		}catch(Exception e){
			
			//e.printStackTrace();
			log.info("",e);
		
		}
	
		
		
		
	}
	
	
	public  static void generateGrant(){
		try{
	
			String schema="ods";
			//System.out.println("hello");
			//List<String> List=gpDAOImp.getTables(schema);
			List<String> List=gpDAOImp.getTables(schema);
			Iterator<String> iter=List.iterator();
			String tableName;
			StringBuilder sbAll= new StringBuilder();
			StringBuilder sbReadonly= new StringBuilder();
			String grantAll="GRANT SELECT,UPDATE,DELETE,INSERT ON TABLE ";
			String grantSelect="GRANT SELECT ON TABLE ";
			//String create="SYS.CREATE_ALL_BUCKETS('";
			String to=" TO ";
			//StringBuilder sb= null;
			
			while(iter.hasNext()){
				//sb=new StringBuilder("call SYS.CREATE_ALL_BUCKETS('");
				tableName=iter.next();
				if(!tableName.endsWith("_1"))
					continue;
				System.out.println("drop table ODS.\""+tableName+"\";");
				//String dk=gpDAOImp.getDistributedKey(schema, tableName);
				//System.out.println(tableName+"\t"+dk);
				sbAll.append(grantAll).append(schema.toUpperCase()).append(".\"").append(tableName.toUpperCase()).append("\"").append(to).append("SIT_SERVICE_SVC,SIT_ETL_SVC,SIT_SSIS_SVC;\n");
				//sb.append(grantAll).append(schema).append(".").append(tableName).append(to).append("dev_tmg_odm_new_write;\n");
				sbReadonly.append(grantSelect).append(schema.toUpperCase()).append(".\"").append(tableName.toUpperCase()).append("\"").append(to).append("SIT_READ_SVC;\n");
				//sb.append(grantSelect).append(schema).append(".").append(tableName).append(to).append("dev_tmg_odm_new_read;\n");
				//sb.append(schema).append(".").append(tableName.toUpperCase()).append("');");
				//System.out.println(sb);
			}
			
			//System.out.println(sbAll);
			//System.out.println(sbReadonly);
			//log.info(sbAll);
			//log.info(sbReadonly);
		}catch(Exception e){
			
			e.printStackTrace();
		
		}
		
		
	}
	
	public static void deleteData(){
		
		
		
		System.out.println("Hello Iam in delete data");
		String schema="ODS",tableName;
		List<String> list=gfDAOImp.getTables(schema);
		//List<String> list=new ArrayList<String>();
		//list.add("CLAIM");
		String noidTable=Properties.getProperty("tmg.gf.noId.file");
		List<String> noIdTableList=FileUtil.string2List(noidTable);
		Iterator<String> iter=list.iterator();
		tableName=iter.next();
		Container<String> container=new Container<String>();
		while(iter.hasNext()){
			tableName=iter.next();
			if(noIdTableList.contains(tableName.toLowerCase())||noIdTableList.contains(tableName.toUpperCase()))
				continue;
			container.put(tableName);
			//System.out.println("in while");
		}
		
		final int threadNo=10;
		List<Integer> reservedClientList= new ArrayList<Integer>();
		reservedClientList.add(9052);
		reservedClientList.add(8003);
		reservedClientList.add(9049);
		reservedClientList.add(8015);
		reservedClientList.add(8006);
		
		DeleteData[] deletes= new DeleteData[threadNo];
		Thread[] threads= new Thread[threadNo];
		
		for(int i=0;i<threadNo;i++){
			deletes[i]=context.getBean("DeleteData", DeleteData.class);
			deletes[i].setContainer(container);
			deletes[i].setReservedClientList(reservedClientList);
			threads[i]= new Thread(deletes[i],"Thread"+i);
			threads[i].start();
			//System.out.println("thread"+i+" started");
		}
		
		
		
		
		
		
		
		/*
		String schema="ODS",tableName;
		List<Integer> clientList= new ArrayList<Integer>();
		//clientList.add(8006);
		//clientList.add(8007);
		clientList.add(8008);
		//List<String> list=gfDAOImp.getTables(schema);
		List<String> list=new ArrayList<String>();
		list.add("CLAIM");
		String noidTable=Properties.getProperty("tmg.gf.noId.file");
		List<String> noIdTableList=FileUtil.string2List(noidTable);
		Iterator<String> iter=list.iterator();
		for(Integer clientId:clientList){
			while(iter.hasNext()){
				tableName=iter.next();
				if(noIdTableList.contains(tableName.toLowerCase())||noIdTableList.contains(tableName.toUpperCase()))
					continue;
				StringBuilder sql=new StringBuilder("delete from ").append(schema).append(".\"").append(tableName).append("\"");
				sql.append(" where client_id=").append(clientId).append(";");
				boolean f=true;
				while(f){
						try{
							gfDAOImp.executeUpdate(sql.toString());
							f=false;
							log.info(tableName+" succeed");
						}catch(Exception e){
							f=true;
							log.info(tableName+" failed");
							log.info("",e);
						}
					}
				
			}
		}
		
		*/
	}
	
	public static Map<String,Map<Integer,Integer>> getTotal(){
		
		String schema="ODS";
		//System.out.println("hello");
		List<String> List=gfDAOImp.getTables(schema);
		//List<String> List=gpDAOImp.getTables(schema);
		Iterator<String> iter=List.iterator();
		String tableName;
		
		int count=0,total=0;
		//String createBucket="call sys.create_all_buckets('";
		//StringBuilder sql=new StringBuilder();
		
		//client_id is the key
		//client_id is the key, total is row count value
		Map<String,Map<Integer,Integer>> map=new HashMap<String,Map<Integer,Integer>>();
		String noidTable=Properties.getProperty("tmg.gf.noId.file");
		List<String> noIdTableList=FileUtil.string2List(noidTable);
		
		String sql1="select count(1)   from ods.PERSON_EVENT_ATTRIBUTE where client_id=8006";
		
		
		
		List<Integer> clientList= new ArrayList<Integer>();
		//clientList.add(9030);
		//clientList.add(8006);
		//clientList.add(8002);
		//clientList.add(8050);
		/*				
		clientList.add(1);
		clientList.add(9);
		clientList.add(10);
		clientList.add(11);
		clientList.add(12);
		clientList.add(13);
		clientList.add(14);
		clientList.add(31);
		clientList.add(6007);
		clientList.add(7001);
		clientList.add(7002);
		clientList.add(7003);
		clientList.add(8001);
		clientList.add(8002);
		clientList.add(8003);
		clientList.add(8004);
		clientList.add(8005);
		clientList.add(8006);
		clientList.add(8007);
		clientList.add(8008);
		clientList.add(8009);
		clientList.add(8011);
		clientList.add(8012);
		clientList.add(8013);
		clientList.add(8014);
		clientList.add(8015);
		clientList.add(8016);
		//sit
		//clientList.add(8002);
*/		
		
		
		//List<Integer> clientList=gfDAOImp.getClientList();
		Map<Integer,Integer> clientMap=null;
		
			
		
		//table
		
		while(iter.hasNext()){
			tableName=iter.next();
			
			//System.out.println("select count(1) from "+schema+"."+iter.next()+";");
			
			if(noIdTableList.contains(tableName.toLowerCase())||noIdTableList.contains(tableName.toUpperCase()))
				continue;
			/*
			StringBuilder sql= new StringBuilder("select count(1) from ").append(schema).append(".\"").append(tableName).append("\"").append(" where client_id=8006");
			//sql.append("truncate table ods.").append("\"").append(tableName).append("\";\n");
			
			
			try{
				
				int c=gfDAOImp.execute(sql.toString());
				System.out.println(tableName+":"+c);
			}catch(Exception e){
				log.info("",e);
				
			}
			*/

			
			clientMap= new HashMap<Integer,Integer>();
			if(clientList.isEmpty()){
				StringBuilder sql=new StringBuilder("select count(1) as total from ").append(schema).append(".\"").append(tableName).append("\"");
				try{
					total=gfDAOImp.execute(sql.toString());
					System.out.println(tableName+":"+total);
				
				}catch(Exception e){
				
				log.info("",e);
				}
			}else{
				for(int i=0;i<clientList.size();i++){
					
					int client_id=clientList.get(i);
					StringBuilder sql=new StringBuilder("select count(1) as total from ").append(schema).append(".\"").append(tableName).append("\"");
					sql.append(" where client_id=").append(client_id);
					try{
						total=gfDAOImp.execute(sql.toString());
						clientMap.put(client_id, total);
					
					}catch(Exception e){
					
					log.info("",e);
					}
				
				}
				map.put(tableName, clientMap);
			}
			
		}

		/*
		for(Entry<String,Map<Integer,Integer>> entry:map.entrySet()){
			
			String tName=entry.getKey();
			Map<Integer,Integer> m=entry.getValue();
			for(int key:m.keySet())
				System.out.println(tName+"\t"+key+"\t"+m.get(key));
		}
		*/
		
		return map;
		
		/*
		StringBuilder sb= new StringBuilder();
		sb.append("       \t");
		for(int i=0;i<clientList.size();i++)
			sb.append(clientList.get(i)).append("\t");
		sb.append("\n");
		Map<Integer,Integer> tmp=null;//client_id,count
		for(String key:map.keySet()){
			tmp=map.get(key);
			if(tmp==null)
				System.out.println("tableName:"+key+" doesn't have any client");
			if(noIdTableList.contains(key.toLowerCase())||noIdTableList.contains(key.toUpperCase()))
				continue;
			sb.append(key+"\t");//tablename
			for(int i=0;i<clientList.size();i++){
				count=tmp.get(clientList.get(i));
				sb.append(count+"\t");
			}
			sb.append("\n");
			
			
		}
		
		
		//log.info(sb);
		//gfDAOImp.executeMultipleQuery(sql.toString());
		
	}
	
	
	public static String createBucket(){
		
		
		String schema="ods";
		String tableName;
		List<String> List=gpDAOImp.getTables(schema);
		Iterator<String> iter=List.iterator();
		
		StringBuilder sb= new StringBuilder();
		
		final String  create="call sys.create_all_buckets('";
		
		while(iter.hasNext()){
			tableName=iter.next().toUpperCase();
			sb.append(create).append(schema.toUpperCase()).append(".").append(tableName).append("');\n");
		}
		
		System.out.println(sb);
		return sb.toString();
		
	}
	
	
	
	public static void test(){
		
		String a="test.abc";
		
		String[] tmp=a.split("\\.");
		
		System.out.println("tmp[0] "+tmp[0]+",tmp[1] "+tmp[1]);
		
		
		Table table=gpDAOImp.getTableMetaData("sandbox","dt");
		List<Column> list=table.getCloumns();
		
		Iterator<Column> it=list.iterator();
		
		while(it.hasNext()){
			Column column=it.next();
			
			System.out.println(column);
			
			
		}
		
		
		
		/*
		StringBuilder sql= new StringBuilder("Hello");
		
		sql.append("\"");
		
		System.out.println(sql);
		
		int a=223/2/10*10;
		System.out.println(a);
		
		log.debug("This is a test");
		
		
		
		String dsKey=gpDAOImp.getDistributedKey("ods", "claim");
		System.out.println("dsKey:"+dsKey);
		*/
		
		/*
		String schema="ods";
		List<String> sequenceList=gpDAOImp.getSequences(schema);
		Iterator<String> ite =sequenceList.iterator();
		String name,columnName=null;
		int columnLen=0;
		
		while(ite.hasNext()){
			name=ite.next();
			Table table=gpDAOImp.getTableMetaData(schema, name);
			List<Column> columnList=table.getCloumns();
			System.out.println(name);
			Iterator<Column> itColumn=columnList.iterator();
			
			while(itColumn.hasNext()){
				columnLen=0;
				columnName=null;
				Column column=itColumn.next();
				columnName=column.getName();
				columnLen=column.getLen();
				System.out.println("\t"+columnName+"\t"+columnLen);
				
			}
		}
		*/
		
		
		/*
		//testing the system properties
		java.util.Properties p=System.getProperties();
		
		for(Object key:p.keySet()){
			String value=p.getProperty(key.toString());
			
			System.out.println(key.toString()+":"+value);
			
		}
		*/
	}
	
	public static void compareTable(String schema1,String table1,String schema2,String table2){
		
		Table gp=gpDAOImp.getTableMetaData(schema1, table1);
		Table gf=gfDAOImp.getTableMetaData(schema2, table2);
		/*
		List<Column> list=gf.getCloumns();
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i));
			
			
		}
		*/
		
		System.out.println(gf);
		
		gp=tableUtil.convertGP2GF(gp);
		
		tableUtil.compareTable(gp, gf);
		
		
		
	}
	
	public static void getTablesWithoutPK(){
		
		String schema="odm";
		List<String> tableList=gpDAOImp.getTables(schema);
		for(String table:tableList){
			if(table.startsWith("_"))
				continue;
			List<String> pkList=gpDAOImp.getPrimaryKey(schema, table);
			if(pkList.isEmpty())
				System.out.println(table);
			
			
		}
		
		
		
	}
	
	
	public static void getTableDistribution(){
		
		Map<String,Map<Integer,Long>> result= new HashMap<String,Map<Integer,Long>>();
		String schema="odm";
		List<String> tableList=gpDAOImp.getTables(schema);
		
		
		Map<Integer,Long> map1=gpDAOImp.getDistributedData(schema, "fact_member_ssn");
		Set<Integer> set=new HashSet<Integer>(map1.keySet());
		/*
		for(int segment:set)
			System.out.println(segment);
		
		List<String> tableList= new ArrayList<String>();
		tableList.add("fact_member_ssn");
		tableList.add("dim_hotline_program_type");
		tableList.add("pre_ftd_task_run");
		*/
		for(String table:tableList){
			//System.out.println(table);
			if(table.startsWith("_"))
				continue;
			long start=System.currentTimeMillis();
			Map<Integer,Long> map=gpDAOImp.getDistributedData(schema, table);
			long end=System.currentTimeMillis();
			log.info(table+" finished,using "+(end-start));
			result.put(table, map);
		}
		
		//System.out.println("==============================================");
		BufferedWriter writer= null;
		try{
			writer = new BufferedWriter(new FileWriter(new File("C://work//data.txt")));
			//writer.write("test");
			
			StringBuilder sb= new StringBuilder("      \t");
			//header
			for(String table:tableList){
				if(table.startsWith("_"))
					continue;
				sb.append(table).append("\t");
			}
			sb.append("\n");
			writer.write(sb.toString());
			
			sb= new StringBuilder();
			for(int segment:set){
				sb.append(segment).append("\t");
				for(String table:tableList){
					if(table.startsWith("_"))
						continue;
					sb.append(result.get(table).get(segment));
					sb.append("\t");
				}
				
				
				sb.append("\n");
				
				
			}
			
			
			writer.write(sb.toString());
			
		}catch (Exception e){
			
			log.info("",e);
			
		}finally{
			
			try{
				
				writer.close();
			}catch(Exception e){
				
				log.info("close file error",e);
			}
			
			
		}
		
		
	
		
		
		
	}
	
	public static void  generateImportExportSql(){
		
		String schema,tableName,COLUMNDELIMITER=";;",CHARACTERDELIMITER="%%",CODESET="null";
		String fileDir="/tmp/data/";
		List<String> schemaList= new ArrayList<String>();
		schemaList.add("ODS");
		schemaList.add("MONITOR");
		
		Iterator<String> schemaIter= schemaList.iterator();
		StringBuilder sbExport= new StringBuilder();
		StringBuilder sbImport= new StringBuilder();
		while(schemaIter.hasNext()){
			schema=schemaIter.next();
			List<String> tableList=gfDAOImp.getTables(schema);
			Iterator<String> tableIter=tableList.iterator();
			while(tableIter.hasNext()){
				tableName=tableIter.next();
				sbExport.append("SYSCS_UTIL.EXPORT_TABLE(");
				sbExport.append("'").append(schema).append("'").append(",");
				sbExport.append("'").append(tableName).append("'").append(",");
				sbExport.append("'").append(fileDir).append(tableName).append(".sql'").append(",");
				sbExport.append("'").append(COLUMNDELIMITER).append("'").append(",");
				sbExport.append("'").append(CHARACTERDELIMITER).append("'").append(",");
				sbExport.append(CODESET).append(");\n");
				sbImport.append("SYSCS_UTIL.IMPORT_TABLE(");
				sbImport.append("'").append(schema).append("'").append(",");
				sbImport.append("'").append(tableName).append("'").append(",");
				sbImport.append("'").append(fileDir).append(tableName).append(".sql'").append(",");
				sbImport.append("'").append(COLUMNDELIMITER).append("'").append(",");
				sbImport.append("'").append(CHARACTERDELIMITER).append("'").append(",");
				sbImport.append(CODESET).append(",");
				sbImport.append("0").append(");\n");//not replace mode
			}
			
			
		}
		
		log.info(sbExport);
		log.info(sbImport);
		
		
		
		
		
	}
	
	
	public static void testTransaction(){
		
		StringBuilder sql= new StringBuilder("drop table if exists  PERSON_EVENT1; Create table PERSON_EVENT1(\r\n" + 
				"    PRSN_EVNT_ID BIGINT GENERATED BY DEFAULT AS IDENTITY  NOT NULL,\r\n" + 
				"    VER BIGINT NOT NULL,\r\n" + 
				"    CLIENT_ID BIGINT NOT NULL,\r\n" + 
				"    SRC_TYP_ID BIGINT NOT NULL,\r\n" + 
				"    PRSN_ID BIGINT,\r\n" + 
				"    ATTACH_SRC_ID BIGINT,\r\n" + 
				"    EVNT_TYP_REF_ID BIGINT NOT NULL,\r\n" + 
				"    EVNT_DESCR LONG VARCHAR,\r\n" + 
				"    EFF_DT DATE,\r\n" + 
				"    EXPR_DT DATE,\r\n" + 
				"    VLD_FRM_DT TIMESTAMP NOT NULL,\r\n" + 
				"    VLD_TO_DT TIMESTAMP,\r\n" + 
				"    SRC_SYS_REF_ID LONG VARCHAR NOT NULL,\r\n" + 
				"    SRC_SYS_REC_ID LONG VARCHAR NOT NULL,\r\n" + 
				"	CLIENT_EVNT_TYP_REF_ID BIGINT,\r\n" + 
				"    PRIMARY KEY (CLIENT_ID,PRSN_EVNT_ID)\r\n" + 
				")\r\n" + 
				"PARTITION BY COLUMN (prsn_evnt_id);");
		sql.append("call SYS.CREATE_ALL_BUCKETS('PERSON_EVENT1');");
		
		sql.append("insert into PERSON_EVENT1\r\n" + 
				"select PRSN_EVNT_ID,VER,CLIENT_ID,SRC_TYP_ID,PRSN_ID,ATTACH_SRC_ID,EVNT_TYP_REF_ID,\r\n" + 
				"EVNT_DESCR,EFF_DT,EXPR_DT,VLD_FRM_DT,VLD_TO_DT,SRC_SYS_REF_ID,SRC_SYS_REC_ID,CLIENT_EVNT_TYP_REF_ID \r\n" + 
				"from test.person_event where client_id in(8005,9032,9052);");
		
		gfDAOImp.executeMultipleQuery(sql.toString());
		
		
		sql= new StringBuilder();
		
		sql.append("truncate table test.PERSON_EVENT;");
		
		
		
		sql.append("insert into test.PERSON_EVENT\r\n" + 
				"select PRSN_EVNT_ID,VER,CLIENT_ID,SRC_TYP_ID,PRSN_ID,ATTACH_SRC_ID,EVNT_TYP_REF_ID,\r\n" + 
				"EVNT_DESCR,EFF_DT,EXPR_DT,VLD_FRM_DT,VLD_TO_DT,SRC_SYS_REF_ID,SRC_SYS_REC_ID,CLIENT_EVNT_TYP_REF_ID \r\n" + 
				"from person_event1;");
		
		sql.append("drop table PERSON_EVENT1");
		
		
		gfDAOImp.executeMultipleQuery(sql.toString());
		
		
		
		
	}
	
	
	public static void getIndex(String schema){
		
		
		List<String> tableList=gfDAOImp.getTables(schema);
		int count=0;
		StringBuilder sb= new StringBuilder();
		StringBuilder sb1= new StringBuilder();
		for(String table:tableList){
			
			Map<String,Map<Integer,String>> indexMap=gfDAOImp.getIndex(schema, table);
			List<String> pkList=gfDAOImp.getPKList(schema, table);
			Map<Integer,String> index=null;
			if(table.equalsIgnoreCase("CHECK"))
				table="\""+table+"\"";
			for(Map.Entry<String,Map<Integer,String>> indexEntry:indexMap.entrySet()){
				String indexName=indexEntry.getKey();
				if(!indexName.startsWith("IX"))
					continue;
				sb1.append("drop index if exists ").append(schema).append(".").append(indexName).append(";\n");
				index=indexEntry.getValue();
				if(index.size()==pkList.size()){
					count=0;
					for(String column:index.values()){
						if(!pkList.contains(column)){
							sb.append("CREATE INDEX ").append(indexName).append(" ON ").append(schema).append(".").append(table);
							sb.append(" (CLIENT_ID,");
							for(Map.Entry<Integer, String> entry:index.entrySet()){
								if(entry.getValue().equals("CLIENT_ID"))
									continue;
								sb.append(entry.getValue());
								sb.append(",");
							}
							//remove the last comma
							sb.deleteCharAt(sb.length()-1);
							sb.append(");\n");
							break;
						}
							
						count++;
					}
				}else{
					sb.append("CREATE INDEX ").append(indexName).append(" ON ").append(schema).append(".").append(table);
					sb.append(" (CLIENT_ID,");
					for(Map.Entry<Integer, String> entry:index.entrySet()){
						if(entry.getValue().equals("CLIENT_ID"))
							continue;
						sb.append(entry.getValue());
						sb.append(",");
					}
					//remove the last comma
					sb.deleteCharAt(sb.length()-1);
					sb.append(");\n");
					
				}
					
				if(count==pkList.size())
					log.info(table+"'s index:"+indexName+" has the same columns with primary key");
				
					
				
					
					
			}
				
				
				
		}
		System.out.println(sb1);

	}
	
	
	public static void prepareAccess(){
		
		String schema="ODS";
		List<String> userList=gfDAOImp.getList("select distinct u.user_name from monitor.users u join monitor.privilege p on u.user_id=p.user_id where group_id=5");
		
		StringBuilder userSb= new StringBuilder();
		
		for(String user:userList){
			
			userSb.append(user).append(",");
			
		}
		
		//remove the last comma
		if(userSb.length()!=0){
			userSb.deleteCharAt(userSb.length()-1);
			List<String> tableList=gfDAOImp.getTables(schema);
			for(String table:tableList){
				StringBuilder sb= new StringBuilder("revoke trigger,alter,references ON TABLE ");
				sb.append(schema).append(".\"").append(table).append("\" from ");
				sb.append(userSb).append(";");
				System.out.println(sb);
			}
			
		}
			
		List<Integer> groupList= new ArrayList<Integer>();
		groupList.add(1);
		groupList.add(2);
		groupList.add(3);
		groupList.add(4);
		groupList.add(5);
		
		
		StringBuilder insertSb= new StringBuilder("insert into monitor.request_history (user_name,group_id,schema_name) values");
		
		for(Integer groupId:groupList){
			userList=gfDAOImp.getList("select distinct u.user_name from monitor.users u join monitor.privilege p on u.user_id=p.user_id where schema_name='"+schema+"' and group_id="+groupId);
			
			if(userList.isEmpty())
				continue;
			
			
			for(String user:userList){
				insertSb.append("(");
				insertSb.append("'").append(user).append("',");
				if(groupId==1)
					insertSb.append(5).append(",");
				else if(groupId==2)
					insertSb.append(4).append(",");
				else if(groupId==3)
					insertSb.append(1).append(",");
				else if(groupId==4)
					insertSb.append(2).append(",");
				else if(groupId==5)
					insertSb.append(3).append(",");
				
				insertSb.append("'").append(schema).append("'),");
				
			}
			
			
		}
		
		//remove the last comma
		insertSb.deleteCharAt(insertSb.length()-1);
		insertSb.append(";");
		System.out.println(insertSb);
		
		
	}
	
	public static void findIllegalTables(){
		
		List<String> ignoreList= new ArrayList<String>();
		ignoreList.add("ODS");
		ignoreList.add("MONITOR");
		ignoreList.add("SYSSTAT");
		ignoreList.add("SYSPROC");
		ignoreList.add("SYSIBM");
		ignoreList.add("SYSFUN");
		ignoreList.add("SYSCS_UTIL");
		ignoreList.add("SYSCS_DIAG");
		ignoreList.add("SYSCAT");
		ignoreList.add("SYS");
		ignoreList.add("SQLJ");
		
		List<String> schemaList=gfDAOImp.getSchemaList();
		for(String schema:schemaList){
			
			if(ignoreList.contains(schema))
				continue;
			
			List<String> tableList=gfDAOImp.getTables(schema);
			
			for(String table:tableList)
				System.out.println("drop table "+schema+"."+table);
			
		}

		
	}
	
	
	public static void getDataDistribution(String schema){
		
		
		List<String> tableList=gfDAOImp.getTables(schema);
		
		for(String table:tableList){
			StringBuilder sb=new StringBuilder("select TABLE_NAME,HOST,sum(NUM_ROWS) from sys.MEMORYANALYTICS where table_name='");
			sb.append(schema).append(".").append(table).append("'");
			sb.append(" group by TABLE_NAME,HOST");
			
			String result=gfDAOImp.execute2(sb.toString());
			System.out.println(result);
			
		}
		
		
		
		
	}
	
	
	
	public static void createTableFromMS2Gf(){
		
		String sourceSchema="dbo",targetSchema="metadata";
		List<String> tableList= new ArrayList<String>();
		
		/*
		tableList.add("stage_tasks_tables");
		tableList.add("stage_source_target_map");
		tableList.add("meta_gfxd_load");
		tableList.add("meta_gfxd_column_map");
		tableList.add("transx_gfxd_tables");
		tableList.add("transx_register_mt"); 
		
		tableList.add("job_unit_conversion_tasks"); 
		tableList.add("conv_meta_data"); 
		tableList.add("meta_gfxd_column_map"); 
		*/

		tableList.add("ref_env_parameters");
		
		for(String table:tableList){
			//msDAOImp.getTables("dbo");
			
			Table t= msDAOImp.getTableMetaData(sourceSchema, table);
			//System.out.println(t);
			String ddl=msDAOImp.generateSchema(targetSchema, t);
			System.out.println(ddl);
			String select=msDAOImp.getSelectStatement(sourceSchema,targetSchema,t);
			System.out.println("bcp \""+select+"\""+" queryout C:/tmp/"+table+".sql -c -t, -T -S DEVCDCDBV1");
			
			
			
		}
		
		
		
	}
	
public static void generateDMLfromMSsql(){
		
		String sourceSchema="dbo",targetSchema="metadata";
		List<String> tableList= new ArrayList<String>();
		
		/*
		tableList.add("stage_tasks_tables");
		tableList.add("stage_source_target_map");
		tableList.add("meta_gfxd_load");
		tableList.add("meta_gfxd_column_map");
		tableList.add("transx_gfxd_tables");
		tableList.add("transx_register_mt"); 
		tableList.add("job_unit_conversion_tasks"); 
		tableList.add("conv_meta_data"); 
		tableList.add("meta_gfxd_column_map"); 
		*/
		

		for(String tableName:tableList){
			Table t= msDAOImp.getTableMetaData(sourceSchema, tableName);
			String select=msDAOImp.getSelectStatement(sourceSchema,targetSchema,t);
			System.out.println("bcp \""+select+"\""+" queryout C:/tmp/"+tableName+".sql -c -t, -T -S DEVCDCDBV1");
			
			
			
		}
		
		
		
	}


	
	
}
