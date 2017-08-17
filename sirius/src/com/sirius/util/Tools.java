package com.sirius.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sirius.entity.User;
import com.sirius.submail.SmsUtil;

public class Tools {

	public static final String WHOLESALERAPP = "wholesalerApp";
	public static final String WHOLESALER = "wholesaler";
	public static final String SHOPKEEPER = "shopkeeper";//店主
	public static final String SCANNER = "scanner";
	public static final String PLATFORM = "platform";//平台
	

	private static final Map<String, Object> phoneCode = new HashMap<String, Object>();

	/**
	 * 获得供应商登录信息
	 * 
	 * @param request
	 * @return
	 */
	public static User getWholesaler(HttpServletRequest request) {
		return (User) request.getAttribute(WHOLESALER);
	}

	public static User getPCWholesaler(HttpSession session) {
		return (User) session.getAttribute(WHOLESALER);
	}

	public static void setPCWholesaler(HttpSession session, User wholesaler) {
		session.setAttribute(WHOLESALER, wholesaler);
	}
	
	public static User getPCWholesaler(HttpServletRequest request) {
		return getPCWholesaler(request.getSession());
	}

	/**
	 * 获得实体店主登录信息
	 * 
	 * @param request
	 * @return
	 */
	public static User getShopkeeper(HttpServletRequest request) {
		return (User) request.getAttribute(SHOPKEEPER);
	}
	
	public static User getShopkeeper(HttpSession session) {
		return (User) session.getAttribute(SHOPKEEPER);
	}
	
	public static User getScanner(HttpServletRequest request) {
		return (User) request.getAttribute(SCANNER);
	}

	public void sendCode(String phone) {
		Random random = new Random();
		String code = (random.nextInt(899999) + 100000) + "";
		phoneCode.put(phone, code);
		SmsUtil.sendCode(phone, code);
	}
	
	/**
	 * 获得平台登录信息
	 * 
	 * @param request
	 * @return
	 */
	public static User getPlatform(HttpServletRequest request) {
		return (User) request.getAttribute(PLATFORM);
	}

	public static User getPCPlatform(HttpSession session) {
		return (User) session.getAttribute(PLATFORM);
	}

	public static void setPCPlatform(HttpSession session, User platform) {
		session.setAttribute(PLATFORM, platform);
	}
	
	public static User getPCPlatform(HttpServletRequest request) {
		return getPCPlatform(request.getSession());
	}
	
	/***
	 * 获取当天的时间戳
	 * @return
	 */
	public static long getTodayTime(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

}
