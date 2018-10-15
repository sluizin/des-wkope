package des.wangku.operate;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.objectweb.asm.ClassReader;

import com.alibaba.fastjson.JSON;

import des.wangku.operate.dao.Opc_contentMapper;
import des.wangku.operate.dao.Opc_userMapper;
import des.wangku.operate.model.Opc_content;
import des.wangku.operate.model.Opc_user;
import des.wangku.operate.standard.PV;
import des.wangku.operate.standard.TaskConsts;
import des.wangku.operate.standard.database.MainSource;
import des.wangku.operate.standard.dialog.LoadingProgressBar;
import des.wangku.operate.standard.PV.Env;
import des.wangku.operate.standard.task.AbstractTask;
import des.wangku.operate.standard.utls.UtilsPathFile;
import des.wangku.operate.standard.utls.UtilsFile;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class Utils {

	static Logger logger = Logger.getLogger(Utils.class);

	public static final List<String> getModelJarList() {
		//logger.debug("System.getProperty(\"user.dir\"):"+System.getProperty("user.dir"));
		//logger.debug("pathgetProjectPath:" + UtilsPathFile.getProjectPath());
		String path = getModelJarBasicPath();
		File file = new File(path);
		if (!file.exists()) file.mkdirs();
		//logger.debug("path:" + path);
		List<String> jarList = getJarList(path);
		for (int i = 0; i < jarList.size(); i++) {
			String jarPath = jarList.get(i);
			//logger.debug("jarList[" + i + "]:" + jarPath);
			isSearchJar(jarPath);
		}
		return jarList;
	}

	@SuppressWarnings("resource")
	static final boolean isSearchJar(String jarPath) {
		try {
			JarFile jarFile = new JarFile(jarPath);
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				if (jarEntry.isDirectory() || (!jarEntry.getName().endsWith(".class"))) continue;
				//logger.debug(">>:" + jarEntry.getName());
				InputStream input = jarFile.getInputStream(jarEntry);
				ClassReader cr = new ClassReader(input);
				MQClassVisitor mqcv = new MQClassVisitor();
				cr.accept(mqcv, 0);
				cr = null;
				if (mqcv.classFile == null) continue;
				if (!mqcv.isStructure1) {
					logger.debug("[\"+jarEntry.getName()+\"]缺少构造函数 参数:(Lorg/eclipse/swt/widgets/Composite;)");
					continue;
				}
				if (!mqcv.isStructure2) {
					logger.debug("[" + jarEntry.getName() + "]缺少构造函数 参数:(Lorg/eclipse/swt/widgets/Composite;I)");
					continue;
				}
				URL url1 = new URL("file:" + jarPath);
				URLClassLoader myClassLoader = new URLClassLoader(new URL[] { url1 }, Thread.currentThread().getContextClassLoader());
				Class<?> myClass1 = myClassLoader.loadClass(mqcv.classFile);
				Const.extendTaskMap.put(mqcv.classFile, myClass1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Const.extendTaskMap.isEmpty()) return false;
		return true;
	}

	/**
	 * 得到某个目录里所有的jar文件
	 * @param path String
	 * @return List<String>
	 */
	public static final List<String> getJarList(String path) {
		List<String> jarList = new ArrayList<String>();
		UtilsPathFile.getJarList(jarList, path);
		return jarList;
	}

	/**
	 * 得到model绝对目录
	 * @return String
	 */
	public static final String getModelJarBasicPath() {
		if (PV.ACC_ENV == Env.DEV) return "D:/Eclipse/eclipse-oxygen/Workspaces/des-wkope/build/libs/" + Const.ACC_modelpath;
		URL c = Const.class.getClassLoader().getResource("");
		//logger.debug(" Config.class.getClassLoader().getResource:" + c);
		if (c == null) {
			return UtilsPathFile.getJarBasicPath() + "" + "/" + Const.ACC_modelpath;
		} else {
			try {
				File file = new File(c.toURI().getPath());
				String filePath = file.getAbsolutePath();//得到windows下的正确路径
				//logger.debug(" Config.class.getClassLoader().getResource c.toURI().getPath():" + filePath);
				return filePath + "/" + Const.ACC_modelpath;
			} catch (URISyntaxException e) {
				return "";
			}
		}
	}

	/**
	 * 任务菜单设置
	 * @param parent Menu
	 */
	static void remarkMenu(Menu parent){
		if (parent == null) return;
		Image taskImage = SWTResourceManager.getImage(Desktop.class, "/images/icon/star.gif");
		Composite compositeMini = new Composite(parent.getShell(), SWT.NONE);
		compositeMini.setEnabled(false);
		compositeMini.setVisible(false);
		compositeMini.setBounds(0, 0, 0, 0);
		/** 排序菜单 */
		Map<String, Class<?>> extendTaskMapOrder = new TreeMap<String, Class<?>>();
		try {
			for (String key : Const.extendTaskMap.keySet()) {
				Class<?> cc = Const.extendTaskMap.get(key);
				Constructor<?> c1 = cc.getDeclaredConstructor(new Class[] { Composite.class });
				AbstractTask a1 = (AbstractTask) c1.newInstance(new Object[] { compositeMini });//Desktop.compositeMini
				extendTaskMapOrder.put(a1.getMenuText(), cc);
			}
		} catch (Exception excep) {
			excep.printStackTrace();
		}
		for (String key : extendTaskMapOrder.keySet()) {
			MenuItem menuItem = new MenuItem(parent, SWT.NONE);
			Class<?> c = extendTaskMapOrder.get(key);
			menuItem.setText(key);
			menuItem.setImage(taskImage);
			menuItem.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					try {
						Desktop.compositeMain.dispose();
						Desktop.resetMainComposite();
						Constructor<?> c2 = c.getDeclaredConstructor(new Class[] { Composite.class, int.class });
						LoadingProgressBar load = new LoadingProgressBar(Desktop.shell, 0);
						ExecutorService pool = Executors.newFixedThreadPool(1);
						Runnable task = (Runnable) load;//new LoadingProgressBar(Desktop.shell, 0);
						pool.submit(task);
						@SuppressWarnings("unused")
						AbstractTask a2 = (AbstractTask) c2.newInstance(new Object[] { Desktop.compositeMain, SWT.NONE });
						load.getShell().dispose();
						pool.shutdown();
						Desktop.repaintMainComposite();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
		}

	}

	/**
	 * 初始化运营中心组织结构信息
	 */
	static void InitOperaOrg() {
		String content = getContentFromDB();
		if (content == null) {
			logger.debug("des-wkope-task-opera-org Originate from the Local document");
			String filename = "/json/des-wkope-task-opera-org.json";
			InputStream in = Utils.class.getResourceAsStream(filename);
			content = UtilsFile.readFile(in).toString();
		} else {
			logger.debug("des-wkope-task-opera-org Originate from the database");
		}
		TaskConsts.operOrg = JSON.parseObject(content, des.wangku.operate.standard.unit.OperOrg.class);
	}

	/**
	 * 通过网络得到组织值 如果为空或网络不通，则返回null
	 * @return String
	 */
	static final String getContentFromDB() {
		String content = "";
		SqlSession session = MainSource.getSession();
		Opc_contentMapper mapper = session.getMapper(Opc_contentMapper.class);
		try {
			Opc_content user = mapper.selectByKey("opera-org");
			if (user == null) return null;
			content = user.getContent();
			/* session.commit(); */
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return null;
	}

	/**
	 * 通过帐号与密码得到会员信息
	 * @param username String
	 * @param password String
	 * @return Opc_user
	 */
	static final Opc_user getDBUser(String username, String password) {
		SqlSession session = MainSource.getSession();
		Opc_userMapper mapper = session.getMapper(Opc_userMapper.class);
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("username", username);
			map.put("password", password);
			Opc_user user = mapper.getUser(map);
			if (user == null) return null;
			session.commit();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return null;
	}
}
