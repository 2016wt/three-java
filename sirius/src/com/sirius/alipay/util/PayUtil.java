package com.sirius.alipay.util;

import java.net.URLEncoder;

import com.sirius.entity.PayRecord;

public class PayUtil {
	public static String getNewOrderInfo(PayRecord payRecord) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(payRecord.getOrderNo());
		sb.append("\"&subject=\"");
		sb.append(payRecord.getDescribe());
		sb.append("\"&body=\"");
		sb.append(payRecord.getDescribe());
		sb.append("\"&total_fee=\"");
		sb.append(payRecord.getMoney());
		sb.append("\"&notify_url=\"");
		// 网址需要做URL编码
		sb.append(URLEncoder.encode("http://www.isonetech.com/pay/alipayCallback"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
