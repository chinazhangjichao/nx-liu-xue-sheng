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
                <el-table :data="tableData" v-loading="tableLoading"  stripe style="width: 100%" >
                    <el-table-column prop="majorId" label="编号" width="100"></el-table-column>
                    <el-table-column prop="majorName" label="专业名称" width="200" ></el-table-column>
                    <el-table-column prop="majorEnglish" label="英文名称" width="200"></el-table-column>
                    <el-table-column prop="majorDetail" label="专业介绍" ></el-table-column>
                    <el-table-column prop="majorSort" label="排序号" width="80" ></el-table-column>
                    <el-table-column prop="online" label="状态" width="100">
                        <template slot-scope="scope">
                            <el-switch @change="handleStatus(scope.row)"
                                       v-model="scope.row.majorStatus"
                                       active-color="#13ce66"
                                       inactive-color="#ff4949"
                                       :active-value="1"
                                       :inactive-value="0">
                            </el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作"  width="150">
                        <template slot-scope="scope">
                            <el-button-group>
                                <el-button type="success" icon="el-icon-edit" size="small" title="编辑" v-on:click="handleEdit(scope.row)"></el-button>
                                <el-button type="danger" icon="el-icon-delete" size="small" title="删除" v-on:click="handleDelete(scope.$index,scope.row)" ></el-button>
                            </el-button-group>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
        </el-main>
    </el-container>
    <el-dialog title="编辑专业信息" :visible.sync="editDialogVisible" :close-on-click-modal="false">
        <el-form>
            <el-form-item label="排序号" label-width="100px">
                <el-input v-model="editObj.majorSort" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="专业名称" label-width="100px">
                <el-input v-model="editObj.majorName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="英文名称" label-width="100px">
                <el-input v-model="editObj.majorEnglish" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="专业介绍" label-width="100px">
                <el-input v-model="editObj.majorDetail"  type="textarea" :rows="5" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleSave" :loading="saveLoading">保 存</el-button>
        </div>
    </el-dialog>
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
                tableData: [],
                saveLoading:false,
                editDialogVisible:false,
                editObj:{"majorId":0,"majorSort":1,"majorStatus":1,"majorName":"","majorEnglish":"","majorDetail":""},
            },
            methods: {
                handleRefresh: function () {
                    location.reload();
                },
                handleCreate: function () {
                    this.editObj={"majorId":0,"majorSort":1,"majorStatus":1,"majorName":"","majorEnglish":"","majorDetail":""};
                    this.editDialogVisible=true;
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
                    if(that.editObj.majorName==""){
                        that.$message.error("专业名称不能为空！");
                        return false;
                    }
                    if(that.editObj.majorEnglish==""){
                        that.$message.error("英文名称不能为空！");
                        return false;
                    }
                    that.saveLoading=true;
                    $.ajax({
                        url:"/major/save",
                        type:"post",
                        data:JSON.stringify(that.editObj),
                        headers:{"Content-Type":"application/json;charset=utf-8"},
                        dataType:"json",
                        success:function (res) {
                            that.saveLoading=false;
                            if(res.code=="success"){
                                that.editDialogVisible=false;
                                if(that.editObj.majorId==0){
                                    that.tableData.push(res.data.obj);
                                }
                            }else{
                                that.$message.error(res.message);
                            }
                        }
                    });
                },
                handleDelete: function (idx, row) {
                    var that = this;
                    this.$confirm("确认要删除数据"+row.majorName+"？", '提示', {confirmButtonText: '确定',cancelButtonText: '取消',type: 'warning'}).then(function () {
                        $.ajax({
                            url:"/major/delete",
                            type:"post",
                            data:JSON.stringify({"id":row.majorId}),
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
                handleStatus: function (row) {
                    var that = this;
                    this.$confirm('确定要' + (row.majorStatus == 1 ? '启用' : '停用') + '该数据吗？', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(function () {
                        $.ajax({
                            url:"/major/status",
                            type:"post",
                            data:JSON.stringify({
                                id: row.majorId,
                                status: row.majorStatus
                            }),
                            headers:{"Content-Type":"application/json;charset=utf-8"},
                            dataType:"json",
                            success:function (res) {
                                if (res.code == "success") {
                                    that.$message({type: 'success', message: res.message});
                                } else {
                                    that.$message({type: 'error', message: res.message});
                                }
                            }
                        });
                    }).catch(function () {
                        that.$message({type: 'error', message: '操作异常，已取消'});
                    });
                },
                loadData: function () {
                    var that = this;
                    that.tableLoading=true;
                    $.ajax({
                        url:"/major/all",
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
