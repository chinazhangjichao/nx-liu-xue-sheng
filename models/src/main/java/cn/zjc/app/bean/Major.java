package cn.zjc.app.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription: 专业信息
 * @date: 2023-02-03 13:53
 * @since JDK 1.8
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Major implements Serializable {
    /**Id*/
    private Integer majorId;
    /**专业名称*/
    private String majorName;
    /**英文名称*/
    private String majorEnglish;
    /**专业类型:1专业列表可见 0专业列表不可见*/
    private Integer majorStatus;
    /**专业详情*/
    private String majorDetail;
    /**
     * 排序号
     */
    private Integer majorSort;

    public Major(Integer majorId) {
        this.majorId = majorId;
    }

    public Major(Integer majorId, Integer majorStatus) {
        this.majorId = majorId;
        this.majorStatus = majorStatus;
    }
}
