<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <title>${applicationScope.ADMINTITLE }</title>
    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html"/>
    <![endif]-->
    <link rel="shortcut icon" href="favicon.ico">
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/font-awesome.min.css" rel="stylesheet">
    <link href="/css/animate.css" rel="stylesheet">
    <link href="/css/style.css" rel="stylesheet">
</head>

<body class="fixed-sidebar full-height-layout gray-bg"
      style="overflow: hidden">
<div id="wrapper">
    <!--左侧导航开始-->
    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="nav-close">
            <i class="fa fa-times-circle"></i>
        </div>
        <div class="sidebar-collapse">
            <ul class="nav" id="side-menu">
                <li class="nav-header">
                    <div class="dropdown profile-element">
                        <a data-toggle="dropdown" class="dropdown-toggle"
                           href="javascript:;"> <span class="block m-t-xs"><strong
                                class="font-bold">${LOGINUSER.userName}</strong></span> <span
                                class="text-muted text-xs block">${LOGINUSER.realName}<b
                                class="caret"></b></span> <span class="text-muted text-xs block">第${LOGINUSER.loginNum}次登录</span>
                        </a>
                        <ul class="dropdown-menu animated fadeInRight m-t-xs">
                            <li><a class="J_menuItem" href="changepwd">修改密码</a></li>
                            <li class="divider"></li>
                            <li><a href="/logout">安全退出</a></li>
                        </ul>
                    </div>
                    <div class="logo-element">${LOGINUSER.userName}</div>
                </li>
                <li><a href="#"><i class="fa fa-language"></i> <span
                        class="nav-label">学生管理</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li><a class="J_menuItem" href="/student/verifyindex">在线申请记录</a></li>
                        <li><a class="J_menuItem" href="/student/index">档案管理</a></li>
                        <li><a class="J_menuItem" href="/message/index">选课管理</a></li>
                        <li><a class="J_menuItem" href="/message/index">学习管理</a></li>
                        <li><a class="J_menuItem" href="/message/index">作业管理</a></li>
                        <li><a class="J_menuItem" href="/message/index">证书申请管理</a></li>
                    </ul>
                </li>
                <li><a href="#"><i class="fa fa-diamond"></i> <span
                        class="nav-label">课程管理</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li><a class="J_menuItem" href="/prize/index">课程管理</a></li>
                        <li><a class="J_menuItem" href="/lottery/index">课程评论管理</a></li>
                    </ul>
                </li>
                <li><a href="#"><i class="fa fa-credit-card"></i> <span
                        class="nav-label">考试管理</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li><a class="J_menuItem" href="/exam/index">考试管理</a></li>
                        <li><a class="J_menuItem" href="/question/index">考题管理</a></li>
                    </ul>
                </li>
                <li><a href="#"><i class="fa fa-newspaper-o"></i> <span
                        class="nav-label">信息管理</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li><a class="J_menuItem" href="/teacher/index">师资队伍管理</a></li>
                        <li><a class="J_menuItem" href="/major/index">专业管理</a></li>
                        <li><a class="J_menuItem" href="/advert/index">广告(BANNER)管理</a></li>
                        <li><a class="J_menuItem" href="/adverttype/index">广告类别管理</a></li>
                        <li><a class="J_menuItem" href="/articletype/index">文章类别管理</a></li>
                        <li><a class="J_menuItem" href="/article/index">文章管理</a></li>
                    </ul>
                </li>
                <li><a href="#"><i class="fa fa-cogs"></i> <span
                        class="nav-label">系统管理</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li><a class="J_menuItem" href="/menu/index">网站菜单管理</a></li>
                        <li><a class="J_menuItem" href="/user/index">系统用户管理</a></li>
                        <li><a class="J_menuItem" href="/role/index">角色管理</a></li>
                        <li><a class="J_menuItem" href="/dictionary/index">参数设置</a></li>
                        <li><a class="J_menuItem" href="/log/index">日志查询</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </nav>
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <div class="row border-bottom">
            <nav class="navbar navbar-static-top" role="navigation"
                 style="margin-bottom: 0">
                <div class="navbar-header">
                    <a class="navbar-minimalize minimalize-styl-2 btn btn-primary "
                       href="#"><i class="fa fa-bars"></i> </a>
                    <div id="title"
                         style="line-height: 60px; font-weight: bolder; font-size: 24px; padding-left: 72px; margin-bottom: 0;">${ADMINTITLE }</div>
                </div>
                <ul class="nav navbar-top-links navbar-right">
                    <li class="dropdown hidden-xs">当前登录：<span>${LOGINUSER.realName}</span></li>
                    <li class=" hidden-xs"><a href="/logout"><i
                            class="fa fa fa-sign-out"></i>退出</a></li>
                </ul>
            </nav>
        </div>
        <div class="row content-tabs">
            <button class="roll-nav roll-left J_tabLeft">
                <i class="fa fa-backward"></i>
            </button>
            <nav class="page-tabs J_menuTabs">
                <div class="page-tabs-content">
                    <a href="javascript:;" class="active J_menuTab" data-id="/welcome">首页</a>
                </div>
            </nav>
            <button class="roll-nav roll-right J_tabRight">
                <i class="fa fa-forward"></i>
            </button>
            <div class="btn-group roll-nav roll-right">
                <button class="dropdown J_tabClose" data-toggle="dropdown">关闭操作<span class="caret"></span></button>
                <ul role="menu" class="dropdown-menu dropdown-menu-right">
                    <li class="J_tabShowActive"><a>定位当前选项卡</a></li>
                    <li class="divider"></li>
                    <li class="J_tabCloseAll"><a>关闭全部选项卡</a></li>
                    <li class="J_tabCloseOther"><a>关闭其他选项卡</a></li>
                </ul>
            </div>
        </div>
        <div class="row J_mainContent" id="content-main">
            <iframe class="J_iframe" name="iframe0" width="100%" height="100%"
                    data-id="/welcome" src="/welcome" frameborder="0" seamless></iframe>
        </div>
    </div>
</div>
<!-- 全局js -->
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script type="text/javascript" src="/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script type="text/javascript" src="/js/hplus.js"></script>
<script type="text/javascript" src="/js/contabs.js"></script>
</body>
</html>

