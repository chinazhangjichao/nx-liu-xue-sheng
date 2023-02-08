package cn.zjc.app.service;

import cn.zjc.app.bean.SysUserRole;

import java.util.List;

/**
 * @author ZJC
 * @decription: 用户角色 业务操作
 * @date: 2022/1/12 15:18
 * @since JDK 1.8
 */
public interface SysUserRoleService {

    /**
     * 批量设置某用户的角色信息
     * @param userId 当前用户id
     * @param roleIds 拥有的角色id
     * @return
     */
    public int save(Integer userId, List<Integer> roleIds);


    /**
     * 查询用户拥有的角色信息
     * @param userId
     * @return
     */
    public List<SysUserRole> queryByUser(Integer userId);
}
