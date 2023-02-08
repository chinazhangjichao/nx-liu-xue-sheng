package cn.zjc.app.dao;

import cn.zjc.app.bean.SysDictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author ZJC
 * @decription: 数据字典数据库操作接口
 * @date: 2022/1/11 15:11
 * @since JDK 1.8
 */
@Mapper
public interface SysDictionaryDao {

    /**
     * 查询所有
     * 
     * @return
     */
    public List<SysDictionary> queryAll();

    /**
     * 根据key加载参数
     * 
     * @param key
     * @return
     */
    public SysDictionary queryByKey(@Param("key") String key);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public SysDictionary queryById(@Param("id") Integer id);

    /**
     * 更新
     * 
     * @param obj
     * @return
     */
    public int update(SysDictionary obj);

    /**
     * 保存参数
     * @param obj
     * @return
     */
    public int save(SysDictionary obj);

    /**
     * 删除参数
     * @param dicId
     * @return
     */
    public int delete(Integer dicId);

}
