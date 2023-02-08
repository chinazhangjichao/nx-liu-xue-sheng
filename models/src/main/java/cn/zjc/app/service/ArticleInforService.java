package cn.zjc.app.service;

import cn.zjc.app.bean.ArticleFile;
import cn.zjc.app.bean.ArticleInfor;

import java.util.List;

/**
 * @author ZJC
 * @decription: 文章信息 业务逻辑操作接口
 * @date: 2022/1/19 13:32
 * @since JDK 1.8
 */
public interface ArticleInforService {
    /**
     * 保存或更新
     *
     * @param obj 文章信息
     * @return
     */
    int saveOrUpdate(ArticleInfor obj);

    /**
     * 根据id删除文章，同时删除文章相关的附件
     *
     * @param id
     * @return
     */
    int delete(Integer id);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    int deleteMulti(Integer[] ids);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public ArticleInfor queryById(Integer id);

    /**
     * 后台：分页查询
     *
     * @param pageNo
     * @param pageSize
     * @param type     文章类型编码
     * @param keyword  关键字
     * @return
     */
    public List<ArticleInfor> queryByPage(int pageNo, int pageSize, Integer type, String keyword);

    /**
     * 后台：查询记录数
     *
     * @param type    文章类型编码
     * @param keyword 关键字
     * @return
     */
    public int queryCount(Integer type, String keyword);

    /**
     * * 前台：分页查询
     *
     * @param pageNo
     * @param pageSize * @param type    child为0时传递文章类型ID或child为1时传递文章类型编码
     * @param child    1查询该类别及其子类下的内容,0只查询该类别下的内容(仅存在类别时生效)
     * @param keyword  关键字
     * @param command  推荐
     * @param top      置顶
     * @return
     */

    public List<ArticleInfor> queryByPage4UI(int pageNo, int pageSize, Integer type, Integer child, String keyword, Integer command, Integer top);

    /**
     * 前台：查询记录数
     *
     * @param type    child为0时传递文章类型ID或child为1时传递文章类型编码
     * @param child   1查询该类别及其子类下的内容,0只查询该类别下的内容(仅存在类别时生效)
     * @param keyword 关键字
     * @param command 推荐
     * @param top     置顶
     * @return
     */
    public int queryCount4UI(Integer type, Integer child, String keyword, Integer command, Integer top);
}
