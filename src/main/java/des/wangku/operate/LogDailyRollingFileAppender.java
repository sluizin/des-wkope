package des.wangku.operate;

import java.io.File;

import ch.qos.logback.core.FileAppender;

// import org.apache.log4j.FileAppender;

import des.wangku.operate.standard.Pv;

/**
 * log相对路径设置
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class LogDailyRollingFileAppender extends FileAppender<Object> {
	private static final String logPat = "log";

	public void setFile(String file) {
		Pv.Initialization();
		String path = Pv.getJarBasicPath();
		String fuhao = File.separator;
		String filepath = path + fuhao + logPat + fuhao + file;
		super.setFile(filepath);
	}
}
