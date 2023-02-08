package cn.zjc.app.service.impl;

import cn.zjc.app.bean.SysLog;
import cn.zjc.app.dao.SysLogDao;
import cn.zjc.app.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author ZJC
 * @decription: 系统日志业务操作
 * @date: 2022/1/12 13:40
 * @since JDK 1.8
 */
@Service
@Transactional
public class SysLogServiceImpl implements SysLogService {

    @Resource
    private SysLogDao sysLogDao;

    @Override
    public int queryCount(String user, String module, String content, Integer status, Long startTime, Long endTime) {
        return sysLogDao.queryCount(user, module, content,status, startTime, endTime);
    }

    @Override
    public List<SysLog> queryByPage(int pageNo, int pageSize, String user, String module, String content,Integer status, Long startTime, Long endTime) {
        return sysLogDao.queryByPage((pageNo - 1) * pageSize, pageSize, user, module, content,status, startTime, endTime);
    }

    @Override
    public SysLog queryById(Integer id) {
        if (null == id || id <= 0)
        return null;
        return sysLogDao.queryById(id);
    }

    @Override
    public int save(SysLog obj) {
        if (null == obj)
            return 0;
        obj.setLogTime(System.currentTimeMillis());
        return sysLogDao.save(obj);
    }

    @Override
    public int delete(Integer id) {
        if (null == id || id <= 0)
            return 0;
        return this.sysLogDao.delete(id);
    }

    @Override
    public int deleteMulti(Integer[] ids) {
        if (null == ids || ids.length <= 0)
            return 0;
        return this.sysLogDao.deleteMulti(ids);
    }

}
