package cn.zjc.app.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * @author ZJC
 * @decription: 系统角色信息
 * @date: 2022/1/11 14:44
 * @since JDK 1.8
 */
@Data
@ToString
public class SysRole implements Serializable {

	private static final long serialVersionUID = -3109368806866275687L;
	/** 编号 */
	private Integer roleId;
	/** 角色名称 */
	private String roleName;
	/** 角色描述 */
	private String roleDesc;

	public SysRole() {
	}

	public SysRole(Integer id) {
		this.roleId = id;
	}

	public SysRole(Integer roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}

}
