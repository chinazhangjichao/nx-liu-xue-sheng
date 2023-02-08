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
public class ExportStudentInfor implements Serializable {
    /**学号*/
    private String stuNo;
    /**姓*/
    private String firstName;
    /**名*/
    private String lastName;
    /**中文名*/
    private String chineseName;
    /**性别*/
    private String gender;
    /**生日*/
    private String birth;
    /**电子邮件*/
    private String email;
    /**国籍*/
    private String country;
    /**联系电话*/
    private String phone;
    /**联系地址*/
    private String address;
    /**证件类型*/
    private String certificateType;
    /**证件号码*/
    private String certificateNo;
    /**审核状态*/
    private String stuVerify;
    /**审核时间*/
    private String verifyTime;
    /**审核人*/
    private String verifyUser;
    /**审核IP*/
    private String verifyIp;
    /**审核备注*/
    private String verifyRemark;
    /**学生登录状态*/
    private String stuStatus;
    /**学生类型：学历生和非学历生*/
    private String stuType;
    /**创建时间*/
    private String createTime;
    /**创建IP*/
    private String createIp;
    /**登录时间*/
    private String loginTime;
    /**登录IP*/
    private String loginIp;


}

