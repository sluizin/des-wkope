package des.wangku.operate;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Menu;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wb.swt.SWTResourceManager;

import des.wangku.operate.standard.Pv;
import des.wangku.operate.standard.desktop.DesktopUtils;
import des.wangku.operate.standard.desktop.LoadTaskUtils;
import des.wangku.operate.standard.dialog.RunDialog;
import des.wangku.operate.standard.dialog.Version;
import des.wangku.operate.standard.utls.UtilsConsts;
import des.wangku.operate.standard.utls.UtilsDialogState;
import des.wangku.operate.standard.utls.UtilsSWTListener;
import des.wangku.operate.standard.utls.UtilsSWTMessageBox;
import des.wangku.operate.standard.utls.UtilsSWTTray;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;

/**
 * 桌面主程序
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public final class Desktop {
	/** 日志 */
	static Logger logger = LoggerFactory.getLogger(Desktop.class);
	static boolean isAdmin = false;
	static final int ACC_width = 900;
	static final int ACC_height = 550;
	static Display display = Display.getDefault();
	static Shell shell = new Shell(display, SWT.CLOSE | SWT.MIN | SWT.DIALOG_TRIM);
	/** 窗体顶部菜单 */
	static Menu menu = new Menu(shell, SWT.BAR);
	/** 具体的任务菜单 */
	static Menu menu_List;
	/** 窗体第一列菜单 */
	static MenuItem menuItemCommondList = new MenuItem(menu, SWT.CASCADE);
	static Composite compositeMain = new Composite(shell, SWT.NONE);
	static Composite compositeStates = null;
	static Composite compositeStatesInsert = null;
	static Label insertlabel = null;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		/** 环境初始化 */
		Pv.Initialization();
		/** 初始化工程信息 */
		InitializationProject();
		shell.setImage(SWTResourceManager.getImage(Desktop.class, "/images/icon/exit.gif"));
		shell.setMinimumSize(new Point(120, 27));
		shell.setSize(912, 642);
		shell.setText(UtilsConsts.ACC_ProjectTitleDefault);
		shell.setMenuBar(menu);
		UtilsDialogState.changeDialogCenter(shell);
		menuItemCommondList.setText("任务列表");
		menuItemCommondList.setEnabled(false);
		menu_List = new Menu(menuItemCommondList);
		menuItemCommondList.setMenu(menu_List);

		initializationSetMenu();

		compositeMain.setBounds(0, 0, 900, 550);

		/**
		 * 主框架中的鼠标右键
		 * Menu compositeMainMenu = new Menu(compositeMain);
		 * MenuItem miversion = new MenuItem(compositeMainMenu, SWT.NONE);
		 * miversion.setText("版本说明");
		 * miversion.addListener(SWT.Selection, UtilsSWTListener.getListenerShowVersion(Desktop.class, "/update.info"));
		 */
		MenuItem menuVersionList = new MenuItem(menu, SWT.CASCADE);
		menuVersionList.setText("版本信息");
		Menu menuVersion = new Menu(menuVersionList);
		menuVersionList.setMenu(menuVersion);

		MenuItem menuItem_21 = new MenuItem(menuVersion, SWT.NONE);
		menuItem_21.setText("版本\tCtrl+S");
		menuItem_21.setAccelerator(SWT.CTRL + 'S');
		menuItem_21.setImage(SWTResourceManager.getImage(Desktop.class, "/images/icon/version.gif"));
		menuItem_21.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				InputStream is = Const.class.getClassLoader().getResourceAsStream("update.info");
				Version ver = new Version(shell, 0, is);
				ver.open();
			}
		});
		MenuItem menuItem_exit = new MenuItem(menu, SWT.NONE);
		menuItem_exit.setText("退出");
		menuItem_exit.setAccelerator(SWT.CTRL + 'Q');
		menuItem_exit.setImage(SWTResourceManager.getImage(Desktop.class, "/images/icon/exist.gif"));
		menuItem_exit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				DesktopUtils.existProject(compositeMain);
			}
		});
		Initialization();
		UtilsSWTTray.initTrayDisplay(shell, display);
		//initializationStatus();
		if (!isAdmin) {
			compositeMain.dispose();
			resetMainComposite();
			new Login(compositeMain, SWT.NONE);
			compositeMain.setLayout(new FillLayout());
			compositeMain.setBounds(0, 0, ACC_width, ACC_height);
			shell.open();
		}
		while (!shell.isDisposed()) {
			if (display != null && !display.readAndDispatch()) {
				display.sleep();
			}
		}
		if (display != null) display.dispose();
	}

	@SuppressWarnings("unused")
	static final void initializationStatus() {
		CoolBar coolBar = new CoolBar(shell, SWT.FLAT);
		coolBar.setBounds(0, 558, 150, 30);
		CoolItem coolItem = new CoolItem(coolBar, SWT.NONE);
		coolItem.setSize(100, 20);
		ToolBar toolbar = new ToolBar(coolBar, SWT.NONE);
		ToolItem getItem = new ToolItem(toolbar, SWT.PUSH);
		getItem.setText("取得");
		coolItem.setControl(toolbar);
		CoolItem coolItem_1 = new CoolItem(coolBar, SWT.NONE);
		CoolItem coolItem_2 = new CoolItem(coolBar, SWT.NONE);
	}

	/**
	 * 重置容器
	 */
	static final void resetMainComposite() {
		//MainDesktopComposite.resetMainComposite(shell,compositeMain, ACC_width, ACC_height);
		compositeMain = new Composite(shell, SWT.NONE);
		compositeMain.setLayout(new FillLayout());
		compositeMain.redraw(0, 0, ACC_width, ACC_height, true);
	}

	/**
	 * 设置完容器后刷新
	 */
	static final void repaintMainComposite() {
		//MainDesktopComposite.repaintMainComposite(compositeMain, ACC_width, ACC_height);
		compositeMain.setLayout(new FillLayout());
		compositeMain.setBounds(0, 0, ACC_width, ACC_height);
	}

	/**
	 * 设置鼠标右键
	 */
	@Deprecated
	static final void setPopMenu() {
		/** 主框架中的鼠标右键 */
		Menu compositeMainMenu = new Menu(compositeMain);
		MenuItem miversion = new MenuItem(compositeMainMenu, SWT.NONE);
		miversion.setText("版本说明");
		miversion.addListener(SWT.Selection, UtilsSWTListener.getListenerShowVersion(Desktop.class, "/update.info"));
	}

	/**
	 * 初始化
	 */
	static final void Initialization() {
		LoadTaskUtils.getModelJarList();
		Utils.remarkMenu(menu_List);
	}

	/**
	 * 修改设置菜单
	 */
	static final void initializationSetMenu() {
		MenuItem set = new MenuItem(menu, SWT.CASCADE);
		set.setText("设置");
		set.setEnabled(true);
		Menu menu_Set = new Menu(set);
		set.setMenu(menu_Set);
		MenuItem menuItem = new MenuItem(menu_Set, SWT.CASCADE);
		menuItem.setText("声音");
		menuItem.setImage(SWTResourceManager.getImage(Desktop.class, "/images/icon/voice.ico"));
		Menu menuGroup = new Menu(shell, SWT.DROP_DOWN);
		menuItem.setMenu(menuGroup);
		MenuItem m1 = new MenuItem(menuGroup, SWT.CHECK);
		m1.setSelection(false);
		m1.setText("提示警告");
		m1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				UtilsSWTMessageBox.VOICE_AlERT = m1.getSelection();
			}
		});
		MenuItem m2 = new MenuItem(menuGroup, SWT.CHECK);
		m2.setSelection(false);
		m2.setText("确认弹窗");
		m2.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				UtilsSWTMessageBox.VOICE_CONFIRM = m2.getSelection();
			}
		});
		MenuItem m3 = new MenuItem(menuGroup, SWT.CHECK);
		m3.setSelection(false);
		m3.setText("线程结束");
		m3.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				RunDialog.VOICE_RUNDIALOG = m3.getSelection();
			}
		});
	}

	/**
	 * 初始化运营结构
	 */
	static final void InitializationProject() {
		Utils.initOperaOrg();
	}

	/**
	 * 得到登陆人员的等级。分为最高管理员与管理员
	 * @return String
	 */
	public static final String getAdminLevel() {
		if (isAdmin) return "最高管理员";
		return "管理员";
	}
}
