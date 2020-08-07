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
import des.wangku.operate.standard.Pv;
import des.wangku.operate.standard.desktop.DesktopConst;
import des.wangku.operate.standard.desktop.DesktopUtils;
import des.wangku.operate.standard.desktop.LoadTaskUtils;
import des.wangku.operate.standard.dialog.HelpDialog;
import des.wangku.operate.standard.dialog.RunDialog;
import des.wangku.operate.standard.utls.UtilsDialogState;
import des.wangku.operate.standard.utls.UtilsExtLibs;
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
	 * @param args String[]
	 */
	public static void main(String[] args) {
		/** 环境初始化 */
		Pv.Initialization();
		/** 初始化工程信息 */
		InitializationProject();
		shell.setImage(ImageConst.ACC_Shell);
		shell.setMinimumSize(new Point(120, 27));
		shell.setSize(912, 642);
		shell.setText(DesktopConst.ACC_ProjectTitleDefault);
		shell.setMenuBar(menu);
		UtilsDialogState.changeDialogCenter(shell);
		menuItemCommondList.setText("任务列表");
		menuItemCommondList.setEnabled(false);
		menuItemCommondList.setImage(ImageConst.ACC_M0task);
		menu_List = new Menu(menuItemCommondList);
		menuItemCommondList.setMenu(menu_List);

		initializationSetMenu();

		compositeMain.setBounds(0, 0, 900, 550);

		MenuItem menuVersionList = new MenuItem(menu, SWT.CASCADE);
		menuVersionList.setText("版本信息");
		menuVersionList.setImage(ImageConst.ACC_M0ver);
		Menu menuVersion = new Menu(menuVersionList);
		menuVersionList.setMenu(menuVersion);

		MenuItem menuItem_21 = new MenuItem(menuVersion, SWT.NONE);
		menuItem_21.setText("版本\tCtrl+S");
		menuItem_21.setAccelerator(SWT.CTRL + 'S');
		menuItem_21.setImage(ImageConst.getImagesIcon("version.gif"));
		menuItem_21.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				InputStream is = Const.class.getClassLoader().getResourceAsStream("update.info");
				HelpDialog ver = new HelpDialog(shell, 0, is);
				ver.open();
			}
		});
		MenuItem menuItem_exit = new MenuItem(menu, SWT.NONE);
		menuItem_exit.setText("退出");
		menuItem_exit.setAccelerator(SWT.CTRL + 'Q');
		menuItem_exit.setImage(ImageConst.ACC_M0exit);
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
		set.setImage(ImageConst.ACC_M0set);
		Menu menu_Set = new Menu(set);
		set.setMenu(menu_Set);
		MenuItem menuItem = new MenuItem(menu_Set, SWT.CASCADE);
		menuItem.setText("声音");
		menuItem.setImage(ImageConst.getImagesIcon("voice.ico"));
		Menu menuGroup = new Menu(shell, SWT.DROP_DOWN);
		menuItem.setMenu(menuGroup);
		{
			MenuItem m = new MenuItem(menuGroup, SWT.CHECK);
			boolean val = DesktopConst.isSysMSVwarning;
			m.setSelection(val);
			UtilsSWTMessageBox.VOICE_AlERT = val;
			m.setText("提示警告");
			m.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					UtilsSWTMessageBox.VOICE_AlERT = m.getSelection();
				}
			});
		}
		{
			MenuItem m = new MenuItem(menuGroup, SWT.CHECK);
			boolean val = DesktopConst.isSysMSVconfirm;
			m.setSelection(val);
			UtilsSWTMessageBox.VOICE_CONFIRM = val;
			m.setText("确认弹窗");
			m.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					UtilsSWTMessageBox.VOICE_CONFIRM = m.getSelection();
				}
			});

		}
		{
			MenuItem m = new MenuItem(menuGroup, SWT.CHECK);
			boolean val = DesktopConst.isSysMSVthread;
			m.setSelection(val);
			RunDialog.VOICE_RUNDIALOG = val;
			m.setText("线程结束");
			m.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					RunDialog.VOICE_RUNDIALOG = m.getSelection();
				}
			});
		}
		MenuItem menuItemRem = new MenuItem(menu_Set, SWT.CASCADE);
		menuItemRem.setText("记录");
		menuItemRem.setImage(ImageConst.getImagesIcon("Memory.ico"));
		Menu menuGroupRem = new Menu(shell, SWT.DROP_DOWN);
		menuItemRem.setMenu(menuGroupRem);
		{
			MenuItem m = new MenuItem(menuGroupRem, SWT.CHECK);
			m.setSelection(DesktopConst.isRemember_Input);
			m.setText("输入框记忆");
			m.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					DesktopConst.isRemember_Input = m.getSelection();
				}
			});
		}
	}

	/**
	 * 初始化工程信息
	 */
	static final void InitializationProject() {
		Utils.initOperaOrg();
		if(DesktopConst.isExtJarLibs) {
			String extPath=DesktopUtils.getJarBasicPathExtLibs();
			logger.debug("extPath:"+extPath);
			UtilsExtLibs.addSystemExtLibsCatalog(extPath);
		}
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
