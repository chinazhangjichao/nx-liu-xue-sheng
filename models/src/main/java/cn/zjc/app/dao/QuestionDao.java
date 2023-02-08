package cn.zjc.app.dao;

import cn.zjc.app.bean.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZJC
 * @decription: 考题信息 数据库操作接口
 * @date: 2023/02/04 08:38
 * @since JDK 1.8
 */
@Mapper
public interface QuestionDao {

    /**
     * 添加
     *
     * @param obj
     * @return
     */
    public int save(Question obj);

    /**
     * 批量保存
     * @param objList
     * @return
     */
    int saveList(@Param("objList") List<Question> objList);

    /**
     * 更新
     *
     * @param obj
     * @return
     */
    public int update(Question obj);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public int delete(Integer id);

    /**
     * 删除某门课程相关的考题
     * @param courseId
     * @return
     */
    int deleteByCourse(Integer courseId);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    public Question queryById(Integer id);


    /**
     * 分页查询
     *
     * @param start
     * @param size
     * @param course  所属课程
     * @param exam    是否参与考试
     * @param test    是否参与自测
     * @param type    题目类型
     * @param name 题目名称-模糊查询
     * @return
     */
    public List<Question> queryByPage(@Param("start") int start, @Param("size") int size, @Param("course") Integer course, @Param("exam") Integer exam, @Param("test") Integer test, @Param("type") Integer type, @Param("name") String name);

    /**
     * 查询记录数
     *
     * @param course  所属课程
     * @param exam    是否参与考试
     * @param test    是否参与自测
     * @param type    题目类型
     * @param name 题目名称-模糊查询
     * @return
     */
    int queryCount(@Param("course") Integer course, @Param("exam") Integer exam, @Param("test") Integer test, @Param("type") Integer type, @Param("name") String name);

}
