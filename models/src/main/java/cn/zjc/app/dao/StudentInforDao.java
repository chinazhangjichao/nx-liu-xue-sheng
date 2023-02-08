package cn.zjc.app.dao;

import cn.zjc.app.bean.StudentInfor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZJC
 * @decription: 学生 数据库操作接口
 * @date: 2023/02/06 19:38
 * @since JDK 1.8
 */
@Mapper
public interface StudentInforDao {

    /**
     * 添加
     *
     * @param obj
     * @return
     */
    public int save(StudentInfor obj);

    /**
     * 更新
     *
     * @param obj
     * @return
     */
    public int update(StudentInfor obj);

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
    public StudentInfor queryById(Integer id);

    /**
     * 根据学号查询
     * @param no
     * @return
     */
    public StudentInfor queryByNo(Long no);

    /**
     * 查询最新学生信息
     * @return
     */
    public Long queryLatest();


    /**
     * 分页查询
     * @param start
     * @param size
     * @param stuNo 学号
     * @param type 类型 1学历生 2非学历生
     * @param verifyStatus 审核状态
     * @param status 登录状态
     * @param firstName 姓
     * @param lastName 名
     * @param name 中文名
     * @param timeType 时间类型 1注册时间 2审核时间 3登录时间
     * @param startTime  时间-开始
     * @param endTime 时间-结束
     * @return
     */
    public List<StudentInfor> queryByPage(@Param("start") int start, @Param("size") int size, @Param("stuNo") Long stuNo,@Param("type") Integer type,@Param("verifyStatus") Integer verifyStatus,@Param("status") Integer status, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("name") String name, @Param("timeType") Integer timeType,@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 查询记录数
     * @param stuNo 学号
     * @param type 类型 1学历生 2非学历生
     * @param verifyStatus 审核状态
     * @param status 登录状态
     * @param firstName 姓
     * @param lastName 名
     * @param name 中文名
     * @param timeType 时间类型 1注册时间 2审核时间 3登录时间
     * @param startTime  时间-开始
     * @param endTime 时间-结束
     * @return
     */
     int queryCount(@Param("stuNo") Long stuNo,@Param("type") Integer type,@Param("verifyStatus") Integer verifyStatus,@Param("status") Integer status, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("name") String name, @Param("timeType") Integer timeType,@Param("startTime") Long startTime, @Param("endTime") Long endTime);

}
