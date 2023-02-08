package cn.zjc.app.service;

import cn.zjc.app.bean.Question;

import java.util.List;

/**
 * @author ZJC
 * @decription: 考题信息 业务逻辑操作接口
 * @date: 2023/02/04 09:32
 * @since JDK 1.8
 */
public interface QuestionService {

    /**
     * 保存或修改
     * 
     * @param obj
     * @return
     */
    public int saveOrUpdate(Question obj);

    /**
     * 批量保存
     * @param objList
     * @return
     */
    int saveList(List<Question> objList);

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
    public Question queryById(Integer id);

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param course  所属课程
     * @param exam    是否参与考试
     * @param test    是否参与自测
     * @param type    题目类型
     * @param name 题目名称-模糊查询
     * @return
     */
    public List<Question> queryByPage(Integer pageNo, Integer pageSize, Integer course, Integer exam, Integer test, Integer type, String name);

    /**
     * 查询记录数
     * @param course  所属课程
     * @param exam    是否参与考试
     * @param test    是否参与自测
     * @param type    题目类型
     * @param name 题目名称-模糊查询
     * @return
     */
    int queryCount(Integer course, Integer exam, Integer test, Integer type, String name);

}
