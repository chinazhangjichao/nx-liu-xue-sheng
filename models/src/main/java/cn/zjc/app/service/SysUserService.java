package cn.zjc.app.service;

import cn.zjc.app.bean.SysUser;

import java.util.List;

public interface SysUserService {


	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @param name 用户名或姓名
	 * @param status 用户状态 1-正常 0-封停
	 * @return
	 */
	public List<SysUser> queryByPage(int pageNo, int pageSize, String name, Integer status);

	/**
	 * 查询记录总数
	 * @param name
	 * @param status
	 * @return
	 */
	public int queryCount(String name, Integer status);

	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return
	 */
	public SysUser queryById(Integer id);

	/**
	 * 根据用户名查询
	 * 
	 * @param userName
	 * @return
	 */
	public SysUser queryByName(String userName);

	/**
	 * 保存或更新
	 * 
	 * @param obj
	 * @return
	 */
	public int saveOrUpdate(SysUser obj);

	/**
	 * 更新登录信息
	 *
	 * @param obj
	 * @return
	 */
	public int updateLogin(SysUser obj);
	/**
	 * 更新离线信息
	 * 
	 * @param obj
	 * @return
	 */
	public int updateOffline(SysUser obj);

	/**
	 * 根据多个id批量重置密码
	 * 
	 * @param ids
	 * @param userPwd
	 * @return
	 */
	public int updatePwd(List<Integer> ids, String userPwd);

	/**
	 * 根据id删除
	 * 
	 * @param id
	 * @return
	 */
	public int delete(Integer id);
}
