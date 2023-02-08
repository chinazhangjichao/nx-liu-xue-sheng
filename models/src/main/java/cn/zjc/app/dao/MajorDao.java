package cn.zjc.app.dao;

import cn.zjc.app.bean.Major;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZJC
 * @decription: 专业 数据库操作接口
 * @date: 2023/02/03 13:28
 * @since JDK 1.8
 */
@Mapper
public interface MajorDao {

    /**
     * 添加
     * 
     * @param obj
     * @return
     */
    public int save(Major obj);

    /**
     * 更新
     * 
     * @param obj
     * @return
     */
    public int update(Major obj);

    /**
     * 删除
     * 
     * @param id
     * @return
     */
    public int delete(Integer id);

    /**
     * 根据ID查询
     * 
     * @param id
     * @return
     */
    public Major queryById(Integer id);


    /**
     * 根据名称查询
     * @param typeName
     * @return
     */
    public Major queryByName(String typeName);

    /**
     * 查询所有
     * @param status 状态
     * @return
     */
    public List<Major> queryAll(Integer status);

}
