package com.szhua.webCollect;

import java.util.ArrayList;
import java.util.List;

import com.szhua.myparser.ContextParser;
import com.szhua.myparser.ContextParser4ximi_com;

/**
 * 命令模式
 * 命令执行类 CommandManager
 * @author Hua 2013-4-21 下午7:39:09
 */
public class TaskManager {

	private List<ContextParser> contextParsers = new ArrayList<ContextParser>();
	
	/**
	 * 
	 * 构造方法TaskManager
	 * @author Hua 2013-4-21 下午7:36:43
	 */
	public TaskManager() {
		
		//注册命令
		//contextParsers.add(new ContextParser4tuicool_com());
		
		//contextParsers.add(new ContextParser4GanSuLvYouMeiWen()); //20151114 bage not found

		contextParsers.add(new ContextParser4ximi_com());
	}

	/**
	 * manage work 执行所有contextParsers的doTask方法
	 * @author Hua 2013-4-21 下午7:38:49
	 */
	public void doWork() {
		for (ContextParser contextParser : contextParsers) {
			contextParser.doTast();
		}
		
	}

}
