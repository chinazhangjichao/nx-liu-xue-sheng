package cn.zjc.app.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription: 文章附件信息
 * @date: 2023-02-01 13:35
 * @since JDK 1.8
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArticleFile implements Serializable {

    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 相关文章
     */
    private Integer article;
    /**
     * 唯一编码
     */
    private String no;
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件地址
     */
    private String url;
    /**
     * 文件大小
     */
    private Integer size;
    /**
     * 文件类型
     */
    private String type;
    /**
     * 创建人
     */
    private SysUser createUser;
    /**
     * 创建时间
     */
    private Long createTime;
}
