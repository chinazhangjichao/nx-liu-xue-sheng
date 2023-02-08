package cn.zjc.app.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author ZJC
 * @decription: 文章信息
 * @date: 2022/1/19 09:18
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
@ToString
public class ArticleInfor implements Serializable {
	private static final long serialVersionUID = 1981582832710860809L;
	/**
	 * 文章编号
	 */
	private Integer articleId;

	/**
	 * 文章标题
	 */
	private String articleTitle;

	/**
	 * 文章标签
	 */
	private String articleTag;
	/**
	 * 文章简介
	 */
	private String articleDesc;
	/**
	 * 文章展示图
	 */
	private String articleImg;
	/**
	 * 文章详情
	 */
	private String articleDetail;
	/**
	 * 发布日期
	 */
	private Long sendDate;
	/**
	 * 发布人
	 */
	private String sendUser;
	/**
	 * 阅读数量
	 */
	private Integer readNum;
	/**
	 * 排序：降序显示
	 */
	private Integer sortNum;
	/**
	 * 文章类别
	 */
	private ArticleType articleType;
	/**
	 * 是否置顶
	 */
	private Integer isTop;
	/**
	 * 是否推荐
	 */
	private Integer isCommand;
	/**
	 * 是否在线显示
	 */
	private Integer isOnline;
	/**
	 * 是否展示在英文版块
	 */
	private Integer isEn;
	/**
	 * 最后修改
	 */
	private Long modifyTime;
	/**
	 * 编辑用户
	 */
	private SysUser modifyUser;

	/**
	 * 只用于传输数据，不做持久化
	 */
	private List<ArticleFile> articleFiles;



}
