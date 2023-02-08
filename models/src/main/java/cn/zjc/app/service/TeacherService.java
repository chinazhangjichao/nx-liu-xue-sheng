package cn.zjc.app.service;

import cn.zjc.app.bean.Teacher;

import java.util.List;

/**
 * @author ZJC
 * @decription: 师资队伍 业务逻辑操作接口
 * @date: 2023/02/03 21:32
 * @since JDK 1.8
 */
public interface TeacherService {

    /**
     * 保存或修改
     * 
     * @param obj
     * @return
     */
    public int saveOrUpdate(Teacher obj);

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
    public Teacher queryById(Integer id);

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param status 展示状态 1-展示 0-不展示
     * @param name 姓名-模糊查询
     * @return
     */
    public List<Teacher> queryByPage(Integer pageNo,Integer pageSize,Integer status,String name);

    /**
     * 查询记录数
     * @param status
     * @param name
     * @return
     */
    int queryCount(Integer status,String name);

}
