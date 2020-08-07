package des.wangku.operate;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.SWTResourceManager;



/**
 * 图标常量
 * 
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class ImageConst {

	/** images目录 */
	public static final String ACC_Images = "/images";
	/** images/icon目录 */
	public static final String ACC_ImagesIcon = ACC_Images+"/icon";
	/** 主窗口的图标 */
	public static final Image ACC_Shell = getImagesIcon("favicon.ico");
	/** 任务菜单前的图标 */
	public static final Image ACC_M0task_taskImage = getImagesIcon("star.gif");
	/** 任务数组前的图标 */
	public static final Image ACC_M0task_taskGroupImage = getImagesIcon("key_list.gif");
	/** 主菜单中第一行中主任务 */
	public static final Image ACC_M0task = getImagesIcon("task.gif");
	/** 主菜单中第一行中设置 */
	public static final Image ACC_M0set = getImagesIcon("set.gif");
	/** 主菜单中第一行中版本 */
	public static final Image ACC_M0ver = getImagesIcon("ver.gif");
	/** 主菜单中第一行中退出 */
	public static final Image ACC_M0exit = getImagesIcon("exist.gif");
	/**
	 * 得到桌面包中图片 /images/
	 * @param filename String
	 * @return Image
	 */
	public static final Image getImages(String filename) {
		if (filename == null) return null;
		return SWTResourceManager.getImage(ImageConst.class, ACC_Images + "/" + filename);
	}
	/**
	 * 得到桌面包中图标图片 /images/icon/
	 * @param filename String
	 * @return Image
	 */
	public static final Image getImagesIcon(String filename) {
		if (filename == null) return null;
		return SWTResourceManager.getImage(ImageConst.class, ACC_ImagesIcon + "/" + filename);
	}
}
