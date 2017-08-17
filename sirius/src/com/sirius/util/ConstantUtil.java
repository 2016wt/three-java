package com.sirius.util;

public class ConstantUtil {
	/**
	 * 商家可以考虑读取配置文件
	 */

	// 初始化
	public static final String APP_ID = "wx55a5736f5fcd77c2";// 微信开发平台应用id
	public static final String APP_SECRET = "7e92c5e65124d2ac112e7a23b9005d14";// 应用对应的凭证
	public static final String NOTIFY_URL = "http://www.isonetech.com/pay/wechatCallBack";// 应回调
	public static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	// 应用对应的密钥
	public static final String APP_KEY = "L8LrMqqeGRxST5reouB0K66CaYAWpqhAVsq7ggKkxHCOastWksvuX1uvmvQclxaHoYd3ElNBrNO2DHnnzgfVG9Qs473M3DTOZug5er46FhuGofumV8H2FVR9qkjSlC5K";
	public static final String PARTNER = "1343911701";// 财付通商户号
	public static final String PARTNER_KEY = "yL0VDTVGBGtpArlkbLh0ffD6VuJhpbn1";// 商户号对应的密钥
	public static final String TOKENURL = "https://api.weixin.qq.com/cgi-bin/token";// 获取access_token对应的url
	public static final String GRANT_TYPE = "client_credential";// 常量固定值
	public static final String EXPIRE_ERRCODE = "42001";// access_token失效后请求返回的errcode
	public static final String FAIL_ERRCODE = "40001";// 重复获取导致上一次获取的access_token失效,返回错误码
	public static final String GATEURL = "https://api.weixin.qq.com/pay/genprepay?access_token=";// 获取预支付id的接口url
	public static final String ACCESS_TOKEN = "access_token";// access_token常量值
	public static final String ERRORCODE = "errcode";// 用来判断access_token是否失效的值
	public static final String SIGN_METHOD = "sha1";// 签名算法常量值
	// package常量值
	public static final String packageValue = "bank_type=WX&body=%B2%E2%CA%D4&fee_type=1&input_charset=GBK&notify_url=http%3A%2F%2F127.0.0.1%3A8180%2Ftenpay_api_b2c%2FpayNotifyUrl.jsp&out_trade_no=2051571832&partner=1900000109&sign=10DA99BCB3F63EF23E4981B331B0A3EF&spbill_create_ip=127.0.0.1&time_expire=20131222091010&total_fee=1";
	public static final String traceid = "testtraceid001";// 测试用户id
}
