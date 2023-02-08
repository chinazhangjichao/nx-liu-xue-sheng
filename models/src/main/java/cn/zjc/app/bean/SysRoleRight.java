package cn.zjc.app.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription: 角色权限信息
 * @date: 2022/1/11 14:50
 * @since JDK 1.8
 */
@Data
@ToString
public class SysRoleRight implements Serializable {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 3648911328211313272L;
	private Integer rrId;
	/**
	 * 角色
	 */
	private Integer rrRole;
	/**
	 * 功能
	 */
	private SysFunction rrFun;
	
	public SysRoleRight() {
	}
	
	

	public SysRoleRight(Integer rrRole, SysFunction rrFun) {
		super();
		this.rrRole = rrRole;
		this.rrFun = rrFun;
	}



}
