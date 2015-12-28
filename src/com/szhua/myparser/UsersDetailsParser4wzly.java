package com.szhua.myparser;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.szhua.dao.Jdbc4WzlyUsers;
import com.szhua.pojo.WzlyUserDetailsPOJO;
import com.szhua.util.ConnectUtil;

/**
 * 
 */
public class UsersDetailsParser4wzly extends ContextParser {

	public UsersDetailsParser4wzly() {
		super("http://www.7799520.com/jiaoyou/home/");
		charset = "utf-8";
		type = 5; // 计算机互联网 计算机互联网评论文章

		props.put("Host", "7799520.com");
		//props.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; rv:42.0) Gecko/20100101 Firefox/42.0");
//		props.put("Cookie", "3874900881=2; Hm_lvt_fd93b7fb546adcfbcf80c4fc2b54da2c=1449066233,1449159667; _ga=GA1.2.847100239.1449066240; jandan_rate_72309=4; wp_zan_72309=72309; 3874900881=1");
		props.put("DNT", "1");
		props.put("Connection", "keep-alive");

	}

	@Override
	public void doTast() {
		
		
		
		Jdbc4WzlyUsers jdbc = new Jdbc4WzlyUsers();
		
		String key = String.valueOf(Math.random());
		List<String> uids = jdbc.user_checkOut(key,5);
		
		while(uids.size() > 0){ // LOOP ALL
			List<WzlyUserDetailsPOJO> details = new ArrayList<WzlyUserDetailsPOJO>();
			for (String uid : uids) {  //LOOP PAGE
				if(uid.length()>0){
					String str = ConnectUtil.fetchPageContext(fromUrl + uid, charset, props);
					if (str.length() < 100) {
						System.err.println("Download!-->" + str);
					} else {
						System.out.println("Download!-->" + str.length());
						details.add(getPOJO(str));
						System.out.println("Analysised!");
					}
				}
			}
			
			if(jdbc.saveWzlyUsersDetails(details)){
				if(key!=null && !"".equals(key)){
					jdbc.user_checkIn(key);
					System.out.println("Saved!key="+key);
				}
			}
			
			key = String.valueOf(Math.random());
			uids = jdbc.user_checkOut(key,5);
		}
	}

	private String splitGet(String str, String regex, int i) {
		String s = "";
		try {
			s = str.split(regex)[i];
		} catch (Exception e) {s = "";}
		return s;
	}
	
	@Override
	public WzlyUserDetailsPOJO getPOJO(String inputHtml) {
		WzlyUserDetailsPOJO pojo = new WzlyUserDetailsPOJO();
		if (inputHtml.length() > 0) {
			Parser parser = Parser.createParser(inputHtml, charset);
			AndFilter filter = new AndFilter(new NodeFilter[]{ new CssSelectorNodeFilter(".paper-article")});

			NodeList ns = null; 
			try {
				ns = parser.parse(filter);
			} catch (ParserException e1) {
				e1.printStackTrace();
			}
			
			if(ns==null || ns.size()==0){
				return null;
			}
			
			try {
				NodeList tns =  ns.extractAllNodesThatMatch(new CssSelectorNodeFilter(".infolist>li"),true);
				pojo.setUserid(splitGet(tns.elementAt(0).toPlainTextString(),"：",1));
				pojo.setName(splitGet(tns.elementAt(1).toPlainTextString(),"：",1));
				pojo.setSex(splitGet(tns.elementAt(2).toPlainTextString(),"：",1));
				pojo.setMarry(splitGet(tns.elementAt(3).toPlainTextString(),"：",1));
				pojo.setAge(splitGet(tns.elementAt(4).toPlainTextString(),"：",1));
				pojo.setEdu(splitGet(tns.elementAt(5).toPlainTextString(),"：",1));
				pojo.setHeight(splitGet(tns.elementAt(6).toPlainTextString(),"：",1));
				
				pojo.set_sr(splitGet(tns.elementAt(7).toPlainTextString(),"：",1));
				pojo.set_xz(splitGet(tns.elementAt(8).toPlainTextString(),"：",1));
				pojo.set_sx(splitGet(tns.elementAt(9).toPlainTextString(),"：",1));
				pojo.set_szd(splitGet(tns.elementAt(10).toPlainTextString(),"：",1)); //"：" not ":"
				pojo.set_jg(splitGet(tns.elementAt(11).toPlainTextString(),"：",1));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				Node n = ns.extractAllNodesThatMatch(new CssSelectorNodeFilter(".pcontent"),true).elementAt(1);
				pojo.set_nxdb(n.getChildren().elementAt(1).toPlainTextString().trim());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			NodeList tns =  ns.extractAllNodesThatMatch(new CssSelectorNodeFilter(".tb-home"),true);
			try {
				NodeList tnss = tns.elementAt(0).getChildren().extractAllNodesThatMatch(new TagNameFilter("td"),true); //择友要求
				
				pojo.setYq_sex(tnss.elementAt(1).toPlainTextString());
				pojo.setYq_photo(tnss.elementAt(3).toPlainTextString());
				pojo.setYq_age(tnss.elementAt(5).toPlainTextString());
				pojo.setYq_height(tnss.elementAt(7).toPlainTextString());
				pojo.setYq_type(tnss.elementAt(9).toPlainTextString());//交友类型
				pojo.setYq_marryhis(tnss.elementAt(11).toPlainTextString());//婚史状况
				pojo.setYq_edu(tnss.elementAt(13).toPlainTextString());//xl
				pojo.setYq_cx(tnss.elementAt(15).toPlainTextString());//cx
				pojo.setYq_dq(tnss.elementAt(17).toPlainTextString().trim());//交友类型
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				NodeList tnss = tns.elementAt(1).getChildren().extractAllNodesThatMatch(new TagNameFilter("td"),true); //
				pojo.set_xx(tnss.elementAt(7).toPlainTextString());//xx
				pojo.set_mz(tnss.elementAt(9).toPlainTextString());//mz
				pojo.set_ywzn(tnss.elementAt(11).toPlainTextString());//
				pojo.set_gcqk(tnss.elementAt(13).toPlainTextString());
				pojo.set_zfqk(tnss.elementAt(15).toPlainTextString());
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				NodeList tnss = tns.elementAt(2).getChildren().extractAllNodesThatMatch(new TagNameFilter("td"),true); //个性描述
				pojo.setXgxm_gxms(tnss.elementAt(1).toPlainTextString());//xx
				pojo.setXgxm_zp(tnss.elementAt(3).toPlainTextString());
				pojo.setXgxm_tz(tnss.elementAt(5).toPlainTextString());
				pojo.setXgxm_tx(tnss.elementAt(7).toPlainTextString());
				pojo.setXgxm_mlbw(tnss.elementAt(9).toPlainTextString());
				pojo.setXgxm_fx(tnss.elementAt(11).toPlainTextString());
				//pojo.setXgxm_fs(tnss.elementAt(13).toPlainTextString());
				pojo.setXgxm_lx(tnss.elementAt(15).toPlainTextString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				NodeList tnss = tns.elementAt(3).getChildren().extractAllNodesThatMatch(new TagNameFilter("td"),true); //工作与学习
				pojo.setGzxx_sr(tnss.elementAt(3).toPlainTextString());//xx
				pojo.setGzxx_gzzk(tnss.elementAt(5).toPlainTextString());//gzzk
				pojo.setGzxx_xl(tnss.elementAt(9).toPlainTextString());//xl
				pojo.setGzxx_zy(tnss.elementAt(11).toPlainTextString());//xx
				pojo.setGzxx_zhiy(tnss.elementAt(13).toPlainTextString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				pojo.setShms_(tns.elementAt(4).toPlainTextString().replace("	", ""));
				pojo.setXqah_(tns.elementAt(5).toPlainTextString().replace("	", ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pojo;
	}

	
}
