package com.tmg.greenplum.DAO;

/**
 * @author Haojie Ma
 * @date Sep 24, 2015
 */
public interface CommonDAO {
	
/*   public <T> void batchInsert(List<T> list);
   public <T> void batchUpdate(List<T> list);
   public <T> void batchUpInsert(List<T> list);
   public int executeSql(String sql);*/
   public int[] executeMultipleQuery(String sql);

}
