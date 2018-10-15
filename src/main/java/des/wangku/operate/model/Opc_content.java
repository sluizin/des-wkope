package des.wangku.operate.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class Opc_content implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String keyword;
	String content;
	Timestamp modify_time;



	public final String getKeyword() {
		return keyword;
	}

	public final void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public final String getContent() {
		return content;
	}

	public final void setContent(String content) {
		this.content = content;
	}

	public final Timestamp getModify_time() {
		return modify_time;
	}

	public final void setModify_time(Timestamp modify_time) {
		this.modify_time = modify_time;
	}

	@Override
	public String toString() {
		return "Opc_content [" + (keyword != null ? "keyword=" + keyword + ", " : "") + (content != null ? "content=" + content + ", " : "") + (modify_time != null ? "modify_time=" + modify_time : "") + "]";
	}



}
