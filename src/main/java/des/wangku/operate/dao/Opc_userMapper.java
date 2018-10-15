package des.wangku.operate.dao;

import java.util.Map;

import des.wangku.operate.model.Opc_user;

/**
 * 
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 *
 */
public interface Opc_userMapper {
	public Opc_user getUser(Map<String,Object> map);
}
