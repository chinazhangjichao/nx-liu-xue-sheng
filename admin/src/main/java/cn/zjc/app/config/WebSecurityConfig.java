package cn.zjc.app.config;


import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.aspectj.LogAspect;
import cn.zjc.app.bean.SysLog;
import cn.zjc.app.bean.SysUser;
import cn.zjc.app.controller.LoginController;
import cn.zjc.app.filter.ValidateCodeFilter;
import cn.zjc.app.service.SysLogService;
import cn.zjc.app.service.SysUserService;
import cn.zjc.app.utils.Constants;
import cn.zjc.app.utils.ResponseData;
import cn.zjc.app.utils.ServletUtil;
import org.apache.tomcat.util.http.ResponseUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.naming.ldap.Control;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ZJC
 * @decription:
 * @date: 2022-08-04 8:59
 * @since JDK 1.8
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration(proxyBeanMethods = false)
public class WebSecurityConfig {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysLogService sysLogService;
    @Autowired
    private ValidateCodeFilter validateCodeFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //对于满足/api/**的请求直接放行
                .antMatchers("/api/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/upload/**").permitAll()
                .antMatchers("/ueditor/**").permitAll()
                .antMatchers("/imagecode").permitAll()
                //对于除上述意外的请求，进行认证
                .antMatchers("/**").authenticated()
                //自定义登录页，且放行登录页，不然也会被拦截
                .and().formLogin()
                .loginPage("/login")   //自定义登录页面
                .loginProcessingUrl("/login")//自定义loginPage后，如果不自定义处理url，则默认使用loginPage来处理
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .permitAll()//对于登录相关的url直接放行
                .and().csrf().disable();//禁用csrf保护（跨站请求伪造）
                //解决iframe不允许访问的问题
                http.headers().frameOptions().disable();
                http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * 自定义登录成功
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                User user = (User)authentication.getPrincipal();
                //更新登录状态
                SysUser sysUser= sysUserService.queryByName(user.getUsername());
                sysUser.setUserName(user.getUsername());
                sysUser.setLoginTime(System.currentTimeMillis());
                sysUser.setLoginIp(ServletUtil.getIpAddr(request));
                sysUserService.updateLogin(sysUser);

                SysLog log  = new SysLog();
                log.setLogModule("系统登录");
                log.setLogContent("登录");
                log.setLogIp(ServletUtil.getIpAddr(request));
                log.setLogUser(sysUser.getRealName() + "-" + sysUser.getUserId());
                log.setLogTime(System.currentTimeMillis());
                log.setRequestMethod(request.getMethod());
                log.setRequestParams("{}");
                log.setControllerMethod(LoginController.class.getName()+".login()");
                log.setBrowser(ServletUtil.getBrowser());
                log.setSystem(ServletUtil.getSystem());
                log.setLogFlag(1);
                sysLogService.save(log);
                //使用Session
                request.getSession().setAttribute(Constants.LOGINUSER,sysUser);
                ServletUtil.responseJson(response, HttpStatus.OK,new ResponseData("success","登录成功！"));
            }
        };
    }

    /**
     * 自定义登录失败后返回json
     * @return
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

                SysLog log  = new SysLog();
                log.setLogModule("系统登录");
                log.setLogContent("登录");
                log.setLogIp(ServletUtil.getIpAddr(request));
                log.setLogUser("未登录");
                log.setLogTime(System.currentTimeMillis());
                log.setRequestMethod(request.getMethod());
                log.setRequestParams("{}");
                log.setControllerMethod(LoginController.class.getName()+".login()");
                log.setBrowser(ServletUtil.getBrowser());
                log.setSystem(ServletUtil.getSystem());
                log.setLogFlag(0);
                log.setException("登录失败："+exception.getMessage());
                sysLogService.save(log);

                ServletUtil.responseJson(response,HttpStatus.OK,new ResponseData("failure",exception.getMessage()));
            }
        };
    }

    /**
     * 自定义忽略哪些请求的验证(官方不推荐)
     */
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers("/css/**","/images/**", "/js/**", "/upload/**", "/ueditor/**");
//    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
