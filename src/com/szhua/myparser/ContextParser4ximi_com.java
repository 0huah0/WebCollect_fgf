package com.szhua.myparser;

import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.szhua.pojo.TextContextPOJO;
import com.szhua.util.DateUtil;

/**
 * http://www.apple4.cn/page/2/
 * 
 * @author Hua 2013-4-21 下午7:42:03
 */
public class ContextParser4ximi_com extends ContextParser {
	

	public ContextParser4ximi_com() {
		super("http://www.apple4.cn/page/2/");
		charset = "utf-8";
		type = 5; //计算机互联网   计算机互联网评论文章
	}

	/**
	 * 抽取纯文本信息 解析提取http://www.tuicool.com/ah/的信息
	 * @param inputHtml
	 * @return
	 */
	@Override
	public List<TextContextPOJO> getTextList(String inputHtml) {
		if (inputHtml.length() > 0) {
			Parser parser = Parser.createParser(inputHtml, charset);
			NodeFilter[] nfFilters = { new HasAttributeFilter("class", "entry-wrap") };//, new TagNameFilter("article")

			AndFilter filter = new AndFilter(nfFilters);

			try {
				NodeList nodes = parser.parse(filter);
				for (int i = 0; i < nodes.size(); i++) {
					TextContextPOJO text = new TextContextPOJO();
					NodeList ns = nodes.elementAt(i).getChildren();
					Node n = ns.extractAllNodesThatMatch(new HasAttributeFilter("class", "entry-title"), true).elementAt(0);
					n = n.getChildren().elementAt(1);
					if (n instanceof LinkTag) {
						LinkTag linkTag = (LinkTag) n;
						text.setTitleUrl(linkTag.getLink());
						text.setTitle(linkTag.getAttribute("title"));
					}
					
					try {
						TagNode n1 = (TagNode)ns.extractAllNodesThatMatch(new HasAttributeFilter("class", "entry-time"), true).elementAt(0);
						text.setPublishDt(DateUtil.getDate(n1.getAttribute("datetime").replace("T", " ").substring(0,19), "yyyy-MM-dd HH:mm:ss"));
					} catch (Exception e) {
						System.err.println("日期提取异常");
					}
					
					try {
						n = ns.extractAllNodesThatMatch(new HasAttributeFilter("class", "entry-comments-link"), true).elementAt(0).getFirstChild().getFirstChild();
						text.setVisitTimes(Integer.parseInt(n.getText().substring(0,n.getText().indexOf(" "))));
					} catch (Exception e) {
						System.err.println("访问量提取异常");
					}
					
					n = ns.extractAllNodesThatMatch(new HasAttributeFilter("class", "entry-summary"), true).elementAt(0);
					text.setContext(n.toPlainTextString());

					n = ns.extractAllNodesThatMatch(new HasAttributeFilter("class", "entry-terms"), true).elementAt(0);
					System.out.println(n.toHtml(true));
					System.out.println(n.toString());
					text.setTags(n.toPlainTextString());

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
