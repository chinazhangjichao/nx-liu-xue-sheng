package cn.zjc.app.service.impl;

import cn.zjc.app.bean.Teacher;
import cn.zjc.app.dao.TeacherDao;
import cn.zjc.app.service.TeacherService;
import cn.zjc.app.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZJC
 * @decription: 师资队伍 业务逻辑操作
 * @date: 2023/02/03 21:32
 * @since JDK 1.8
 */
@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    @Resource
    private TeacherDao teacherDao;

    @Override
    public int saveOrUpdate(Teacher obj) {
        if (null == obj)
            return 0;
        if (AppUtil.isVaildId(obj.getId())) {
            return this.teacherDao.update(obj);
        } else {
            return this.teacherDao.save(obj);
        }
    }

    @Override
    public int delete(Integer id) {
        if (!AppUtil.isVaildId(id))
            return 0;
        Teacher type = this.teacherDao.queryById(id);
        if (null == type) {
            throw new IllegalArgumentException("讲师资料不存在!");
        }

        //查询关联数据--确认是否能够删除

        return this.teacherDao.delete(id);
    }

    @Override
    public Teacher queryById(Integer id) {
        if (!AppUtil.isVaildId(id))
            return null;
        return this.teacherDao.queryById(id);
    }

    @Override
    public List<Teacher> queryByPage(Integer pageNo, Integer pageSize, Integer status, String name) {
        pageNo = null == pageNo ? 1 : pageNo;
        pageSize = null == pageSize ? 10 : pageSize;
        return this.teacherDao.queryByPage((pageNo - 1) * pageSize, pageSize, status, name);
    }

    @Override
    public int queryCount(Integer status, String name) {
        return teacherDao.queryCount(status, name);
    }
}
