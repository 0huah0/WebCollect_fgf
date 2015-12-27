package com.szhua.myparser;

import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.szhua.pojo.TextContextPOJO;

/**
 * http://www.tuicool.com/ah/
 * 
 * @author Hua 2013-4-21 下午7:42:03
 */
public class ContextParser4tuicool_com extends ContextParser {
	

	public ContextParser4tuicool_com() {
		super("http://www.tuicool.com/ah/");
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
			NodeFilter[] nfFilters = { new TagNameFilter("div"), new HasAttributeFilter("class", "single_fake") };

			AndFilter filter = new AndFilter(nfFilters);

			try {
				NodeList nodes = parser.parse(filter);
				for (int i = 0; i < nodes.size(); i++) {
					TextContextPOJO text = new TextContextPOJO();
					NodeList ns = nodes.elementAt(i).getChildren();
					Node n = ns.extractAllNodesThatMatch(new HasAttributeFilter("class", "article_title"), true).elementAt(0);
					n = n.getChildren().elementAt(1);
					if (n instanceof LinkTag) {
						LinkTag linkTag = (LinkTag) n;
						text.setTitleUrl(linkTag.getLink());
						text.setTitle(linkTag.getAttribute("title"));
					}

					n = ns.extractAllNodesThatMatch(new HasAttributeFilter("class", "article_cut"), true).elementAt(0).getFirstChild();
					text.setContext(n.getText());

					ns = ns.extractAllNodesThatMatch(new HasAttributeFilter("class", "article_thumb"), true);
					if (ns.size() > 0) {
						n = ns.elementAt(0).getChildren().elementAt(1);
						if (n instanceof ImageTag) {
							ImageTag img = (ImageTag) n;
							text.setImgUrl(img.getImageURL());
							// String imgLocUrl = saveImg2Ftp(img.getImageURL());
							// text.setImgLocUrl(imgLocUrl);
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
