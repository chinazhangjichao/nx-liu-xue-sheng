package cn.zjc.app.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription: 广告类别
 * @date: 2022/1/25 13:18
 * @since JDK 1.8
 */
@Data
@ToString
public class AdvertType implements Serializable {

    private static final long serialVersionUID = 6361720153936720272L;
    /**
     * 编号ID
     */
    private Integer typeId;

    /**
     * 名称
     */
    private String typeName;


    public AdvertType() {
    }

    public AdvertType(Integer typeId) {
	this.typeId = typeId;
    }


}
