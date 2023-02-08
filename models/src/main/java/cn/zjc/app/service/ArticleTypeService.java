package cn.zjc.app.service;

import cn.zjc.app.bean.ArticleType;

import java.util.List;

/**
 * @author ZJC
 * @decription: 文章类别 业务逻辑操作接口
 * @date: 2022/1/19 13:32
 * @since JDK 1.8
 */
public interface ArticleTypeService {

    /**
     * 保存或修改
     * 
     * @param obj
     * @return
     */
    public int saveOrUpdate(ArticleType obj);

    /**
     * 删除
     * 
     * @param id
     * @return
     */
    public int delete(Integer id);

    /**
     * 根据Id查询
     * 
     * @param id
     * @return
     */
    public ArticleType queryById(Integer id);

    /**
     * 根据父类查询
     * 
     * @param parent
     * @return
     */
    public List<ArticleType> queryByParent(Integer parent);

}
