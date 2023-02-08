package cn.zjc.app.dao;

import cn.zjc.app.bean.ArticleType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZJC
 * @decription: 文章类别 数据库操作接口
 * @date: 2022/1/19 09:28
 * @since JDK 1.8
 */
@Mapper
public interface ArticleTypeDao {

    /**
     * 添加
     * 
     * @param obj
     * @return
     */
    public int save(ArticleType obj);

    /**
     * 更新
     * 
     * @param obj
     * @return
     */
    public int update(ArticleType obj);

    /**
     * 删除
     * 
     * @param id
     * @return
     */
    public int delete(@Param("id") Integer id);

    /**
     * 根据ID查询
     * 
     * @param id
     * @return
     */
    public ArticleType queryById(@Param("id") Integer id);

    /**
     * 根据类别编号查询
     * @param typeNo
     * @return
     */
    public ArticleType queryByNo(@Param("typeNo") Integer typeNo);

    /**
     * 根据类别查询
     * 
     * @param parent
     * @return
     */
    public List<ArticleType> queryByParent(@Param("parent") Integer parent);

}
