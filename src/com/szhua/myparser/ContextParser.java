package com.szhua.myparser;

import java.util.ArrayList;
import java.util.List;

import com.szhua.dao.Jdbc4TextContext;
import com.szhua.pojo.TextContextPOJO;
import com.szhua.util.ConnectUtil;

/**
 * 
 * 
 * */
public abstract class ContextParser {
	List<TextContextPOJO> list = new ArrayList<TextContextPOJO>();
	private String fromUrl = "";
	protected String charset = "utf-8";
	protected int type = 6;//未分类
	
	public ContextParser(String fromUrl) {
		this.fromUrl = fromUrl;
	}

	/**
	 * 抽取纯文本信息
	 * 
	 * @param inputHtml
	 * @return
	 */
	protected abstract List<TextContextPOJO> getTextList(String inputHtml);

	/**
	 * 1.下载 2.解析 3.检查，无重复保存
	 */
	public void doTast() {
		String str = ConnectUtil.fetchPageContext(fromUrl,charset);
		if(str.length()<100){
			System.out.println("Download!-->"+str);
		}else{
			System.out.println("Download!-->"+str.length());
			
			List<TextContextPOJO> texts = getTextList(str);
			System.out.println("Analysised!");			
			
			new Jdbc4TextContext().saveTextContexts(texts,fromUrl);
			System.out.println("Saved!"+texts.size());
		}
	}

}
