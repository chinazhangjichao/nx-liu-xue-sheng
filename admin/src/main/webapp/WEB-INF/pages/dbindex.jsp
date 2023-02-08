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

</head>
<body class="gray-bg">
<div id="app" >
    <el-container>
        <el-header style="height: auto;">
		    <div style="background-color: #fff;margin-top:10px;padding: 10px;">
		    		<el-button-group size="small" >
		                <el-button size="small" type="success" icon="el-icon-download" v-on:click="handleBackup">备份</el-button>
		                </el-button>
		            </el-button-group>
		            <el-button-group style="float: right;">
		                <el-button size="small" type="primary" icon="el-icon-refresh" v-on:click="handleRefresh">刷新</el-button>
		            </el-button-group>
		    </div>
        </el-header>
        <el-main>
            <template>
                <el-table
                        :data="tableData"
                        stripe
                        style="width: 100%">
                    <el-table-column prop=fileName label="文件名" ></el-table-column>
                      <el-table-column prop="fileTime" label="创建时间" width="200">
                        <template slot-scope="scope">
                            <span>{{ formatTime(scope.row.fileTime) }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                        <el-button-group size="small" >
		                <el-button size="small" type="warning" icon="el-icon-delete" v-on:click="handleRestore(scope.row)">还原</el-button></el-button>
		                <el-button size="small" type="danger" icon="el-icon-delete" v-on:click="handleDelete(scope.row)">刪除</el-button></el-button>
		            </el-button-group>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
        </el-main>
    </el-container>
</div>
<script src="/js/vue.min.js" type="text/javascript"></script>
<script src="/js/element-ui2.13.1.js" type="text/javascript"></script>
<script src="/js/jquery-1.12.4.min.js" type="text/javascript"></script>
<script src="/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
    new Vue({
        el: '#app',
        data: {
            tableData: []
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
            handleDelete: function ( row) {
                var that = this;
                this.$confirm('此操作将永久删除改记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    $.post("/db/delete", {
                        name: row.fileName
                    }, function (res) {
                    	if(!hasRoleRight(res)){
                			that.$message({type: 'error', message: '权限不足'});
                		}else{
                			if(typeof(res)=='string'){res = JSON.parse(res);}
                            if (res.code == "success") {
                                that.$message({type: 'success', message: res.message});
                                 if(that.tableData){
    		                            for(var i=0;i<that.tableData.length;i++){
    	                            		if(that.tableData[i].fileName==row.fileName){
    	                            			 that.tableData.splice(i, 1);
    	                            		}
    		                            }
    	                            } 
                            } else {
                                that.$message({type: 'error', message: res.message});
                            } 
                		}
                    });
                }).catch(function () {
                    that.$message({type: 'error', message: '已取消'});
                });
            },
             handleBackup: function () {
                var that = this;
                this.$confirm('确认要备份数据库吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    $.post("/db/backup", {
                    }, function (res) {
                    	if(!hasRoleRight(res)){
                			that.$message({type: 'error', message: '权限不足'});
                		}else{
                			if(typeof(res)=='string'){res = JSON.parse(res);}
                            if (res.code == "success") {
                                location.reload();
                            } else{
                                that.$message({type: 'error', message: res.message});
                            }
                		}
                    	
                    	
                    });
                }).catch(function () {
                    that.$message({type: 'error', message: '已取消'});
                });
            },
            handleRestore: function (row) {
                var that = this;
                this.$confirm('确认要使用该备份还原数据库吗，还原有可能会导致数据丢失?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    $.post("/db/restore", {
                    name: row.fileName
                    }, function (res) {
                    	if(!hasRoleRight(res)){
                			that.$message({type: 'error', message: '权限不足'});
                		}else{
                			if(typeof(res)=='string'){res = JSON.parse(res);}
                            if (res.code == "success") {
                                that.$message({type: 'success', message: res.message});
                            } else {
                                that.$message({type: 'error', message: res.message});
                            }
                		}
                    	
                    });
                }).catch(function () {
                    that.$message({type: 'error', message: '已取消'});
                });
            },
            formatTime: function (ms) {
                if (isNaN(ms)) {return '';}
                return formatTime(new Date(ms));
            },
            loadData: function () {
                var that = this;
                $.post("/db/page", {
                    currentPage: this.currentPage,
                    pageSize: this.pageSize,
                    startTime: this.startTime,
                    endTime: this.endTime,
                    searchName: this.searchName
                }, function (res) {
                	if(!hasRoleRight(res)){
            			that.$message({type: 'error', message: '权限不足，无法加载用户列表！'});
            		}else{
            			if(typeof(res)=='string'){res = JSON.parse(res);}
                        if (res.code == "success") {
                            that.tableData = res.data.list;
                        } else{
                            that.$message.error(res.message);
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