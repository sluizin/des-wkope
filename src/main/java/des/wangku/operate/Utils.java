package des.wangku.operate;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import com.alibaba.fastjson.JSON;
import des.wangku.operate.dao.Opc_contentMapper;
import des.wangku.operate.dao.Opc_userMapper;
import des.wangku.operate.model.Opc_content;
import des.wangku.operate.model.Opc_user;
import des.wangku.operate.standard.TaskConsts;
import des.wangku.operate.standard.database.DBSource;
import des.wangku.operate.standard.desktop.LoadTaskUtils;
import des.wangku.operate.standard.desktop.TaskObjectClass;
import des.wangku.operate.standard.dialog.LoadingProgressBar;
import des.wangku.operate.standard.task.AbstractTask;
import des.wangku.operate.standard.utls.UtilsSWTMessageBox;
import des.wangku.operate.standard.utls.UtilsFile;
import static des.wangku.operate.standard.desktop.DesktopConst.ExtendTaskMap;
/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class Utils {
	/** 日志 */
	static Logger logger = LoggerFactory.getLogger(Utils.class);

	/**
	 * 任务菜单设置
	 * @param parent Menu
	 */
	static void remarkMenu(Menu parent) {
		if (parent == null) return;
		//Image taskImage = SWTResourceManager.getImage(Desktop.class, "/images/icon/star.gif");
		Composite compositeMini = new Composite(parent.getShell(), SWT.NONE);
		compositeMini.setEnabled(false);
		compositeMini.setVisible(false);
		compositeMini.setBounds(0, 0, 0, 0);
		sortMenu2(compositeMini, parent);
	}

	/**
	 * 对菜单进行排序并输出到菜单栏
	 * @param mini Composite
	 * @param parent Menu
	 */
	private static final void sortMenu2(Composite mini, Menu parent) {
		LoadTaskUtils.filterTaskObject(mini);
		for (String key : ExtendTaskMap.keySet()) {
			TaskObjectClass t = ExtendTaskMap.get(key);
			t.setMenu(Desktop.menu);
		}
		/*
		for (String key : ExtendTaskMap.keySet()) {
			TaskObjectClass t = ExtendTaskMap.get(key);
			logger.warn(getOutLines(t));
		}*/
		makeGroup(parent,ExtendTaskMap);
		for (String key : ExtendTaskMap.keySet()) {
			TaskObjectClass t = ExtendTaskMap.get(key);
			if (t.getGroup() != null && t.getGroup().length() > 0) continue;
			addMenuItem(parent,t);
		}
	}
	static String getOutLines(TaskObjectClass t) {
		String group=t.getGroup().length()==0?"NULL":t.getGroup();
		return t.isExpire() + "\t" +group+"\t"+ 
				t.getIdentifier() + "\t" + 
				t.getName();
	}
	/**
	 * 生成菜单
	 * @param parent Menu
	 * @param t TaskObjectClass
	 * @return MenuItem
	 */
	static final MenuItem addMenuItem(Menu parent,TaskObjectClass t) {
		MenuItem mi = new MenuItem(parent, SWT.NONE);
		String name = t.getMenuText();
		String state="";
		if (t.isExpire()) {
			mi.setEnabled(false);
			state="[已过期]";
		}
		if(!t.isEffective()) {
			mi.setEnabled(false);
			state="[已超期]";
		}
		mi.setText(name+state);
		mi.setImage(ImageConst.ACC_M0task_taskImage);
		mi.addListener(SWT.Selection, getMenuItemListener(t));
		mi.setID(t.getId());
		return mi;
	}
	/**
	 * 先按组进行显示菜单
	 * @param parent Menu
	 * @param map Map&lt;String, TaskObjectClass&gt;
	 */
	static final void makeGroup(Menu parent, Map<String, TaskObjectClass> map) {
		Map<String, List<TaskObjectClass>> newmap = new TreeMap<>();
		for (String key : map.keySet()) {
			TaskObjectClass t = map.get(key);
			if (t.getGroup() == null || t.getGroup().length() == 0) continue;
			if (!newmap.containsKey(t.getGroup())) newmap.put(t.getGroup(), new ArrayList<>());
			newmap.get(t.getGroup()).add(t);
		}
		for (String key : newmap.keySet()) {
			MenuItem menuItem = new MenuItem(parent, SWT.CASCADE);
			menuItem.setText(key);
			menuItem.setImage(ImageConst.ACC_M0task_taskGroupImage);
			Menu menuGroup = new Menu(parent.getShell(), SWT.DROP_DOWN);
			menuItem.setMenu(menuGroup);
			List<TaskObjectClass> list = newmap.get(key);
			for(TaskObjectClass e:list) {
				addMenuItem(menuGroup,e);
			}
		}

	}

	/**
	 * 菜单监听器
	 * @param toc TaskObjectClass
	 * @return Listener
	 */
	private static Listener getMenuItemListener(TaskObjectClass toc) {
		Listener t = new Listener() {
			public void handleEvent(Event e) {
				loadTaskObject(toc);
			}
		};
		return t;
	}
	/**
	 * 调取TaskObjectClass对象
	 * @param toc TaskObjectClass
	 */
	static final void loadTaskObject(TaskObjectClass toc) {
		if(toc==null)return;
		try {
			Desktop.compositeMain.dispose();
			Desktop.resetMainComposite();
			Constructor<?> c2 = toc.getMyClass().getDeclaredConstructor(new Class[] { Composite.class, int.class });
			LoadingProgressBar load = new LoadingProgressBar(Desktop.shell, 0);
			ExecutorService pool = Executors.newFixedThreadPool(1);
			Runnable task = (Runnable) load;//new LoadingProgressBar(Desktop.shell, 0);
			pool.submit(task);
			AbstractTask a2 = (AbstractTask) c2.newInstance(new Object[] { Desktop.compositeMain, SWT.NONE });
			//a2.setAnnoProjectMap(c.map);
			load.getShell().dispose();
			pool.shutdown();
			String error = a2.precondition();
			if (error != null) {
				Desktop.compositeMain.dispose();
				UtilsSWTMessageBox.Alert(Desktop.shell, error);
				return;
			}
			a2.startup();
			a2.setBaseTaskTOC(toc);
			a2.afterLoadProject();
			Desktop.repaintMainComposite();
			a2.afterRepaintComposite();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}
	/**
	 * 自动调用最新的自动装载的工程
	 */
	static final void autoLoadTask() {
		TaskObjectClass e=LoadTaskUtils.getNewestAutoLoadTask();
		if(e==null)	return;
		logger.debug("autoLoadTask:"+e.toString());
		loadTaskObject(e);
	}
	/**
	 * 初始化扩展库修改class环境变量
	 */
	static final void initExtLibs() {
		
	}
	/**
	 * 初始化运营中心组织结构信息
	 */
	static final void initOperaOrg() {
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
		SqlSession session = DBSource.getMybatisSession();
		if (session == null) return null;
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
		SqlSession session = DBSource.getMybatisSession();
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
