package cn.zjc.app.service.impl;

import cn.zjc.app.bean.Menu;
import cn.zjc.app.dao.MenuDao;
import cn.zjc.app.service.MenuService;
import cn.zjc.app.utils.AppUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZJC
 * @decription: 网站菜单 业务逻辑
 * @date: 2023/02/03 09:32
 * @since JDK 1.8
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuDao menuDao;

    @Override
    @CacheEvict(value = "menu", allEntries = true, condition = "#result>0 ")
    public int saveOrUpdate(Menu obj) {
        if (null == obj)
            return 0;
        if (AppUtil.isVaildId(obj.getMenuId())) {
            return this.menuDao.update(obj);
        } else {
            return this.menuDao.save(obj);
        }
    }

    @Override
    @CacheEvict(value = "menu", allEntries = true, condition = "#result>0 ")
    public int delete(Integer id) {
        if (!AppUtil.isVaildId(id))
            return 0;
        Menu type =this.menuDao.queryById(id);
        if(null==type){
            throw new IllegalArgumentException("类别不存在!");
        }
        List list = this.menuDao.queryByParent(id);
        if (!AppUtil.isEmpty(list)) {
            throw new IllegalArgumentException("存在子类!");
        }
        return this.menuDao.delete(id);
    }

    @Override
    @Cacheable(value = "menu", key = "'ID:'+#id", unless = "#result == null")
    public Menu queryById(Integer id) {
        if (!AppUtil.isVaildId(id))
            return null;
        return this.menuDao.queryById(id);
    }

    @Override
    @Cacheable(value = "menu", key = "'PARENT:'+#parent", unless = "#result?.size() == 0")
    public List<Menu> queryByParent(Integer parent) {
        return this.menuDao.queryByParent(parent);
    }
}
