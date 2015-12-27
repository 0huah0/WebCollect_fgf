package com.szhua.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * MySQL JDBC URL的格式： jdbc:mysql://[hostname][:port]/[dbname][?param1=value1][&param2=value2]….
 * 
 * 例如： jdbc:mysql://localhost:3306/jdbc_db","root","1234"
 * 
 * 常见参数： <br/>
 * 
 * user 用户名 password 密码 autoReconnect 联机失败，是否重新联机（true/false） <br/>
 * 
 * maxReconnect 尝试重新联机次数 <br/>
 * 
 * initialTimeout 尝试重新联机间隔 <br/>
 * 
 * maxRows 传回最大行数 <br/>
 * 
 * useUnicode 是否使用Unicode字体编码（true/false） <br/>
 * 
 * characterEncoding 何种编码（GB2312/UTF-8/…）<br/>
 * 
 * relaxAutocommit 是否自动提交（true/false） <br/>
 * 
 * capitalizeTypeNames 数据定义的名称以大写表示<br/>
 */
public class MysqlJdbc {

	protected Connection conn = null;

	public MysqlJdbc() {
		try {
			Class.forName("com.mysql.jdbc.Driver"); // 加载驱动
		} catch (ClassNotFoundException e) {
			System.out.println("装载驱动包出现异常!请查正！");
			e.printStackTrace();
		}
		try {
			/**
			 * 建立jdbc连接，但要注意此方法的第一个参数， 如果127.0.0.1出现CommunicationsException异常， 可能就需要改为localhost才可以
			 **/
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/webc", "webc", "webc1111");
		} catch (SQLException e) {
			System.out.println("链接数据库发生异常!");
			e.printStackTrace();
		}
	}
}
