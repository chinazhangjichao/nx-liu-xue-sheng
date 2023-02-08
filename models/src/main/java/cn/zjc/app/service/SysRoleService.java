package cn.zjc.app.service;

import cn.zjc.app.bean.SysRole;

import java.util.List;

public interface SysRoleService {

    /**
     * 查询所有
     * @return
     */
    public List<SysRole> queryAll();

    /**
     * 根据id查询
     * 
     * @param id
     * @return
     */
    public SysRole queryById(Integer id);

    /**
     * 根据角色名称查询
     * @param name
     * @return
     */
    public SysRole queryByName(String name);

    /**
     * 保存或更新角色信息
     * 
     * @param role
     * @return
     */
    public int saveOrUpdate(SysRole role);

    /**
     * 根据id删除
     * 
     * @param id
     * @return
     */
    public int delete(Integer id);
}
