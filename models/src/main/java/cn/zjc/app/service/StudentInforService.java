package cn.zjc.app.service;

import cn.zjc.app.bean.StudentInfor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZJC
 * @decription: 学生信息 业务逻辑操作接口
 * @date: 2023/02/03 21:32
 * @since JDK 1.8
 */
public interface StudentInforService {

    /**
     * 保存或修改
     *
     * @param obj
     * @return
     */
    public int saveOrUpdate(StudentInfor obj);

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
    public StudentInfor queryById(Integer id);

    /**
     * 根据学号查询
     * @param no
     * @return
     */
     StudentInfor queryByNo(Long no);

    /**
     *  分页查询
     * @param pageNo 页码
     * @param pageSize 每页大小
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
     List<StudentInfor> queryByPage(Integer pageNo, Integer pageSize, Long stuNo,  Integer type,  Integer verifyStatus,  Integer status,  String firstName,  String lastName,  String name,  Integer timeType,  Long startTime,  Long endTime);

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
    int queryCount( Long stuNo, Integer type, Integer verifyStatus, Integer status,  String firstName,  String lastName,  String name,  Integer timeType, Long startTime,  Long endTime);


}
