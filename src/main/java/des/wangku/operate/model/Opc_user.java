package des.wangku.operate.model;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * 管理人员帐号信息
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 *
 */
public class Opc_user implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id;
	String username;
	String password;
	Timestamp modify_time;

	public final int getId() {
		return id;
	}

	public final void setId(int id) {
		this.id = id;
	}

	public final String getUsername() {
		return username;
	}

	public final void setUsername(String username) {
		this.username = username;
	}

	public final String getPassword() {
		return password;
	}

	public final void setPassword(String password) {
		this.password = password;
	}

	public final Timestamp getModify_time() {
		return modify_time;
	}

	public final void setModify_time(Timestamp modify_time) {
		this.modify_time = modify_time;
	}

	@Override
	public String toString() {
		return "Opc_user [id=" + id + ", " + (username != null ? "username=" + username + ", " : "") + (password != null ? "password=" + password + ", " : "") + (modify_time != null ? "modify_time=" + modify_time : "") + "]";
	}

}
