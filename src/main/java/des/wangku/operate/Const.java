package des.wangku.operate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 常量池
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class Const {
	/* 日志 */
	static Logger logger = LoggerFactory.getLogger(Const.class);

	/** 属性集，主要是从config_zh_CN.properties中读取的 */
	static final Properties ACC_Properties = getPropertiesConfig();

	/** 桌面类 */
	static final Class<?> ACC_DesktopClazz = Desktop.class;


	/** 是否连接用户数据库 */
	static final boolean isLinkUserDB = false;

	/**
	 * 从config配置文件中读取配置对象
	 * @return Properties
	 */
	public static final Properties getPropertiesConfig() {
		Properties properties = new Properties();
		InputStream is2 = Const.class.getClassLoader().getResourceAsStream("config_zh_CN.properties");
		try {
			properties.load(is2);
			return properties;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
