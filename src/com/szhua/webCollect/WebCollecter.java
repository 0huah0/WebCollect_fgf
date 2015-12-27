package com.szhua.webCollect;

/**
 * web信息收集器
 * @author Hua 2013-4-21 下午7:27:44
 */
public class WebCollecter {

	/**
	 * web信息收集器主入口
	 * @author Hua 2013-4-21 下午7:36:07
	 * @param args
	 */
	public static void main(String[] args) {
		new TaskManager().doWork();
		System.exit(0);
	}

}
