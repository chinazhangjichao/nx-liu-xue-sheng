package cn.zjc.app.service.impl;

import cn.zjc.app.bean.SysRoleRight;
import cn.zjc.app.dao.SysRoleRightDao;
import cn.zjc.app.service.SysRoleRightService;
import cn.zjc.app.utils.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SysRoleRightServiceImpl implements SysRoleRightService {

	@Autowired
	private SysRoleRightDao sysRoleRightDao;

	@Override
	@CacheEvict(value = "roleright", allEntries = true)
	public int saveList(List<SysRoleRight> objList) {
		if (null != objList && objList.size() > 0) {
			SysRoleRight obj = objList.get(0);
			this.sysRoleRightDao.deleteByRole(obj.getRrRole());
			return this.sysRoleRightDao.saveList(objList);
		}
		return 0;
	}

	@Override
	@Cacheable(value = "roleright", key = "'role:'+#roleId",unless = "#result?.size() == 0")
	public List<SysRoleRight> queryByRole(Integer roleId) {
		if (!AppUtil.isVaildId(roleId))
			return null;
		return this.sysRoleRightDao.queryByRole(roleId);
	}

	@Override
	@Cacheable(value = "roleright", key = "'roles:'+#roleIds",unless = "#result?.size() == 0")
	public List<String> queryByRoles(List<String> roleIds) {
		if (AppUtil.isEmpty(roleIds))
			return null;
		return sysRoleRightDao.queryByRoles(roleIds);
	}

}
