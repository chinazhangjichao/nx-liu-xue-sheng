package cn.zjc.app.dao;

import cn.zjc.app.bean.AdvertType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZJC
 * @decription: 广告类别 数据库操作接口
 * @date: 2022/1/19 09:28
 * @since JDK 1.8
 */
@Mapper
public interface AdvertTypeDao {

    /**
     * 添加
     * 
     * @param obj
     * @return
     */
    public int save(AdvertType obj);

    /**
     * 更新
     * 
     * @param obj
     * @return
     */
    public int update(AdvertType obj);

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
    public AdvertType queryById(@Param("id") Integer id);


    /**
     * 根据名称查询
     * @param typeName
     * @return
     */
    public AdvertType queryByName(@Param("typeName") String typeName);

    /**
     * 查询所有
     * 
     * @return
     */
    public List<AdvertType> queryAll();

}
