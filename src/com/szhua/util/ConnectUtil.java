package com.szhua.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Properties;

/**
 * 网页内容抓取类，提供字符串内容
 * @author Hua
 *
 */
public class ConnectUtil {
	public final static String TIMEOUT = "TIMEOUT";
	public final static String ERROR = "ERROR";

	/**
	 * 初始化代理
	 */
	private static void initProxy(String host, int port, final String username, final String password) {
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, new String(password).toCharArray());
			}
		});
		Properties prop = System.getProperties();
		prop.setProperty("http.proxyHost", host);
		prop.setProperty("http.proxyPort", Integer.toString(port));
		prop.setProperty("http.proxyType", "4");
		prop.setProperty("http.proxySet", "true");

		// 设置不需要通过代理服务器访问的主机，可以使用*通配符，多个地址用|分隔
		prop.setProperty("http.nonProxyHosts", "localhost|10.*|*.efoxconn.com");
		// 设置安全访问使用的代理服务器地址与端口
		// 它没有https.nonProxyHosts属性，它按照http.nonProxyHosts 中设置的规则访问
		// prop.setProperty("https.proxyHost", "ehome-a.efoxconn.com");
		// prop.setProperty("https.proxyPort", "443");
		// HttpURLConnection是基于HTTP协议的，其底层通过socket通信实现。如果不设置超时（timeout），在网络异常的情况下，可能会导致程序僵死而不继续往下执行。可以通过以下两个语句来设置相应的超时：
		// System.setProperty("sun.net.client.defaultConnectTimeout",
		// "30000");
		// System.setProperty("sun.net.client.defaultReadTimeout", "30000");
	}

	/**
	 * 从网址weburl下载页面内容，以utf-8字符串返回
	 */
	public static String fetchPageContext(String weburl,String charset) {
		StringBuffer sTotalString = new StringBuffer("");
		try {
			/*String proxy = "ehome-a.efoxconn.com";
			int port = 3128;
			String username = "F3229233";
			String password = "password";
			initProxy(proxy, port, username, password);*/

			URL url = new URL(weburl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// JDK 1.5以前的版本，只能通过设置这两个系统属性来控制网络超时。在1.5中，还可以使用HttpURLConnection的父类URLConnection的以下两个方法：
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(50000);
			// 增加报头，模拟浏览器，防止屏蔽
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
			conn.setRequestProperty("Accept", "text/html");// 只接受text/html类型，当然也可以接受图片,pdf,*/*任意，就是tomcat/conf/web里面定义那些
			conn.connect();
			InputStream is = conn.getInputStream();
			// java.io.InputStreamReader isReader = new java.io.InputStreamReader(is);
			// java.io.BufferedReader l_reader = new java.io.BufferedReader(isReader);
			BufferedInputStream fin = new BufferedInputStream(is);
			/*String sCurrentLine = "";
			while ((sCurrentLine = l_reader.readLine()) != null) {
				sTotalString.append(sCurrentLine);
			}*/
			int byteread = 0; // 读入多个字节到字节数组中，byteread为一次读入的字节数
			byte[] buffer = new byte[2048 * 1024];
			while ((byteread = fin.read(buffer)) != -1) {
				sTotalString.append(new String(buffer, 0, byteread,charset));
			}

			// System.out.println(sTotalString.toString());
			System.out.println("====================");
			// new String(sTotalString.getBytes("utf-8")),"utf-8"
		} catch (SocketTimeoutException e) {
			return TIMEOUT;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return sTotalString.toString();
	}

	/**
	 * html from file
	 * 
	 * 从html文件读取内容，以utf-8字符串返回
	 */
	public static String fromFile(String file,String charset) {
		StringBuffer sTotalString = new StringBuffer("");
		try {
			FileInputStream fis = new FileInputStream(new File(file));
			BufferedInputStream fin = new BufferedInputStream(fis);
			int byteread = 0; // 读入多个字节到字节数组中，byteread为一次读入的字节数
			byte[] buffer = new byte[2048 * 1024];
			while ((byteread = fin.read(buffer)) != -1) {
				sTotalString.append(new String(buffer, 0, byteread, charset));
			}
			System.out.println("====================");
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return sTotalString.toString();
	}

}
