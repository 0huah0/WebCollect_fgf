package com.szhua.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class AccessJdbc {

	protected Connection conn = null;

	public AccessJdbc() {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");   // 加载驱动
		} catch (ClassNotFoundException e) {
			System.out.println("装载驱动包出现异常!请查正！");
			e.printStackTrace();
		}
		 
		try {
	        String dbur1 = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=F:\\workspace\\WebCollect_fgf\\sql\\fgf.mdb";
	        Properties pro = new Properties();
	        pro.setProperty("charSet","GB2312");
	        pro.setProperty("username","szhua");
	        pro.setProperty("password","1");
	        conn = DriverManager.getConnection(dbur1, pro); 
		} catch (SQLException e) {
			System.out.println("链接数据库发生异常!");
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws Exception {  
		Connection conn = new AccessJdbc().conn;
		Statement stmt = null;  
		 ResultSet rs = null;  
		 	
		stmt = conn.createStatement();  
        rs = stmt.executeQuery("select ID,name from users");
        
        while (rs.next()) {  
            System.out.print("[" + rs.getInt("ID"));  
            System.out.print("\t" + rs.getString("name"));  
        }  
    }  
}
