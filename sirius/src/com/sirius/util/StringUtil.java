package com.sirius.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static boolean isNullOrEmpty(String text) {
		return text == null || text.trim().isEmpty();
	}

	/**
	 * 校验手机号码格式是否正确
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean phoneCheck(String phone) {
		Pattern p = Pattern.compile("^[\\d]{11}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	public static String getUuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 驼峰转下划线
	 * 
	 * @param arg
	 * @return
	 */
	public static String underscoreToCamelCase(String arg) {
		for (int i = 0; i < arg.length(); i++) {
			if (arg.charAt(i) >= 'A' && arg.charAt(i) <= 'Z') {
				arg = arg.replace(arg.charAt(i) + "", "_"
						+ (arg.charAt(i) + "").toLowerCase());
			}
		}
		return arg;
	}
}
