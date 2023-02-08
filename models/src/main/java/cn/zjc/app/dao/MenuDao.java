package cn.zjc.app.dao;

import cn.zjc.app.bean.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZJC
 * @decription: 网站菜单 数据库操作接口
 * @date: 2023/02/03 09:28
 * @since JDK 1.8
 */
@Mapper
public interface MenuDao {

    /**
     * 添加
     * 
     * @param obj
     * @return
     */
    public int save(Menu obj);

    /**
     * 更新
     * 
     * @param obj
     * @return
     */
    public int update(Menu obj);

    /**
     * 删除
     * 
     * @param id
     * @return
     */
    public int delete(@Param("id") Integer id);

    /**
     * 根据ID查询
     * 
     * @param id
     * @return
     */
    public Menu queryById(@Param("id") Integer id);

    /**
     * 根据类别查询
     * 
     * @param parent
     * @return
     */
    public List<Menu> queryByParent(@Param("parent") Integer parent);

}
