package des.wangku.operate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class Const {
	static Logger logger = LoggerFactory.getLogger(Const.class);

	/** 任务列表 */
	static final Map<String, TaskObjectClass> ExtendTaskMap = new TreeMap<>();
	
	/** 属性集，主要是从config_zh_CN.properties中读取的 */
	static final Properties ACC_Properties = getPropertiesConfig();
	/** 项目目录 */
	static final String ACC_modelpath = "model";

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

	static {
		//PropertyConfigurator.configure(Const.class.getClassLoader().getResource("log4j.properties"));
	}

}
