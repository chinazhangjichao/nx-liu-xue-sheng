package cn.zjc.app.service.impl;

import cn.zjc.app.bean.SysRole;
import cn.zjc.app.bean.SysUserRole;
import cn.zjc.app.dao.SysUserRoleDao;
import cn.zjc.app.service.SysUserRoleService;
import cn.zjc.app.utils.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZJC
 * @decription:
 * @date: 2022/1/12 15:19
 * @since JDK 1.8
 */
@Service
@Transactional
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Override
    @CacheEvict(value = "userroles", key = "'user:'+#userId")
    public int save(Integer userId, List<Integer> roleIds) {
        if (AppUtil.isEmpty(roleIds))
            return 0;
        if (!AppUtil.isVaildId(userId))
            return 0;
        //先删除当前用户的历史角色信息
        sysUserRoleDao.deleteByUser(userId);
        //保存用户新的角色信息
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (Integer roleId :
                roleIds) {
            list.add(new SysUserRole(userId, new SysRole(roleId)));
        }
        return sysUserRoleDao.saveList(list);
    }

    @Override
    @Cacheable(value = "userroles", key = "'user:'+#userId", unless = "#result?.size() == 0")
    public List<SysUserRole> queryByUser(Integer userId) {
        if (!AppUtil.isVaildId(userId))
            return null;
        List<SysUserRole> list =  sysUserRoleDao.queryByUser(userId);
        return list;
    }
}
