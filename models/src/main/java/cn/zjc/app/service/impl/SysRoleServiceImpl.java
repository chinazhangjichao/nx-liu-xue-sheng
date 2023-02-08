package cn.zjc.app.service.impl;

import cn.zjc.app.bean.SysRole;
import cn.zjc.app.dao.SysRoleDao;
import cn.zjc.app.dao.SysRoleRightDao;
import cn.zjc.app.dao.SysUserRoleDao;
import cn.zjc.app.service.SysRoleService;
import cn.zjc.app.utils.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * @author ZJC
 * @decription: 系统日志业务操作
 * @date: 2022/1/12 13:40
 * @since JDK 1.8
 */
@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	private SysRoleRightDao sysRoleRightDao;

	@Override
	@Cacheable(value = "roles", key = "'all'",unless = "#result.size() == 0")
	public List<SysRole> queryAll() {
		return this.sysRoleDao.queryAll();
	}

	@Override
	@Cacheable(value = "roles", key = "'ID:'+#id",unless = "#result == null")
	public SysRole queryById(Integer id) {
		if (!AppUtil.isVaildId(id))
			return null;
		return this.sysRoleDao.queryById(id);
	}

	@Override
	@Cacheable(value = "roles", key = "'NAME:'+#id",unless = "#result == null")
	public SysRole queryByName(String name) {
		if(!StringUtils.hasLength(name))
			return null;
		return this.sysRoleDao.queryByName(name);
	}

	@Override
	@CacheEvict(value = "roles", allEntries= true,condition = "#result>0")
	public int saveOrUpdate(SysRole role) {
		if (null == role)
			return 0;
		SysRole r = sysRoleDao.queryByName(role.getRoleName());
		if (AppUtil.isVaildId(role.getRoleId())) {
			if( null!=r && role.getRoleId().intValue()!=r.getRoleId()){
				throw  new IllegalArgumentException("角色名"+role.getRoleName()+"已存在！");
			}
			return this.sysRoleDao.update(role);
		} else {

			if(null!=r){
				throw  new IllegalArgumentException("角色名已存在！");
			}
			return this.sysRoleDao.save(role);
		}
	}

	@Override
	@CacheEvict(value = "roles", allEntries= true)
	public int delete(Integer id) {
		if (!AppUtil.isVaildId(id))
			return 0;
		//删除相关权限信息
		this.sysRoleRightDao.deleteByRole(id);
		//删除用户角色信息
		this.sysUserRoleDao.deleteByRole(id);
		//删除角色信息
		return this.sysRoleDao.delete(id);
	}

}
