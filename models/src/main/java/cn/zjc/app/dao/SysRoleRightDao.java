package cn.zjc.app.dao;

import cn.zjc.app.bean.SysRoleRight;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ZJC
 * @decription: 角色权限数据库操作接口
 * @date: 2022/1/11 14:58
 * @since JDK 1.8
 */
@Mapper

public interface SysRoleRightDao {

    /**
     * 批量保存
     * 
     * @param objList
     * @return
     */
    public int saveList(@Param("objList") List<SysRoleRight> objList);

    /**
     * 根据角色删除
     * 
     * @param roleId
     * @return
     */
    public int deleteByRole(@Param("roleId") Integer roleId);

    /**
     * 根据角色查询
     * 
     * @param roleId
     * @return
     */
    public List<SysRoleRight> queryByRole(@Param("roleId") Integer roleId);

    /**
     * 根据多个角色id查询权限信息(仅登录后查询用户权限使用)
     * @param roleIds
     * @return
     */
    public List<String> queryByRoles(@Param("roleIds") List<String> roleIds);

}
