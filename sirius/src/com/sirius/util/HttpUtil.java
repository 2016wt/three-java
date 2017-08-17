package com.sirius.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpUtil {
	/**
	 * 获取客户端ip地址(微信)
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemortIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 网络请求，上传xml（微信）
	 * 
	 * @param url
	 * @param xmlString
	 * @return
	 */
	public static String post(String url) {

		// 创建httpclient工具对象
		HttpClient client = new HttpClient();
		// 创建post请求方法
		PostMethod myPost = new PostMethod(url);
		// 设置请求超时时间
		client.setConnectionTimeout(300 * 1000);
		String responseString = null;
		try {
			// 设置请求头部类型
			myPost.setRequestHeader("Content-Type", "text/html; charset=utf-8");
			// myPost.setRequestHeader("charset", "utf-8");

			// 设置请求体，即xml文本内容，注：这里写了两种方式，一种是直接获取xml内容字符串，一种是读取xml文件以流的形式
			// myPost.setRequestBody(xmlString);

			int statusCode = client.executeMethod(myPost);
			if (statusCode == HttpStatus.SC_OK) {
				BufferedInputStream bis = new BufferedInputStream(
						myPost.getResponseBodyAsStream());
				byte[] bytes = new byte[1024];
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int count = 0;
				while ((count = bis.read(bytes)) != -1) {
					bos.write(bytes, 0, count);
				}
				byte[] strByte = bos.toByteArray();
				responseString = new String(strByte, 0, strByte.length, "utf-8");
				bos.close();
				bis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		myPost.releaseConnection();
		return responseString;
	}
}
