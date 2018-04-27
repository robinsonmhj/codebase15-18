/**
 * 
 */
package com.tmg.Action;



import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.tmg.gf.DAOImp.ProcedureDAOImp;
import com.tmg.gf.DAOImp.GemfireDAOImp;
import com.tmg.gf.DAOImp.GroupDAOImp;
import com.tmg.gf.DAOImp.PrivilegeDAOImp;
import com.tmg.gf.DAOImp.RequestHistoryDAOImp;
import com.tmg.gf.DAOImp.RoleDAOImp;
import com.tmg.gf.Model.GroupModel;
import com.tmg.gf.Model.PrivilegeModel;
import com.tmg.gf.Model.ProcedureModel;
import com.tmg.gf.Model.RequestHistoryModel;




/**
 * @author Haojie Ma
 * @date Jun 5, 2015
 */
public class Main {
	
	
	private static Logger log=Logger.getLogger(Main.class);
	
	
	private static AbstractApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
	private static PrivilegeDAOImp privilegeDAO;
	private static GroupDAOImp groupDAO;
	private static RequestHistoryDAOImp requestDAO;
	private static GemfireDAOImp gemfireDAO;
	private static ProcedureDAOImp procedureDAO;
	private static RoleDAOImp roleDAO;

	public static void main(String[] args) {
		
		
		privilegeDAO = context.getBean(PrivilegeDAOImp.class);
		groupDAO = context.getBean(GroupDAOImp.class);
		gemfireDAO = context.getBean(GemfireDAOImp.class);
		procedureDAO=context.getBean(ProcedureDAOImp.class);
		requestDAO=context.getBean(RequestHistoryDAOImp.class);
		roleDAO=context.getBean(RoleDAOImp.class);
		
		
		int len=args.length;
		
		for(int i=0;i<len;i++)
			log.info("parameter"+i+":"+args[i]);
		
		if(len!=3&&len!=2){
			System.out.println("=================================================================");
			System.out.println("Usage: java -jar GrantPermisson.jar userName,groupName,schemaName");//grant permission to new user
			System.out.println("\t\t\t\t\tOR\t\t\t\t\t\t\t");
			System.out.println("Usage: java -jar GrantPermisson.jar schemaName,tableName");//grant permission to new table
			System.out.println("=================================================================");
			return;
		}
		
		
		if(len==3){

			String userName=args[0];
			String groupName=args[1];
			String schemaName=args[2].replaceAll("'", "");
			grantPermission(userName,groupName,schemaName);
			
		}	
		if(len==2){
			String schemaName=args[0].replaceAll("'", "");
			String tableName=args[1].replaceAll("'", "");
			reGrant(schemaName,tableName);
		}
		
		
		
		context.close();
		
	}
	

	//userName is used to track request history, userName, groupName, schemaName and time
	//schemaName is useless, it is used to distinguish 2 paramters and 3 parameters
	public static void grantPermission(String userName,String groupName,String schemaName){
		
		//check if the group_name exists
		int groupId=groupDAO.getIdByName(groupName);
		if(groupId<=0){
			log.info("Group name:"+groupName+" doesn't exists!");
			System.out.println("Group "+groupName+" doesn't exists!\nAttention:groupName is case sensitive");
			return;
		}
		
		//check if the schema exists
		List<String> schemaList=gemfireDAO.getSchema();
		if(!schemaList.contains(schemaName.toUpperCase())){	
			System.out.println("No such schema "+schemaName);
			return;
			
		}
		
		
		//check if the user has already requested permission  on the schema with the same groupName,
		//users may change group_name, so only check the previous one
		String previousSchema=requestDAO.getPreviousSchemaByUserNameGroupName(userName, groupId);
		if(schemaName.toUpperCase().equals(previousSchema)){
			System.out.println("User "+userName+" already has the permission on schema "+schemaName);
			return;	
		}
		
		
		
		
		int roleId =privilegeDAO.getByGroupIdandSchemaName(groupId, schemaName);
		if(roleId<=0){
			System.out.println("Data may lost, please contact your system administrator");
			log.error("no roleId for group_id="+groupId+" and schemaName="+schemaName+",groupName="+groupName);
			return;
			
		}
		
		String privilege=roleDAO.getPrivilegeById(roleId);
		if(StringUtils.isEmpty(privilege)){
			System.out.println("Data may lost, please contact your system administartor");
			log.error("no privilege for roleId="+roleId+",group_id="+groupId+" and schemaName="+schemaName+",groupName="+groupName);
			return;
		}
		
		/*
		//CALL SYS.REFRESH_LDAP_GROUP('group_name');
		//make sure the ldap group is the latest;
		gemfireDAO.callProcedure("call SYS.REFRESH_LDAP_GROUP('"+groupName+"')");
		StringBuilder userSql=new StringBuilder(" to ldapGroup:");
		userSql.append(groupName);
		*/
		
		//use the user name to grant permission
		String[] users=userName.split(",");
		StringBuilder userSql=new StringBuilder(" to ");
		for(int i=0;i<users.length;i++){
			userName=users[i];
			if(!StringUtils.isEmpty(userName))
				userSql.append(userName).append(",");
		}
		userSql.deleteCharAt(userSql.length()-1);	
		
		
		StringBuilder sql=new StringBuilder("");
		if(privilege.contains("execute")){
				//execute should be in the last of the privilege list
				privilege=privilege.substring(0, privilege.indexOf("execute")-1);
				//grant execute permission on the function which belong to this schema 
				List<String> functionList=gemfireDAO.getFunctionListBySchemaName(schemaName);
				if(functionList!=null&&!functionList.isEmpty()){
					for(String function:functionList){
						sql.append("grant execute on function ");
						sql.append(schemaName).append(".").append(function);
						sql.append(userSql);
						sql.append(";\n");
					}
				}
				
				//grant permission to the user if the group has permissions to call the procedures
				String level=roleDAO.getRoleNameById(roleId);
				List<ProcedureModel> procedureList=procedureDAO.getProcedureListByLevel(level);
				if(procedureList!=null&&!procedureList.isEmpty()){
					//StringBuilder procedureSql=new StringBuilder("");
					for(ProcedureModel procedure:procedureList){
						sql.append("grant execute on procedure ");
						sql.append(procedure.getSchema_name()).append(".").append(procedure.getProcedure_name());
						sql.append(userSql);
						sql.append(";\n");
						
					}
					
					
				}
				
				
			}
			
			List<String> tableList=gemfireDAO.getTablesBySchema(schemaName, null);
			//StringBuilder grant= new StringBuilder("");
			for(String tableName:tableList){
				sql.append("grant ");
				sql.append(privilege).append(" on table ");
				sql.append(schemaName).append(".").append("\"").append(tableName).append("\"");
				sql.append(userSql);
				sql.append(";\n");
			}
			sql.delete(sql.lastIndexOf(";"), sql.length());
			try{
				int[] result=gemfireDAO.executeMultipleQuery(sql.toString());
				if(result==null){
					System.out.println("Grant permission error");
					return;
				}
				for(int i=0;i<users.length;i++){
					userName=users[i];
					if(StringUtils.isEmpty(userName))
						continue;
					RequestHistoryModel history= new RequestHistoryModel();
					history.setGroup_id(groupId);
					history.setUser_name(userName);
					history.setSchema_name(schemaName);
					requestDAO.insert(history);
				}
				
				log.info("successfully grant permisson on schema "+schemaName+",count="+result.length);
				System.out.println("successfully grant permisson on schema "+schemaName+",count="+result.length);
			}catch(Exception e){
				log.info("grant permisison error",e);
				log.info("failed sql is "+sql);
				System.out.println("Error when granting permission on procedures, please contact system administrator");
				return;
			}
			

		
	}
	
	
	public static void  reGrant(String schema,String table){
		
		List<String> schemaList=gemfireDAO.getSchema();
		if(!schemaList.contains(schema)){
			System.out.println("Schema "+schema+" doesn't exist!");
			return;
		}
		
		List<String> tableList=gemfireDAO.getTablesBySchema(schema, null);
		List<Integer> maxList=requestDAO.getMaxRequestIdBySchema(schema);
		if(!table.equals("*")){
			
			String[] tables=table.split(",");
			StringBuilder sql= new StringBuilder();
			for(int i=0;i<tables.length;i++){
				table=tables[i];
				if(!tableList.contains(table)){
					System.out.println("No such table "+table+" in schema "+schema);
					return;
				}
					
				List<GroupModel> groupList=groupDAO.getAll();
				for(GroupModel g:groupList){
					int groupId=g.getGroup_id();
					String groupName=g.getGroup_name();
					int roleId=privilegeDAO.getByGroupIdandSchemaName(groupId, schema);
					if(roleId<=0){
						System.out.println("Error,data may be lost. Contact your administor for assistance");
						log.error("no privilege for roleId="+roleId+",group_id="+groupId+" and schemaName="+schema+",groupName="+groupName);
						return;
					}
					
					List<String> userList=requestDAO.getLatestUserNameByGroupId(maxList, groupId);
					if(userList==null||userList.isEmpty()){
						log.info("No users found for groupName:"+groupName);
						continue;
					}
					
					String privilege=roleDAO.getPrivilegeById(roleId);
					if(privilege.contains("execute")){
						privilege=privilege.substring(0, privilege.indexOf("execute")-1);
					}
					
					sql.append("grant ");
					sql.append(privilege);
					sql.append(" on table ").append(schema).append(".");
					sql.append("\"").append(table).append("\"");
					sql.append(" to ");
					//use userName to grant permission
					for(String user:userList)
						sql.append(user).append(",");
					sql.deleteCharAt(sql.length()-1);//remove the last comma
					sql.append(";");
					//use ldap group to grant permission
					//sql.append(" to ldapGroup:").append(groupName);
					
					
					
					
				}
				
				
			}
			
			
			//gemfireDAO.callProcedure("call SYS.REFRESH_LDAP_GROUP('"+groupName+"')");
			int[] result=gemfireDAO.executeMultipleQuery(sql.toString());
			if(result==null){
				System.out.println("Granting permission error");
				return;
			}
					
			System.out.println("Grant permisison on table successfully");
			
			
			return;

		}
		
		List<GroupModel> groupList=groupDAO.getAll();
		for(GroupModel g:groupList){
			StringBuilder sql= new StringBuilder();
			int groupId=g.getGroup_id();
			String groupName=g.getGroup_name();
			int roleId=privilegeDAO.getByGroupIdandSchemaName(groupId, schema);
			if(roleId<=0){
				System.out.println("Error,data may be lost. Contact your administor for assistance");
				log.error("no privilege for roleId="+roleId+",group_id="+groupId+" and schemaName="+schema+",groupName="+groupName);
				continue;
			}
			List<String> userList=requestDAO.getLatestUserNameByGroupId(maxList, groupId);
			if(userList==null||userList.isEmpty()){
				log.info("No users found for groupName:"+groupName);
				continue;
			}
			
			String privilege=roleDAO.getRoleNameById(roleId);
			if(privilege.contains("execute")){
				privilege=privilege.substring(0, privilege.indexOf("execute")-1);
			}
			for(int i=0;i<tableList.size();i++){
				String tableName=tableList.get(i);
				sql.append("grant ");
				sql.append(privilege);
				sql.append(" on table ").append(schema).append(".");
				sql.append("\"").append(tableName).append("\"");
				sql.append(" to ");
				//use userName to grant permission
				for(String user:userList)
					sql.append(user).append(",");
				sql.deleteCharAt(sql.length()-1);//remove the last comma
				
				//use ldap group to grant permission
				//sql.append(" to ldapGroup:").append(groupName);
				sql.append(";\n");
			}
			sql.delete(sql.lastIndexOf(";"), sql.length());
			
			//gemfireDAO.callProcedure("SYS.REFRESH_LDAP_GROUP('"+groupName+"')");
			int[] result=gemfireDAO.executeMultipleQuery(sql.toString());
			if(result==null){
					System.out.println("Grant permission to group:"+groupName+"failed");
					continue;
			}
			System.out.println("Grant permisison to group:"+groupName+" successfully");
			
			
		}

		
	}

	public static void testHibernate() {

		groupDAO = context.getBean(GroupDAOImp.class);
		System.out.println("hello I am in test Hibernate");
		

		List<GroupModel> list = groupDAO.getAll();
		for (GroupModel p : list) {
			System.out.println(p);
		}

		List<PrivilegeModel> PrivilegeList = privilegeDAO.getAll();
		for (PrivilegeModel p : PrivilegeList) {
			System.out.println(p);
		}
		
		// close resources
		context.close();

	}
	
	
	public static boolean succeed(int[] result){
		
		for(int i=0;i<result.length;i++){
			if(result[i]==0){
				log.info("The "+i+" statement failed!");
				return false;
			}
				
		}
		
		return true;
	}
	
}