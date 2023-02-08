<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"
	value="${pageContext.servletContext.contextPath}"></c:set>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no">
    <title>${applicationScope.ADMINTITLE }</title>
    <link href="${contextPath }/css/element-ui.css" rel="stylesheet" type="text/css">
    <script src="${contextPath }/js/vue.min.js" type="text/javascript"></script>
    <script src="${contextPath }/js/element-ui2.13.1.js" type="text/javascript"></script>
</head>
<body>
<div id="app" style="background-color: #FFFFFF;padding: 10px;">
    <el-calendar v-model="value"></el-calendar>
</div>
<script type="text/javascript">
    new Vue({
        el: '#app',
        data: {
            value: new Date()
        }
    });
</script>
</body>
</html>