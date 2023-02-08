<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no">
    <title>${applicationScope.ADMINTITLE }</title>
    <link href="/css/element-ui.css" rel="stylesheet" type="text/css">
    <link href="/css/style.css" rel="stylesheet">
    <style type="text/css">
        .file-uploader{display: inline;}
    </style>
</head>
<body class="gray-bg">
<div id="app" >
    <el-container>
        <el-header style="height: auto;">
            <div id="searchDiv" style="background-color: #fff; padding: 10px 10px 0 10px;">
                <el-form inline>
                    <el-form-item>
                        <el-date-picker size="small" v-model="partition" type="month"  placeholder="选择月份" format="yyyy年MM月" ></el-date-picker>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" type="primary" v-on:click="handleSearch">查询</el-button>
                        <el-button size="small" type="warning" v-on:click="handleClear">清空</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div style="background-color: #fff;margin-top:10px;padding: 10px;overflow: hidden;">
                <el-button-group size="small" >
                    <el-button size="small" type="success" icon="el-icon-download" :loading="exportLoading" v-on:click="handleExport">导出缴费记录到Excel</el-button>
                </el-button-group>
                <el-button-group style="float: right;">
                    <el-button size="small" type="primary" icon="el-icon-refresh" v-on:click="handleRefresh">刷新</el-button>
                </el-button-group>
            </div>

        </el-header>
        <el-main>
            <template>
                <el-table :data="tableData"  stripe  v-loading="loading"  style="width: 100%" >
                    <el-table-column prop="labourName" label="区县名称" ></el-table-column>
                    <el-table-column prop="labourPeople" label="区县人数" width="150"></el-table-column>
                    <el-table-column prop="chargeNum"  label="缴费人数"  width="150"></el-table-column>
                    <el-table-column prop="chargeMoney"  label="缴费金额"  width="200"></el-table-column>
                    <el-table-column prop="chargeRate"  label="缴费比率"  width="100">
                        <template slot-scope="scope">
                            <span>{{null==scope.row.chargeRate?0:scope.row.chargeRate*100}}%</span>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
        </el-main>
    </el-container>
</div>

<script type="text/javascript" src="/js/vue.min.js"></script>
<script type="text/javascript" src="/js/element-ui.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/jquery-1.12.4.min.js" type="text/javascript"></script>
<script src="/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
    new Vue({
        el: '#app',
        data: {
            loading:true,
            partition:null,
            tableData: [],
            exportLoading:false
        },
        methods: {
            handleRefresh: function () {
                location.reload();
            },
            handleSearch: function () {
                if(null==this.partition){
                    this.$message.error("请选择月份");
                    return false;
                }
                this.currentPage=1;
                this.loadData();
            },
            handleClear: function () {
                this.partition=new Date();
                this.loadData();
            },
            formatPartition:function (v) {
                var reg=/^20\d{4}$/;
                if(reg.test(v)){
                    return Math.floor(v/100)+"年"+formatNum(v%100)+"月"
                }
                return "";
            },
            formatTime: function (ms) {
                if(ms==undefined) return '';
                var reg=/^\d{10,}$/
                if (reg.test(ms)) {
                    return formatTime(new Date(ms));
                }
                return '';
            },
            handleExport:function(){
                if(null==this.partition){
                    this.$message.error("请选择月份");
                    return false;
                }
                var that=this;
                this.exportLoading=true;
                setTimeout(function () {
                    that.exportLoading=false;
                },10000);
                var partition=null==this.partition?0:this.partition.getFullYear()*100+this.partition.getMonth()+1;
                location.href="/admin/charge/statistic/districtexport?partition="+partition;
            },
            loadData: function () {
                var that = this;
                that.loading=true;
                $.ajax({
                    url:"/admin/charge/statistic/districtpage",
                    type:"post",
                    data:JSON.stringify({
                        partition:null==this.partition?0:this.partition.getFullYear()*100+this.partition.getMonth()+1
                    }),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        that.loading=false;
                        if(res.code=="success"){
                            that.tableData=res.data;
                        }else{
                            that.tableData=[];
                        }
                    }
                });
            }
        },
        created: function () {
            this.partition = new Date();
            this.loadData();
        }
    });
</script>
</body>
</html>