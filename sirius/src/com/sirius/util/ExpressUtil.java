package com.sirius.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSONObject;
import com.sirius.po.BDExp;

public class ExpressUtil {

	private final static String HTTPURL = "http://apis.baidu.com/netpopo/express/express1";

	public static BDExp request(String type, String number) {
		// StringBuffer buffer = new StringBuffer();
		// buffer.append("type=");
		// buffer.append(expressage.getType());
		// buffer.append("&number=");
		// buffer.append(expressage.getExpressNumber());
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		String httpUrl = HTTPURL + "?type=" + type + "&number=" + number;

		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			// 填入apikey到HTTP header
			connection.setRequestProperty("apikey",
					"ee1268667508f47088fbe332e746ac1f");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				// sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONObject.parseObject(result, BDExp.class);
	}
}
