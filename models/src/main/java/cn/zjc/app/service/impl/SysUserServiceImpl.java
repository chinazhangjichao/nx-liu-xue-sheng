package cn.zjc.app.service.impl;

import cn.zjc.app.bean.SysRole;
import cn.zjc.app.bean.SysUser;
import cn.zjc.app.bean.SysUserRole;
import cn.zjc.app.dao.SysUserDao;
import cn.zjc.app.dao.SysUserRoleDao;
import cn.zjc.app.service.SysUserService;
import cn.zjc.app.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户/员工 业务逻辑
 * @author 张吉超
 *
 * @date 2021年3月14日
 */
@Service("sysUserService")
@Transactional
public class SysUserServiceImpl implements SysUserService {
	@Resource
	private SysUserDao sysUserDao;
	@Resource
	private SysUserRoleDao sysUserRoleDao;

	@Override
	public List<SysUser> queryByPage(int pageNo, int pageSize, String name, Integer status) {
		return sysUserDao.queryByPage((pageNo - 1) * pageSize, pageSize, name,status);
	}
	@Override
	public int queryCount(String name,Integer status) {
		return sysUserDao.queryCount( name,status);
	}

	@Override
	public SysUser queryById(Integer id) {
		if (!AppUtil.isVaildId(id))
			return null;
		return sysUserDao.queryById(id);
	}

	@Override
	public SysUser queryByName(String userName) {
		if(!StringUtils.hasLength(userName))
			return null;
		return sysUserDao.queryByName(userName);
	}


	@Override
	public int saveOrUpdate(SysUser obj) {
		if (null == obj)
			return 0;
		SysUser user = this.sysUserDao.queryByName(obj.getUserName());
		if (!AppUtil.isVaildId(obj.getUserId())) {
			if(null!=user){
				throw  new IllegalArgumentException("用户名"+obj.getUserName()+"已存在！");
			}
			return sysUserDao.save(obj);
		} else {
			if(null!=user && obj.getUserId().intValue()!=user.getUserId()){
				throw  new IllegalArgumentException("用户名"+obj.getUserName()+"已存在！");
			}
			return sysUserDao.update(obj);
		}
	}

	@Override
	public int updateLogin(SysUser obj) {
		if (null == obj || !AppUtil.isVaildId(obj.getUserId()))
			return 0;
		return sysUserDao.updateLogin(obj);
	}

	@Override
	public int updateOffline(SysUser obj) {
		if (null == obj || !AppUtil.isVaildId(obj.getUserId()))
			return 0;
		return sysUserDao.updateOffline(obj);
	}

	@Override
	public int delete(Integer id) {
		if (!AppUtil.isVaildId(id))
			return 0;
		return sysUserDao.delete(id);
	}

	@Override
	public int updatePwd(List<Integer> ids, String userPwd) {
		if(AppUtil.isEmpty(ids))
			return 0;
		if(!StringUtils.hasLength(userPwd))
			return 0;
		return sysUserDao.updatePwd(ids, userPwd);
	}
}
