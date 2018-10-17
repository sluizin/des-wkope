package des.wangku.operate;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import des.wangku.operate.model.Opc_user;
import des.wangku.operate.standard.PV;
import des.wangku.operate.standard.PV.Env;
import des.wangku.operate.standard.task.AbstractTask;
import des.wangku.operate.standard.utls.UtilsDialogState;
import des.wangku.operate.standard.utls.UtilsSWTMessageBox;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Group;

/**
 * 登陆系统
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class Login extends Composite {
	/** 日志 */
	static Logger logger = Logger.getLogger(Login.class);
	Group group = new Group(this, SWT.NONE);
	Label label_1 = new Label(group, SWT.NONE);
	Label label = new Label(group, SWT.NONE);
	private Text textUsername = new Text(group, SWT.BORDER);
	private Text textPassword = new Text(group, SWT.BORDER);
	Button submit = new Button(group, SWT.NONE);
	private Label label_2 = new Label(group, SWT.NONE);

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Login(Composite parent, int style) {
		super(parent, style);
		this.setBounds(0, 0, AbstractTask.ACC_CpsWidth, AbstractTask.ACC_CpsHeight);
		if (PV.ACC_ENV == Env.DEV) {
			textUsername.setText(getValueSpace("username"));
			textPassword.setText(getValueSpace("password"));
		}
		group.setText("登陆");
		group.setBounds(404, 230, 322, 233);
		UtilsDialogState.changeDialogCenter(this, group);

		textUsername.setBounds(152, 43, 114, 18);
		textUsername.addKeyListener(getKeyListener(parent));
		label.setBounds(57, 46, 54, 15);
		label.setText("用户名");

		label_1.setBounds(57, 84, 54, 15);
		label_1.setText("密码");

		textPassword.setBounds(152, 81, 114, 18);
		textPassword.setEchoChar('*');
		textPassword.addKeyListener(getKeyListener(parent));
		label_2.setAlignment(SWT.CENTER);

		label_2.setBounds(101, 118, 165, 12);

		submit.setBounds(117, 152, 72, 22);
		submit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!(islogin())) return;
				loginchk(parent);
			}
		});
		submit.setText("登陆");
	}

	/**
	 * 登陆
	 * @param parent Composite
	 */
	void loginchk(Composite parent) {
		UtilsSWTMessageBox.Alert(parent.getShell(), "登陆成功", "欢迎[" + Desktop.getAdminLevel() + "]进入系统!");
		Shell s = submit.getShell();
		Desktop.menuItemCommondList.setEnabled(true);
		submit.getParent().dispose();
		s.setLayout(new FillLayout());
		s.layout();
	}

	/**
	 * 输入框回车输入
	 * @param parent Composite
	 * @return KeyListener
	 */
	KeyListener getKeyListener(Composite parent) {
		KeyListener t = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
					if (!(islogin())) return;
					loginchk(parent);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		};
		return t;
	}

	/**
	 * @return boolean
	 */
	boolean islogin() {
		String username = textUsername.getText().trim();
		String password = textPassword.getText().trim();
		if (username.length() == 0) {
			label_2.setText("用户名不为空");
			textUsername.setFocus();
			return false;
		}
		if (password.length() == 0) {
			label_2.setText("密码不为空");
			textPassword.setFocus();
			return false;
		}
		Properties properties = Const.ACC_Properties;

		String confusername = "admin", confpassword = "admin1234";
		if (properties != null) {
			confusername = properties.getProperty("username");
			confpassword = properties.getProperty("password");
		}
		if (username.equals(confusername) && password.equals(confpassword)) {
			return Desktop.isAdmin = true;
		}
		if(Const.isLinkUserDB) {
			Opc_user user = Utils.getDBUser(username, password);
			if (user != null) { return true; }			
		}
		label_2.setText("用户名与密码不正确！");
		textPassword.setFocus();
		return false;
	}
	/**
	 * 得到配置内容
	 * @param key String
	 * @return String
	 */
	String getValue(String key) {
		if (Const.ACC_Properties == null) return null;
		return Const.ACC_Properties.getProperty(key);
	}
	/**
	 * 得到配置内容 为null时则返回""
	 * @param key String
	 * @return String
	 */
	String getValueSpace(String key) {
		String value=getValue(key);
		if(value==null)return "";
		return value;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
