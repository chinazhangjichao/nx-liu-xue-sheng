package cn.zjc.app.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription: 用户角色信息
 * @date: 2022/1/11 14:50
 * @since JDK 1.8
 */
@Data
@ToString
public class SysUserRole implements Serializable{


    private static final long serialVersionUID = 6875097409836748428L;

    private Integer urId;
    private Integer urUser;
    private SysRole urRole;

    public SysUserRole() {
    }

    public SysUserRole(Integer urUser, SysRole urRole) {
        this.urUser = urUser;
        this.urRole = urRole;
    }

}
