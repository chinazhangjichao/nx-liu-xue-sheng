<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>后台管理系统 - 登录</title>
    <link rel="shortcut icon" href="favicon.ico"> 
    <link href="/css/element-ui.css" rel="stylesheet" type="text/css">
    <link href="/css/login.css" rel="stylesheet" type="text/css">
    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
    <script>if(window.top !== window.self){ window.top.location = window.location;}</script>
    <style type="text/css">
    	input{outline: none;}
    </style>
</head>
<body>
<div id="app">
    <div class="login-wrap">
        <div class="ms-login">
            <div style="color: #000;" class="ms-title">后台管理系统</div>
            <form class="el-form ms-content">
                <div class="el-form-item is-success is-required ">
                    <div class="el-form-item__content" style="margin-left: 0px;">
                        <div class="el-input el-input-group el-input-group--prepend">
                            <div class="el-input-group__prepend">
                                <button type="button" tabindex="-1"
                                        class="el-button el-button--default "><i
                                        class="el-icon-user"></i></button>
                            </div>
                            <input type="text" autocomplete="off" placeholder="请输入用户名" class="el-input__inner" ref="username" v-model="userName">
                        </div>
                    </div>
                </div>
                <div class="el-form-item is-required">
                    <div class="el-form-item__content" style="margin-left: 0px;">
                        <div class="el-input el-input-group el-input-group--prepend">
                            <div class="el-input-group__prepend">
                                <button type="button" tabindex="-1" class="el-button el-button--default "><i class="el-icon-lock"></i></button>
                            </div>
                            <input type="password" autocomplete="off" placeholder="请输入密码" class="el-input__inner" ref="password" v-model="userPwd">
                        </div>
                    </div>
                </div>
                <div class="el-form-item is-required ">
                    <div class="el-form-item__content" style="margin-left: 0px;">
                        <div class="el-input  el-input-group el-input-group--prepend">
                            <div class="el-input-group__prepend">
                                <button type="button" tabindex="-1" class="el-button el-button--default"><i class="el-icon-key"></i></button>
                            </div>
                            <input type="text" autocomplete="off" placeholder="请输入验证码" name="imgCode" class="el-input__inner" style=" width: 60%;display: inline-block;text-transform:uppercase" v-model="imgCode">
                            <img :src="imgCodeUrl" @click="handleCode" class="el-input__inner" id="imgCode"  style="width: 38%;height: 40px;float: right;padding: 0;border-radius: 4px;">
                        </div>
                    </div>
                </div>
                <div class="login-btn">
                    <button type="button" class="el-button el-button--primary" v-on:click="login">
                        <span>登录系统</span></button>
                </div>
                <p class="login-tips"></p></form>
        </div>
    </div>
</div>
<input type="hidden" value="" id="hiddenPath">
<script src="/js/vue.min.js" type="text/javascript"></script>
<script src="/js/element-ui.js" type="text/javascript"></script>
<script src="/js/jquery-1.12.4.min.js" type="text/javascript"></script>
<script type="text/javascript">
    new Vue({
        el: '#app',
        data: {
            userName: 'admin',
            userPwd: '123',
            imgCode: '',
            imgCodeUrl:"/imagecode"
        },
        methods: {
            handleCode:function () {
                this.imgCodeUrl="/imagecode?r="+new Date().getTime();
            },
            login: function () {
                var that = this;
                if (this.userName == "") {
                    that.$refs.username.focus();
                    that.$message({message: '请输入用户名', type: 'warning'});
                    return false;
                }
                if (this.userPwd == "") {
                    that.$refs.password.focus();
                    that.$message({message: '请输入密码', type: 'warning'});
                    return false;
                }
                var $imgCode = $("[name='imgCode']");
                if ($imgCode.val() == "") {
                    $imgCode.focus();
                    that.$message({message: '请输入验证码', type: 'warning'});
                    return false;
                }
                $.post("/login", {
                    username: this.userName,
                    password: this.userPwd,
                    imgCode: this.imgCode
                }, function (res) {
                    if (res.code == "success") {
                        location.href = "/index";
                    }else {
                       that.$message.error(res.message);
                       that.handleCode();
                    }
                },"json");
            }
        },
        mounted:{

        }
    });
</script>
</body>
</html>