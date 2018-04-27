/**
 * 
 */
package com.tmg.thread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static java.nio.file.StandardCopyOption.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tmg.Bean.LightTable;
import com.tmg.Bean.MapContainer;
import com.tmg.Bean.Row;
import com.tmg.Bean.TableContainer;
import com.tmg.Util.FileUtil;
import com.tmg.core.Properties;
import com.tmg.greenplum.DAOImp.GreenplumDAOImp;


/**
 * @author Haojie Ma
 * @date Dec 18, 2015
 */
@Component
public class FlatFileCombine implements Runnable{
	
	@Autowired
	@Qualifier("GreenplumDAO")
	private  GreenplumDAOImp gpDaoImp;
	private Logger log=Logger.getLogger(FlatFileCombine.class);
	private MapContainer<String,String[]> map;
	private TableContainer tableContainer;
	
	public void setgpDaoImp(GreenplumDAOImp gpDaoImp){
		this.gpDaoImp=gpDaoImp;
	}
	
	public FlatFileCombine(){
		
		
	}
	
	public FlatFileCombine(MapContainer<String,String[]> map,TableContainer tableContainer){
		this.map=map;
		this.tableContainer=tableContainer;
		
	}
	
	
	
	
	
	public void run(){
		
		String fileName;
		int len;
		String tableName=null;
		String basePath=Properties.getProperty("tmg.flatfile.path");
		String processedPath=Properties.getProperty("tmg.flatfile.processed.path");
		
		
		while(true){
			boolean flag=true;//fist file, used to get tableName
			String[] fileList=map.getFromQueue();
			if(fileList==null)
				return;
			len=fileList.length;
			if(len==0)
				continue;
			List<Row> finalList= new ArrayList<Row>();
			for(int i=0;i<len;i++){
				fileName=fileList[i];
				if(flag){
					tableName=FileUtil.processfileName(fileName);
					flag=false;
				}
				List<Row> list=FileUtil.processFile(fileName);
				finalList.addAll(list);
				//log.info("processed file "+fileName);
				
				
				//move the file after processed
				try{
					String sourcePath=basePath+tableName+"/"+fileName;
					String targetPath=processedPath+fileName;
					File source=new File(sourcePath);
					File target=new File(targetPath);
					Files.move(source.toPath(), target.toPath(),REPLACE_EXISTING);
				}catch(Exception e){
					log.info("Move file error",e);
				}
				
			}
			
			
			write2File(finalList,tableName);
			tableContainer.add2Queue(tableName);
			
		}
		
		
	}
	
	private void write2File(List<Row> list, String tableName){
		Long start=System.currentTimeMillis();
		BufferedWriter writer=null;
		String schemaName=Properties.getProperty("tmg.flatfile.schma.name");
		String ext=Properties.getProperty("tmg.gpfdist.file.extension");
		try{
			String path=Properties.getProperty("tmg.flatfile.gpfdist.path");
			LightTable table=gpDaoImp.getLightTable(schemaName, tableName);
			List<String> columnList=table.getColumnList();
			writer= new BufferedWriter(new FileWriter(new File(path+tableName+ext)));
			
			Iterator<Row> itera=list.iterator();
			
			while(itera.hasNext()){	
				Row row=itera.next();
				String operator=row.getOperation();
				Map<String,String> map=row.getMap();
				int columnCount=columnList.size();
				for(int i=0;i<columnCount;i++){
					String key=columnList.get(i);
					String value=map.get(key);
					if(key.equals("ver")&&operator.equalsIgnoreCase("DELETE"))
						value="-"+value;
					else if(key.equals("del"))
						value="false";
					if(value==null){
						writer.write("null");
						//System.out.println("value is null "+tableName+",key="+key);
						
					}else
						writer.write(value);

					if(i!=(columnCount-1))
						writer.write(",");
				}
				writer.write("\n");
			}
		}catch(Exception e){
			
			log.info("",e);
			
		}finally{
			
			try{
				writer.close();
			}catch(Exception e){
				log.info("Close file error",e);
				
			}
			
		}
		long end=System.currentTimeMillis();
		long used=end-start;
		log.info(tableName+",write to file using "+used);
	}

	public MapContainer<String, String[]> getMap() {
		return map;
	}

	public void setMap(MapContainer<String, String[]> map) {
		this.map = map;
	}

	public TableContainer getTableContainer() {
		return tableContainer;
	}

	public void setTableContainer(TableContainer tableContainer) {
		this.tableContainer = tableContainer;
	}
	
	
	
	

}


