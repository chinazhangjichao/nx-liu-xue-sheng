package cn.zjc.app.dao;

import cn.zjc.app.bean.ArticleInfor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author ZJC
 * @decription: 文章信息 数据库操作接口
 * @date: 2022/1/19 09:58
 * @since JDK 1.8
 */
@Mapper
public interface ArticleInforDao {
	/**
	 * 保存
	 *
	 * @param obj
	 * @return
	 */
	int save(ArticleInfor obj);

	/**
	 * 更新
	 *
	 * @param obj
	 * @return
	 */
	int update(ArticleInfor obj);

	/**
	 * 根据id删除
	 *
	 * @param id
	 * @return
	 */
	int delete(@Param("id") Integer id);

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	int deleteMulti(@Param("ids") Integer[] ids);

	/**
	 * 根据id查询
	 *
	 * @return
	 */
	public ArticleInfor queryById(Integer id);

	/**
	 * 后台：分页查询
	 *
	 * @param start
	 * @param size
	 * @param typeNo    文章类型编码
	 * @param keyword 关键字
	 * @return
	 */
	public List<ArticleInfor> queryByPage(@Param("start") int start, @Param("size") int size, @Param("typeNo") Integer typeNo, @Param("keyword") String keyword);

	/**
	 * 后台：查询记录数
	 *
	 * @param typeNo    文章类型编码
	 * @param keyword 关键字
	 * @return
	 */
	public int queryCount(@Param("typeNo") Integer typeNo, @Param("keyword") String keyword);

	/**
	 * 前台：分页查询
	 *
	 * @param start
	 * @param size
	 * @param type    child为0时传递文章类型ID或child为1时传递文章类型编码
	 * @param child   1查询该类别及其子类下的内容,0只查询该类别下的内容(仅存在类别时生效)
	 * @param keyword 关键字
	 * @param command 是否推荐至首页
	 * @param top     是否置顶
	 * @return
	 */
	public List<ArticleInfor> queryByPage4UI(@Param("start") int start, @Param("size") int size, @Param("type") Integer type, @Param("child") Integer child, @Param("keyword") String keyword,
                                             @Param("command") Integer command, @Param("top") Integer top);

	/**
	 * 前台：查询记录数
	 *
	 * @param type    child为0时传递文章类型ID或child为1时传递文章类型编码
	 * @param child   1查询该类别及其子类下的内容,0只查询该类别下的内容(仅存在类别时生效)
	 * @param keyword 关键字
	 * @param command 是否推荐至首页
	 * @param top     是否置顶
	 * @return
	 */
	public int queryCount4UI(@Param("type") Integer type, @Param("child") Integer child, @Param("child") String keyword, @Param("command") Integer command, @Param("top") Integer top);

}
