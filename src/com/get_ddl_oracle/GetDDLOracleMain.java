package com.get_ddl_oracle;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetDDLOracleMain {

	private Statement stmt = null;
	private Connection conn = null;

	public void findObjectOracle() {
		Connection connection = null; 
		String sql = "select OBJECT_NAME ,OBJECT_TYPE from ALL_OBJECTS where object_type in('TABLE','PROCEDURE','PACKAGE BODY', 'PACKAGE','FUNCTION','TRIGGER','VIEW','SEQUENCE') and owner = 'USER_OWNER_DATA_BASE'";
		try {
			connection = Conn.getConnection();
			stmt = connection.createStatement();
			
			ResultSet resultSet = stmt.executeQuery(sql);
			while (resultSet.next()) {
				
				String objectName = resultSet.getString("OBJECT_NAME");
				String objectType = resultSet.getString("OBJECT_TYPE");
				
				if(objectType.equalsIgnoreCase("PACKAGE BODY")){
					objectType = "PACKAGE_BODY";
				}
				
				getDDL(objectName, objectType);
			}
			System.out.println("Pronto!!!");
		} catch( SQLException e1 ) {
			e1.printStackTrace();
		}
		try {
			connection.close();
			connection.createStatement().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void getDDL(String objectName, String objectType){
		String sql = "select dbms_metadata.get_ddl('"+objectType+"', '"+objectName+"', 'USER_OWNER_DATA_BASE') as ddl from dual";
		try {
			Connection connection = Conn.getConnection();
			stmt = connection.createStatement();
			
			ResultSet resultSet = stmt.executeQuery(sql);
			while (resultSet.next()) {
				String table_ddl = resultSet.getString("ddl");
				
				try {
					
					String filetowrite = objectName+".sql";
					String directoryName = "C://SQL Developer//cockpit-database//"+objectType; 
					
					createFolderIfDoestExist(directoryName);
					FileWriter fw = new FileWriter(directoryName +"//" +filetowrite);
					fw.write(table_ddl);                    
                    fw.close();
				} catch (Exception e) {
				    System.err.println("File doesn't exist");
				    e.printStackTrace();
				}
			}
			
		} catch( SQLException e1 ) {
			e1.printStackTrace();
		}
		
	}
	
	private void createFolderIfDoestExist(String directoryName){
		File theDir = new File(directoryName);
		if (!theDir.exists()) {
		    System.out.println("creating directory: " + directoryName);
		    boolean result = false;

		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		    }        
		    if(result) {    
		        System.out.println("DIR created");  
		    }
		}
	}
}
