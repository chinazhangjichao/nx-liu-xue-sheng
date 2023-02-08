package cn.zjc.app.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription: 师资队伍信息
 * @date: 2023-02-03 19:38
 * @since JDK 1.8
 */

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Teacher implements Serializable {
    /**id*/
    private Integer id;
    /**编号*/
    private String no;
    /**头像*/
    private String headImg;
    /**姓名*/
    private String realName;
    /**职称*/
    private String duty;
    /**专业*/
    private String major;
    /**简介*/
    private String description;
    /**详情*/
    private String detail;
    /**电子邮箱*/
    private String email;
    private Integer sortNum;
    /**状态1展示 0不展示*/
    private Integer status;
    /**编辑时间*/
    private Long modifyTime;
    /**编辑人*/
    private SysUser modifyUser;

    public Teacher(Integer id) {
        this.id = id;
    }

    public Teacher(Integer id, Integer status) {
        this.id = id;
        this.status = status;
    }
}
