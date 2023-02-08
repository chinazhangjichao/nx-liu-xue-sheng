package cn.zjc.app.service.impl;

import cn.zjc.app.bean.StudentInfor;
import cn.zjc.app.dao.StudentInforDao;
import cn.zjc.app.service.StudentInforService;
import cn.zjc.app.utils.AppUtil;
import cn.zjc.app.utils.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

/**
 * @author ZJC
 * @decription: 学生信息 业务逻辑操作
 * @date: 2023/02/04 09:32
 * @since JDK 1.8
 */
@Service
@Transactional
public class StudentInforServiceImpl implements StudentInforService {

    @Resource
    private StudentInforDao studentInforDao;

    @Override
    public synchronized int saveOrUpdate(StudentInfor obj) {
        if (null == obj)
            return 0;
        if (AppUtil.isVaildId(obj.getStuId())) {
            return this.studentInforDao.update(obj);
        } else {
            //查询最新学号
            Long latestNo =this.studentInforDao.queryLatest();
            if(null==latestNo || latestNo==0){
                obj.setStuNo(new Long(obj.getStuNo()+"001"));
            }else{
                if(latestNo.toString().indexOf(obj.getStuNo().toString())==-1){
                    obj.setStuNo(new Long(obj.getStuNo()+"001"));
                }else{
                    obj.setStuNo(latestNo+1);
                }
            }
            return this.studentInforDao.save(obj);
        }
    }


    @Override
    public int delete(Integer id) {
        if (!AppUtil.isVaildId(id))
            return 0;
        StudentInfor type = this.studentInforDao.queryById(id);
        if (null == type) {
            throw new IllegalArgumentException("数据不存在!");
        }

        //查询关联数据--确认是否能够删除

        return this.studentInforDao.delete(id);
    }

    @Override
    public StudentInfor queryById(Integer id) {
        if (!AppUtil.isVaildId(id))
            return null;
        return this.studentInforDao.queryById(id);
    }

    @Override
    public StudentInfor queryByNo(Long no) {
        if (!AppUtil.isVaildId(no))
        return null;
        return studentInforDao.queryByNo(no);
    }

    @Override
    public List<StudentInfor> queryByPage(Integer pageNo, Integer pageSize, Long stuNo, Integer type, Integer verifyStatus, Integer status, String firstName, String lastName, String name, Integer timeType, Long startTime, Long endTime) {
        return studentInforDao.queryByPage((pageNo-1)*pageSize,pageSize,stuNo,type,verifyStatus,status,firstName,lastName,name,timeType,startTime,endTime);
    }

    @Override
    public int queryCount(Long stuNo, Integer type, Integer verifyStatus, Integer status, String firstName, String lastName, String name, Integer timeType, Long startTime, Long endTime) {
        return studentInforDao.queryCount(stuNo,type,verifyStatus,status,firstName,lastName,name,timeType,startTime,endTime);
    }

}
