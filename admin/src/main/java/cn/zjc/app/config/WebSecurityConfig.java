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
                //????????????/api/**?????????????????????
                .antMatchers("/api/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/upload/**").permitAll()
                .antMatchers("/ueditor/**").permitAll()
                .antMatchers("/imagecode").permitAll()
                //?????????????????????????????????????????????
                .antMatchers("/**").authenticated()
                //???????????????????????????????????????????????????????????????
                .and().formLogin()
                .loginPage("/login")   //?????????????????????
                .loginProcessingUrl("/login")//?????????loginPage??????????????????????????????url??????????????????loginPage?????????
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .permitAll()//?????????????????????url????????????
                .and().csrf().disable();//??????csrf??????????????????????????????
                //??????iframe????????????????????????
                http.headers().frameOptions().disable();
                http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * ?????????????????????
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                User user = (User)authentication.getPrincipal();
                //??????????????????
                SysUser sysUser= sysUserService.queryByName(user.getUsername());
                sysUser.setUserName(user.getUsername());
                sysUser.setLoginTime(System.currentTimeMillis());
                sysUser.setLoginIp(ServletUtil.getIpAddr(request));
                sysUserService.updateLogin(sysUser);

                SysLog log  = new SysLog();
                log.setLogModule("????????????");
                log.setLogContent("??????");
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
                //??????Session
                request.getSession().setAttribute(Constants.LOGINUSER,sysUser);
                ServletUtil.responseJson(response, HttpStatus.OK,new ResponseData("success","???????????????"));
            }
        };
    }

    /**
     * ??????????????????????????????json
     * @return
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

                SysLog log  = new SysLog();
                log.setLogModule("????????????");
                log.setLogContent("??????");
                log.setLogIp(ServletUtil.getIpAddr(request));
                log.setLogUser("?????????");
                log.setLogTime(System.currentTimeMillis());
                log.setRequestMethod(request.getMethod());
                log.setRequestParams("{}");
                log.setControllerMethod(LoginController.class.getName()+".login()");
                log.setBrowser(ServletUtil.getBrowser());
                log.setSystem(ServletUtil.getSystem());
                log.setLogFlag(0);
                log.setException("???????????????"+exception.getMessage());
                sysLogService.save(log);

                ServletUtil.responseJson(response,HttpStatus.OK,new ResponseData("failure",exception.getMessage()));
            }
        };
    }

    /**
     * ????????????????????????????????????(???????????????)
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
