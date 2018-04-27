/**
 * 
 */
package com.tmg.thread;

/**
 * @author Haojie Ma
 * @date Nov 2, 2015
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;




import org.springframework.util.StringUtils;

import com.tmg.Bean.Cache;
import com.tmg.Bean.Table;
import com.tmg.Bean.TableContainer;
import com.tmg.Util.FileUtil;
import com.tmg.core.Properties;
import com.tmg.greenplum.DAOImp.GreenplumDAOImp;

public class ImportThread implements Runnable{
	
	private static Logger log=Logger.getLogger(ImportThread.class);
	
	@Autowired
	@Qualifier("GreenplumDAO")
	private GreenplumDAOImp gpDAOImp;
	
	private TableContainer tableContainer;
	private String url;
	private Cache<String,Table> drCache;
	
	public ImportThread(){
			
	}
	
	public ImportThread(TableContainer tableContainer,String url,Cache<String,Table> drCache){
		this.tableContainer=tableContainer;
		this.url=url;
		this.drCache=drCache;
	}
	
	public void setgpDAOImp(GreenplumDAOImp gpDAOImp){
		this.gpDAOImp=gpDAOImp;
	}


	public TableContainer getTableContainer() {
		return tableContainer;
	}

	public void setTableContainer(TableContainer tableContainer) {
		this.tableContainer = tableContainer;
	}

	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	
	
	
	
	public Cache<String, Table> getDrCache() {
		return drCache;
	}

	public void setDrCache(Cache<String, Table> drCache) {
		this.drCache = drCache;
	}

	public void run(){
		
		String tableName,extTableName;
		final String env="DR";
		String ext=Properties.getProperty("tmg.gpfdist.file.extension");
		String path=Properties.getProperty("tmg.gpfdist.dir");
		String backupPath=Properties.getProperty("tmg.gpfdist.processed.dir");
		gpDAOImp.setEnv(env);
		while(true){
			tableName=tableContainer.getTableFromQueue();
			if(tableName==null){
				/*try{
					Thread.sleep(2000);
					log.info(Thread.currentThread().getName()+" is going to sleep for 2 seconds");
					
				}catch(Exception e){
					log.info("I am in touble, I cannot sleep");
				}
				continue;
				*/
				break;
			}
			
			int index=tableName.indexOf(".");
			int len=tableName.length();
			String schema=tableName.substring(0, index);
			String tableNameWithoutSchema=tableName.substring(index+1, len);
			Table table=drCache.get(tableName);
			if(table==null){
				table=gpDAOImp.getTableMetaData(schema, tableNameWithoutSchema);
				drCache.put(tableName, table);
			}
			
			String tsColumn=FileUtil.getTSColumn(table);
			extTableName=tableName+"_extSpecial";
			StringBuilder sql=new StringBuilder();
			
			if(StringUtils.isEmpty(tsColumn)){
				log.warn(tableName+" doesn't have any timestamp columns, it will be truncated and reload again");
				sql.append("truncate table ").append(tableName).append(";");
				
			}
			sql.append("CREATE EXTERNAL TABLE ").append(extTableName);
			sql.append(" (like ").append(tableName).append(")\n");
			sql.append("LOCATION ( '").append(url).append("/").append(tableName).append(ext).append("' )\n");
			sql.append("FORMAT 'TEXT' ( DELIMITER ',' NULL '')").append(";\n");
			
			
			sql.append("insert into ").append(tableName).append(" select * from ").append(extTableName).append(";\n");
			
			sql.append("drop EXTERNAL TABLE ").append(extTableName).append(";\n");
			
			//System.out.println(sql);
			long start=System.currentTimeMillis();
			int[] results=gpDAOImp.executeMultipleQuery(sql.toString());
			long end=System.currentTimeMillis();
			
			long used=end-start;
			
			
			StringBuilder sb= new StringBuilder();
			if(results!=null){
				for(int i=0;i<results.length;i++){
					sb.append(results[i]).append(",");
				}
			}
			
			
			log.info(Thread.currentThread().getName()+":"+tableName+",using time:"+used+";results:"+sb);

				/*
				String date=FileUtil.getDate(0);
				File to=new File(backupPath+tableName+"."+date+ext);
				Files.move(from.toPath(), to.toPath());
				*/
			String remove="false";
			remove=Properties.getProperty("tmg.backup.flatfile");
			if(remove.equals("true"))
				compressFile(tableName);
			try{
				File from= new File(path+tableName+ext);
				from.delete();
			}catch(Exception e){
				log.info("delete file error, file name"+tableName,e);
				
			}
			 
		
		}
		
		
		
	}
	
	
	private void compressFile(String tableName){
		
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
	
	
	

}


