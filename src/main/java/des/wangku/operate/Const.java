package des.wangku.operate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * 
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 *
 */
public class Const {
	static Logger logger = Logger.getLogger(Const.class);
	/** asm查找到的标准的父类 */
	static final String ACC_StandardTaskClass = "des/wangku/operate/standard/task/AbstractTask";
	/** 任务列表 */
	static final Map<String, Class<?>> extendTaskMap = new TreeMap<String, Class<?>>();
	/** 属性集，主要是从config_zh_CN.properties中读取的 */
	static final Properties ACC_Properties = getPropertiesConfig();
	/** 项目目录 */
	static final String ACC_modelpath = "model";
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
		PropertyConfigurator.configure(Const.class.getClassLoader().getResource("log4j.properties"));
	}

}
