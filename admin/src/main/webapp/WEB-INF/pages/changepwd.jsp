<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>后台管理系统 - 修改密码</title>
    <link rel="shortcut icon" href="favicon.ico">
    <link href="/css/element-ui.css" rel="stylesheet" type="text/css">
    <link href="/css/style.css?v=4.1.0" rel="stylesheet">
</head>
<body class="gray-bg">

<div id="app" >
    <el-container>
        <el-header style="padding: 20px;">
            <el-page-header @back="goBack" content="修改密码"></el-page-header>
        </el-header>
        <el-main>
            <el-form ref="form" label-width="80px">
                <el-form-item label="原密码">
                    <el-input ref="oldPwd" v-model="oldPwd" show-password></el-input>
                </el-form-item>
                <el-form-item label="新密码">
                    <el-input ref="newPwd" v-model="newPwd" show-password></el-input>
                </el-form-item>
                <el-form-item label="确认密码">
                    <el-input ref="confirmPwd" v-model="confirmPwd" show-password></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleSave">确认修改</el-button>
                </el-form-item>
            </el-form>
        </el-main>

    </el-container>

</div>
<script src="/js/vue.min.js" type="text/javascript"></script>
<script src="/js/element-ui.js" type="text/javascript"></script>
<script src="/js/jquery-1.12.4.min.js" type="text/javascript"></script>
<script type="text/javascript">
    new Vue({
        el: '#app',
        data: {
            oldPwd: '',
            newPwd: '',
            confirmPwd: ''
        },
        methods: {
            goBack:function() {
                history.back();
            },
            handleSave: function () {
                var that = this;
                var regPwd=/\w{6,20}/;
                if (!regPwd.test(that.oldPwd)) {
                    that.$refs.oldPwd.focus();
                    that.$message({message: '请正确输入原始密码', type: 'warning'});
                    return false;
                }
                if (!regPwd.test(that.newPwd)) {
                    that.$refs.newPwd.focus();
                    that.$message({message: '请正确输入新密码', type: 'warning'});
                    return false;
                }
                if (that.confirmPwd!=that.newPwd) {
                    that.$refs.confirmPwd.focus();
                    that.$message({message: '两次输入的密码不一致', type: 'warning'});
                    return false;
                }

                $.post("/changepwd", {
                    oldPwd: that.oldPwd,
                    newPwd: that.newPwd,
                    confirmPwd: that.confirmPwd
                }, function (res) {
                    if (res.code == "success") {
                        that.$message.success(res.message);
                    }else {
                        that.$message.error(res.message);
                    }
                },"json");
            }
        }
    });
</script>
</body>
</html>
