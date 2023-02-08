package cn.zjc.app.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription:
 * @date: 2023-02-06 14:16
 * @since JDK 1.8
 */

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfor implements Serializable {
    /*主键ID*/
    private Integer stuId;
    /**学号*/
    private Long stuNo;
    /**姓*/
    private String firstName;
    /**名*/
    private String lastName;
    /**中文名*/
    private String chineseName;
    /**性别*/
    private Integer gender;
    /**生日*/
    private Long birth;
    /**头像*/
    private String stuImg;
    /**登录密码*/
    private String stuPwd;
    /**电子邮件*/
    private String email;
    /**国籍*/
    private String country;
    /**联系地址*/
    private String address;
    /**联系电话*/
    private String phone;
    /**证件类型*/
    private String certificateType;
    /**证件号码*/
    private String certificateNo;
    /**审核状态*/
    private Integer stuVerify;
    /**审核时间*/
    private Long verifyTime;
    /**审核人*/
    private SysUser verifyUser;
    /**审核IP*/
    private String verifyIp;
    /**审核备注*/
    private String verifyRemark;
    /**学生登录状态*/
    private Integer stuStatus;
    /**学生类型：学历生和非学历生*/
    private Integer stuType;
    /**登录时间*/
    private Long loginTime;
    /**登录IP*/
    private String loginIp;
    /**创建时间*/
    private Long createTime;
    /**创建IP*/
    private String createIp;
    /**编辑时间*/
    private Long modifyTime;
    /**编辑IP*/
    private String modifyIp;

    public StudentInfor(Integer stuId) {
        this.stuId = stuId;
    }

    public StudentInfor(Integer stuId, Integer stuVerify) {
        this.stuId = stuId;
        this.stuVerify = stuVerify;
    }
}

