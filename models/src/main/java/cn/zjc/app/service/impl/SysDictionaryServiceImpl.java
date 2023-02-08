package cn.zjc.app.service.impl;

import cn.zjc.app.bean.SysDictionary;
import cn.zjc.app.bean.SysRole;
import cn.zjc.app.dao.SysDictionaryDao;
import cn.zjc.app.service.SysDictionaryService;
import cn.zjc.app.utils.AppUtil;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZJC
 * @decription: 数据字典业务操作
 * @date: 2022/1/15 20:34
 * @since JDK 1.8
 */
@Service("sysDictionaryService")
@Transactional
public class SysDictionaryServiceImpl implements SysDictionaryService {
    @Resource
    private SysDictionaryDao sysDictionaryDao;

    @Override
    @CacheEvict(value = "dictionary",allEntries = true)
    public int saveOrUpdate(SysDictionary obj) {
        if (null == obj)
            return 0;
        SysDictionary o = sysDictionaryDao.queryByKey(obj.getDicKey());
        if (AppUtil.isVaildId(obj.getDicId())) {
            if(null!=o && o.getDicId().intValue()!=obj.getDicId()){
                throw  new InvalidPropertyException(SysRole.class,"dicKey","参数名"+obj.getDicKey()+"已存在！");
            }
            return this.sysDictionaryDao.update(obj);
        } else {
            if(null!=o){
                throw  new InvalidPropertyException(SysRole.class,"dicKey","参数名"+obj.getDicKey()+"已存在！");
            }
            return this.sysDictionaryDao.save(obj);
        }
    }

    @Override
    @CacheEvict(value = "dictionary",allEntries = true)
    public int delete(Integer dicId) {
        if (AppUtil.isVaildId(dicId)) {
            return sysDictionaryDao.delete(dicId);
        }
        return 0;
    }

    @Override
    @Cacheable(value = "dictionary", key = "'all'")
    public List<SysDictionary> queryAll() {
	return this.sysDictionaryDao.queryAll();
    }

    @Override
    @Cacheable(value = "dictionary", key = "'key:'+#key")
    public SysDictionary queryByKey(String key) {
        if (!StringUtils.hasLength(key))
            return null;
        return this.sysDictionaryDao.queryByKey(key);
    }

    @Override
    public SysDictionary queryById(Integer id) {
        if (!AppUtil.isVaildId(id))
            return null;
        return this.sysDictionaryDao.queryById(id);

    }
}
