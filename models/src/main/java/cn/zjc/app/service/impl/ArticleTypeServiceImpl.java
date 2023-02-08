package cn.zjc.app.service.impl;

import cn.zjc.app.bean.ArticleType;
import cn.zjc.app.dao.ArticleInforDao;
import cn.zjc.app.dao.ArticleTypeDao;
import cn.zjc.app.service.ArticleTypeService;
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
 * @date: 2022/08/10 23:32
 * @since JDK 1.8
 */
@Service
@Transactional
public class ArticleTypeServiceImpl implements ArticleTypeService {

    @Resource
    private ArticleTypeDao articleTypeDao;
    @Resource
    private ArticleInforDao articleInforDao;

    @Override
    @CacheEvict(value = "articletype", allEntries = true, condition = "#result>0 ")
    public int saveOrUpdate(ArticleType obj) {
        if (null == obj)
            return 0;
        ArticleType type =  this.articleTypeDao.queryByNo(obj.getTypeNo());
        if (AppUtil.isVaildId(obj.getTypeId())) {
            if(null!=type && obj.getTypeId().intValue()!=type.getTypeId()){
                throw  new IllegalArgumentException("编码"+obj.getTypeNo()+"已存在！");
            }
            return this.articleTypeDao.update(obj);
        } else {

            if(null!=type){
                throw  new IllegalArgumentException("编码"+obj.getTypeNo()+"已存在！");
            }
            return this.articleTypeDao.save(obj);
        }
    }

    @Override
    @CacheEvict(value = "articletype", allEntries = true, condition = "#result>0 ")
    public int delete(Integer id) {
        if (!AppUtil.isVaildId(id))
            return 0;
        ArticleType type =this.articleTypeDao.queryById(id);
        if(null==type){
            throw new IllegalArgumentException("类别不存在!");
        }
        //查询子部门
        List list = this.articleTypeDao.queryByParent(id);
        if (!AppUtil.isEmpty(list)) {
            throw new IllegalArgumentException("存在子类!");
        }
        int count = this.articleInforDao.queryCount(type.getTypeNo(),null);
        if (count > 0) {
            throw new IllegalArgumentException("存在文章信息!");
        }
        return this.articleTypeDao.delete(id);
    }

    @Override
    @Cacheable(value = "articletype", key = "'ID:'+#id", unless = "#result == null")
    public ArticleType queryById(Integer id) {
        if (!AppUtil.isVaildId(id))
            return null;
        return this.articleTypeDao.queryById(id);
    }

    @Override
    @Cacheable(value = "articletype", key = "'PARENT:'+#parent", unless = "#result?.size() == 0")
    public List<ArticleType> queryByParent(Integer parent) {
        return this.articleTypeDao.queryByParent(parent);
    }
}
