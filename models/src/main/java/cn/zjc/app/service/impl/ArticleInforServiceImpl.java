package cn.zjc.app.service.impl;

import cn.zjc.app.bean.ArticleFile;
import cn.zjc.app.bean.ArticleInfor;
import cn.zjc.app.dao.ArticleFileDao;
import cn.zjc.app.dao.ArticleInforDao;
import cn.zjc.app.service.ArticleInforService;
import cn.zjc.app.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZJC
 * @decription: 文章信息 业务逻辑
 * @date: 2022/1/19 13:32
 * @since JDK 1.8
 */
@Service
@Transactional
public class ArticleInforServiceImpl implements ArticleInforService {

    @Resource
    private ArticleInforDao articleInforDao;
    @Resource
    private ArticleFileDao articleFileDao;

    @Override
    public int saveOrUpdate(ArticleInfor obj) {
        if (null == obj)
            return 0;
        if (null == obj.getArticleId() || obj.getArticleId() <= 0) {
            this.articleInforDao.save(obj);
        } else {
            this.articleInforDao.update(obj);
        }
        //保存附件
        List<ArticleFile> fileList = new ArrayList<>();
        if(null!=obj.getArticleFiles() && obj.getArticleFiles().size()>0){
            for (ArticleFile file:obj.getArticleFiles()) {
                if(!AppUtil.isVaildId(file.getId())){
                    file.setArticle(obj.getArticleId());
                    fileList.add(file);
                }
            }
        }
        if(fileList.size()>0){
            this.articleFileDao.saveList(fileList);
        }
        return 1;
    }

    @Override
    public int delete(Integer id) {
        if (null == id || id <= 0)
            return 0;
        //删除附件信息
        this.articleFileDao.deleteByArticle(id);
        //删除文章信息
        return this.articleInforDao.delete(id);
    }

    @Override
    public int deleteMulti(Integer[] ids) {
        if (null == ids || ids.length <= 0)
            return 0;
        this.articleFileDao.deleteMultiByArticle(ids);
        return this.articleInforDao.deleteMulti(ids);
    }

    @Override
    public ArticleInfor queryById(Integer id) {
        if (null == id || id <= 0)
            return null;
        return this.articleInforDao.queryById(id);
    }

    @Override
    public List<ArticleInfor> queryByPage(int pageNo, int pageSize, Integer type, String keyword) {
        return this.articleInforDao.queryByPage((pageNo - 1) * pageSize, pageSize, type, keyword);
    }

    @Override
    public int queryCount( Integer type, String keyword) {
        return this.articleInforDao.queryCount( type, keyword);
    }

    @Override
    public List<ArticleInfor> queryByPage4UI(int pageNo, int pageSize, Integer type, Integer parent, String keyword, Integer command, Integer top) {
        return this.articleInforDao.queryByPage4UI((pageNo - 1) * pageSize, pageSize, parent, type, keyword, command, top);
    }

    @Override
    public int queryCount4UI(Integer type, Integer parent, String keyword, Integer command, Integer top) {
        return this.articleInforDao.queryCount4UI(type,parent,  keyword, command, top);
    }

}
