package cn.zjc.app.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription: 广告/banner图信息
 * @date: 2022/1/25 13:18
 * @since JDK 1.8
 */
@Data
@ToString
public class AdvertInfor implements Serializable {

    private static final long serialVersionUID = 229081159667557669L;
    /**
     * 自动编号
     */
    private Integer advertId;
    /**
     * 广告地址
     */
    private String advertUrl;
    /**
     * 广告名称描述
     */
    private String advertName;
    /**
     * 广告展示图
     */
    private String advertImg;
    /**
     * 广告类别 表示
     */
    private AdvertType advertType;
    /**
     * 是否在线
     */
    private Integer isOnline;
    /**
     * 排序号
     */
    private Integer sortNum;
    /**
     * 修改人
     */
    private String modifyUser;
    /**
     * 最后更新
     */
    private Long modifyTime;

    public AdvertInfor() {
    }

    public AdvertInfor(Integer advertId, Integer isOnline) {
        this.advertId = advertId;
        this.isOnline = isOnline;
    }

}
