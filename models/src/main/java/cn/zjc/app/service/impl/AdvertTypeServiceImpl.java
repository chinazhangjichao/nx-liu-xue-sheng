package cn.zjc.app.service.impl;

import cn.zjc.app.bean.AdvertType;
import cn.zjc.app.dao.AdvertInforDao;
import cn.zjc.app.dao.AdvertTypeDao;
import cn.zjc.app.service.AdvertTypeService;
import cn.zjc.app.utils.AppUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZJC
 * @decription: 文章类别 业务逻辑
 * @date: 2022/1/19 13:32
 * @since JDK 1.8
 */
@Service
@Transactional
public class AdvertTypeServiceImpl implements AdvertTypeService {

    @Resource
    private AdvertTypeDao advertTypeDao;
    @Resource
    private AdvertInforDao advertInforDao;

    @Override
    @CacheEvict(value = "adverttype", allEntries = true, condition = "#result>0 ")
    public int saveOrUpdate(AdvertType obj) {
        if (null == obj)
            return 0;
        AdvertType type =  this.advertTypeDao.queryByName(obj.getTypeName());
        if (AppUtil.isVaildId(obj.getTypeId())) {
            if(null!=type && obj.getTypeId().intValue()!=type.getTypeId()){
                throw  new IllegalArgumentException("类别"+obj.getTypeName()+"已存在！");
            }
            return this.advertTypeDao.update(obj);
        } else {
            if(null!=type){
                throw  new IllegalArgumentException("类别"+obj.getTypeName()+"已存在！");
            }
            return this.advertTypeDao.save(obj);
        }
    }

    @Override
    @CacheEvict(value = "adverttype", allEntries = true, condition = "#result>0 ")
    public int delete(Integer id) {
        if (!AppUtil.isVaildId(id))
            return 0;
        AdvertType type =this.advertTypeDao.queryById(id);
        if(null==type){
            throw new IllegalArgumentException("类别不存在!");
        }

        int count = this.advertInforDao.queryCount(type.getTypeId(),null,null);
        if (count > 0) {
            throw new IllegalArgumentException("存在广告/BANNER信息!");
        }
        return this.advertTypeDao.delete(id);
    }

    @Override
    @Cacheable(value = "adverttype", key = "'ID:'+#id", unless = "#result == null")
    public AdvertType queryById(Integer id) {
        if (!AppUtil.isVaildId(id))
            return null;
        return this.advertTypeDao.queryById(id);
    }

    @Override
    @Cacheable(value = "adverttype", key = "'ALL'", unless = "#result.size() == null")
    public List<AdvertType> queryAll() {
        return this.advertTypeDao.queryAll();
    }


}
