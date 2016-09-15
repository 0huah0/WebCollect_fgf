package com.szhua.myparser;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

import com.szhua.dao.Jdbc4SjjyUsers;
import com.szhua.pojo.SjjyUserDetailsPOJO;
import com.szhua.util.ConnectUtil;

/**
 * 
 */
public class UsersDetailsParser4sjjy extends ContextParser {

	public UsersDetailsParser4sjjy() {
		super("http://www.jiayuan.com/");
		charset = "utf-8";
		type = 5; // 计算机互联网 计算机互联网评论文章

		props.put("Host", "jiayuan.com");
		props.put("Referer", "http://login.jiayuan.com/jump/?cb=c3LR1Rcf0VgVomZ88*7F4jUvO4uUnBaLg0UYazS265ybU6XnjRAPPzDC7PnLjkIkPHKC6JLAu8OLrDZtUSmLD9E5XTD0u4CQfejFzwmD85bhcnQGbaubrKqxa0*SUqSlCEELZa54x6Lu-LeNQGD1rp91lwt7qQfBrDT8LEMOkSQ93FF4UNCFEjqSIb9r98HjRWQxzFJHQtZKRq3w");
		props.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; rv:42.0) Gecko/20100101 Firefox/42.0");
		props.put("Cookie", "save_jy_login_name=13551067431; stadate1=130824630; myloc=62%7C6201; myage=25; mysex=m; myuid=130824630; myincome=40; jy_ztlogin_zadan_130824630=zadan; is_searchv2=1; view_m=1; ip_loc=44; PHPSESSID=4cbafd810cbb4f4d8ab807f9909c8297; last_login_time=1473962915; upt=4uy%2AMn0lYelog478hOaBZ0MMUntFtDrvWW2MpnFUzrBAR9QsR11RkwORQIhO3s7RKLkKhfW5qE7ocCRYHA..; IM_S=%7B%22IM_CID%22%3A5886330%2C%22svc%22%3A%7B%22code%22%3A0%2C%22nps%22%3A0%2C%22unread_count%22%3A%226%22%2C%22ocu%22%3A0%2C%22ppc%22%3A0%2C%22jpc%22%3A0%2C%22regt%22%3A%221422123716%22%2C%22using%22%3A%22%22%2C%22user_type%22%3A%220%22%2C%22uid%22%3A131824630%7D%2C%22IM_SV%22%3A%22123.59.161.4%22%7D; IM_CS=0; IM_ID=1; IM_TK=1473921165514; IM_M=%5B%7B%22cmd%22%3A57%2C%22data%22%3A%22123.59.161.4%22%7D%5D; IM_CON=%7B%22IM_TM%22%3A1473921165514%2C%22IM_SN%22%3A1%7D; SESSION_HASH=7332f7ee09c962d0c7de6715d3379592a7b3ce36; user_access=1; PROFILE=131824630%3AHua%3Am%3Aa1.jyimg.com%2F11%2Fa6%2F67d4b705c8a51eee53698d9bc21e%3A1%3A%3A1%3A67d4b705c_2_avatar_p.jpg%3A1%3A1%3A1020%3A0; RAW_HASH=GihGsDT97GOcU8tMdSfxfSBbSh8wRx3knbO-Q4MKemgDxK1Et4Lsysy1Vig0MVmpZ9K%2AmImm6cYqDN%2Aab4tKE3-uvAjvbiL3ZMMCSsmzVqmJUF4.; COMMON_HASH=1167d4b705c8a51eee53698d9bc21ea6; sl_jumper=%26cou%3D17%26omsg%3D0%26dia%3D0%26lst%3D2016-09-15");
		props.put("DNT", "1");
		props.put("Connection", "keep-alive");

	}

	@Override
	public void doTast() {
		
		Jdbc4SjjyUsers jdbc = new Jdbc4SjjyUsers();
		
		String key = String.valueOf(Math.random());
		List<String> uids = jdbc.user_checkOut(key,5);
		
		while(uids.size() > 0){ // LOOP ALL
			List<SjjyUserDetailsPOJO> details = new ArrayList<SjjyUserDetailsPOJO>();
			for (String uid : uids) {  //LOOP PAGE
				if(uid.length()>0){
					String str = ConnectUtil.fetchPageContext(fromUrl + uid +"?fxly=search_v2_index", charset, props);
					if (str.length() < 100) {
						System.err.println("Download!-->" + str);
					} else {
						System.out.println("Download!-->" + str.length());
						details.add(getPOJO(str));
						System.out.println("Analysised!");
					}
				}
			}
			
			if(jdbc.saveUsersDetails(details)){
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
	public SjjyUserDetailsPOJO getPOJO(String inputHtml) {
		SjjyUserDetailsPOJO pojo = new SjjyUserDetailsPOJO();
		if (inputHtml.length() > 0) {
			Parser parser = Parser.createParser(inputHtml, charset);
			

			NodeList ns0 = null; 
			try {
				ns0 = parser.extractAllNodesThatMatch(new TagNameFilter("body"));
			} catch (ParserException e1) {
				e1.printStackTrace();
				return null;
			}
			
			NodeList ns = null; 
			try {
				ns = ns0.extractAllNodesThatMatch(new CssSelectorNodeFilter(".ml_ico"),true);
				if( ns.size() > 0){
					pojo.setRealUid(splitGet(ns.elementAt(0).getParent().getChildren().extractAllNodesThatMatch(new CssSelectorNodeFilter("H4")).elementAt(0).toPlainTextString(),":",1));	//realUid
				}
			} catch (Exception e0) {
				e0.printStackTrace();
			}
			
			
			try {
				ns = ns.elementAt(0).getParent().getChildren().extractAllNodesThatMatch(new CssSelectorNodeFilter("ul li"),true);	//会员info
				if( ns.size() > 0){
					SimpleNodeIterator it = ns.elements();
					while(it.hasMoreNodes()){
						Node n = it.nextNode();
						if(n.getChildren().elementAt(1).toPlainTextString().contains("购车")){
							pojo.setHas_car(n.getChildren().elementAt(3).toPlainTextString().trim());
						}else if(n.getChildren().elementAt(1).toPlainTextString().contains("月薪")){
							pojo.setSalary(n.getChildren().elementAt(3).toPlainTextString().trim());
						}else if(n.getChildren().elementAt(1).toPlainTextString().contains("住房")){
							pojo.setHas_house(n.getChildren().elementAt(3).toPlainTextString().trim());
						}else if(n.getChildren().elementAt(1).toPlainTextString().contains("体重")){
							pojo.setWeight(n.getChildren().elementAt(3).toPlainTextString().trim());
						}else if(n.getChildren().elementAt(1).toPlainTextString().contains("星座")){
							pojo.setXz(n.getChildren().elementAt(3).toPlainTextString().trim());
						}else if(n.getChildren().elementAt(1).toPlainTextString().contains("民族")){
							pojo.setMz(n.getChildren().elementAt(3).toPlainTextString().trim());
						}else if(n.getChildren().elementAt(1).toPlainTextString().contains("属相")){
							pojo.setSx(n.getChildren().elementAt(3).toPlainTextString().trim());
						}else if(n.getChildren().elementAt(1).toPlainTextString().contains("血型")){
							pojo.setXx(n.getChildren().elementAt(3).toPlainTextString().trim());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				ns = ns0.extractAllNodesThatMatch(new CssSelectorNodeFilter(".member_dj"),true);
				if( ns.size() > 0){
					pojo.setMember_dj(ns.elementAt(0).toPlainTextString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				ns = ns0.extractAllNodesThatMatch(new CssSelectorNodeFilter(".member_name .col_blue"),true);
				if( ns.size() > 0){
					pojo.setMember_from(ns.elementAt(0).toPlainTextString()+"/"+ns.elementAt(1).toPlainTextString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			try {
				ns = ns0.extractAllNodesThatMatch(new CssSelectorNodeFilter(".content_705 .js_box"),true);
				if( ns.size() > 0){
					SimpleNodeIterator it = ns.elements();
					while(it.hasMoreNodes()){
						Node n = it.nextNode();
						if(n.getChildren().elementAt(1).toPlainTextString().contains("自我介绍")){
							pojo.setZwjs(n.getChildren().elementAt(3).toPlainTextString().trim());
						}else if(n.getChildren().elementAt(1).toPlainTextString().contains("择偶要求")){
							NodeList ls = n.getChildren().extractAllNodesThatMatch(new CssSelectorNodeFilter("ul li"),true);
							SimpleNodeIterator it1 = ls.elements();
							while(it1.hasMoreNodes()){
								Node n1 = it1.nextNode();
								String key = n1.getChildren().elementAt(0).toPlainTextString().replace(" ", "").replace("&nbsp;", "");
								if(key.contains("年龄")){
									pojo.setYq_nl(n1.getChildren().elementAt(1).toPlainTextString());
								}else if(key.contains("身高")){
									pojo.setYq_sg(n1.getChildren().elementAt(1).toPlainTextString());
								}else if(key.contains("民族")){
									pojo.setYq_mz(n1.getChildren().elementAt(1).toPlainTextString());
								}else if(key.contains("学历")){
									pojo.setYq_xl(n1.getChildren().elementAt(1).toPlainTextString());
								}else if(key.contains("婚姻状况")){
									pojo.setYq_hyzk(n1.getChildren().elementAt(1).toPlainTextString());
								}else {
									String key1 = n1.getChildren().elementAt(1).toPlainTextString().replace(" ", "").replace("&nbsp;", "");
									if(key1.contains("居住地")){
										pojo.setYq_jzd(n1.getChildren().elementAt(3).toPlainTextString());
									}
								}
								
							}
						}else if(n.getChildren().elementAt(1).toPlainTextString().contains("生活方式")){
							pojo.setShfs(getKVstr(n,new CssSelectorNodeFilter("ul li")));
						}else if(n.getChildren().elementAt(1).toPlainTextString().contains("经济实力")){
							pojo.setJjsl(getKVstr(n,new CssSelectorNodeFilter("ul li")));
						}else if(n.getChildren().elementAt(1).toPlainTextString().contains("工作学习")){
							pojo.setGzxx(getKVstr(n,new CssSelectorNodeFilter("ul li")));
						}else if(n.getChildren().elementAt(1).toPlainTextString().contains("婚姻观念")){
							pojo.setHygn(getKVstr(n,new CssSelectorNodeFilter("ul li")));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return pojo;
	}

	private String getKVstr(Node n, CssSelectorNodeFilter cssSelectorNodeFilter) {
		String string = "";
		NodeList ls = n.getChildren().extractAllNodesThatMatch(cssSelectorNodeFilter,true);
		SimpleNodeIterator it = ls.elements();
		while(it.hasMoreNodes()){
			Node n1 = it.nextNode();
			string += n1.getChildren().elementAt(1).toPlainTextString()+n1.getChildren().elementAt(3).toPlainTextString()+";;;";
		}
		
		return string.replace("&nbsp;", "").replace(" ", "");
	}

	
}
