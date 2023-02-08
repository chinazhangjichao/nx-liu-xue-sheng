<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${applicationScope.ADMINTITLE }</title>
<link rel="shortcut icon" href="favicon.ico">
<link href="/css/bootstrap.min.css" rel="stylesheet">
<link href="/css/font-awesome.min.css" rel="stylesheet">
<link href="/css/animate.css" rel="stylesheet">
<link href="/css/style.css" rel="stylesheet">
<style type="text/css">
	small{padding-right: 15px;}
</style>
</head>
<body class="gray-bg">
	<div id="app" class="wrapper wrapper-content">
		<template>
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>
							数据统计（更新于<span>{{formatTime(time)}}</span>）
						</h5>
						<div class="pull-right">
							<div class="btn-group" id="statisticButtons">
								<button @click="handleRefresh" type="button"
									class="btn btn-xs btn-primary">刷新</button>
							</div>
						</div>
						<div class="pull-right">
                            <div class="btn-group" id="times">
                                <button type="button" @click="handleTimeType(1)" :class="timeType==1?'btn btn-xs btn-white active':'btn btn-xs btn-white'">当天</button>
                                <button type="button" @click="handleTimeType(2)" :class="timeType==2?'btn btn-xs btn-white active':'btn btn-xs btn-white'">当月</button>
                                <button type="button" @click="handleTimeType(3)" :class="timeType==3?'btn btn-xs btn-white active':'btn btn-xs btn-white'">当年</button>
                                <button type="button" @click="handleTimeType(0)" :class="timeType==0?'btn btn-xs btn-white active':'btn btn-xs btn-white'">全部</button>
                            </div>
                        </div>
					</div>
					<div class="ibox-content ">
						<div class="row">
							<div class="col-sm-6"
								style="background-color: #e7eaec; padding-top: 5px;">
								<div class="ibox float-e-margins">
									<div class="ibox-title">
										<h5>注册会员</h5>
									</div>
									<div class="ibox-content" id="qyDiv">
										<h1 class="no-margins">{{memNum}}</h1>
										<div class="stat-percent font-bold"></div>
									</div>
								</div>
							</div>
							<div class="col-sm-6"
								style="background-color: #e7eaec; padding-top: 5px;">
								<div class="ibox float-e-margins">
									<div class="ibox-title">
										<h5>注册工会</h5>
									</div>
									<div class="ibox-content" id="jyDiv">
										<h1 class="no-margins" >{{labourNum}}</h1>
										<div class="stat-percent font-bold"></div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		</template>
	</div>
	<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
	<script type="text/javascript" src="/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/js/admin/plugins/echarts/echarts-all.js"></script>
	<script type="text/javascript" src="/js/common.js"></script>
	<script src="/js/vue.min.js" type="text/javascript"></script>
	<script src="/js/element-ui.js" type="text/javascript"></script>
	<script src="/js/jquery-1.12.4.min.js" type="text/javascript"></script>
	<script src="/js/common.js" type="text/javascript"></script>
	<script type="text/javascript">
        new Vue({
            el: '#app',
            data: {
                loading:true,
                timeType:0,
				time:null,
                memNum:0,
				labourNum:0
            },
            methods: {
                formatTime: function (ms) {
                    if (isNaN(ms)) {return '';}
                    return formatTime(new Date(ms));
                },
                handleRefresh:function(){
                    location.reload();
				},
                handleTimeType: function (v) {
                    this.timeType=v;
                    this.loadData();
                },
                loadData: function () {
                    var that = this;
                    that.loading=true;
                    $.ajax({
                        url:"/admin/loadstatistic",
                        type:"post",
                        data:{timeType:this.timeType},
                        dataType:"json",
                        success:function (res) {
                            that.loading=false;
                            if(res.code=="success"){
                                that.memNum=res.data.memNum;
                                that.labourNum=res.data.labourNum;
                                that.time=res.data.now;
                            }
                        }
                    });
                }
            },
            mounted: function () {
                this.loadData();
            }
        });
	</script>
</body>
</html>
