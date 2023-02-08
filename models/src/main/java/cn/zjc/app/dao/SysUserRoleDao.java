package cn.zjc.app.dao;

import cn.zjc.app.bean.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ZJC
 * @decription: 用户角色数据库操作接口
 * @date: 2022/1/11 15:00
 * @since JDK 1.8
 */
@Mapper

public interface SysUserRoleDao {

    /**
     * 批量保存用户角色信息
     * @param objList
     * @return
     */
    public int saveList(@Param("objList") List<SysUserRole> objList);

    /**
     * 根据用户删除其拥有的角色信息
     * @param userId
     * @return
     */
    public int deleteByUser(Integer userId);
    /**
     * 根据角色删除相关信息
     * @param roleId
     * @return
     */
    public int deleteByRole(Integer roleId);

    /**
     * 查询用户拥有的角色信息
     * @param userId
     * @return
     */
    public List<SysUserRole> queryByUser(Integer userId);
}
