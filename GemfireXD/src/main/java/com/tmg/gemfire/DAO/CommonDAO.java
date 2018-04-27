package com.tmg.gemfire.DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tmg.gemfire.Bean.Table;

public interface CommonDAO {
	
	
	
	public String generateSchema(Table table);
	public ArrayList<String> getTables(String schema);
	public int executeUpdate(String sql) throws SQLException;
	public Map<Integer,Long> execute1(String sql) throws SQLException;
	public int execute(String sql) throws SQLException;
	public Table getTableMetaData(String schemaName, String tableName);

}
