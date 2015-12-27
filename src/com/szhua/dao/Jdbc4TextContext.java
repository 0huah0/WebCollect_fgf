package com.szhua.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.szhua.pojo.TextContextPOJO;
import com.szhua.util.DateUtil;	

public class Jdbc4TextContext extends AccessJdbc {
	
	/**
	 * 按照传入的条件在textcontext中查询记录count
	 * @param cs conditionstr like 'a=12 and b=0'
	 * @return 记录count int
	 */
	private int getCountBy(String cs) {
		String countsql = "select count(*) as ct from textcontext where "+cs;
		
		int count = 99999;
		try {
			ResultSet rs = conn.createStatement().executeQuery(countsql);
			if(rs!=null){
				rs.next();
				count = rs.getInt("ct");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(countsql+"==>count="+count);
		return count;

	}	
	
	/**
	 * 保存单个textcontext
	 * @param text textcontext
	 * @return textcontext
	 */
	protected TextContextPOJO saveTextContext(TextContextPOJO text) {
		
		if(text.getTitleUrl()==null || getCountBy("titleUrl='"+text.getTitleUrl()+"';")<1){
			
			String insertsql = "insert into TextContext ("
					+ "fromUrl,title,titleUrl,context,imgLocUrl,imgUrl,visitTimes,status,type,gettedDt,publishDt) values('"
					+ text.getFromUrl() + "','" + text.getTitle() + "','"
					+ text.getTitleUrl() + "','" + text.getContext() + "','"
					+ text.getImgLocUrl() + "','" + text.getImgUrl()
					+ "',"+text.getVisitTimes()+",1,"+text.getType()+",now(),"+
					(text.getPublishDt()==null?null:("'"+DateUtil.formatDate(text.getPublishDt(),"yyyy-MM-dd HH:mm:ss")+"'"))
					+");";
			System.out.println(insertsql);
			
			try {
				conn.createStatement().execute(insertsql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("Exist title:"+text.getTitle());
		}
		
		return text;

	}

	/**
	 * 保存多个textcontext
	 * @param texts 
	 * @param fromUrl 来源的页面路径
	 */
	public void saveTextContexts(List<TextContextPOJO> texts,
			String fromUrl) {
		try {
			conn.setAutoCommit(false);
			for (TextContextPOJO text : texts) {
				text.setFromUrl(fromUrl);
				saveTextContext(text);
			}
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
