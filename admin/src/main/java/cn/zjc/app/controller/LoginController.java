package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.SysUser;
import cn.zjc.app.service.SysUserService;
import cn.zjc.app.utils.Constants;
import cn.zjc.app.utils.ResponseData;
import cn.zjc.app.utils.ServletUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author ZJC
 * @decription:
 * @date: 2022-08-22 14:20
 * @since JDK 1.8
 */
@Controller
public class LoginController {


    @Resource
    private SysUserService sysUserService;
    @ControllerLog(module = "系统登录",description = "访问")
    @GetMapping("/login")
    public String login(){
        return "login";
    }

}
