package cn.zjc.app.dao;

import cn.zjc.app.bean.ArticleFile;
import cn.zjc.app.bean.ArticleFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author ZJC
 * @decription: 文章附件信息 数据库操作接口
 * @date: 2023/02/01 13:58
 * @since JDK 1.8
 */
@Mapper
public interface ArticleFileDao {
	/**
	 * 批量保存
	 * @param objList
	 * @return
	 */
	int saveList(@Param("objList") List<ArticleFile> objList);

	/**
	 * 根据no删除
	 *
	 * @param no
	 * @return
	 */
	int delete(String no);

	/**
	 * 根据文章删除所有相关附件
	 *
	 * @param articleId
	 * @return
	 */
	int deleteByArticle(Integer articleId);

	/**
	 * 批量删除多个文章的附件信息
	 * @param ids
	 * @return
	 */
	int deleteMultiByArticle(@Param("ids") Integer[] ids);

	/**
	 * 根据id查询
	 *
	 * @return
	 */
	public ArticleFile queryById(Integer id);

	/**
	 * 根据文件编码查询
	 * @param no
	 * @return
	 */
	public ArticleFile queryByNo(String no);


	/**
	 * 根据文章查询
	 *
	 * @param articleId 相关文章id
	 * @return
	 */
	public List<ArticleFile> queryByArticle(Integer articleId);
}
