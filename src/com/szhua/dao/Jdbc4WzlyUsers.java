package com.szhua.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.szhua.pojo.WzlyUserDetailsPOJO;
import com.szhua.pojo.WzlyUserPOJO;

public class Jdbc4WzlyUsers extends AccessJdbc {
	
	private int getUserCountBy(String cs) {
		String countsql = "select count(*) as ct from wzly_users where "+cs;
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
		String countsql = "select count(*) as ct from wzly_users_details where "+cs;
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
	 * 保存单个WzlyUserPOJO
	 */
	protected WzlyUserPOJO saveWzlyUser(WzlyUserPOJO pojo) {
		
		if(pojo.getUserid()==null || getUserCountBy("userid='"+pojo.getUserid()+"';")<1){
			String insertsql = "insert into wzly_users(userid,name,content,head_img_url,from_url,status) "
					+ " values('"
					+ pojo.getUserid() + "','" 
					+ pojo.getName() + "','" 
					+ pojo.getContent() + "','" 
					+ pojo.getHead_img_url() + "','" 
					+ pojo.getFrom_url() + "','" 
					+ pojo.getStatus() 
					+"');";
			System.out.println(insertsql);
			try {
				conn.createStatement().execute(insertsql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("Exist title:"+pojo.getUserid());
		}
		return pojo;
	}

	/**
	 * 保存多个pojoconpojo
	 * @param pojos 
	 * @param fromUrl 来源的页面路径
	 */
	public void saveWzlyUsers(List<WzlyUserPOJO> pojos, String fromUrl) {
		try {
			conn.setAutoCommit(false);
			for (WzlyUserPOJO pojo : pojos) {
				pojo.setFrom_url(fromUrl);
				saveWzlyUser(pojo);
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存单个WzlyUserPOJO
	 * @throws SQLException 
	 */
	protected WzlyUserDetailsPOJO saveWzlyUserDetails(WzlyUserDetailsPOJO pojo) throws SQLException {

		if(getDetailCountBy("userid='"+pojo.getUserid()+"';")<1){
			
			String insertsql = "insert into wzly_users_details (userid,name,sex,marry,age,edu,height,_sr,_xz,_sx,_szd,_jg,_nxdb,_xx,_mz,_ywzn,_gcqk,_zfqk,xgxm_gxms,xgxm_zp,xgxm_tz,xgxm_tx,xgxm_mlbw,xgxm_fx,xgxm_lx,gzxx_sr,gzxx_gzzk,gzxx_xl,gzxx_zy,gzxx_zhiy,yq_sex,yq_age,yq_photo,yq_height,yq_type,yq_marryhis,yq_edu,yq_cx,yq_dq,shms_,xqah_) "
					+ " values('" +
					pojo.getUserid() + "','" +
					pojo.getName() + "','" +
					pojo.getSex() + "','" +
					pojo.getMarry() + "','" +
					pojo.getAge() + "','" +
					pojo.getEdu() + "','" +
					pojo.getHeight() + "','" +
					pojo.get_sr() + "','" +
					pojo.get_xz() + "','" +
					pojo.get_sx() + "','" +
					pojo.get_szd() + "','" +
					pojo.get_jg() + "','" +
					pojo.get_nxdb() + "','" +
					pojo.get_xx() + "','" +
					pojo.get_mz() + "','" +
					pojo.get_ywzn() + "','" +
					pojo.get_gcqk() + "','" +
					pojo.get_zfqk() + "','" +
					pojo.getXgxm_gxms() + "','" +
					pojo.getXgxm_zp() + "','" +
					pojo.getXgxm_tz() + "','" +
					pojo.getXgxm_tx() + "','" +
					pojo.getXgxm_mlbw() + "','" +
					pojo.getXgxm_fx() + "','" +
					pojo.getXgxm_lx() + "','" +
					pojo.getGzxx_sr() + "','" +
					pojo.getGzxx_gzzk() + "','" +
					pojo.getGzxx_xl() + "','" +
					pojo.getGzxx_zy() + "','" +
					pojo.getGzxx_zhiy() + "','" +
					pojo.getYq_sex() + "','" +
					pojo.getYq_age() + "','" +
					pojo.getYq_photo() + "','" +
					pojo.getYq_height() + "','" +
					pojo.getYq_type() + "','" +
					pojo.getYq_marryhis() + "','" +
					pojo.getYq_edu() + "','" +
					pojo.getYq_cx() + "','" +
					pojo.getYq_dq() + "','" +
					pojo.getShms_() + "','" +
					pojo.getXqah_()
					+"');";
			//System.out.println(insertsql);
			
			conn.createStatement().execute(insertsql);
		}else{
			System.out.println("Exist title:"+pojo.getUserid());
		}
		return pojo;
	}
	
	/**
	 * 保存多个WzlyUserDetailsPOJO
	 * @param pojos 
	 * @param fromUrl 来源的页面路径
	 */
	public boolean saveWzlyUsersDetails(List<WzlyUserDetailsPOJO> pojos) {
		try {
			for (WzlyUserDetailsPOJO pojo : pojos) {
				if(pojo!=null  && pojo.getUserid()!=null && !"".equals(pojo.getUserid())){
					saveWzlyUserDetails(pojo);
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
		String sql = "update wzly_users set status='"+key+"' where userid in (select top "+i+" userid from wzly_users where status='new');";
		System.out.println(sql);
		List<String> userids = new ArrayList<String>(); 
		try {
			conn.createStatement().executeUpdate(sql);
			ResultSet rs = conn.createStatement().executeQuery("select userid from wzly_users where status='"+key+"';");

			while(rs.next()){
				userids.add(rs.getString("userid"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userids;
	}
	
	public boolean user_checkIn(String key) {
		String sql = "update wzly_users set status='detail' where userid in (select userid from wzly_users where status='"+key+"');";
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
