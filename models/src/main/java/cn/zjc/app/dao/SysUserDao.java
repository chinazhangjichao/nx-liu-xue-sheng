package cn.zjc.app.dao;

import cn.zjc.app.bean.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author ZJC
 * @decription: 系统用户数据库操作接口
 * @date: 2022/1/11 14:58
 * @since JDK 1.8
 */
@Mapper
public interface SysUserDao {

	/**
	 * 查询记录总数
	 * @param name 姓名/昵称
	 * @param status 状态 0封停 1启用
	 * @return
	 */
	public int queryCount(@Param("name") String name, @Param("status") Integer status);

	/**
	 * 分页查询
	 * @param start 起始行数
	 * @param pageSize 每页大小
	 * @param name 姓名/昵称
	 * @param status 状态 0封停 1启用
	 * @return
	 */
	public List<SysUser> queryByPage(@Param("start") int start, @Param("pageSize") int pageSize,@Param("name") String name, @Param("status") Integer status);

	/**
	 * 根据创建时间查询（用于为工会批量生成用户时，查询该批次的用户信息）
	 * @param createTime
	 * @return
	 */
	public List<SysUser> queryByCreateTime(@Param("createTime") Long createTime);


	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return
	 */
	public SysUser queryById(@Param("id") int id);

	/**
	 * 根据登录用户名查询
	 * 
	 * @param name
	 * @return
	 */
	public SysUser queryByName(@Param("name") String name);

	/**
	 * 保存
	 * 
	 * @param obj
	 * @return
	 */
	public int save(SysUser obj);

	/**
	 * 批量保存
	 * @param objList
	 * @return
	 */
	public int saveList(@Param("objList") List<SysUser> objList);

	/**
	 * 修改
	 * 
	 * @param obj
	 * @return
	 */
	public int update(SysUser obj);

	/**
	 * 更新登录
	 * 
	 * @param obj
	 * @return
	 */
	public int updateLogin(SysUser obj);

	/**
	 * 更新离线
	 * 
	 * @param obj
	 * @return
	 */
	public int updateOffline(SysUser obj);

	/**
	 * 批量密码重置
	 * @param ids 重置的多个用户id
	 * @param userPwd 重置的密码
	 * @return
	 */
	public int updatePwd(@Param("ids") List<Integer> ids, @Param("userPwd") String userPwd);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	public int delete(int id);
}
