package cn.zjc.app.service;

import cn.zjc.app.bean.SysRoleRight;

import java.util.List;

/**
 * @author ZJC
 * @decription: 角色权限 业务操作接口
 * @date: 2022/1/12 13:22
 * @since JDK 1.8
 */
public interface SysRoleRightService {

    /**
     * 批量保存
     * 
     * @param objList
     * @return
     */
    public int saveList(List<SysRoleRight> objList);

    /**
     * 根据角色查询
     * 
     * @param roleId
     * @return
     */
    public List<SysRoleRight> queryByRole(Integer roleId);

    /**
     * 根据多个角色查询(仅登录后查询用户权限使用)
     * @param roleIds
     * @return
     */
    public List<String> queryByRoles(List<String> roleIds);

}
