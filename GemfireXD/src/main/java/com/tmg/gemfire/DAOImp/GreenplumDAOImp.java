package com.tmg.gemfire.DAOImp;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.tmg.core.Mapper;
import com.tmg.core.Properties;
import com.tmg.gemfire.Bean.Column;
import com.tmg.gemfire.Bean.Table;
import com.tmg.gemfire.DAO.CommonDAO;
import com.tmg.gemfire.Util.FileUtil;
import com.tmg.gemfire.Util.TableUtil;


public class GreenplumDAOImp implements CommonDAO {
	
	private static Logger log= Logger.getLogger(GreenplumDAOImp.class);
	
	@Autowired
	@Qualifier("dataSourceGreenplum")
	private DataSource dataSource;
	
	
	/*
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	*/
	
	
	public Table getTableMetaData(String tableName) {

		return getTableMetaData(null, tableName);

	}

	public List<String> getPrimaryKey(String schemaName, String tableName){
		
		ResultSet rs = null;
		Connection conn=null;
		List<String> primaryList = new ArrayList<String>();
		tableName=tableName.toLowerCase();
		try{
			conn=dataSource.getConnection();
			DatabaseMetaData dbmeta = conn.getMetaData();

			rs = dbmeta.getPrimaryKeys(null, schemaName, tableName);
			while (rs.next()) {
				// String tName=rsKey.getString("TABLE_NAME");
				String pri = rs.getString("COLUMN_NAME");
				primaryList.add(pri);
			}
		}catch(Exception e){
			
			log.info("Exception:",e);
			
		}finally{
			
			try{
				if(rs!=null)
					rs.close();
				conn.close();
			}catch(Exception e){
				log.error("Close connection error",e);
			}
			
		}
		
		return primaryList;
		
	}
	
	public Table getTableMetaData(String schemaName, String tableName) {
		//greenplum doesn't recognize big case letter
		//gemfire doesn't recognize low case letter
		long start=System.currentTimeMillis();
		schemaName=schemaName.toLowerCase();
		tableName=tableName.replaceAll("\"","").toLowerCase();
		log.debug("getTableMetaData function,tableName:"+tableName);
		Table table = new Table(tableName);
		table.setName(tableName);
		List<String> primaryList = new ArrayList<String>();
		List<Column> columnList = new ArrayList<Column>();
		String columnName;
		String columnType;
		String defaultValue;
		int columnLen;
		int decimalLen = -1;
		String flag;
		ResultSet rs = null;// get column meta data
		ResultSet rsKey = null;// for primary key
		
		//igmore all the columns when generating the tables
		String columns=Properties.getProperty("tmg.gp.ignore.column");
		List<String> ignoreColumnList=FileUtil.string2List(columns);
		
		Connection conn=null;
		try {

			conn=dataSource.getConnection();
			DatabaseMetaData dbmeta = conn.getMetaData();

			rsKey = dbmeta.getPrimaryKeys(null, schemaName, tableName);
			while (rsKey.next()) {
				// String tName=rsKey.getString("TABLE_NAME");
				String pri = rsKey.getString("COLUMN_NAME");
				primaryList.add(pri);
				// String index=rsKey.getString("KEY_SEQ");
				// String primary=rsKey.getString("PK_NAME");
				// System.out.println("Primary Key===="+tName+"\t"+col+"\t"+index+"\t"+primary);
			}

			rs = dbmeta.getColumns(null, schemaName, tableName, "%");
			if(rs==null){
				
				log.info("No such table "+tableName);
				return null;
				
			}
			while (rs.next()) {

				columnName = rs.getString("COLUMN_NAME");// column name
				if(ignoreColumnList.contains(columnName.toLowerCase())){
					log.info("The column "+columnName+" is ignored!");
					continue;
				}
					
				columnType = rs.getString("TYPE_NAME");// type name
				columnLen = Integer.valueOf(rs.getString("COLUMN_SIZE"));// column
																			// size
				if (rs.getString("DECIMAL_DIGITS") != null) {
					try {
						decimalLen = Integer.valueOf(rs
								.getString("DECIMAL_DIGITS"));// decimal len
					} catch (Exception e) {
						log.error("error",e);
					}

				}
				flag = rs.getString("IS_NULLABLE");// YES --- if the column can
													// include NULLs NO --- if
													// the column cannot include
													// NULLs
				defaultValue=rs.getString("COLUMN_DEF");
				Column column = new Column(columnName, columnType, columnLen,
						flag, decimalLen,defaultValue);
				if (primaryList.contains(columnName))
					column.setPrimary(true);

				columnList.add(column);
			}

			table.setCloumns(columnList);

		} catch (Exception e) {
			log.error("error",e);;
		} finally {
			try {
				if(rs!=null)
					rs.close();
				rsKey.close();
				conn.close();
			} catch (Exception e) {
				log.error("error",e);
			}

		}

		long end=System.currentTimeMillis();
		long time=end-start;
		log.debug("getTableMetaData,GP, time used:"+time);
		return table;

	}
	
public int[] executeMultipleQuery(String sql){
		
		
		//long start=System.currentTimeMillis();
		Connection conn=null;
		PreparedStatement ps=null;
		String single=null;
		int[] results=null;
		
		try{
			
			conn=dataSource.getConnection();
			conn.setAutoCommit(false);
			String[] sqls=sql.split(";");
			int count=sqls.length;
			results= new int[count];
			for(int i=0;i<count;i++){
				single=sqls[i];
				ps=conn.prepareStatement(single);
				//ps.setQueryTimeout(60*40);
				results[i]=ps.executeUpdate();
				log.debug(single+", the result is "+results[i]);
			}
			
			conn.commit();
			
			
		}catch(Exception e){
			
			if(conn!=null)
				try {
					conn.rollback();
				} catch (SQLException e1) {
					log.info("Roll back error",e);
				}
			
			log.info("failed sql is "+single,e);
			
			
		}finally{
			
			try{
				
				
				if(ps!=null)
					ps.close();
					
				if(conn!=null)
					conn.close();	
			}catch(Exception e){
				
				log.error("close connection error",e);
			}
			
			
		}
		
		
		//long end=System.currentTimeMillis();
		
		//System.out.println("Using time:"+(end-start));
		
		return results;
		
	}
	public int executeUpdate(String sql){
		Connection conn=null;
		PreparedStatement ps=null;
		sql=sql.toLowerCase();
		int count=-1;
		try{
			conn=dataSource.getConnection();
			ps= conn.prepareStatement(sql.toLowerCase());
			count=ps.executeUpdate();
		}catch(Exception e){
			log.info(sql);
			log.error("error",e);
			
		}finally{
			
			try{
				ps.close();
				conn.close();
			}catch(Exception e){
				log.error("close connection error",e);
			}
			
		}
		
		
		return count;
	}
	
	public Map<Integer,Long> execute1(String sql) throws SQLException{
		
		return null;
	}
	
	
	
	
	
	//only used to get the count of a table
	
	public int execute(String sql) {
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		int total=0;
		sql=sql.toLowerCase();
		try{
			conn=dataSource.getConnection();
			ps= conn.prepareStatement(sql.toLowerCase());
			rs=ps.executeQuery();
			while(rs.next())
				total=rs.getInt(1);
			log.debug("sql is "+sql+"result is "+total);
		}catch(Exception e){
			
			log.error("error",e);
			log.info("sql is "+sql);
			
		}finally{
			
			try{
				if(rs!=null)
					rs.close();
				ps.close();
				conn.close();
			}catch(Exception e){
				log.error("close connection error",e);
			}
			
		}
		
		return total;
	}
	
	public List<String> getColumnList(String schemaName,String tableName){
		List<String> columnList=new ArrayList<String>();
		Connection conn=null;
		ResultSet rs=null;
		try {

			conn=dataSource.getConnection();
			DatabaseMetaData dbmeta = conn.getMetaData();


			rs = dbmeta.getColumns(null, schemaName, tableName, "%");
			if(rs==null){
				log.info("No such table "+tableName);
				return null;
			}
			while (rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");// column name
				columnList.add(columnName);
			}

			

		} catch (Exception e) {
			log.error("error",e);;
		} finally {
			try {
				if(rs!=null)
					rs.close();
				conn.close();
			} catch (Exception e) {
				log.error("error",e);
			}

		}
		
		return columnList;
		
	}
	
	
	
	
	//the ds key is based on the order of the column
	public List<String> getDistributedKey(String schema,String table){
		
		List<String> dsKey= new ArrayList<String>();
		ResultSet dsRS=null,columnRS=null;;
		Connection conn=null;
		PreparedStatement ps=null;
		
		//remove the double quote
		schema=schema.replaceAll("\"", "").toLowerCase();
		table=table.replaceAll("\"", "").toLowerCase();
		
		StringBuilder sql=new StringBuilder("select table_owner,table_name, COALESCE(distribution_keys,'DISTRIBUTED RANDOMLY') distribution_keys from (\n");
		sql.append("SELECT pgn.nspname as table_owner,\n");
		sql.append("pgc.relname as table_name,\n");
		sql.append("pga.attname as distribution_keys\n");
		sql.append("FROM \n");
		sql.append("\t(SELECT gdp.localoid,\n");
		sql.append("\t\tCASE\n");
		sql.append("\t\tWHEN ( Array_upper(gdp.attrnums, 1) > 0 ) THEN Unnest(gdp.attrnums)\n");
		sql.append("\t\tELSE NULL\n");
		sql.append("\t\tEND AS attnum\n");
		sql.append("\tFROM gp_distribution_policy gdp	ORDER BY gdp.localoid\n");
		sql.append("\t) AS distrokey\n");
		sql.append("\tINNER JOIN pg_class AS pgc\n");
		sql.append("\t\tON distrokey.localoid = pgc.oid\n");
		sql.append("\tINNER JOIN pg_namespace pgn\n");
		sql.append("\t\tON pgc.relnamespace = pgn.oid\n");
		sql.append("\tLEFT OUTER JOIN pg_attribute pga\n");
		sql.append("\t\tON distrokey.attnum = pga.attnum\n");
		sql.append("\t\tAND distrokey.localoid = pga.attrelid\n");
		//sql.append("\tORDER BY pgn.nspname,pgc.relname,pga.attname\n");
		sql.append(") as a where table_owner='").append(schema).append("'").append(" and table_name='").append(table).append("';");
		
		
		try{
			conn=dataSource.getConnection();
			DatabaseMetaData dbmeta = conn.getMetaData();
			columnRS = dbmeta.getColumns(null, schema, table, "%");
			List<String> columnList= new ArrayList<String>();
			while (columnRS.next()) {
				String columnName = columnRS.getString("COLUMN_NAME");// column name
				columnList.add(columnName);
			}
			
			ps= conn.prepareStatement(sql.toString());
			dsRS=ps.executeQuery();
			List<String> list=new ArrayList<String>();
			while(dsRS.next()){
				String key=dsRS.getString("distribution_keys");
				list.add(key);
			}
			
			//sort based on the column order
			for(String column:columnList){
				if(list.contains(column))
					dsKey.add(column);
			}
			
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			try{
				
				ps.close();
				if(dsRS!=null)
					dsRS.close();
				if(columnRS!=null)
					columnRS.close();
				conn.close();
			}catch(Exception e){
				
				log.error("close conneciton error",e);
				
			}

		}
		
		return dsKey;
		
		
	}
	
	
	public List<String> getDistinctDataByColumn(String schema,String table,String column,String suffix){
		
		
		List<String> list= new ArrayList<String>();
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement ps=null;
		StringBuilder sb= new StringBuilder("select distinct(").append(column).append(") from ").append(schema).append(".").append(table);
		if(suffix!=null&&!suffix.trim().equals(""))
			sb.append(" where ").append(suffix);
		sb.append(";");
		
		String value;//store the value of each column in each row
		try{
			conn=dataSource.getConnection();
			ps=conn.prepareStatement(sb.toString().toLowerCase());
			rs=ps.executeQuery();
			
			while(rs.next()){
				value=rs.getString(column);
				list.add(value);
			}
			
			
		}catch(Exception e){
			
			log.error("Exception:",e);
			
			
		}finally{
			
				try{
					if(rs!=null)
						rs.close();
					ps.close();
					conn.close();
				
			}catch(Exception e){
				log.error("close connection error",e);
				
			}
			
		}
		
		log.debug("getDistinctDataByColumn done");
		return list;
		
	}
	
	

	public ArrayList<String> getTables(String schema) {

		ArrayList<String> list = new ArrayList<String>();
		ResultSet tables = null;
		String tableName;
		Connection conn = null;
		try {
			conn=dataSource.getConnection();
			DatabaseMetaData dbMeta = conn.getMetaData();
			String[] types = { "TABLE" };
			tables = dbMeta.getTables(null, schema, "%", types);
			String ignoreTable=Properties.getProperty("tmg.gp.ignore.file");
			List<String> ignoreList=FileUtil.string2List(ignoreTable);
			while (tables.next()) {
				tableName = tables.getString(3);
				if(tableName.matches(".*_[0-9]{1,}_prt.*"))
					continue;
				if(ignoreList!=null&&ignoreList.contains(tableName))
					continue;
				list.add(tableName);
			}

		} catch (Exception e) {

			log.error("error", e);

		} finally {

			try {
				tables.close();
				conn.close();

			} catch (Exception e) {

				log.error("close connection error", e);
			}
		}
		return list;

	}

	
	
	
	public ArrayList<String> getPartitions(String schema,String table) {

		ArrayList<String> list = new ArrayList<String>();
		ResultSet tables = null;
		String tableName;
		Connection conn = null;
		try {
			conn=dataSource.getConnection();
			DatabaseMetaData dbMeta = conn.getMetaData();
			String[] types = { "TABLE" };
			tables = dbMeta.getTables(null, schema, table+"%", types);
			while (tables.next()) {
				tableName = tables.getString(3);
				if(tableName.matches(table+"_[0-9]{1,}_prt_[a-zA-Z]{1,}")){
					list.add(tableName.substring(tableName.lastIndexOf("_")+1, tableName.length()));
				}
				
			}

		} catch (Exception e) {

			log.error("error", e);

		} finally {

			try {
				tables.close();
				conn.close();

			} catch (Exception e) {

				log.error("close connection error", e);
			}
		}
		return list;

	}

	/*
	 * 
	 * generate the create schema statement based on a table object Para: Table
	 * object Return value: the statement to create a shema
	 */
	/*
	//used when generated the tables partitioned by client_id
	public String generateSchema(String schema,Table table,List<String> dsKeyList,boolean underScore){
		
		//boolean underScore=false;
		
		
		Map<String,List<String>> identityMap=TableUtil.getTableIdentity();
		Map<String,String> dsKeyTableMap= TableUtil.getDistributedKeyTableMap();
		final String identity=" GENERATED BY DEFAULT AS IDENTITY ";
		
		
		
		//used to colocate with tables
		Map<String,String> map= new LinkedHashMap<String,String>();
		//map.put("persons","prsn_id");
		map.put("clients","client_id");
		
		String tableName, columnName, oldcolumnType, newColumnType, flag,tmpKey;
		int columnLen, columnDecimal;
		tableName = table.getName();
		//tableName=tableName.toLowerCase();
		
		String capital=Properties.getProperty("tmg.gf.table.default");
		if(capital==null||capital.trim().equals(""))
			capital="capital";
		
		if(capital.equalsIgnoreCase("capital")){
			tableName=tableName.toUpperCase();
			schema=schema.toUpperCase();
		}
			
	
		StringBuilder createTable = new StringBuilder("Create table ");
		if(schema!=null&&!schema.equals("")){
			createTable.append(schema).append(".");
		}
		
		// add doubel quoto for gemfire, becasue if the table contains reserved key word, there will be an error
				//for example, a table named check
		if(underScore)
			//create the duplicated table with _
			createTable.append("\"").append(tableName).append("_1\"").append("(");
		else
			createTable.append("\"").append(tableName).append("\"").append("(");
		
		
		
		List<Column> columns = table.getCloumns();
		Iterator<Column> columnIt = columns.iterator();
		List<String> primaryKeyList = new ArrayList<String>();
		String dsKey="";
		List<String> specialDT= new ArrayList<String>();
		specialDT.add("NUMERIC");
		specialDT.add("numeric");
		int precision=40;
		int scale=10;
		while (columnIt.hasNext()) {

			Column column = columnIt.next();
			columnName = column.getName();
			if(columnName.equalsIgnoreCase("del"))
				continue;
			oldcolumnType = column.getType();
			newColumnType = Mapper.GP2Gemfire(oldcolumnType);// convert types
			columnLen = column.getLen();
			flag = column.getFlag();
			if (newColumnType == null) {
				log.info(columnName+":No such type:" + oldcolumnType+ " is definied the mapper file\n");
				continue;
			} else if (newColumnType.equals("incompatible")) {
				log.info(oldcolumnType+ " is a incompatible datatype\n");
				//System.out.println("\t"+columnName+":"+oldcolumnType);
				continue;
			}
			
			//temporary codes to generate partition by
			if(dsKeyList!=null&&dsKeyList.contains(columnName)){
				
				dsKey+=columnName+",";
			}
			
			createTable.append("\n    ").append(columnName).append(" ").append(newColumnType);

			if (Mapper.getParameter(oldcolumnType) == 1) {
				
					createTable.append("(").append(columnLen).append(")");

			} else if (Mapper.getParameter(oldcolumnType) == 2) {
				
				columnDecimal = column.getDecimalLen();
				//the default len of numeric is 131089
				
				//make a concession in gemfire, as the default precision and scale are different from GP.
				//By default, the two value of GP is infinite, precison in GF is 5 and scale is 0;
				//Now make the default as below
				//precision=40, scale=10
				
				if(specialDT.contains(oldcolumnType)){
					if(columnLen==131089)//the default len of numberic is 131089
						columnLen=precision;
					if(columnDecimal==0)
						columnDecimal=scale;
				}
				
				//if(oldcolumnType.equalsIgnoreCase("NUMERIC")&&columnLen==131089)
					//log.info(columnName+",NUMERIC with default length 131089");
				//else
					createTable.append("(").append(columnLen).append(",").append(columnDecimal).append(")");

			}

			String key=schema+"."+tableName;
			if(identityMap.containsKey(key)){
				
				List<String> columnList=identityMap.get(key);
				
				if(columnList.contains(columnName.toUpperCase()))
					createTable.append(identity);
					
				
			}
			
			if (flag.equals("NO"))
				createTable.append(" NOT NULL");

			createTable.append(",");
			// System.out.println("	"+column.getName()+"	"+column.getType()+"	"+column.getLen()+"	"+column.getFlag()+"\t"+column.getDecimalLen());
			//ver doesn't need to be part of primary in gf
			if (column.isPrimary()&&!columnName.equals("ver")){
				primaryKeyList.add(columnName);
			}
				
		}
		if (!primaryKeyList.isEmpty()) {
			String primaryKey="";
			if(primaryKeyList.contains("client_id"))
				primaryKey+="client_id,";
			for(int i=0;i<primaryKeyList.size();i++){
				tmpKey=primaryKeyList.get(i);
				if(!tmpKey.equals("client_id"))
					primaryKey+=tmpKey+",";
			}
			primaryKey = primaryKey.substring(0, primaryKey.length() - 1);//remove the last comma of the primary key
			//log.info("primary key is " + primaryKey+"\n");
			createTable.append("\n    PRIMARY KEY (").append(primaryKey).append(")");
		} else {
			createTable.deleteCharAt(createTable.length() - 1);//remove the last comma of the statement
		}
		
		
		//temporary code for generate distributed key and colocate with
		String value;
		//if we have distributed key, we will use redundant, otherwise no redundant in the schema
		log.debug("dsKeyList="+dsKeyList);
		if(dsKeyList!=null){
			createTable.append("\n)");
			if(!dsKey.equals("")){
				for(String key:map.keySet()){
					value=map.get(key);
					if(dsKey.contains(value)){
						createTable.append("\nPARTITION BY COLUMN (").append(value).append(")");
						break;// partition by only one column
					}
				}
			}
			
			
			//determine if the table is included in the map, if not in the map, there is no colocate
			boolean f=false;	
			for(String key:map.keySet()){
				if(tableName.equals(key)){
					f=true;
					break;
				}
			}
			
			//if the table is not included in the map
			if(!f){
				//System.out.println("I am here");
				for(String key:map.keySet()){
					value=map.get(key);
					//put a double quoto around the table name
					if(capital.equalsIgnoreCase("capital"))
						key=key.toUpperCase();
					key="\""+key+"\"";
					
					//System.out.println("value="+value+",dsKey="+dsKey);
					if(dsKey.contains(value)){
						if(underScore){
							createTable.append("\nCOLOCATE WITH (").append(schema).append(".").append("CLIENTS_1").append(")");
							createTable.append("\nEVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW 'DISK_STORE_1' ASYNCHRONOUS;\n");
						}else{
							createTable.append("\nCOLOCATE WITH (").append(schema).append(".").append(key).append(")");
							createTable.append("\nREDUNDANCY 1");
							//createTable.append("\nPERSISTENT 'PERSISTENT-STORE' ASYNCHRONOUS");
							createTable.append("\nEVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW PERSISTENT 'DISK_STORE' ASYNCHRONOUS;\n");
						}	
						break;
					}
				}
				
			}			
			
		}
		
		String noIdTableString=Properties.getProperty("tmg.gf.noId.file");
		List<String> noIdTableList=FileUtil.string2List(noIdTableString);
		
		if(noIdTableList.contains(tableName.toLowerCase())||noIdTableList.contains(tableName.toUpperCase()))
		
		{
			
			if(!underScore)
				createTable.append("\nEVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW PERSISTENT 'DISK_STORE' ASYNCHRONOUS;\n");
			else
				createTable.append("\nEVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW 'DISK_STORE_1' ASYNCHRONOUS;\n");
			
		}
		return createTable.toString();
	}
	
	public String generateSchema(Table table,List<String> dsKeyList) {
		
		return generateSchema(null,table,dsKeyList,false);

	}
	
	*/
	
	
	/*
	 * 
	 * generate the create schema statement based on a table object Para: Table
	 * object Return value: the statement to create a shema
	 */
	
	//new version generateSchema,partitioned by the distributed key and and colocate using configuration file
	public String generateSchema(String schema,Table table,boolean underScore){
		
		//boolean underScore=false;
		
		
		

		

		Map<String,String> dsKeyTableMap= TableUtil.getDistributedKeyTableMap();
		final String identity=" GENERATED BY DEFAULT AS IDENTITY ";
		
		String tableName, columnName, oldcolumnType, newColumnType, flag,tmpKey,defaultValue;
		int columnLen, columnDecimal;
		tableName = table.getName();
		//tableName=tableName.toLowerCase();
		
		
		//it is used for static get table identity
		//Map<String,List<String>> identityMap=TableUtil.getTableIdentity();
		
		// it is used for dynamic generating table identity
		Map<String,List<String>> identityMap=new HashMap<String,List<String>>();
		List<String> indentityColumn= new ArrayList<String>();
		List<String> seqList=getSequences(schema.toLowerCase(),tableName.toLowerCase());
		for(int i=0;i<seqList.size();i++){
			String sequence=seqList.get(i);
			String column=sequence.substring(tableName.length()+1, sequence.length()-4);
			//System.out.println(column);
			indentityColumn.add(column.toUpperCase());
		}
		identityMap.put(tableName.toUpperCase(), indentityColumn);
		
		
		List<String> dsList=getDistributedKey(schema,tableName);
		String dsKey="";
		for(String c:dsList)
				dsKey+=c+",";
		if(dsKey==null||dsKey.trim().equals("")){
			log.error(tableName+" doesn't have any distributed key");
			return null;
		}
		//remove the last comma
		dsKey=dsKey.substring(0, dsKey.length()-1);
		String capital=Properties.getProperty("tmg.gf.table.default");
		if(capital==null||capital.trim().equals(""))
			capital="capital";
		
		if(capital.equalsIgnoreCase("capital")){
			tableName=tableName.toUpperCase();
			schema=schema.toUpperCase();
		}
			
	
		StringBuilder createTable = new StringBuilder("Create table ");
		if(schema!=null&&!schema.equals("")){
			createTable.append(schema).append(".");
		}
		
		// add doubel quoto for gemfire, becasue if the table contains reserved key word, there will be an error
				//for example, a table named check
		if(underScore)
			//create the duplicated table with _
			createTable.append("\"").append(tableName).append("_1\"").append("(");
		else
			createTable.append("\"").append(tableName).append("\"").append("(");
		
		
		
		List<Column> columns = table.getCloumns();
		Iterator<Column> columnIt = columns.iterator();
		List<String> primaryKeyList = new ArrayList<String>();
		List<String> specialDT= new ArrayList<String>();
		specialDT.add("NUMERIC");
		specialDT.add("numeric");
		int precision=40;
		int scale=10;
		while (columnIt.hasNext()) {

			Column column = columnIt.next();
			columnName = column.getName();
			//the following is two lines are commented out, as tmg.gp.ignore.columns are configured
			// and the column in the list is ignored when getTablemetaData
			//if(columnName.equalsIgnoreCase("del"))
				//continue;
			oldcolumnType = column.getType();
			newColumnType = Mapper.GP2Gemfire(oldcolumnType);// convert types
			columnLen = column.getLen();
			flag = column.getFlag();
			defaultValue=column.getDefaultValue();
			if (newColumnType == null) {
				log.info(columnName+":No such type:" + oldcolumnType+ " is definied the mapper file\n");
				continue;
			} else if (newColumnType.equals("incompatible")) {
				log.info(oldcolumnType+ " is a incompatible datatype\n");
				//System.out.println("\t"+columnName+":"+oldcolumnType);
				continue;
			}
			
			//Some column name has space in it. such as Provider Network Team
			//fix bug EPP-152
			if(columnName.contains(" "))
				columnName="\""+columnName+"\"";
			
			createTable.append("\n    ").append(columnName).append(" ").append(newColumnType);

			if (Mapper.getParameter(oldcolumnType) == 1) {
				
					createTable.append("(").append(columnLen).append(")");

			} else if (Mapper.getParameter(oldcolumnType) == 2) {
				
				columnDecimal = column.getDecimalLen();
				//the default len of numeric is 131089
				
				//make a concession in gemfire, as the default precision and scale are different from GP.
				//By default, the two value of GP is infinite, precison in GF is 5 and scale is 0;
				//Now make the default as below
				//precision=40, scale=10
				
				if(specialDT.contains(oldcolumnType)){
					if(columnLen==131089)//the default len of numberic is 131089
						columnLen=precision;
					if(columnDecimal==0)
						columnDecimal=scale;
				}
				
				//if(oldcolumnType.equalsIgnoreCase("NUMERIC")&&columnLen==131089)
					//log.info(columnName+",NUMERIC with default length 131089");
				//else
					createTable.append("(").append(columnLen).append(",").append(columnDecimal).append(")");

			}
			
			
			
			//String key=schema+"."+tableName;
			if(identityMap.containsKey(tableName.toUpperCase())){
				
				List<String> columnList=identityMap.get(tableName.toUpperCase());
				
				if(columnList.contains(columnName.toUpperCase()))
					createTable.append(identity);
					
				
			}
			
			if (flag.equals("NO"))
				createTable.append(" NOT NULL");
			
			if(defaultValue!=null&&!defaultValue.trim().equals("")){
				if(defaultValue.equals("now()"))
						defaultValue="current_timestamp";
				createTable.append(" default ").append(defaultValue);
			}
				
			
			
			createTable.append(",");
			// System.out.println("	"+column.getName()+"	"+column.getType()+"	"+column.getLen()+"	"+column.getFlag()+"\t"+column.getDecimalLen());
			//ver doesn't need to be part of primary in gf
			if (column.isPrimary()&&!columnName.equals("ver")){
				primaryKeyList.add(columnName);
			}
				
		}
		if (!primaryKeyList.isEmpty()) {
			String primaryKey="";
			if(primaryKeyList.contains("client_id"))
				primaryKey+="client_id,";
			for(int i=0;i<primaryKeyList.size();i++){
				tmpKey=primaryKeyList.get(i);
				if(!tmpKey.equals("client_id"))
					primaryKey+=tmpKey+",";
			}
			primaryKey = primaryKey.substring(0, primaryKey.length() - 1);//remove the last comma of the primary key
			//log.info("primary key is " + primaryKey+"\n");
			createTable.append("\n    PRIMARY KEY (").append(primaryKey).append(")");
		} else {
			createTable.deleteCharAt(createTable.length() - 1);//remove the last comma of the statement
		}
		

			createTable.append("\n)");
			createTable.append("\nPARTITION BY COLUMN (").append(dsKey).append(")");

			//if the distributed key is in the dsKeyTableMap, it will be colocated with the table defined in the map
			dsKey=dsKey.toUpperCase();
			if(dsKeyTableMap.containsKey(dsKey)){
				
				String tmpTableName=dsKeyTableMap.get(dsKey);
				
				if(!tmpTableName.equals(tableName.toUpperCase())){
					
					if(underScore){
						createTable.append("\nCOLOCATE WITH (").append(schema).append(".").append(tmpTableName).append(")");
					}else{
						createTable.append("\nCOLOCATE WITH (").append(schema).append(".").append(tmpTableName).append(")");
					}	
					
				}
			}

					
		if(underScore){
				createTable.append("\nASYNCEVENTLISTENER(ODSEventListener)");
				createTable.append("\nEVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW 'DISK_STORE_1' ASYNCHRONOUS;\n");
		}else{
				createTable.append("\nREDUNDANCY 1");
				createTable.append("\nASYNCEVENTLISTENER(ODSEventListener)");
				createTable.append("\nEVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW PERSISTENT 'DISK_STORE' ASYNCHRONOUS;\n");
		}	
		return createTable.toString();
	}
	
	public String generateSchema(Table table) {
		
		return generateSchema(null,table,false);

	}
	
	
	
	
	/*
public List<Map<String,String>> getData(String schemaName,String tableName){
	
	return getData(schemaName,tableName, null);
	
}
*/
	
public List<Map<String,String>> getData(String schemaName,String tableName,String cName,String suffixSql){
		long start=System.currentTimeMillis();
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement ps=null;
		tableName=tableName.toLowerCase();
		String value;//store the value of each column in each row
		final String aliasTmp="tmp";
		final String aliasTable="c";
		final String latest="vld_frm_dt";
		Column column=null;
		String columnName,columnType;
		Map<String,String> recordMap;//key:columnName,value:columnValue
		
		String client_id=null;
		if(suffixSql!=null&&!suffixSql.trim().equals(""))
			client_id=suffixSql.split("=")[1];
		
		String tmpTable="tmp_"+tableName.replace("\"", "");
		if(client_id!=null&&!client_id.trim().equals(""))
			tmpTable=tmpTable+"_"+client_id;
		
		String tmpSchema="gf_ods";
		try{
			tmpSchema=Properties.getProperty("tmg.gp.target.schema");
		}catch(Exception e){
			
			log.error("tmg.gp.target.schema not found");
			
		}
		StringBuilder sb= new StringBuilder("select ");
	
		List<Map<String,String>> recordsList= new ArrayList<Map<String,String>>();//store all the records
		try {
			Table table = getTableMetaData(schemaName, tableName);
			if(table==null)
				return null;
			List<Column> columnList = table.getCloumns();
			
			if(columnList==null||columnList.size()==0){
				log.error("There is no clomuns for table "+tableName+",in other words,the table doesn't exists");
				return null;
			}
			for(int i=0;i<columnList.size();i++) {
				column = columnList.get(i);
				columnName = column.getName();
				//the following is two lines are commented out, as tmg.gp.ignore.columns are configured
				// and the column in the list is ignored when getTablemetaData
				//if(columnName.equalsIgnoreCase("del"))
					//continue;
				columnType=column.getType();
				if(columnType.equalsIgnoreCase("timetz")||columnType.equalsIgnoreCase("timestamptz"))//change the time with time zone to utc 
					sb.append("timezone('UTC',").append(aliasTable).append(".").append(columnName).append(") as ").append(columnName).append(",");
				else
					sb.append(aliasTable).append(".").append(columnName).append(",");

			}
			
			sb = sb.deleteCharAt(sb.length()-1);// remove the last ,
			sb.append(" from ").append(schemaName).append(".").append(tableName).append(" ").append(aliasTable);
			if(cName!=null){
				sb.append(" join ").append(tmpSchema).append(".").append(tmpTable).append(" ").append(aliasTmp);
				sb.append(" on ").append(aliasTable).append(".").append(cName).append("=").append(aliasTmp).append(".").append(cName);
			}

			if(suffixSql!=null)
				sb.append(" where ").append(aliasTable).append(".").append(suffixSql);
			sb.append(" order by ");
			List<String> pkList=getPrimaryKey(schemaName,tableName);
			for(int i=0;i<pkList.size();i++){
				cName=pkList.get(i);
				if(!cName.equalsIgnoreCase("client_id")&&!cName.equalsIgnoreCase("ver"))
					sb.append(aliasTable).append(".").append(cName).append(",");
			}
			if(!pkList.isEmpty())
				sb.deleteCharAt(sb.length()-1);//remove the last comma
			sb.append(aliasTable).append(".").append(latest).append(" asc");
			
			//use for hashmap
			/*
			boolean f=false;
			if(columnMap!=null&&columnMap.size()>0){
				sb.append(" where ");
				for(String key:columnMap.keySet()){
					value=columnMap.get(key);
					if(key!=null&&!f){
						sb.append(key).append("=").append(value);
						f=true;
					}else if(key!=null&&f){
						sb.append(" and ");
						sb.append(key).append("=").append(value);
					}
					
				}
				
			}
			*/
			sb.append(";");
			log.debug("select sql is");
			log.debug(sb);
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sb.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				
				recordMap= new HashMap<String,String>();// new list for each record
				for(int i=0;i<columnList.size();i++){
					columnName=columnList.get(i).getName();
					//the following is two lines are commented out, as tmg.gp.ignore.columns are configured
					// and the column in the list is ignored when getTablemetaData
					//if(columnName.equalsIgnoreCase("del"))
						//continue;
					value=rs.getString(columnName);
					recordMap.put(columnName,value);
					
				}
				
				recordsList.add(recordMap);
				
			}
			

		} catch (Exception e) {

			log.info("Exception", e);
			//log.info("\n");

		}finally{
			
			try{
				if(rs!=null)
					rs.close();
				ps.close();
				conn.close();
				
			}catch(Exception e){
				log.error("close connection error",e);
			}
			
		}
/*
		Iterator<Map<String,String>> i= recordsList.iterator();
		Map<String,String> m= new HashMap<String,String>();
		while(i.hasNext()){
			
			m=i.next();
			
			for(String key:m.keySet())
				log.info(key+":"+m.get(key));
			
			
		}
		*/
		long end=System.currentTimeMillis();
		long time=end-start;
		log.info("get data done, time used:"+time);
		return recordsList;
	}




public List<Map<String,String>> getLatestRecord(String schemaName,String tableName,String cName,String suffixSql){
	long start=System.currentTimeMillis();
	ResultSet rs=null;
	Connection conn=null;
	PreparedStatement ps=null;
	tableName=tableName.toLowerCase();
	String client_id=null;
	if(suffixSql!=null&&!suffixSql.trim().equals(""))
		client_id=suffixSql.split("=")[1];
	
	String tmpTable="tmp_"+tableName.replace("\"", "");
	if(client_id!=null&&!client_id.trim().equals(""))
		tmpTable=tmpTable+"_"+client_id;
	
	String tmpSchema="gf_ods";
	try{
		tmpSchema=Properties.getProperty("tmg.gp.target.schema");
	}catch(Exception e){
		
		log.error("tmg.gp.target.schema not found");
		
	}
	StringBuilder sb= new StringBuilder("select ");
	String value;//store the value of each column in each row
	
	Column column=null;
	String columnName,columnType;
	final String alias="cl.";
	final String latest="vld_frm_dt";
	//2015-06-22 removed ver from primary
	//there are many dups with the max validate from, so add max(ver) to get the only one record
	final String largest="ver";
	boolean isPrimary;
	Map<String,String> recordMap;//key:columnName,value:columnValue
	List<Map<String,String>> recordsList= new ArrayList<Map<String,String>>();//store all the records
	List<String> keyList= new ArrayList<String>();
	try {
		Table table = getTableMetaData(schemaName, tableName);
		if(table==null)
			return null;
		List<Column> columnList = table.getCloumns();
		
		if(columnList==null||columnList.size()==0){
			log.error("There is clomuns for table "+tableName+",in other words,the table doesn't exists");
			return null;
		}
			

		for(int i=0;i<columnList.size();i++) {
			column = columnList.get(i);
			columnName = column.getName();
			columnType=column.getType();
			isPrimary=column.isPrimary();
			if(isPrimary&&!columnName.equalsIgnoreCase("ver"))
				keyList.add(columnName);
			if(columnType.equalsIgnoreCase("timetz")||columnType.equalsIgnoreCase("timestamptz"))//change the time with time zone to utc 
				sb.append("timezone('UTC',").append(alias).append(columnName).append(") as ").append(columnName).append(",");
			else
				sb.append(alias).append(columnName).append(",");

		}
		
		//subquery, select a.* from 
		//(select max(vld_frm_dt) as vld_frm_dt,prsn_id,clm_id,client_id from ods."claim" where client_id=5000 group by prsn_id,clm_id,	client_id ) a 
		//join tmp on tmp.id=a.prsn_id 
		StringBuilder subquery= new StringBuilder("select b.* from (select max(");
		if(cName!=null){
			subquery.append(latest).append(") as ").append(latest).append(",");
			subquery.append("max(").append(largest).append(") as ").append(largest).append(",");
			for(int i=0;i<keyList.size();i++){
				subquery.append(keyList.get(i)).append(",");
			}
			
			//remove the last comma
			subquery.deleteCharAt(subquery.length()-1);
			
			subquery.append(" from ").append(schemaName).append(".").append(tableName);
			
			//suffix format: columnName=value or columnName1=value1 and columnname2=value2
			if(suffixSql!=null&&!suffixSql.trim().equals(""))
				subquery.append(" where ").append(suffixSql);
			
			
			subquery.append(" group by ");
			for(int i=0;i<keyList.size();i++){
				subquery.append(keyList.get(i)).append(",");
			}
			
			subquery.deleteCharAt(subquery.length()-1);
			
			subquery.append(") b join "+tmpSchema+"."+tmpTable+" on "+tmpSchema+"."+tmpTable+".").append(cName).append("=");
			subquery.append("b.").append(cName);
			
		}
		
		
		
		sb = sb.deleteCharAt(sb.length()-1);// remove the last ,
		sb.append(" from ").append(schemaName).append(".").append(tableName).append(" ").append(alias.substring(0, alias.length()-1)).append("");
		if(cName!=null){
			sb.append(",(").append(subquery).append(") a where ");
			//add the primary =
			for(int i=0;i<keyList.size();i++){
				columnName=keyList.get(i);
				sb.append(alias).append(columnName).append("=a.").append(columnName).append(" and ");
			}
			sb.append(alias).append(latest).append("=a.").append(latest);
			sb.append(" and ");
			sb.append(alias).append(largest).append("=a.").append(largest);
		}else{
			
			final String  tmpAlia="a";
			//subquery used to find the latest records when no need to divide data
			StringBuilder subquery2= new StringBuilder(" join (select max(");
			subquery2.append(latest).append(") as ").append(latest).append(",");
			subquery2.append("max(").append(largest).append(") as ").append(largest).append(",");
			for(int i=0;i<keyList.size();i++){
				subquery2.append(keyList.get(i)).append(",");
			}
			
			//remove the last comma
			subquery2.deleteCharAt(subquery2.length()-1);
			
			subquery2.append(" from ").append(schemaName).append(".").append(tableName);
			
			//suffix format: columnName=value or columnName1=value1 and columnname2=value2
			if(suffixSql!=null&&!suffixSql.trim().equals(""))
				subquery2.append(" where ").append(suffixSql);
			
			
			subquery2.append(" group by ");
			for(int i=0;i<keyList.size();i++){
				subquery2.append(keyList.get(i)).append(",");
			}
			
			subquery2.deleteCharAt(subquery2.length()-1);
			subquery2.append(") ").append(tmpAlia).append(" on ");
			
			//join on
			for(int i=0;i<keyList.size();i++){
				String pri=keyList.get(i);
				subquery2.append(alias).append(pri).append("=").append(tmpAlia).append(".").append(pri).append(" and ");
			}
			
			//add ver as ver is not in the keyList
			subquery2.append(alias).append(largest).append("=").append(tmpAlia).append(".").append(largest);
			sb.append(subquery2);
			
		}
		
		
		
		
		//suffix format: columnName=value or columnName1=value1 and columnname2=value2
		if(suffixSql!=null&&!suffixSql.trim().equals("")){
		
			//add the alias name in the column
			for(int i=0;i<keyList.size();i++){
				columnName=keyList.get(i);
				suffixSql=suffixSql.toLowerCase();
				if(suffixSql.contains(columnName.toLowerCase())){
					suffixSql=suffixSql.replaceAll(columnName, alias+columnName);
				}
			}
			if(cName!=null){
				sb.append(" and ");
				
				// fix the bug when is no need to divide the results
			}else{
				sb.append(" where ");
			}
				
			sb.append(suffixSql);
			
			
			
		}
			
		
	
		sb.append(";");
		log.debug("select sql is");
		log.debug(sb);
		conn = dataSource.getConnection();
		ps = conn.prepareStatement(sb.toString());
		rs = ps.executeQuery();
		while(rs.next()){
			
			recordMap= new HashMap<String,String>();// new list for each record
			for(int i=0;i<columnList.size();i++){
				columnName=columnList.get(i).getName();
				value=rs.getString(columnName);
				recordMap.put(columnName,value);
				
			}
			
			recordsList.add(recordMap);
			
		}
		

	} catch (Exception e) {

		log.info("Exception", e);
		//log.info("\n");

	}finally{
		
		try{
			if(rs!=null)
				rs.close();
			ps.close();
			conn.close();
			
		}catch(Exception e){
			log.error("close connection error",e);
		}
		
	}
/*
	Iterator<Map<String,String>> i= recordsList.iterator();
	Map<String,String> m= new HashMap<String,String>();
	while(i.hasNext()){
		
		m=i.next();
		
		for(String key:m.keySet())
			log.info(key+":"+m.get(key));
		
		
	}
	*/
	long end=System.currentTimeMillis();
	long time=end-start;
	log.debug("get data done, time used:"+time);
	return recordsList;
}
	

public  void getTableType(){
	
	
	Connection conn=null;
	ResultSet rs=null;
	try{
		conn=dataSource.getConnection();
		DatabaseMetaData dbmeta = conn.getMetaData();

		rs=dbmeta.getTableTypes();
		String name;
		while(rs.next()){
			name = rs.getString(1);
			System.out.println(name);
			
		}
		
			
		
		
	}catch(Exception e){
		e.printStackTrace();
		
	}
	
	
	
}

public ArrayList<String> getSequences(String schema,String table) {

	ArrayList<String> list = new ArrayList<String>();
	ResultSet rs = null;
	String name;
	Connection conn=null;
	try {
		conn=dataSource.getConnection();
		DatabaseMetaData dbMeta = conn.getMetaData();
		String[] types = { "SEQUENCE" };

		rs = dbMeta.getTables(null, schema, table+"%", types);

		while (rs.next()) {
			// System.out.println(tables.getString(2)+","+tables.getString(3)+","+tables.getString(4)+","+tables.getString(5));
			name = rs.getString(3);
			
				list.add(name);
		}

	} catch (Exception e) {

		log.error("error",e);

	} finally {

		try {
			rs.close();
			conn.close();

		} catch (Exception e) {

			log.error("close connection error",e);
		}
	}
	
	return list;
}


public Map<Integer,Long> getDistributedData(String schema,String table){
	
	Map<Integer,Long> map= new HashMap<Integer,Long>();
	ResultSet rs = null;
	Connection conn=null;
	StringBuilder sql= new StringBuilder("select gp_segment_id,count(1) from ");
	try {
		sql.append(schema).append(".").append(table);
		sql.append(" GROUP BY gp_segment_id order by gp_segment_id;");
		conn=dataSource.getConnection();
		PreparedStatement ps=conn.prepareStatement(sql.toString());
		rs=ps.executeQuery();
		while (rs.next()) {
			int segmentId=rs.getInt(1);
			long count=rs.getLong(2);
			map.put(segmentId,count);
			
		}

	} catch (Exception e) {

		log.error("error",e);

	} finally {

		try {
			rs.close();
			conn.close();

		} catch (Exception e) {

			log.error("close connection error",e);
		}
	}
	
	return map;
	
	
	
}



}
