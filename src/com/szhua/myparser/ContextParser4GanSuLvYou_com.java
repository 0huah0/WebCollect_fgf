package com.szhua.myparser;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.szhua.pojo.TextContextPOJO;
import com.szhua.util.DateUtil;

/**
 * http://www.gsta.gov.cn/pub/lyzw/gzdt/szkx/index.html
 * @author Hua 2013-4-21 下午11:08:23
 */
public class ContextParser4GanSuLvYou_com extends ContextParser {
	List<TextContextPOJO> list = new ArrayList<TextContextPOJO>();

	public ContextParser4GanSuLvYou_com() {
		super("http://www.gsta.gov.cn/pub/lyzw/gzdt/szkx/index.html");
	}

	@Override
	public List<TextContextPOJO> getTextList(String inputHtml) {
		if (inputHtml.length() > 0) {
			Parser parser = Parser.createParser(inputHtml, "utf-8");
			CssSelectorNodeFilter filter = new CssSelectorNodeFilter ("td[class='font-108']"); 

			try {
				NodeList nodes = parser.parse(filter);
				for (int i = 0; i < nodes.size(); i++) {					
					TextContextPOJO text = new TextContextPOJO();
					
					Node trNode = nodes.elementAt(i).getParent();
					
					NodeList ns = trNode.getChildren().extractAllNodesThatMatch(new CssSelectorNodeFilter ("a"), true);
					Node n = nodes.elementAt(0).getChildren().elementAt(1);
					if (n instanceof LinkTag) {
						LinkTag linkTag = (LinkTag) n;
						text.setTitleUrl(linkTag.getLink());
						text.setTitle(linkTag.getAttribute("title"));
					}
					//发布shiji
					ns = trNode.getChildren().extractAllNodesThatMatch(new CssSelectorNodeFilter ("td[class='font-10']"), true);
					n = nodes.elementAt(0).getFirstChild();
					text.setPublishDt(DateUtil.getDate(n.getText(), "yyyy-MM-dd"));

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
