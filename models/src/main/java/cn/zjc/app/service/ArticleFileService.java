package cn.zjc.app.service;

import cn.zjc.app.bean.ArticleFile;
import java.util.List;


/**
 * @author ZJC
 * @decription: 文章附件信息 业务逻辑接口
 * @date: 2023/02/01 13:58
 * @since JDK 1.8
 */
public interface ArticleFileService {


	/**
	 * 根据no删除
	 *
	 * @param no
	 * @return
	 */
	int delete(String no);

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
