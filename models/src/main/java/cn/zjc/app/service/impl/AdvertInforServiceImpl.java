package cn.zjc.app.service.impl;

import cn.zjc.app.bean.AdvertInfor;
import cn.zjc.app.dao.AdvertInforDao;
import cn.zjc.app.service.AdvertInforService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZJC
 * @decription: 广告/banner 业务逻辑
 * @date: 2022/1/25 13:32
 * @since JDK 1.8
 */
@Transactional
@Service
public class AdvertInforServiceImpl implements AdvertInforService {

    @Resource
    private AdvertInforDao advertInforDao;

    @Override
    public int saveOrUpdate(AdvertInfor obj) {
        if (null == obj)
            return 0;
        if (null == obj.getAdvertId() || obj.getAdvertId() <= 0) {
            return this.advertInforDao.save(obj);
        } else {
            return this.advertInforDao.update(obj);
        }
    }

    @Override
    public int delete(Integer id) {
        if (null == id || id <= 0)
            return 0;
        return this.advertInforDao.delete(id);
    }

    @Override
    public int deleteMulti(Integer[] ids) {
        if (null == ids || ids.length <= 0)
            return 0;
        return this.advertInforDao.deleteMulti(ids);
    }

    @Override
    public AdvertInfor queryById(Integer id) {
        if (null == id || id <= 0)
            return null;
        return this.advertInforDao.queryById(id);
    }

    @Override
    public List<AdvertInfor> queryByPage(int pageNo, int pageSize, Integer type, String keyword, Integer online) {
        return this.advertInforDao.queryByPage((pageNo - 1) * pageSize, pageSize, type, keyword, online);
    }

    @Override
    public int queryCount(Integer type,String keyword, Integer online) {
        return this.advertInforDao.queryCount(type, keyword, online);

    }

}
