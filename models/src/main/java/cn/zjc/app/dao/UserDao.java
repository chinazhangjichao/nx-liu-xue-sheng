package cn.zjc.app.dao;

import cn.zjc.app.bean.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户 数据库操作接口
 * 
 * @author 张吉超
 *
 * @date 2021年8月6日
 */
@Mapper
public interface UserDao {
	/**
	 * 登录
	 * 
	 * @param userName
	 * @param userPwd
	 * @return
	 */
	public SysUser queryLogin(@Param("userName") String userName, @Param("userPwd") String userPwd);

	/**
	 * 查询记录总数
	 * 
	 * @return
	 */
	public int queryCount(@Param("roleId") Integer roleId, @Param("name") String name, @Param("status") Integer status);

	/**
	 * 分页查询
	 * 
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public List<SysUser> queryByPage(@Param("start") int start, @Param("pageSize") int pageSize, @Param("roleId") Integer roleId, @Param("name") String name, @Param("status") Integer status);

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
	 * 根据拍卖师证号查询
	 * 
	 * @param name
	 * @return
	 */
	public SysUser queryByAuctioneerNo(@Param("auctioneerNo") String auctioneerNo);

	/**
	 * 保存
	 * 
	 * @param obj
	 * @return
	 */
	public int save(SysUser obj);

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
	 *
	 * @param ids 用户id集合
	 * @param userPwd 默认密码
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
