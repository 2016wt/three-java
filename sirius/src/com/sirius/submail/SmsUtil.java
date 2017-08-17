package com.sirius.submail;


public class SmsUtil {

//	private static Logger log = LoggerFactory.getLogger(SmsUtil.class);

	
	
	public static void sendCode(String phone,String code){
		AppConfig config = ConfigLoader.load(ConfigLoader.ConfigType.Message);
		MESSAGEXsend submail = new MESSAGEXsend(config);
		submail.addTo(phone);
		submail.setProject("HC1AF2");
		submail.addVar("code", code);
		submail.xsend();
	}
}