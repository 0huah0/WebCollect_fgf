package com.szhua.myparser;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.szhua.dao.Jdbc4WzlyUsers;
import com.szhua.pojo.WzlyUserPOJO;
import com.szhua.util.ConnectUtil;

/**
 * 
 */
public class UsersParser4wzly extends ContextParser {

	public UsersParser4wzly() {
//		super("http://www.7799520.com/jiaoyou/index.php?c=user&a=list&type=more&s_sex=2&s_dist1=6&s_dist2=77&s_avatar=1&page=");
		super("http://www.7799520.com/jiaoyou/index.php?c=user&a=list&type=more&s_sex=2&s_dist1=5&s_avatar=1&page=");
		
//		super("http://www.7799520.com/jiaoyou/index.php?c=user&a=list&type=more&s_sex=2&s_sage=20&s_eage=27&s_dist1=6&s_sheight=155&s_eheight=165&s_sedu=30&s_eedu=70&s_marry=1&s_avatar=1&page=");
		charset = "utf-8";
		type = 5; // 计算机互联网 计算机互联网评论文章

		props.put("Host", "7799520.com");
		//props.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; rv:42.0) Gecko/20100101 Firefox/42.0");
		props.put("Cookie", "3874900881=2; Hm_lvt_fd93b7fb546adcfbcf80c4fc2b54da2c=1449066233,1449159667; _ga=GA1.2.847100239.1449066240; jandan_rate_72309=4; wp_zan_72309=72309; 3874900881=1");
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
				List<WzlyUserPOJO> texts = getPOJO(str);
				System.out.println("Download!-->" + str.length());
				
				System.out.println("Analysised!");

				if(texts.size()==0){
					break;
				}
				
				new Jdbc4WzlyUsers().saveWzlyUsers(texts, fromUrl);
				System.out.println("Saved!" + texts.size());
			}

		}

	}

	@Override
	public List<WzlyUserPOJO> getPOJO(String inputHtml) {
		List<WzlyUserPOJO> texts = new ArrayList<WzlyUserPOJO>();

		if (inputHtml.length() > 0) {
			Parser parser = Parser.createParser(inputHtml, charset);
			AndFilter filter = new AndFilter(new NodeFilter[]{ new CssSelectorNodeFilter(".content>ul>li")});

			NodeList nodes = null; 
			try {
				nodes = parser.parse(filter);
			} catch (ParserException e1) {
				e1.printStackTrace();
			}
			
			if(nodes==null){
				return texts;
			}
			
			
			for (int i = 0; i < nodes.size(); i++) {
				try {
					WzlyUserPOJO text = new WzlyUserPOJO();
					NodeList ns = nodes.elementAt(i).getChildren(); // post
					
					LinkTag lt = (LinkTag) ns.extractAllNodesThatMatch(new HasAttributeFilter("class", "img"),true).elementAt(0).getFirstChild();
					text.setFrom_url(lt.getAttribute("href"));
					text.setHead_img_url(( (ImageTag) lt.getFirstChild()).getAttribute("src"));
					text.setUserid(text.getFrom_url().substring(14)); //'/jiaoyou/home/226137'
					
					lt = (LinkTag)ns.extractAllNodesThatMatch(new HasAttributeFilter("class", "title"),true).elementAt(0);
					text.setName(lt.getLinkText());
					
					TagNode n = (TagNode) ns.extractAllNodesThatMatch(new HasAttributeFilter("class", "conten"), true).elementAt(0);
					text.setContent(n.toPlainTextString());
					
					text.setStatus("new");
					texts.add(text);
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return texts;
	}
}
