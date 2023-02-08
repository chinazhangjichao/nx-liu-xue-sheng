package cn.zjc.app.dao;

import cn.zjc.app.bean.Teacher;
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
public interface TeacherDao {

    /**
     * 添加
     * 
     * @param obj
     * @return
     */
    public int save(Teacher obj);

    /**
     * 更新
     * 
     * @param obj
     * @return
     */
    public int update(Teacher obj);

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
    public Teacher queryById(Integer id);


    /**
     * 分页查询
     * @param start
     * @param size
     * @param status 展示状态 0不展示 1展示
     * @param name 姓名
     * @return
     */
    public List<Teacher> queryByPage(@Param("start") int start, @Param("size")int size, @Param("status")Integer status, @Param("name")String name);

    /**
     * 查询记录数
     * @param status 展示状态 0不展示 1展示
     * @param name 姓名
     * @return
     */
     int queryCount(@Param("status")Integer status, @Param("name")String name);

}
