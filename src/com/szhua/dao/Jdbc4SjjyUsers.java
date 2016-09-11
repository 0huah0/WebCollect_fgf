package com.szhua.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.szhua.pojo.SjjyUserDetailsPOJO;
import com.szhua.pojo.SjjyUserPOJO;

public class Jdbc4SjjyUsers extends AccessJdbc {
	
	private int getUserCountBy(String cs) {
		String countsql = "select count(*) as ct from sjjy_users where "+cs;
		int count = 99999;
		try {
			ResultSet rs = conn.createStatement().executeQuery(countsql);
			if(rs!=null && rs.next()){
				count = rs.getInt("ct");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(countsql+"==>count="+count);
		return count;
	}	
	
	private int getDetailCountBy(String cs) {
		String countsql = "select count(*) as ct from sjjy_users_details where "+cs;
		int count = 99999;
		try {
			ResultSet rs = conn.createStatement().executeQuery(countsql);
			if(rs!=null && rs.next()){
				count = rs.getInt("ct");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(countsql+"==>count="+count);
		return count;
	}	
	
	/**
	 * 保存单个SjjyUserPOJO
	 */
	protected SjjyUserPOJO saveUser(SjjyUserPOJO pojo) {
		
		if(pojo.getRealUid()==null || getUserCountBy("realUid='"+pojo.getRealUid()+"';")<1){
			String insertsql = "insert into sjjy_users(realUid,nickname,sex,marriage,height,education,income,work_location"
					+ ",work_sublocation,age,image,helloUrl,shortnote,status) "
					+ " values('"
					+ pojo.getRealUid() + "','" 
					+ pojo.getNickname() + "','" 
					+ pojo.getSex() + "','" 
					+ pojo.getMarriage() + "','" 
					+ pojo.getHeight()+ "','" 
					+ pojo.getEducation()+ "','" 
					+ pojo.getIncome()+ "','" 
					+ pojo.getWork_location()+ "','" 
					+ pojo.getWork_sublocation()+ "','" 
					+ pojo.getAge()+ "','" 
					+ pojo.getImage()+ "','" 
					+ pojo.getHelloUrl()+ "','" 
					+ pojo.getShortnote()+ "','" 
					+ pojo.getStatus() 
					+"');";
			System.out.println(insertsql);
			try {
				conn.createStatement().execute(insertsql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("Exist title:"+pojo.getRealUid());
		}
		return pojo;
	}

	/**
	 * 保存多个pojoconpojo
	 * @param pojos 
	 * @param fromUrl 来源的页面路径
	 */
	public void saveUsers(List<SjjyUserPOJO> pojos, String fromUrl) {
		try {
			conn.setAutoCommit(false);
			for (SjjyUserPOJO pojo : pojos) {
				saveUser(pojo);
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存单个SjjyUserPOJO
	 * @throws SQLException 
	 */
	protected SjjyUserDetailsPOJO saveUserDetails(SjjyUserDetailsPOJO pojo) throws SQLException {

		if(getDetailCountBy("realUid='"+pojo.getRealUid()+"';")<1){
			
			String insertsql = "insert into sjjy_users_details (realUid,member_dj,"
					+ "member_from,has_car,salary,has_house,weight,xz,mz,sx,xx,zwjs,"
					+ "shfs,jjsl,gzxx,hygn_gyzj,hygn_gyjt,yq_nl,yq_sg,yq_mz,yq_xl,yq_hyzg,yq_jzd) "
					+ " values('" +
					pojo.getRealUid() + "','" +
					pojo.getMember_dj() + "','" +
					pojo.getMember_from() + "','" +
					pojo.getHas_car() + "','" +
					pojo.getSalary() + "','" +
					pojo.getHas_house() + "','" +
					pojo.getWeight() + "','" +
					pojo.getXz() + "','" +
					pojo.getMz() + "','" +
					pojo.getSx() + "','" +
					pojo.getXx() + "','" +
					pojo.getZwjs() + "','" +
					pojo.getShfs() + "','" +
					pojo.getJjsl() + "','" +
					pojo.getGzxx()+ "','" +
					pojo.getHygn_gyzj() + "','" +
					pojo.getHygn_gyjt() + "','" +
					pojo.getYq_nl() + "','" +
					pojo.getYq_sg() + "','" +
					pojo.getYq_mz() + "','" +
					pojo.getYq_xl() + "','" +
					pojo.getYq_hyzg() + "','" +
					pojo.getYq_jzd()
					+"');";
			//System.out.println(insertsql);
			
			conn.createStatement().execute(insertsql);
		}else{
			System.out.println("Exist title:"+pojo.getRealUid());
		}
		return pojo;
	}
	
	/**
	 * 保存多个SjjyUserDetailsPOJO
	 * @param pojos 
	 * @param fromUrl 来源的页面路径
	 */
	public boolean saveUsersDetails(List<SjjyUserDetailsPOJO> pojos) {
		try {
			for (SjjyUserDetailsPOJO pojo : pojos) {
				if(pojo!=null  && pojo.getRealUid()!=null && !"".equals(pojo.getRealUid())){
					saveUserDetails(pojo);
				}
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(">>>rollbacked.");
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			return false;
		}
		return true;
	}

	public List<String> user_checkOut(String key,int i) {
		String sql = "update sjjy_users set status='"+key+"' where userid in (select top "+i+" userid from sjjy_users where status='new');";
		System.out.println(sql);
		List<String> userids = new ArrayList<String>(); 
		try {
			conn.createStatement().executeUpdate(sql);
			ResultSet rs = conn.createStatement().executeQuery("select userid from sjjy_users where status='"+key+"';");

			while(rs.next()){
				userids.add(rs.getString("userid"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userids;
	}
	
	public boolean user_checkIn(String key) {
		String sql = "update sjjy_users set status='detail' where userid in (select userid from sjjy_users where status='"+key+"');";
		System.out.println(sql);
		try {
			conn.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
