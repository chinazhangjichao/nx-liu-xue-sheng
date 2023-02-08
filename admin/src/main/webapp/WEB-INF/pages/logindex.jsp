<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no">
    <title>${applicationScope.ADMINTITLE }</title>
    <link href="/css/element-ui.css" rel="stylesheet" type="text/css">
    <link href="/css/style.css" rel="stylesheet">
</head>
<body class="gray-bg">
<div id="app" >
    <template>
    <el-container>
        <el-header style="height: auto;">
            <div id="searchDiv" style="background-color: #fff; padding: 10px 10px 0 10px;">
                <el-form inline>
                    <el-form-item>
                        <el-date-picker size="small" v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']" @change="handleChangeDate"></el-date-picker>
                    </el-form-item>
                    <el-form-item style="width:100px;">
                        <el-select size="small" v-model="status" placeholder="操作状态">
                            <el-option
                                    v-for="item in statuses"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item style="width:150px;">
                        <el-input size="small" v-model="searchName" placeholder="操作人"></el-input>
                    </el-form-item>
                    <el-form-item style="width:150px;">
                        <el-input size="small" v-model="searchModule" placeholder="操作模块"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input size="small" v-model="searchContent" placeholder="操作内容"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" type="primary" v-on:click="handleSearch">查询</el-button>
                        <el-button size="small" type="warning" v-on:click="handleClear">清空</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div style="background-color: #fff;margin-top:10px;padding: 10px;">
                <el-button-group size="small" >
                    <el-button size="small" type="danger" icon="el-icon-delete-solid" v-on:click="handleMultiDelete">批量刪除</el-button>
                </el-button-group>
                <el-button-group style="float: right;">
                    <el-button size="small" type="primary" icon="el-icon-refresh" v-on:click="handleRefresh">刷新</el-button>
                </el-button-group>
            </div>
        </el-header>
        <el-main>
            <el-table :data="tableData" v-loading="tableLoading" stripe  style="width: 100%"  @selection-change="handleSelectionChange">
                <el-table-column prop="logId" type="selection" width="60"></el-table-column>
                <el-table-column prop="logModule" label="模块" width="200"></el-table-column>
                <el-table-column prop="logContent" label="具体操作" ></el-table-column>
                <el-table-column prop="logFlag" label="状态" width="80" >
                    <template slot-scope="scope">
                        <el-tag type="success" size="small" v-if="scope.row.logFlag==1">成功</el-tag>
                        <el-tag type="danger" size="small" v-else>失败</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="logUser" label="操作人" width="150"></el-table-column>
                <el-table-column prop="logIP" label="操作IP" width="150"></el-table-column>
                <el-table-column prop="logTime" label="操作时间" width="100">
                    <template slot-scope="scope">
                        <span>{{ formatTime(scope.row.logTime) }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                    <template slot-scope="scope">
                        <el-button-group size="small" >
                            <el-button type="info" icon="el-icon-view" size="small" v-on:click="handleDetail(scope.row)" title="查看详情"></el-button>
                            <el-button type="danger" icon="el-icon-delete" size="small"  v-on:click="handleDelete(scope.$index,scope.row)" title="删除"></el-button>
                        </el-button-group>
                    </template>
                </el-table-column>
            </el-table>
        </el-main>
        <el-footer style="text-align: right;">
            <el-pagination
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[10, 20, 50, 100]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </el-footer>
    </el-container>
    <el-dialog title="日志详情" :visible.sync="detailDialogVisible" :fullscreen="true" :close-on-click-modal="false">
        <el-descriptions :column="2" border>
            <el-descriptions-item label="操作人">{{editObj.logUser}}</el-descriptions-item>
            <el-descriptions-item label="操作时间">{{formatTime(editObj.logTime)}}</el-descriptions-item>
            <el-descriptions-item label="操作模块">{{editObj.logModule}}</el-descriptions-item>
            <el-descriptions-item label="具体操作">{{editObj.logContent}}</el-descriptions-item>
            <el-descriptions-item label="请求方式">{{editObj.requestMethod}}</el-descriptions-item>
            <el-descriptions-item label="请求参数">{{editObj.requestParams}}</el-descriptions-item>
            <el-descriptions-item label="操作系统">{{editObj.system}}</el-descriptions-item>
            <el-descriptions-item label="浏览器">{{editObj.browser}}</el-descriptions-item>
            <el-descriptions-item label="操作IP">{{editObj.logIp}}</el-descriptions-item>
            <el-descriptions-item label="状态">
                <el-tag type="success" v-if="editObj.logFlag==1">成功</el-tag>
                <el-tag type="danger" v-if="editObj.logFlag==0">失败</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="失败原因" :span="2" v-if="editObj.logFlag==0">{{editObj.exception}}</el-descriptions-item>
        </el-descriptions>
        <div slot="footer" class="dialog-footer">
            <el-button @click="detailDialogVisible = false">取 消</el-button>
        </div>
    </el-dialog>
    </template>
</div>
<script type="text/javascript" src="/js/vue.min.js"></script>
<script type="text/javascript" src="/js/element-ui.js"></script>
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript">
    new Vue({
        el: '#app',
        data: {
            tableLoading:true,
            currentPage: 1,
            pageSize: 20,
            total: 1,
            searchName: '',
            searchModule: '',
            searchContent: '',
            status:null,
            statuses:[{name:'成功',value:1},{name:'失败',value:0}],
            dateRange:[],
            startTime:0,
            endTime:0,
            tableData: [],
            multipleSelection: [],
            selectIds: [],
            detailDialogVisible:false,
            editObj:{}

        },
        methods: {
            handleRefresh: function () {
                location.reload();
            },
            handleChangeDate:function(){
                if(null!=this.dateRange){
                    this.startTime = this.dateRange[0].getTime();
                    this.endTime = this.dateRange[1].getTime();
                }else{
                    this.startTime = 0;
                    this.endTime = 0;
                }
            },
            handleSearch: function () {
                this.loadData();
            },
            handleClear: function () {
                this.dateRange = [];
                this.startTime = 0;
                this.endTime = 0;
                this.searchName = '';
            },
            handleSelectionChange: function (val) {
                this.multipleSelection = val;
                for (var i = 0; i < this.multipleSelection.length; i++) {
                    this.selectIds[i] = this.multipleSelection[i].logId;
                }
            },
            handleSizeChange: function (val) {
                this.pageSize = val;
                this.loadData();
            },
            handleCurrentChange: function (val) {
                this.currentPage = val;
                this.loadData();
            },
            formatTime: function (ms) {
                if (isNaN(ms)) {return '';}
                return formatTime(new Date(ms));
            },
            handleDetail:function(row){
                var that =this;
                $.ajax({
                    url:"/log/load",
                    type:"post",
                    data:JSON.stringify({id: row.logId}),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            that.detailDialogVisible=true;
                            that.editObj=res.data;
                        }else{
                            that.$message.error(res.message);
                        }
                    }
                });
            },
            handleDelete: function (idx, row) {
                var that = this;
                this.$confirm("确认要删除该信息？", '提示', {confirmButtonText: '确定',cancelButtonText: '取消',type: 'warning'}).then(function () {
                    $.ajax({
                        url:"/log/delete",
                        type:"post",
                        data:JSON.stringify({"id":row.logId}),
                        headers:{"Content-Type":"application/json;charset=utf-8"},
                        dataType:"json",
                        success:function (res) {
                            if(res.code=="success"){
                                that.$message({
                                    message: '删除成功！',
                                    type: 'success'
                                });
                                that.tableData.splice(idx,1);
                            }else{
                                that.$message.error(res.message);
                            }
                        }
                    });
                });
            },
            handleMultiDelete: function () {
                var that = this;
                this.$confirm('确定要删除选中的记录吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    $.ajax({
                        url:"/log/multidelete",
                        type:"post",
                        traditional:true,
                        data:JSON.stringify(that.selectIds),
                        headers:{"Content-Type":"application/json;charset=utf-8"},
                        dataType:"json",
                        success:function(res){
                            if (res.code == "success") {
                                that.$message({type: 'success', message: res.message});
                                if(that.tableData){
                                    for(var i=0;i<that.tableData.length;i++){
                                        for(var j=0;j<that.selectIds.length;j++){
                                            if(that.tableData[i].logId==that.selectIds[j]){
                                                that.tableData.splice(i, 1);
                                            }
                                        }
                                    }
                                }
                            }else{
                                that.$message({type: 'error', message: res.message});
                            }
                        }
                    });
                });
            },
            loadData: function () {
                var that = this;
                that.tableLoading=true;
                $.ajax({
                    url:"/log/page",
                    type:"post",
                    data:JSON.stringify({
                        currentPage: this.currentPage,
                        pageSize: this.pageSize,
                        startTime: this.startTime,
                        endTime: this.endTime,
                        status: this.status,
                        user: this.searchName,
                        module: this.searchModule,
                        content: this.searchContent,
                    }),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        that.tableLoading=false;
                        if (res.code == "success") {
                            that.tableData = res.data.list;
                            if (res.data.pageNo == 1) {
                                that.total = res.data.total;
                            }
                            that.currentPage = res.data.pageNo;
                            that.pageSize = res.data.pageSize;
                        } else{
                            that.tableData =[];
                        }
                    }
                });
            }
        },
        created: function () {
            this.loadData();
        }
    });
</script>
</body>
</html>