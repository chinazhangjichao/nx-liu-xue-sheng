package cn.zjc.app.service.impl;

import cn.zjc.app.bean.ArticleFile;
import cn.zjc.app.dao.ArticleFileDao;
import cn.zjc.app.service.ArticleFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZJC
 * @decription:
 * @date: 2023-02-01 14:10
 * @since JDK 1.8
 */
@Service
@Transactional
public class ArticleFileServiceImpl implements ArticleFileService {
    @Resource
    private ArticleFileDao articleFileDao;

    @Override
    public int delete(String no) {
        if(null==no  || "".equals(no))
            return 0;
        return articleFileDao.delete(no);
    }

    @Override
    public ArticleFile queryById(Integer id) {
        if(null==id || id<=0)
        return null;
        return articleFileDao.queryById(id);
    }

    @Override
    public ArticleFile queryByNo(String no) {
        if(null==no || "".equals(no))
        return null;
        return articleFileDao.queryByNo(no);
    }

    @Override
    public List<ArticleFile> queryByArticle(Integer articleId) {
        if(null==articleId || articleId<=0)
            return null;
        return articleFileDao.queryByArticle(articleId);
    }
}
