package cn.zjc.app.dao;

import cn.zjc.app.bean.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ZJC
 * @decription: 系统角色数据库操作接口
 * @date: 2022/1/11 14:58
 * @since JDK 1.8
 */
@Mapper

public interface SysRoleDao {

    /**
     * 查询所有角色信息
     * @return
     */
    public List<SysRole> queryAll();

    /**
     * 根据id查询
     * 
     * @param id
     * @return
     */
    public SysRole queryById(@Param("id") int id);

    /**
     * 根据角色名称查询
     * @param name
     * @return
     */
    public SysRole queryByName(@Param("name") String name);

    /**
     * 保存角色信息
     * 
     * @param role
     * @return
     */
    public int save(SysRole role);

    /**
     * 更新角色信息
     * 
     * @param role
     * @return
     */
    public int update(SysRole role);

    /**
     * 根据角色id删除，同时删除该角色相关的权限信息以及，删除所有用户拥有的该角色信息
     * @param roleId 需要删除的角色id
     * @return
     */
    public int delete(Integer roleId);


}
