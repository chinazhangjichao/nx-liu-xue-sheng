package cn.zjc.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription: 系统用户信息
 * @date: 2022/1/11 14:44
 * @since JDK 1.8
 */
@Data
@ToString
public class SysUser implements Serializable {
	private static final long serialVersionUID = -5759152480274193052L;
	/**
	 * 编号
	 */
	private Integer userId;

	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 密码
	 */
	@JsonIgnore
	private String userPwd;
	/**
	 * 真实姓名/昵称
	 */
	private String realName;
	/**
	 * 电话
	 */
	private String userPhone;
	/**
	 * 头像
	 */
	private String headImg;
	/**
	 * 性别
	 */
	private String userSex;
	/**
	 * 用户状态
	 */
	private Integer userStatus;
	/**
	 * 备注信息
	 */
	private String userRemark;

	/**
	 * 登录IP
	 */
	private String loginIp;
	/**
	 * 登录次数
	 */
	private Integer loginNum;
	/**
	 * 最后登录时间
	 */
	private Long loginTime;
	/**
	 * 最后登录时间
	 */
	private Long logoutTime;
	/**
	 * 创建时间
	 */
	private Long createTime;
	/**
	 * 最后修改
	 */
	private Long modifyTime;

	public SysUser() {
	}

	public SysUser(Integer userId) {
		this.userId = userId;
	}

	public SysUser(Integer userId, Integer userStatus) {
		this.userId = userId;
		this.userStatus = userStatus;
	}


}
