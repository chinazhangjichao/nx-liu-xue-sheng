package cn.zjc.app.service;

import cn.zjc.app.bean.Menu;

import java.util.List;

/**
 * @author ZJC
 * @decription: 网站菜单 业务逻辑操作接口
 * @date: 2023/02/03 09:32
 * @since JDK 1.8
 */
public interface MenuService {

    /**
     * 保存或修改
     * 
     * @param obj
     * @return
     */
    public int saveOrUpdate(Menu obj);

    /**
     * 删除
     * 
     * @param id
     * @return
     */
    public int delete(Integer id);

    /**
     * 根据Id查询
     * 
     * @param id
     * @return
     */
    public Menu queryById(Integer id);

    /**
     * 根据父类查询
     * 
     * @param parent
     * @return
     */
    public List<Menu> queryByParent(Integer parent);

}
