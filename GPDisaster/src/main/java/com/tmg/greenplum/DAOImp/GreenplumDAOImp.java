/**
 * 
 */
package com.tmg.greenplum.DAOImp;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import com.tmg.Bean.Column;
import com.tmg.Bean.LightTable;
import com.tmg.Bean.Table;
import com.tmg.Util.FileUtil;
import com.tmg.core.Properties;
import com.tmg.greenplum.DAO.CommonDAO;
import com.tmg.thread.CloseStatement;

/**
 * @author Haojie Ma
 * @date Sep 10, 2015
 */
public class GreenplumDAOImp implements CommonDAO {

	private static Logger log = Logger.getLogger(GreenplumDAOImp.class);

	@Autowired
	@Qualifier("dataSourceGreenplum")
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	@Autowired
	@Qualifier("dateSourceGreenplumDR")
	private DataSource dataSourceDR;

	public void setdataSourceDR(DataSource dataSourceDR) {
		this.dataSourceDR = dataSourceDR;
	}

	private String env;
	
	
	

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public int executeSql(String sql) {

		if (sql == null)
			return 0;
		int count = -1;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			count = ps.executeUpdate();

		} catch (Exception e) {

			log.error("", e);
		} finally {

			try {

			} catch (Exception e) {

				log.error("close connection error", e);
			}

		}

		return count;

	}

	public List<String> getPrimaryKey(String schemaName, String tableName) {

		ResultSet rs = null;
		Connection conn = null;
		List<String> primaryList = new ArrayList<String>();
		tableName = tableName.toLowerCase();
		try {
			conn = dataSource.getConnection();
			DatabaseMetaData dbmeta = conn.getMetaData();

			rs = dbmeta.getPrimaryKeys(null, schemaName, tableName);
			while (rs.next()) {
				// String tName=rsKey.getString("TABLE_NAME");
				String pri = rs.getString("COLUMN_NAME").toLowerCase();
				primaryList.add(pri);
			}
			
			
		} catch (Exception e) {

			log.info("Exception:", e);

		} finally {

			try {
				if (rs != null)
					rs.close();
				conn.close();
			} catch (Exception e) {
				log.error("Close connection error", e);
			}

		}


		return primaryList;

	}


	public Map<String, String> getColumnTypeMap(String schemaName,
			String tableName) {
		// greenplum doesn't recognize big case letter
		// gemfire doesn't recognize low case letter
		long start = System.currentTimeMillis();
		schemaName = schemaName.toLowerCase();
		log.debug("getColumnTypeMap function,tableName:" + tableName);
		String columnName, columnType;
		Map<String, String> columnTypeMap = new HashMap<String, String>();
		ResultSet rs = null;// get column meta data

		Connection conn = null;
		try {

			conn = dataSource.getConnection();
			DatabaseMetaData dbmeta = conn.getMetaData();

			rs = dbmeta.getColumns(null, schemaName, tableName, "%");
			if (rs == null) {
				log.info("No such table " + tableName);
				return null;
			}
			while (rs.next()) {
				columnName = rs.getString("COLUMN_NAME").toLowerCase();// column name
				columnType = rs.getString("TYPE_NAME").toLowerCase();// type name
				columnTypeMap.put(columnName, columnType);
			}
		} catch (Exception e) {
			log.error("error", e);
			
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				log.error("error", e);
			}

		}

		long end = System.currentTimeMillis();
		long time = end - start;
		log.debug("getColumnTypeMap,GP, time used:" + time);
		return columnTypeMap;

	}

	public ArrayList<String> getTables(String schema) {

		ArrayList<String> list = new ArrayList<String>();
		ResultSet tables = null;
		String tableName;
		Connection conn = null;
		try {
			if(env!=null)
				conn=dataSourceDR.getConnection();
			else
				conn=dataSource.getConnection();
			DatabaseMetaData dbMeta = conn.getMetaData();
			String[] types = { "TABLE" };
			tables = dbMeta.getTables(null, schema, "%", types);
			String ignoreTable=Properties.getProperty("tmg.gp.ignore.table");
			List<String> ignoreList=FileUtil.string2List(ignoreTable);
			int size=ignoreList.size();
			while (tables.next()) {
				tableName = tables.getString(3);
				boolean flag=false;
				for(int i=0;i<size;i++){
					if(tableName.matches(ignoreList.get(i))){
						flag=true;
						break;
					}
				}
				
				if(flag)
					continue;
				if (list.isEmpty()) {
					list.add(tableName);
				} else {// exclude the partition tables
					int count = 0;
					for (int i = 0; i < list.size(); i++) {

						if (tableName.length() > list.get(i).length()) {
							String tmp=list.get(i);
							if (tableName.substring(0, tmp.length()).equals(tmp)) {
								if(tableName.matches(tmp+"_[0-9]{1,}_prt.*"))
									break;
							}
						}
						count++;
					}
					if (count == list.size())
						list.add(tableName);
					
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
	
	public long getLastProcessTime(){
		
		long last=0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			String sql="select max(file_generate_time) from ods.file_process_history";
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				last=rs.getLong(1);
			}
			
		} catch (Exception e) {

			log.error("", e);
		} finally {

			try {

			} catch (Exception e) {

				log.error("close connection error", e);
			}

		}

		return last;
		
		
	}
	
	
	public Table getTableMetaData(String schemaName, String tableName) {
		long start=System.currentTimeMillis();
		Table table = new Table(tableName);
		table.setName(tableName);
		List<String> primaryList = new ArrayList<String>();
		List<Column> columnList = new ArrayList<Column>();
		String columnName;
		String columnType;
		int columnLen;
		int decimalLen = -1;
		String flag;
		ResultSet rs = null;// get column meta data
		ResultSet rsKey = null;// for primary key
		
		//igmore all the columns when generating the tables
		//String columns=Properties.getProperty("tmg.gp.ignore.column");
		//List<String> ignoreColumnList=FileUtil.string2List(columns);
		
		Connection conn=null;
		try {

			if(env!=null)
				conn=dataSourceDR.getConnection();
			else
				conn=dataSource.getConnection();
			DatabaseMetaData dbmeta = conn.getMetaData();

			rsKey = dbmeta.getPrimaryKeys(null, schemaName, tableName);
			while (rsKey.next()) {
				String pri = rsKey.getString("COLUMN_NAME");
				primaryList.add(pri);;
			}

			rs = dbmeta.getColumns(null, schemaName, tableName, "%");
			if(rs==null){
				log.info("No such table "+tableName);
				return null;
				
			}
			while (rs.next()) {
				columnName = rs.getString("COLUMN_NAME");
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

				Column column = new Column(columnName, columnType, columnLen,
						flag, decimalLen);
				if (primaryList.contains(columnName))
					column.setPrimary(true);

				columnList.add(column);
			}

			table.setCloumns(columnList);
			
			List<String> dsList=getDistributedKey(schemaName,tableName);
			table.setDsList(dsList);

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
		log.debug(env+",getTableMetaData,tableName:"+tableName+", time used:"+time);
		return table;

	}
	
	
	
	public LightTable getLightTable(String schemaName, String tableName) {
		long start=System.currentTimeMillis();
		LightTable table = new LightTable(tableName);
		table.setTableName(tableName);
		List<String> columnList = new ArrayList<String>();
		String columnName;
		ResultSet rs = null;// get column meta data
		//igmore all the columns when generating the tables
		//String columns=Properties.getProperty("tmg.gp.ignore.column");
		//List<String> ignoreColumnList=FileUtil.string2List(columns);
		
		Connection conn=null;
		try {

			if(env!=null)
				conn=dataSourceDR.getConnection();
			else
				conn=dataSource.getConnection();
			DatabaseMetaData dbmeta = conn.getMetaData();


			rs = dbmeta.getColumns(null, schemaName, tableName, "%");
			if(rs==null){
				log.info("No such table "+tableName);
				return null;
				
			}
			while (rs.next()) {
				columnName = rs.getString("COLUMN_NAME");
				columnList.add(columnName);
			}

			table.setColumnList(columnList);
			

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

		long end=System.currentTimeMillis();
		long time=end-start;
		log.debug(env+",getTableMetaData,tableName:"+tableName+", time used:"+time);
		return table;

	}
	
	
	public ArrayList<String> getSequences(String schema) {

		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;
		String name;
		Connection conn=null;
		try {
			conn=dataSource.getConnection();
			DatabaseMetaData dbMeta = conn.getMetaData();
			String[] types = { "SEQUENCE" };

			rs = dbMeta.getTables(null, schema, "%", types);

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

	public long getCurrentSequenceId(String schema,String table){
		
		long seqId=0;
		ResultSet rs = null;
		Connection conn=null;
		try {
			conn=dataSource.getConnection();
			StringBuilder sql=new StringBuilder("select last_value from").append(schema).append(".").append(table);
			PreparedStatement ps=conn.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				seqId=rs.getLong("last_value");
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
		
		return seqId;
	}
	
	
	public boolean isPartitioned(String schema,String table) {
		//log.info("Check partition,"+table);
		long start=System.currentTimeMillis();
		boolean partition=false;
		ResultSet rs = null;
		Connection conn=null;
		try {
			conn=dataSource.getConnection();
			DatabaseMetaData dbMeta = conn.getMetaData();
			String[] types = { "TABLE" };

			rs = dbMeta.getTables(null, schema, table+"%", types);
			while (rs.next()) {
				String tableName=rs.getString(3);
				if(tableName.matches(table+"_[0-9]_prt_.*")){
					partition=true;
					break;
				}
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
		long end=System.currentTimeMillis();
		long used=end-start;
		log.debug("isPartitioned "+table+",using time: "+used);
		return partition;
	}
	
	//get all the tables including partitionedTable and tahle not partitioned
	public List<String> getPartitionedTable(String schema){
		
		ArrayList<String> list = new ArrayList<String>();
		//list not match .*_[0-9]{1,}_prt.*
		ArrayList<String> tmpList = new ArrayList<String>();
		//set contains the basic table of partitioned tables
		Set<String> set= new HashSet<String>();
		String[] tableNames;
		ResultSet tables = null;
		String tableName,basicTable;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			DatabaseMetaData dbMeta = conn.getMetaData();
			String[] types = { "TABLE" };
			tables = dbMeta.getTables(null, schema, "%", types);
			String ignoreTable=Properties.getProperty("tmg.gp.ignore.table");
			List<String> ignoreList=FileUtil.string2List(ignoreTable);
			int size=ignoreList.size();
			while (tables.next()) {
				tableName = tables.getString(3);
				boolean flag=false;
				for(int i=0;i<size;i++){
					if(tableName.matches(ignoreList.get(i))){
						flag=true;
						break;
					}
				}
				if(flag)
					continue;
				if(tableName.matches(".*_[0-9]{1,}_prt.*")){
					list.add(tableName);
					tableNames=tableName.split("_[0-9]{1,}_prt");
					basicTable=tableNames[0];
					set.add(basicTable);
					
				}else{
					tmpList.add(tableName);
					
					
					//isPartitioned() is too slow
					//if(!isPartitioned(schema,tableName))
						//list.add(tableName);
				}
			}
			
			//add the non-partitioned table into the list
			Iterator<String> iterator=tmpList.iterator();
			while(iterator.hasNext()){
				tableName=iterator.next();
				if(!set.contains(tableName))
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
	
	
	
	
	
	 public int[] executeMultipleQuery(String sql){
		
		
		//long start=System.currentTimeMillis();
		Connection conn=null;
		PreparedStatement ps=null;
		String single=null;
		int[] results=null;
		
		try{
			if(env!=null)
				conn=dataSourceDR.getConnection();
			else
				conn=dataSource.getConnection();
			conn.setAutoCommit(false);
			String[] sqls=sql.split(";");
			int count=sqls.length;
			results= new int[count];
			for(int i=0;i<count;i++){
				single=sqls[i];
				ps=conn.prepareStatement(single);
				//log.info(single);
				//ps.setQueryTimeout(60*40);
				results[i]=ps.executeUpdate();
			}
			
			conn.commit();
			
			
		}catch(Exception e){
			
			if(conn!=null)
				try {
					conn.rollback();
				} catch (SQLException e1) {
					log.info("Roll back error",e);
				}
			
			log.info("failed sql is "+single+",ENV:"+env,e);
			
			
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
	
	public long getRowCount(String tableName,String tsColumn,String from,String to){
		long count=0;
		
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		StringBuilder sql=null;
		
		try{
			sql=new StringBuilder("select count(1) from ").append(tableName);
			if(!StringUtils.isEmpty(tsColumn)&&!StringUtils.isEmpty(from)&&!StringUtils.isEmpty(to)){
				sql.append(" where ");
				sql.append(tsColumn).append(">='").append(from).append("'");
				sql.append(" and ");
				sql.append(tsColumn).append(" <'").append(to).append("'");
			}
			
			
			conn=dataSource.getConnection();
			ps=conn.prepareStatement(sql.toString());
			rs=ps.executeQuery();
			
			while(rs.next()){
				count=rs.getLong(1);
			}
			

			
				
		}catch(Exception e){
			
			log.info("failed sql:"+sql,e);
		}finally{
			
			try{
				
				rs.close();
				ps.close();
				conn.close();
			}catch(Exception e){
				
				
			}
			
		}
		
		
		return count;
		
	}
	 
public List<String> getDistributedKey(String schema,String table){
		
		List<String> dsKeyList=new ArrayList<String>();
		String dsKey;
		
		ResultSet rs=null;
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
		sql.append("\tORDER BY pgn.nspname,pgc.relname\n");
		sql.append(") as a where table_owner='").append(schema).append("'").append(" and table_name='").append(table).append("';");
		
		
		try{
			System.out.println(sql);
			conn=dataSource.getConnection();
			ps= conn.prepareStatement(sql.toString());
			rs=ps.executeQuery();
			while(rs.next()){
				dsKey=rs.getString("distribution_keys");
				dsKeyList.add(dsKey);
			}
				
		}catch(Exception e){
			
			log.error("",e);
			
		}finally{
			
			try{
				
				ps.close();
				if(rs!=null)
					rs.close();
				
				conn.close();
			}catch(Exception e){
				
				log.error("close conneciton error",e);
				
			}

		}

		return dsKeyList;
		
		
	}
	

}
