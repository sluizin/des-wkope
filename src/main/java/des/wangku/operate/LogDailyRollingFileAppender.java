package des.wangku.operate;

import java.io.File;

import org.apache.log4j.FileAppender;

import des.wangku.operate.standard.PV;

/**
 * log相对路径设置
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class LogDailyRollingFileAppender extends FileAppender {

	public void setFile(String file) {
		String path = PV.getJarBasicPath();
		String fuhao = File.separator;
		String filepath = path + fuhao + file;
		super.setFile(filepath);
	}
}
