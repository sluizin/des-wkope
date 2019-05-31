package des.wangku.operate;

import java.util.HashMap;
import java.util.Map;

/**
 * 扩展对象
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class TaskObjectClass {
	Map<String, Object> map = new HashMap<>();
	String classFile = null;
	Class<?> myClass = null;
	String identifier = null;
	String name = null;
	String menuText = null;
	boolean expire = false;
	String group = "";
	/** 通过日期判断是否有效 */
	boolean effective = true;

	static final String ACC_annoExpire = "expire";

	/**
	 * 得到项目是否过期
	 * @return boolean
	 */
	boolean getAnnoExpire() {
		if (!map.containsKey(ACC_annoExpire)) return false;
		Object obj = map.get(ACC_annoExpire);
		return obj == null ? false : Boolean.parseBoolean(obj.toString());
	}

	static final String ACC_annoIdentifier = "identifier";

	/**
	 * 得到项目编号
	 * @return String
	 */
	String getAnnoIdentifier() {
		if (!map.containsKey(ACC_annoIdentifier)) return null;
		Object obj = map.get(ACC_annoIdentifier);
		return obj == null ? null : obj.toString();
	}

	static final String ACC_annoName = "name";

	/**
	 * 得到名称
	 * @return String
	 */
	String getAnnoName() {
		if (!map.containsKey(ACC_annoName)) return null;
		Object obj = map.get(ACC_annoName);
		return obj == null ? null : obj.toString();
	}

	static final String ACC_annoGroup = "group";

	/**
	 * 得到组名
	 * @return String
	 */
	String getAnnoGroup() {
		if (!map.containsKey(ACC_annoGroup)) return "";
		Object obj = map.get(ACC_annoGroup);
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 返回名称，如没设置名称，则返回null
	 * @return String
	 */
	String getMenuText() {
		if (menuText != null) return menuText;
		return getAnnoName();
	}

	@Override
	public String toString() {
		return "TaskObjectClass [map=" + map + ", classFile=" + classFile + ", myClass=" + myClass + ", identifier=" + identifier + ", name=" + name + ", menuText=" + menuText + ", expire=" + expire + ", group=" + group + "]";
	}

}
