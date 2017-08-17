package com.sirius.submail;


/**
 * A class obtain the value and create configuration object by loading file
 * app_config.properties,including creating <strong>MailConfig<strong> and
 * creating <strong>MessageConfig<strong>.
 * 
 * @see AppConfig
 * @see MailConfig
 * @see MessageConfig
 * @version 1.0 at 2014/10/28
 * */
public class ConfigLoader {

	//private static Properties pros = null;
	/**
	 * Loading file while class loading.The operation inside of static block
	 * will be run once.
	 * */
//	static {
//		pros = new Properties();
//		try {
//			pros.load(ConfigLoader.class
//					.getResourceAsStream("/app_config.properties"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * enum define two kinds of configuration.
	 * */
	public static enum ConfigType {
		Mail, Message
	};

	/**
	 * A static method for outer class to create configuration by loading file.
	 * 
	 * @param type
	 *            ConfigType
	 * @return If the type is ConfigType#Mail,return the instance of
	 *         {@link MailConfig}. And,if the type is ConfigType#Message,return
	 *         the instance of {@link MessageConfig}.
	 * */
	public static AppConfig load(ConfigType type) {
		switch (type) {
		case Message:
			return createMessageConfig();
		default:
			return null;
		}
	}



	private static AppConfig createMessageConfig() {
		AppConfig config = new MessageConfig();
		config.setAppId("10698");
		config.setAppKey("ad816a0b742b7d7411c03dd478018eae");
		config.setSignType("normal");
		return config;
	}

}
