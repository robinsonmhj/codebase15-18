package com.tmg.gemfire.DAO;

import java.sql.SQLException;
import java.util.Map;




public interface CommonDAO {
	
	
	
	
	
	public int executeUpdate(String sql) throws SQLException;
	public Map<String,String> execute(String sql) throws SQLException;
	

}
