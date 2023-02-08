package cn.zjc.app.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription: 数据字典
 * @date: 2022/1/11 14:44
 * @since JDK 1.8
 */
@Data
@ToString
public class SysDictionary implements Serializable{
    private static final long serialVersionUID = -8078634106540899936L;
    private Integer dicId;
    private String dicKey;
    private String dicValue;
    private String dicDesc;

    public SysDictionary() {
    }

    public SysDictionary(String dicKey, String dicValue) {
        this.dicKey = dicKey;
        this.dicValue = dicValue;
    }

}
