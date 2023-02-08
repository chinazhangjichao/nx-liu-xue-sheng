<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
     <title>${applicationScope.ADMINTITLE }</title>
    <link rel="shortcut icon" href="favicon.ico">
    <link href="/css/element-ui.css" rel="stylesheet" type="text/css">
    <link href="/css/style.css" rel="stylesheet">
</head>
<body class="gray-bg">
<div id="app" >
    <el-container>
        <el-header style="height: auto;">
            <div style="background-color: #fff;margin-top:10px;padding: 10px;">
                <el-button-group size="small" >
                    <el-button size="small" type="success" icon="el-icon-plus" v-on:click="handleCreate">新建</el-button>
                </el-button-group>
                <el-button-group style="float: right;">
                    <el-button size="small" type="primary" icon="el-icon-refresh" v-on:click="handleRefresh">刷新</el-button>
                </el-button-group>
            </div>
        </el-header>
        <el-main>
            <template>
                <el-table :data="tableData" v-loading="tableLoading" stripe style="width: 100%" >
                    <el-table-column prop="dicId" label="编号" width="100"></el-table-column>
                    <el-table-column prop="dicKey" label="字典名" width="200"></el-table-column>
                    <el-table-column prop="dicValue" label="字典值" width="200"></el-table-column>
                    <el-table-column prop="dicDesc" label="字典描述"></el-table-column>
                    <el-table-column label="操作"  width="180">
                        <template slot-scope="scope">
                            <el-button-group>
                                <el-button type="success" icon="el-icon-edit" size="small" title="编辑" v-on:click="handleEdit(scope.row)">编辑</el-button>
                                <el-button type="danger" icon="el-icon-delete" size="small" title="删除" v-on:click="handleDelete(scope.$index,scope.row)" >删除</el-button>
                            </el-button-group>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
        </el-main>
    </el-container>
    <el-dialog title="编辑字典" :visible.sync="editDialogVisible" :close-on-click-modal="false">
        <el-form>
            <el-form-item label="字典名称" label-width="100px">
                <el-input v-model="editObj.dicKey" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="字典值" label-width="100px">
                <el-input v-model="editObj.dicValue" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="字典描述" label-width="100px">
                <el-input v-model="editObj.dicDesc" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleSave" :loading="saveLoading">保 存</el-button>
        </div>
    </el-dialog>
</div>
    <script type="text/javascript" src="/js/vue.min.js" ></script>
    <script type="text/javascript" src="/js/element-ui.js"></script>
    <script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
    <script type="text/javascript"  src="/js/common.js"></script>
	<script type="text/javascript">
        new Vue({
            el: '#app',
            data: {
                tableLoading:true,
                tableData: [],
                saveLoading:false,
                editDialogVisible:false,
                editObj:{"dicId":0,"dicKey":"","dicValue":"","dicDesc":""},
            },
            methods: {
                handleRefresh: function () {
                    location.reload();
                },
                handleCreate: function () {
                    this.editObj={"dicId":0,"dicKey":"","dicValue":"","dicDesc":""};
                    this.editDialogVisible=true;
                },
                handleRight: function (row) {
                    this.editObj=row;
                    this.loadRoleRights(row.roleId);
                    this.rightDialogVisible=true;
                },
                handleEdit: function (row) {
                    this.editObj=row;
                    this.editDialogVisible=true;
                },
                handleSave:function () {
                    var that =this;
                    if(null==that.editObj){
                        that.$message.error("无效操作！");
                        return false;
                    }
                    if(that.editObj.dicKey==""){
                        that.$message.error("字典名不能为空！");
                        return false;
                    }
                    if(that.editObj.dicValue==""){
                        that.$message.error("字典值不能为空！");
                        return false;
                    }
                    that.saveLoading=true;
                    $.ajax({
                        url:"/dictionary/save",
                        type:"post",
                        data:JSON.stringify(that.editObj),
                        headers:{"Content-Type":"application/json;charset=utf-8"},
                        dataType:"json",
                        success:function (res) {
                            that.saveLoading=false;
                            if(res.code=="success"){
                                that.editDialogVisible=false;
                                that.loadData();
                            }else{
                                that.$message.error(res.message);
                            }
                        }
                    });
                },
                handleDelete: function (idx, row) {
                    var that = this;
                    this.$confirm("确认要删除该参数，该操作可能会导致系统运行异常？", '提示', {confirmButtonText: '确定',cancelButtonText: '取消',type: 'warning'}).then(function () {
                        $.ajax({
                            url:"/dictionary/delete",
                            type:"post",
                            data:JSON.stringify({"id":row.dicId}),
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
                loadData: function () {
                    var that = this;
                    $.ajax({
                        url:"/dictionary/all",
                        type:"post",
                        dataType:"json",
                        success:function (res) {
                            that.tableLoading=false;
                            if(res.code=="success"){
                                that.tableData=res.data.list;
                            }else{
                                that.tableData=[];
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
