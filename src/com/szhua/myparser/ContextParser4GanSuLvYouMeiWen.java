package com.szhua.myparser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.szhua.pojo.TextContextPOJO;
import com.szhua.util.ConnectUtil;
import com.szhua.util.DateUtil;

/**
 * http://www.gsta.gov.cn/pub/lyzw/lvzx/gslydt/index.html
 * 
 * @author Hua 2013-4-21 下午11:08:23
 */
public class ContextParser4GanSuLvYouMeiWen extends ContextParser {

	public ContextParser4GanSuLvYouMeiWen() {
		//首页 » 旅游资讯 » 旅游美文 
		super("http://www.gsta.gov.cn/pub/lyzw/lvzx/lymw/index.html"); 
		//super("http://www.gsta.gov.cn/pub/lyzw/gzdt/szkx/index.html");//已经收录至 2013-4-26 下午9:48:24
		charset = "gbk";
		type = 7; //旅游美文 
	}

	@Override
	public List<TextContextPOJO> getTextList(String inputHtml) {
		if (inputHtml.length() > 0) {
			Parser parser = Parser.createParser(inputHtml, charset);

			try {
				NodeList nodes = parser.parse(new CssSelectorNodeFilter ("td[class='font-108']"));
				for (int i = 0; i < nodes.size(); i++) {					
					TextContextPOJO text = new TextContextPOJO();
					
					Node trNode = nodes.elementAt(i).getParent();
					
					//标题&链接
					NodeList ns = trNode.getChildren().extractAllNodesThatMatch(new CssSelectorNodeFilter ("a"), true);
					Node n = ns.elementAt(0);
					if (n instanceof LinkTag) {
						LinkTag linkTag = (LinkTag) n;
						text.setTitleUrl(linkTag.getLink());
						text.setTitle(linkTag.getAttribute("title"));
					}

					//发布时间
					ns = trNode.getChildren().extractAllNodesThatMatch(new CssSelectorNodeFilter ("td[class='font-10']"), true);
					n = ns.elementAt(0).getFirstChild();
					text.setPublishDt(DateUtil.getDate(n.getText(), "yyyy-MM-dd"));

					//System.out.println(text.getTitleUrl());
					
					//主内容或简要
					String str = ConnectUtil.fetchPageContext(text.getTitleUrl(),charset);
					if(str.length()<100){
						System.out.println("Download!-->"+str);
					}else{
						System.out.println("Download!-->"+str.length());
						Parser parser0 = Parser.createParser(str, charset);
						ns = parser0.extractAllNodesThatMatch(new CssSelectorNodeFilter("div[id=wwkjArticleDetail]"));
						
						text.setContext(((Div)(ns.elementAt(0))).getChildrenHTML());
						//内容html
						Pattern pattern = Pattern.compile("来源:甘肃旅游政务网 \\| 添加时间:(.+)\\|\\s*点击:.*>(\\d+)");
						Matcher matcher = pattern.matcher(str);
						if (matcher.find()) {
							System.out.println(matcher.group());
							text.setPublishDt(DateUtil.getDate(matcher.group(1), "yyyy年MM月dd日 HH:mm:ss"));
							System.out.println(matcher.group(1));
							try{
								text.setVisitTimes(Integer.parseInt(matcher.group(2)));
							}catch (NumberFormatException e) {
								text.setVisitTimes(0);
							}
							System.out.println(matcher.group(2));
						}
					}
					
					
					text.setType(type);
					list.add(text);
				}	
			} catch (ParserException e) {
				e.printStackTrace();
			}

			return list;
		}
		return null;
	}
}
