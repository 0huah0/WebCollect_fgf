package com.szhua.myparser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.szhua.dao.Jdbc4SjjyUsers;
import com.szhua.pojo.SjjyUserPOJO;
import com.szhua.util.ConnectUtil;
import com.szhua.util.Native2AsciiUtils;

/**
 * 
 */
public class UsersParser4sjjy extends ContextParser {

	public UsersParser4sjjy() {
		
		super("http://search.jiayuan.com/v2/search_v2.php?sex=f&key=&stc=1%3A62%2C2%3A18.25%2C3%3A155.170%2C23%3A1&sn=default&sv=1&f=select&listStyle=bigPhoto&pri_uid=0&jsversion=v5&p=");	//p = page
		charset = "utf-8";

		props.put("Host", "jiayuan.com");
		//props.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; rv:42.0) Gecko/20100101 Firefox/42.0");
//		props.put("Cookie", "3874900881=2; Hm_lvt_fd93b7fb546adcfbcf80c4fc2b54da2c=1449066233,1449159667; _ga=GA1.2.847100239.1449066240; jandan_rate_72309=4; wp_zan_72309=72309; 3874900881=1");
		props.put("DNT", "1");
		props.put("Connection", "keep-alive");

	}

	@Override
	public void doTast() {

		for (int i = 1; i < 2000; i++) { 
			String str = ConnectUtil.fetchPageContext(fromUrl + i, charset, props);
			if (str.length() < 100) {
				System.err.println("Download!-->" + str);
			} else {
				List<SjjyUserPOJO> texts = getPOJO(str);
				System.out.println("Download!-->" + str.length());
				
				System.out.println("Analysised!");

				if(texts.size()==0){
					break;
				}
				
				new Jdbc4SjjyUsers().saveUsers(texts, fromUrl);
				System.out.println("Saved!" + texts.size());
			}

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SjjyUserPOJO> getPOJO(String inputHtml) {
		inputHtml = inputHtml.substring(inputHtml.indexOf("{"),inputHtml.lastIndexOf("}")+1);
		
		List<SjjyUserPOJO> texts = new ArrayList<SjjyUserPOJO>();
		if (inputHtml.length() > 0) {
			JSONObject jsonObj = JSONObject.fromObject(inputHtml);
			JSONArray data = (JSONArray) jsonObj.get("userInfo");
			for (Iterator<JSONObject> iterator = data.iterator();iterator.hasNext();) {
				
				JSONObject row = iterator.next();
				SjjyUserPOJO t = new SjjyUserPOJO();
				t.setAge(row.getString("age"));
				t.setEducation(Native2AsciiUtils.ascii2Native(row.getString("education")));
				t.setHeight(row.getString("height"));
				t.setHelloUrl(row.getString("helloUrl"));
				t.setImage(row.getString("image"));
				t.setIncome(row.getString("income"));
				t.setMarriage(Native2AsciiUtils.ascii2Native(row.getString("marriage")));
				t.setNickname(row.getString("nickname"));
				t.setRealUid(row.getString("realUid"));
				t.setSex(Native2AsciiUtils.ascii2Native(row.getString("sex")));
				t.setShortnote(Native2AsciiUtils.ascii2Native(row.getString("shortnote")));
				t.setWork_location(Native2AsciiUtils.ascii2Native(row.getString("work_location")));
				t.setWork_sublocation(Native2AsciiUtils.ascii2Native(row.getString("work_sublocation")));
				t.setStatus("new");
				
				texts.add(t);
			}
		}
		return texts;
	
	}
}
