package com.tmg.gemfire.DAOImp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tmg.core.Mapper;
import com.tmg.core.Properties;
import com.tmg.gemfire.Bean.Column;
import com.tmg.gemfire.Bean.Table;
import com.tmg.gemfire.DAO.CommonDAO;
import com.tmg.gemfire.Util.FileUtil;
import com.tmg.gemfire.Util.TableUtil;


public class GemfireDAOImp implements CommonDAO {
	
	private static Logger log= Logger.getLogger(GemfireDAOImp.class);
	@Resource(name="dataSourceGemfireThin")
	private DataSource dataSource;
	
	/*
	@Resource(name="dataSourceGemfireFix")
	private DataSource fixDataSource;
	*/
	/*
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	 
	public void setFixDataSource(DataSource fixDataSource) {
		this.fixDataSource = fixDataSource;
	}
	*/
	
	@Autowired
	private  GreenplumDAOImp gpDaoImp;
	
	/*
	public void setgpDaoImp(GreenplumDAOImp gpDaoImp){
		this.gpDaoImp=gpDaoImp;
	}
	*/
	public Table getTableMetaData(String tableName) {

		return getTableMetaData(null, tableName);

	}

	public Table getTableMetaData(String schemaName, String tableName) {
		long start=System.currentTimeMillis();
		schemaName=schemaName.toUpperCase();
		tableName=tableName.toUpperCase();
		tableName=tableName.replaceAll("\"", "");
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
			while (rs.next()) {

				columnName = rs.getString("COLUMN_NAME");// column name
				columnType = rs.getString("TYPE_NAME");// type name
				columnLen = Integer.valueOf(rs.getString("COLUMN_SIZE"));// column
																			// size
				if (rs.getString("DECIMAL_DIGITS") != null) {
					try {
						decimalLen = Integer.valueOf(rs.getString("DECIMAL_DIGITS"));// decimal len
					} catch (Exception e) {
						log.info("Convert to Integer error",e);
					}

				}

				flag = rs.getString("IS_NULLABLE");// YES --- if the column can
													// include NULLs NO --- if
													// the column cannot include
													// NULLs
				defaultValue=rs.getString("COLUMN_DEF");
				log.debug("columnName:"+columnName+",columnType:"+columnType+",columnLen:"+columnLen+",flag:"+flag+",decimalLen:"+decimalLen);
				Column column = new Column(columnName, columnType, columnLen,flag, decimalLen,defaultValue);
				if (primaryList.contains(columnName))
					column.setPrimary(true);

				columnList.add(column);
			}

			table.setCloumns(columnList);

		} catch (Exception e) {
			log.error("error",e);
		} finally {
			try {
				if(rs!=null)
					rs.close();
				rsKey.close();
				conn.close();
			} catch (Exception ex) {
				log.error("close connection error",ex);
			}

		}

		long end=System.currentTimeMillis();
		long time=end-start;
		log.debug("getTableMetaData,Gemfire, time used:"+time);
		return table;

	}
	
	public List<String> getPKList(String schema,String table){
		
		List<String> pkList= new ArrayList<String>();
		Connection conn=null;
		ResultSet rs=null;
		try{
			conn=dataSource.getConnection();
			DatabaseMetaData dbma=conn.getMetaData();
			rs=dbma.getPrimaryKeys(null, schema, table);
			//System.out.println("Hello");
			while(rs.next()){
				//System.out.println("Hello"+rs.getString("COLUMN_NAME"));
				pkList.add(rs.getString("COLUMN_NAME"));
			}

		}catch(Exception e){

			log.error("",e);
			
		}finally{
			
			try{
				rs.close();
				conn.close();
			}catch(Exception ex){
				//ex.printStackTrace();
				log.error("close connection error",ex);
			}
			
		}
		
		return pkList;
		
	}
	public int executeUpdate(String sql) throws SQLException{
		long start=System.currentTimeMillis();
		//log.info(start+",Begin to execute\n"+sql+"\n");
		Connection conn=null;
		PreparedStatement ps=null;
		int count=-1;
		try{
			conn=dataSource.getConnection();
			ps= conn.prepareStatement(sql);
			count=ps.executeUpdate();
			log.debug(sql);
		}catch(Exception e){
			log.debug("The sql is");
			log.debug(sql);
			log.error("",e);
			try{
				if(ps!=null)
					ps.close();
				if(conn!=null)
					conn.close();
			}catch(Exception ex){
				//ex.printStackTrace();
				log.error("close connection error",ex);
			}
			throw new SQLException();
			
		}finally{
			
			try{
				if(ps!=null)
					ps.close();
				if(conn!=null)
					conn.close();
			}catch(Exception ex){
				//ex.printStackTrace();
				log.error("close connection error",ex);
			}
			
		}
		
		
		long end=System.currentTimeMillis();
		long time=end-start;
		log.debug("execute, time used:"+time);
		
		return count;
	}
	
	
	
	public int execute(String sql) throws SQLException{
		
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		int total=-1;
		try{
			conn=dataSource.getConnection();
			ps= conn.prepareStatement(sql);
			rs=ps.executeQuery(); 
			while(rs.next())
				total=rs.getInt(1);
			
				
			//log.debug("sql is "+sql+",result is "+total);
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
	
	
public void execute(String sql,String parameter) throws SQLException{
		
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		String[] paras=parameter.split(",");
		try{
			conn=dataSource.getConnection();
			ps= conn.prepareStatement(sql);
			if(paras.length==1){
				ps.setInt(1, Integer.valueOf(paras[0]));
			}else if(paras.length==2){
				ps.setInt(1, Integer.valueOf(paras[0]));
				ps.setInt(2, Integer.valueOf(paras[1]));
			}else if(paras.length==3){
				ps.setInt(1, Integer.valueOf(paras[0]));
				ps.setInt(2, Integer.valueOf(paras[1]));
				ps.setInt(3, Integer.valueOf(paras[2]));
			}
			rs=ps.executeQuery(); 
			while(rs.next())
				log.info(rs.getInt(1));
			
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
		
		
		
	}
	
	
	
public List<String> getList(String sql){
		
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<String> list= new ArrayList<String>();
		try{
			conn=dataSource.getConnection();
			ps= conn.prepareStatement(sql);
			rs=ps.executeQuery(); 
			while(rs.next())
				list.add(rs.getString(1));
			
				
			//log.debug("sql is "+sql+",result is "+total);
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
		
		return list;
		
	}
	
	public Map<Integer,Long> execute1(String sql) throws SQLException{
		
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Map<Integer,Long> result=new HashMap<Integer,Long>();
		try{
			conn=dataSource.getConnection();
			ps= conn.prepareStatement(sql);
			rs=ps.executeQuery();
			long total=0;
			int client_id=0; 
			while(rs.next()){
				client_id=rs.getInt("client_id");
				total=rs.getLong("total");
				result.put(client_id, total);
			}
				
			//log.debug("sql is "+sql+",result is "+total);
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
		
		return result;
		
	}
	
	public String execute2(String sql){
		
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		StringBuilder res= new StringBuilder();
		try{
			conn=dataSource.getConnection();
			ps= conn.prepareStatement(sql);
			rs=ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnNumber = rsmd.getColumnCount();
			while(rs.next()){
				for(int i=1;i<=columnNumber;i++)
					res.append(rs.getString(i)).append("\t");
				res.deleteCharAt(res.length()-1);//remove the last \t
				res.append("\n");
			}
				
			//log.debug("sql is "+sql+",result is "+total);
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
		
		return res.toString();
		
	}
	
	public boolean callProcedure(String procedure){
		
		Connection conn=null;
		CallableStatement cs=null;
		boolean f=false;
		try{
			//conn=fixDataSource.getConnection();
			 conn=dataSource.getConnection();
			 cs=conn.prepareCall(procedure);
			f=cs.execute();
		}catch(Exception e){

			log.error("Exception ",e);
			
			
		}finally{
			try{
				cs.close();
				conn.close();
				
				
			}catch(Exception e){
				
				log.error("close conneciton error",e);
			}
			
			
		}
		
		return f;
		
		
		
		
		
	}
	

	public ArrayList<String> getTables(String schema) {

		ArrayList<String> list = new ArrayList<String>();
		ResultSet tables = null;
		String name;
		Connection conn=null;
		try {
			conn=dataSource.getConnection();
			DatabaseMetaData dbMeta = conn.getMetaData();
			String[] types = { "TABLE" };

			tables = dbMeta.getTables(null, schema, "%", types);

			while (tables.next()) {
				// System.out.println(tables.getString(2)+","+tables.getString(3)+","+tables.getString(4)+","+tables.getString(5));
				name = tables.getString(3);
				list.add(name);
				
				//only used for greenplum
				/*
				if (list.isEmpty()) {
					
				} else {// exclude the partition tables
					int flag = 0;
					for (int i = 0; i < list.size(); i++) {

						if (name.length() > list.get(i).length()) {

							if (name.substring(0, list.get(i).length()).equals(
									list.get(i))) {
								break;

							}

						}

						flag++;
					}

					if (flag == list.size())
						list.add(name);

				}
				*/

			}

		} catch (Exception e) {

			log.error("error",e);

		} finally {

			try {
				tables.close();
				conn.close();

			} catch (Exception ex) {

				log.error("error",ex);
			}
		}

		
		// add the table in the configuration to the list

		String file = Properties.getProperty("tmg.gf.additionfile");
		List<String> fileList = FileUtil.string2List(file);

		if(fileList!=null){
			Iterator<String> iter = fileList.iterator();

			while (iter.hasNext()) {
				String value = iter.next();
				if (!list.contains(value.toLowerCase())&& !list.contains(value.toUpperCase())){
					list.add(value);
					log.info("add "+value+" into filelist");
				}
					
			}
		}
		
		// remove the table in the configuration to the list
		file = Properties.getProperty("tmg.gf.ignore.file");
		fileList = FileUtil.string2List(file);
		if (fileList != null) {
			Iterator<String> iter = fileList.iterator();
			while (iter.hasNext()) {
				String value = iter.next();
				if (list.contains(value.toLowerCase())||list.contains(value.toUpperCase())){
					list.remove(value.toLowerCase());
					list.remove(value.toUpperCase());
					log.info("remove "+value+" from filelist");
				}
					
			}

		}
		
		return list;

	}

	/*
	 * 
	 * generate the create schema statement based on a table object Para: Table
	 * object Return value: the statement to create a shema
	 */
	
	   //this one is used to transfom schema from gfxd to snappydata
		public String generateSchema1(String schema,Table table,Map<String,Integer> typeMap){
			
			//boolean underScore=false;
			Map<String,String> dsKeyTableMap= TableUtil.getDistributedKeyTableMap();
			final String identity=" GENERATED BY DEFAULT AS IDENTITY ";
			
			String tableName, columnName, gpcolumnType, gfColumnType, flag,tmpKey,defaultValue;
			int columnLen, columnDecimal;
			tableName = table.getName();
			//tableName=tableName.toLowerCase();
			
			
			//it is used for static get table identity
			//Map<String,List<String>> identityMap=TableUtil.getTableIdentity();
			
			// it is used for dynamic generating table identity
			Map<String,List<String>> identityMap=new HashMap<String,List<String>>();
			List<String> indentityColumn= new ArrayList<String>();
			
			
			String dsKey=getDS(schema.toUpperCase(),tableName.toUpperCase());
			dsKey=dsKey.replace("PARTITION BY COLUMN", "PARTITION_BY").replace("(", "'").replace(")", "'");
			if(dsKey==null||dsKey.trim().equals("")){
				log.error(tableName+" doesn't have any distributed key in GFXD");
				//dsKey+=",";
			}else if(dsKey.equalsIgnoreCase("PARTITION BY PRIMARY KEY")){
				System.err.println(tableName+" is partitioning by primary key, choose one first");
			}
			//remove the last comma
			//dsKey=dsKey.substring(0, dsKey.length()-1);
			String capital=Properties.getProperty("tmg.gf.table.default");
			if(capital==null||capital.trim().equals(""))
				capital="capital";
			
			if(capital.equalsIgnoreCase("capital")){
				tableName=tableName.toUpperCase();
				schema=schema.toUpperCase();
			}
				
		
			StringBuilder createTable = new StringBuilder("\ndrop table if exists ");
			
			if(schema!=null&&!schema.equals("")){
				createTable.append(schema).append(".");
			}
			
			// add doubel quoto for gemfire, becasue if the table contains reserved key word, there will be an error
					//for example, a table named check
			//if(underScore)
				//create the duplicated table with _
				//createTable.append("\"").append(tableName).append("_1\"").append("(");
			//else
			createTable.append("\"").append(tableName).append("\"").append(";\n");
			createTable.append("Create table ");
			if(schema!=null&&!schema.equals("")){
				createTable.append(schema).append(".");
			}
			createTable.append("\"").append(tableName).append("\"").append("(");
			
			List<Column> columns = table.getCloumns();
			Iterator<Column> columnIt = columns.iterator();
			List<String> primaryKeyList = new ArrayList<String>();
			List<String> specialDT= new ArrayList<String>();
			specialDT.add("NUMERIC");
			specialDT.add("numeric");
			int precision=38;
			int scale=10;
			while (columnIt.hasNext()) {

				Column column = columnIt.next();
				columnName = column.getName();
				//the following is two lines are commented out, as tmg.gp.ignore.columns are configured
				// and the column in the list is ignored when getTablemetaData
				//if(columnName.equalsIgnoreCase("del"))
					//continue;
				gfColumnType = column.getType();
				columnLen = column.getLen();
				flag = column.getFlag();
				defaultValue=column.getDefaultValue();
				
				//Some column name has space in it. such as Provider Network Team
				//fix bug EPP-152
				if(columnName.contains(" "))
					columnName="\""+columnName+"\"";
				
				//covert long varchar to varchar(512) for snappydata
				if(gfColumnType.equalsIgnoreCase("long varchar")){
					createTable.append("\n    ").append(columnName).append(" ").append("VARCHAR");
				}else	
					createTable.append("\n    ").append(columnName).append(" ").append(gfColumnType);
				gpcolumnType=Mapper.Gemfire2GP(gfColumnType);
				if (Mapper.getParameter(gpcolumnType) == 1) {
						createTable.append("(").append(columnLen).append(")");

				} else if (Mapper.getParameter(gpcolumnType) == 2) {
					
					columnDecimal = column.getDecimalLen();
					//the default len of numeric is 131089
					
					//make a concession in gemfire, as the default precision and scale are different from GP.
					//By default, the two value of GP is infinite, precison in GF is 5 and scale is 0;
					//Now make the default as below
					//precision=40, scale=10
					
					if(specialDT.contains(gfColumnType)){
						if(columnLen==40)//the default len of numberic is 131089
							columnLen=precision;
						if(columnDecimal==0)
							columnDecimal=scale;
					}
					
					//if(oldcolumnType.equalsIgnoreCase("NUMERIC")&&columnLen==131089)
						//log.info(columnName+",NUMERIC with default length 131089");
					//else
						createTable.append("(").append(columnLen).append(",").append(columnDecimal).append(")");

				}else if(Mapper.getParameter(gpcolumnType) == 0){
					if(gfColumnType.equalsIgnoreCase("long varchar"))
						createTable.append("(512)");
				}
				
				
				if (flag.equals("NO"))
					createTable.append(" NOT NULL");
				
				if(defaultValue!=null&&!defaultValue.trim().equals("")&&!defaultValue.equalsIgnoreCase("GENERATED_BY_DEFAULT")){
					//GENERATED_BY_DEFAULT is a default value, ignore such case
					List<String> columnList=identityMap.get(tableName.toUpperCase());
					if(columnList!=null&&!columnList.contains(columnName.toUpperCase())){
							createTable.append(" default ").append(defaultValue);
					}		
				}

				createTable.append(",");
				// System.out.println("	"+column.getName()+"	"+column.getType()+"	"+column.getLen()+"	"+column.getFlag()+"\t"+column.getDecimalLen());
				//ver doesn't need to be part of primary in gf
				if (column.isPrimary()){
					primaryKeyList.add(columnName);
				}
					
			}
			String primaryKey="";
			if (!primaryKeyList.isEmpty()) {
				if(primaryKeyList.contains("client_id"))
					primaryKey+="client_id,";
				for(int i=0;i<primaryKeyList.size();i++){
					tmpKey=primaryKeyList.get(i);
					if(!tmpKey.equals("client_id"))
						primaryKey+=tmpKey+",";
				}
				primaryKey = primaryKey.substring(0, primaryKey.length() - 1);//remove the last comma of the primary key
				//log.info("primary key is " + primaryKey+"\n");
				//createTable.append("\n    PRIMARY KEY (").append(primaryKey).append(")");
			} else {
				//createTable.deleteCharAt(createTable.length() - 1);//remove the last comma of the statement
			}
			

				
				int type=typeMap.get(tableName.toUpperCase());
				if(type==2){
					createTable.append("\n    PRIMARY KEY (").append(primaryKey).append(")");
					createTable.append("\n)\nUSING ROW\n");
					createTable.append("OPTIONS(");
					if(!dsKey.isEmpty())
						createTable.append("\n    ").append(dsKey).append(",\n");
					createTable.append("    EVICTION_BY 'LRUHEAPPERCENT',\n");
					createTable.append("    PERSISTENCE 'ASYNCHRONOUS',\n");
					createTable.append("    DISKSTORE 'DISK_STORE',\n");
					createTable.append("    OVERFLOW  'true'\n);\n");
				}else if(type==1){
					createTable.deleteCharAt(createTable.length()-1);
					createTable.append("\n)\nUSING COLUMN\n");
					createTable.append("OPTIONS(");
					if(!dsKey.isEmpty())
						createTable.append("\n    ").append(dsKey).append(",\n");
					createTable.append("    KEY_COLUMNS '").append(primaryKey).append("',\n");
					createTable.append("    DISKSTORE 'DISK_STORE'\n);\n");
					
				}else if(type==3){
					createTable.append("\n    PRIMARY KEY (").append(primaryKey).append(")");
					createTable.append("\n)\nUSING ROW\nOPTIONS(\n    DISKSTORE 'DISK_STORE'\n);\n");
				}
				
		
			//createTable.append("\nREDUNDANCY 1");
			//createTable.append("\nEVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW PERSISTENT 'DISK_STORE';\n");
			//createTable.append("call sys.create_all_buckets('").append(schema).append(".").append(tableName).append("');\n");
				
			return createTable.toString();
		}
	
	public String generateSchema(Table table) {
		
		return generateSchema(null,table,false);

	}
	
	public List<Integer> getClientList(){
		
		List<Integer> clientList= new ArrayList<Integer>();
		
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		int clientId=0;
		String sql="select distinct client_id from ods.clients";
		try{
			conn=dataSource.getConnection();
			ps= conn.prepareStatement(sql);
			rs=ps.executeQuery(); 
			while(rs.next()){
				clientId=rs.getInt(1);
				clientList.add(clientId);
			}
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
		
		return clientList;
		
		
		
		
	}
	
	public List<Integer> getClientList(String tableName){
		String schemaName="ods";
		
		List<Integer> clientList= new ArrayList<Integer>();
		
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		int clientId=0;
		String sql="select distinct client_id from "+schemaName+".\""+tableName+"\"";
		try{
			conn=dataSource.getConnection();
			ps= conn.prepareStatement(sql);
			rs=ps.executeQuery(); 
			while(rs.next()){
				clientId=rs.getInt(1);
				clientList.add(clientId);
			}
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
		
		return clientList;
		
	}
	
	
public int[] executeMultipleQuery(String sql){
		
		
		//long start=System.currentTimeMillis();
		Connection conn=null;
		PreparedStatement ps=null;
		String single=null;
		int[] results=null;
		
		try{
			
			conn=dataSource.getConnection();
			int isolationLevel=conn.getTransactionIsolation();
			switch(isolationLevel){
			case 0: System.out.println("TRANSACTION_NONE"); break;
			case 1: System.out.println("TRANSACTION_READ_COMMITTED"); break;
			case 2: System.out.println("TRANSACTION_READ_UNCOMMITTED"); break;
			case 3: System.out.println("TRANSACTION_REPEATABLE_READ"); break;
			case 4: System.out.println("TRANSACTION_SERIALIZABLE"); break;	
			}
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			conn.setAutoCommit(false);
			isolationLevel=conn.getTransactionIsolation();
			switch(isolationLevel){
			case 0: System.out.println("TRANSACTION_NONE"); break;
			case 1: System.out.println("TRANSACTION_READ_COMMITTED"); break;
			case 2: System.out.println("TRANSACTION_READ_UNCOMMITTED"); break;
			case 3: System.out.println("TRANSACTION_REPEATABLE_READ"); break;
			case 4: System.out.println("TRANSACTION_SERIALIZABLE"); break;	
			}
			String[] sqls=sql.split(";");
			int count=sqls.length;
			results= new int[count];
			for(int i=0;i<count;i++){
				single=sqls[i];
				ps=conn.prepareStatement(single);
				//ps.setQueryTimeout(60*40);
				long start=System.currentTimeMillis();
				results[i]=ps.executeUpdate();
				long end=System.currentTimeMillis();
				log.info(single+"\n using time "+(end-start));
				
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
	
	public void deleteRecords(List<String> pkList,String tableName,String clientId){
		StringBuilder sb= new StringBuilder("select ");
		String sql="delete from "+tableName+" where ";
		Connection conn=null;
		int batch=50000;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Statement st=null;
		for(String c:pkList){
			sb.append(c).append(",");
		}
		
		//remove the last comma
		sb.deleteCharAt(sb.length()-1);
		sb.append(" from ").append(tableName).append(" where client_id=").append(clientId);
		try{
			conn=dataSource.getConnection();
			ps=conn.prepareStatement(sb.toString());
			rs=ps.executeQuery();
			int count=0;
			st=conn.createStatement();
			/*
			List<Integer> list= new ArrayList<Integer>();
			list.add(7316892);
			list.add(7517354);
			list.add(7315536);
			list.add(7504472);
			list.add(7439158);
			list.add(7435881);
			list.add(7587640);
			list.add(7549785);
			list.add(7575097);
			Iterator<Integer> iterator=list.iterator();
			*/
			while(rs.next()){
				
				if(count>=batch){
					long start=System.currentTimeMillis();
					conn.setAutoCommit(false);
					conn.commit();
					long end=System.currentTimeMillis();
					count=0;
					log.info("delete "+batch+" records"+",using time "+(end-start));
				}
				
				sb= new StringBuilder(sql);
				//sb.append(" CLIENT_ID=8006 and").append(" PRSN_EVNT_ID=").append(iterator.next());
				
				for(String c:pkList){
					sb.append(c).append("=").append(rs.getInt(c)).append(" and ");
				}
				sb.delete(sb.length()-5, sb.length()-1);
				sb.append(";");
				
				//log.info(sb);
				st.addBatch(sb.toString());
				count++;
			}
			
			if(count>0){
				long start=System.currentTimeMillis();
				conn.setAutoCommit(false);
				conn.commit();
				long end=System.currentTimeMillis();
				log.info("delete "+count+" records"+",using time "+(end-start));
			}
			
		
		}catch(Exception e){
			log.info("error",e);
		}finally{
			try{
				if(ps!=null)
					ps.close();
				if(st!=null)
					st.close();
				if(conn!=null)
					conn.close();	
			}catch(Exception e){
				
				log.error("close connection error",e);
			}
			
			
		}
		
		
	}

	
	public Map<String,Map<Integer,String>> getIndex(String schema,String table){
		Set<String> set= new HashSet<String>();
		Map<String,Map<Integer,String>> map=new HashMap<String,Map<Integer,String>>();
		
		Connection conn=null;
		ResultSet rs=null;
		try{
			conn=dataSource.getConnection();
			DatabaseMetaData meta=conn.getMetaData();
			rs=meta.getIndexInfo(null, schema, table, false, false);
			while(rs.next()){
				String indexName=rs.getString("INDEX_NAME");
				int position=rs.getInt("ORDINAL_POSITION");
				String columnName=rs.getString("COLUMN_NAME");
				Map<Integer,String> tmpMap=map.get(indexName);
				if(tmpMap==null){	
					tmpMap= new HashMap<Integer,String>();
				}
				tmpMap.put(position, columnName);
				map.put(indexName, tmpMap);
				set.add(indexName);
			}
		}catch(Exception e){
			
			log.error("error",e);
			
			
		}finally{
			try{
				conn.close();
			}catch(Exception e){
				log.error("close connection error",e);
			}
			
		}
		
		//return set;
		return map;
		
		
		
		
		
		
		
	}
	
	
	
	public List<String> getSchemaList(){

			ArrayList<String> list = new ArrayList<String>();
			ResultSet schemas = null;
			String name;
			Connection conn=null;
			try {
				conn=dataSource.getConnection();
				DatabaseMetaData dbMeta = conn.getMetaData();

				schemas = dbMeta.getSchemas();
				//schemas=dbMeta.getCatalogs();

				while (schemas.next()) {
					name = schemas.getString("TABLE_SCHEM");
					list.add(name);
					
				}

			} catch (Exception e) {

				log.error("error",e);

			} finally {

				try {
					schemas.close();
					conn.close();

				} catch (Exception ex) {

					log.error("error",ex);
				}
			}
		
		return list;
		
		
	}
	
	
	public String getDS(String schema,String table){
		String res= null;
		
		ResultSet rs = null;
		Connection conn=null;
		try {
			conn=dataSource.getConnection();
			String sql="select resolver from sys.SYSTABLES where tableschemaname=? and tablename=?";
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1, schema);
			ps.setString(2, table);
			rs=ps.executeQuery();
			while(rs.next()){
				res=rs.getString(1);
			}

		} catch (Exception e) {

			log.error("error",e);

		} finally {

			try {
				rs.close();
				conn.close();

			} catch (Exception ex) {

				log.error("error",ex);
			}
		}
	
	return res;
		
	}

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
			
			String tableName, columnName, gpcolumnType, gfColumnType, flag,tmpKey,defaultValue;
			int columnLen, columnDecimal;
			tableName = table.getName();
			//tableName=tableName.toLowerCase();
			
			
			//it is used for static get table identity
			//Map<String,List<String>> identityMap=TableUtil.getTableIdentity();
			
			// it is used for dynamic generating table identity
			Map<String,List<String>> identityMap=new HashMap<String,List<String>>();
			List<String> indentityColumn= new ArrayList<String>();
			List<String> seqList=gpDaoImp.getSequences(schema.toLowerCase(),tableName.toLowerCase());
			for(int i=0;i<seqList.size();i++){
				String sequence=seqList.get(i);
				String column=sequence.substring(tableName.length()+1, sequence.length()-4);
				//System.out.println(column);
				indentityColumn.add(column.toUpperCase());
			}
			identityMap.put(tableName.toUpperCase(), indentityColumn);
			
			
			List<String> dsList=gpDaoImp.getDistributedKey(schema,tableName);
			String dsKey="";
			for(String c:dsList)
					dsKey+=c+",";
			if(dsKey==null||dsKey.trim().equals("")){
				log.error(tableName+" doesn't have any distributed key in GP");
				dsKey=getDS(schema.toUpperCase(),tableName.toUpperCase());
				dsKey+=",";
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
				
		
			StringBuilder createTable = new StringBuilder("\ndrop table if exists ");
			
			if(schema!=null&&!schema.equals("")){
				createTable.append(schema).append(".");
			}
			
			// add doubel quoto for gemfire, becasue if the table contains reserved key word, there will be an error
					//for example, a table named check
			//if(underScore)
				//create the duplicated table with _
				//createTable.append("\"").append(tableName).append("_1\"").append("(");
			//else
			createTable.append("\"").append(tableName).append("\"").append(";\n");
			createTable.append("Create table ");
			if(schema!=null&&!schema.equals("")){
				createTable.append(schema).append(".");
			}
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
				gfColumnType = column.getType();
				columnLen = column.getLen();
				flag = column.getFlag();
				defaultValue=column.getDefaultValue();
				
				//Some column name has space in it. such as Provider Network Team
				//fix bug EPP-152
				if(columnName.contains(" "))
					columnName="\""+columnName+"\"";
				
				createTable.append("\n    ").append(columnName).append(" ").append(gfColumnType);
				gpcolumnType=Mapper.Gemfire2GP(gfColumnType);
				if (Mapper.getParameter(gpcolumnType) == 1) {
					
						createTable.append("(").append(columnLen).append(")");
	
				} else if (Mapper.getParameter(gpcolumnType) == 2) {
					
					columnDecimal = column.getDecimalLen();
					//the default len of numeric is 131089
					
					//make a concession in gemfire, as the default precision and scale are different from GP.
					//By default, the two value of GP is infinite, precison in GF is 5 and scale is 0;
					//Now make the default as below
					//precision=40, scale=10
					
					if(specialDT.contains(gfColumnType)){
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
				
				if(defaultValue!=null&&!defaultValue.trim().equals("")&&!defaultValue.equalsIgnoreCase("GENERATED_BY_DEFAULT")){
					//GENERATED_BY_DEFAULT is a default value, ignore such case
					List<String> columnList=identityMap.get(tableName.toUpperCase());
					if(!columnList.contains(columnName.toUpperCase())){
							createTable.append(" default ").append(defaultValue);
					}
						
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
				
				
				//partition by primary key
				createTable.append("\nPARTITION BY PRIMARY KEY");
				
				/*
				if(dsKey.contains("PARTITION BY"))
					createTable.append("\n").append(dsKey);
				else
					createTable.append("\nPARTITION BY COLUMN (").append(dsKey).append(")");
				*/
	
				/*
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
				*/
						
			if(underScore){
					createTable.append("\nASYNCEVENTLISTENER(ODSEventListener)");
					createTable.append("\nEVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW 'DISK_STORE_1' ASYNCHRONOUS;\n");
			}else{
					//createTable.append("\nREDUNDANCY 1");
					//createTable.append("\nASYNCEVENTLISTENER(ODSEventListener)");
					//createTable.append("\nEVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW PERSISTENT 'DISK_STORE' ASYNCHRONOUS;\n");
					//snappydata
					createTable.append("\nEVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW PERSISTENT 'DISK_STORE';\n");
					createTable.append("call sys.create_all_buckets('").append(schema).append(".").append(tableName).append("');\n");
			}	
			return createTable.toString();
		}
	

	/*
	public int update(String table,String oldColumn,String newColumn, List<String> pKlist){
		
		final String schema="ODS.";
		
		long count=0;
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Iterator<String> iterator=pKlist.iterator();
		StringBuilder select= new StringBuilder("select ");
		StringBuilder update= new  StringBuilder("update ");
		update.append(schema).append(table).append(" set ").append(newColumn).append("=? where ");
		while(iterator.hasNext()){
			select.append(iterator.next());
			select.append(",");
			update.append(iterator.next());
			update.append("=? and ");
			
		}
		
		select.append(oldColumn).append(" from ").append(schema).append(table).append(" where ").append(oldColumn).append(" is not null");
		update.delete(update.length()-7, update.length());
		
		try{
			conn=dataSource.getConnection();
			ps= conn.prepareStatement(select.toString());
			rs=ps.executeQuery(); 
			while(rs.next()){
				clientId=rs.getInt(1);
				clientList.add(clientId);
			}
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
		
		
	}

*/
}
