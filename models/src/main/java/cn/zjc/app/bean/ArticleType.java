package cn.zjc.app.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription: 文章类别
 * @date: 2023/02/01 13:25
 * @since JDK 1.8
 */
@Data
@ToString
public class ArticleType implements Serializable {
    private static final long serialVersionUID = -7677787993977774829L;

    /**
     * 编号ID
     */
    private Integer typeId;
    /**
     * 编码规则：必须是两位数字，且不能以0开头；
     * 下级类别的编码为：上级类别编码+2位下级编码；
     * 比如一级类别编码为10，则二级为1011，1012...，三级类别为101111，101112...
     */
    private Integer typeNo;
    /**
     * 排序号:降序
     */
    private Integer sortNum;
    /**
     * 图标
     */
    private String typeImg;
    /**
     * 名称
     */
    private String typeName;
    /**
     * 英文名称
     */
    private String typeEnglish;
    /**
     * 描述
     */
    private String typeDesc;
    /**
     * 所属父类
     */
    private ArticleType typeParent;

    public ArticleType() {
    }

    public ArticleType(Integer typeId) {
	this.typeId = typeId;
    }

}
