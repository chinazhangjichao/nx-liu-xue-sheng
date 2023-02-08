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
                    <el-table-column prop="roleId" label="角色编号" width="100"></el-table-column>
                    <el-table-column prop="roleName" label="角色名称" width="200"></el-table-column>
                    <el-table-column prop="roleDesc" label="角色描述"></el-table-column>
                    <el-table-column label="操作"  width="160">
                        <template slot-scope="scope">
                            <el-button-group>
                                <el-button type="success" icon="el-icon-edit" size="small" title="编辑" v-on:click="handleEdit(scope.row)"></el-button>
                                <el-button type="primary" icon="el-icon-setting" size="small" title="设置权限" v-on:click="handleRight(scope.row)"></el-button>
                                <el-button type="danger" icon="el-icon-delete" size="small" title="删除" v-if="scope.row.roleId>4" v-on:click="handleDelete(scope.$index,scope.row)" ></el-button>
                            </el-button-group>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
        </el-main>
    </el-container>
    <el-dialog title="编辑角色信息" :visible.sync="editDialogVisible" :close-on-click-modal="false">
        <el-form>
            <el-form-item label="角色名称" label-width="100px">
                <el-input v-model="editObj.roleName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="角色描述" label-width="100px">
                <el-input v-model="editObj.roleDesc" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleSave" :loading="saveLoading">保 存</el-button>
        </div>
    </el-dialog>
    <el-dialog title="设置权限信息" :visible.sync="rightDialogVisible" :fullscreen="true">
        <el-tree :data="treeData" :props="defaultProps" :default-checked-keys="treeCheckedData" show-checkbox default-expand-all node-key="id" ref="tree" highlight-current >
        </el-tree>
        <div slot="footer" class="dialog-footer">
            <el-button @click="rightDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleSaveRight" :loading="saveLoading">保 存</el-button>
        </div>
    </el-dialog>
</div>
<script src="/js/vue.min.js" type="text/javascript"></script>
<script src="/js/element-ui.js" type="text/javascript"></script>
<script src="/js/jquery-1.12.4.min.js" type="text/javascript"></script>
<script src="/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
    new Vue({
        el: '#app',
        data: {
            tableLoading:true,
            tableData: [],
            saveLoading:false,
            editDialogVisible:false,
            editObj:{"roleId":0,"roleName":"","roleDesc":""},
            rightDialogVisible:false,
            treeData:[],
            defaultProps: {
                children: 'children',
                label: 'label'
            },
            treeCheckedData:[]
        },
        methods: {
            handleRefresh: function () {
                location.reload();
            },
            handleCreate: function () {
                this.editObj={"roleId":0,"roleName":"","roleDesc":""};
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
                if(that.editObj.roleName==""){
                    that.$message.error("角色名称不能为空！");
                    return false;
                }
                that.saveLoading=true;
                $.ajax({
                    url:"/role/save",
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
            handleSaveRight:function () {
                var that =this;
                if(null==that.editObj){
                    that.$message.error("无效操作！");
                    return false;
                }
                var checked=this.$refs.tree.getCheckedNodes();
                var params=[];
                for (var i in checked){
                    params.push({"rrRole":that.editObj.roleId,"rrFun":{"funId":checked[i].id}})
                }
                that.saveLoading=true;
                $.ajax({
                    url:"/roleright/save",
                    type:"post",
                    data:JSON.stringify(params),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        that.saveLoading=false;
                        if(res.code=="success"){
                            that.$message({
                                message: '设置成功！',
                                type: 'success'
                            });
                            that.rightDialogVisible=false;
                        }else{
                            that.$message.error(res.message);
                        }
                    }
                });
            },
            handleDelete: function (idx, row) {
                var that = this;
                this.$confirm("确认要删除该角色？", '提示', {confirmButtonText: '确定',cancelButtonText: '取消',type: 'warning'}).then(function () {
                    $.ajax({
                        url:"/role/delete",
                        type:"post",
                        data:JSON.stringify({"id":row.roleId}),
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
                that.loading=true;
                $.ajax({
                    url:"/role/all",
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
            },
            loadTreeData: function () {
                var that = this;
                $.ajax({
                    url:"/function/tree",
                    type:"post",
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            that.treeData=res.data.list;
                        }else{
                            that.treeData=[];
                        }
                    }
                });
            },
            loadRoleRights: function (roleId) {
                var that = this;
                $.ajax({
                    url:"/roleright/byrole",
                    type:"post",
                    data:JSON.stringify({"roleId":roleId}),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            var list = res.data.list;
                            var arr = [];
                            for(var i in list){
                                arr.push(list[i].rrFun.funId);
                            }
                            that.treeCheckedData=arr;
                        }else{
                            that.treeCheckedData=[];
                        }
                        that.$nextTick(function () {
                            that.$refs.tree.setCheckedKeys(that.treeCheckedData);
                        });
                    }
                });
            }
        },
        mounted: function () {
            this.loadData();
            this.loadTreeData();
        }
    });
</script>
</body>
</html>