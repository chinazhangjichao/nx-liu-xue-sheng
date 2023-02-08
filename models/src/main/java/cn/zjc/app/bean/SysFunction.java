package cn.zjc.app.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription: 系统功能模块信息
 * @date: 2022/1/11 14:44
 * @since JDK 1.8
 */
@Data
@ToString
public class SysFunction implements Serializable {

	private static final long serialVersionUID = -3694014187000378809L;
	/**
	 * 编号
	 */
	private Integer funId;
	/**
	 * 功能名称
	 */
	private String funName;
	/**
	 * 访问地址
	 */
	private String funUrl;
	/**
	 * 所属父模块
	 */
	private Integer funParent;
	/**
	 * 功能等级
	 */
	private Integer funLevel;
	/**
	 * 判断是否有该功能，数据库不保存
	 */
	private int checked;


}
