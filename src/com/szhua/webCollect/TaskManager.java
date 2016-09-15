package com.szhua.webCollect;

import java.util.ArrayList;
import java.util.List;

import com.szhua.myparser.ContextParser;
import com.szhua.myparser.UsersDetailsParser4sjjy;

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
//		contextParsers.add(new UsersParser4wzly());
//		contextParsers.add(new UsersDetailsParser4wzly());
//		contextParsers.add(new UsersParser4sjjy());
		contextParsers.add(new UsersDetailsParser4sjjy());
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
