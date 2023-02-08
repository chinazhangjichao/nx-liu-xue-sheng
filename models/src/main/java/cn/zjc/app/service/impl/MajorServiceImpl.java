package cn.zjc.app.service.impl;

import cn.zjc.app.bean.Major;
import cn.zjc.app.dao.AdvertInforDao;
import cn.zjc.app.dao.MajorDao;
import cn.zjc.app.service.MajorService;
import cn.zjc.app.utils.AppUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZJC
 * @decription: 专业 业务逻辑操作
 * @date: 2023/02/03 14:32
 * @since JDK 1.8
 */
@Service
@Transactional
public class MajorServiceImpl implements MajorService {

    @Resource
    private MajorDao majorDao;

    @Override
    @CacheEvict(value = "major", allEntries = true, condition = "#result>0 ")
    public int saveOrUpdate(Major obj) {
        if (null == obj)
            return 0;
        Major type =  this.majorDao.queryByName(obj.getMajorName());
        if (AppUtil.isVaildId(obj.getMajorId())) {
            if(null!=type && obj.getMajorId().intValue()!=type.getMajorId()){
                throw  new IllegalArgumentException("专业"+obj.getMajorName()+"已存在！");
            }
            return this.majorDao.update(obj);
        } else {
            if(null!=type){
                throw  new IllegalArgumentException("专业"+obj.getMajorName()+"已存在！");
            }
            return this.majorDao.save(obj);
        }
    }

    @Override
    @CacheEvict(value = "major", allEntries = true, condition = "#result>0 ")
    public int delete(Integer id) {
        if (!AppUtil.isVaildId(id))
            return 0;
        Major type =this.majorDao.queryById(id);
        if(null==type){
            throw new IllegalArgumentException("专业不存在!");
        }

        //查询关联数据--确认是否能够删除

        return this.majorDao.delete(id);
    }

    @Override
    @Cacheable(value = "major", key = "'ID:'+#id", unless = "#result == null")
    public Major queryById(Integer id) {
        if (!AppUtil.isVaildId(id))
            return null;
        return this.majorDao.queryById(id);
    }

    @Override
    @Cacheable(value = "major", key = "'ALL:'+#status", unless = "#result.size() == null")
    public List<Major> queryAll(Integer status) {
        return this.majorDao.queryAll(status);
    }


}
