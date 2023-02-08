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
        .avatar-uploader .el-upload{border:1px dashed #d9d9d9;border-radius:6px;cursor:pointer;position:relative;overflow:hidden}
        .avatar-uploader .el-upload:hover{border-color:#409EFF}
        .avatar-uploader-icon{font-size:28px;color:#8c939d;width:100px;height:100px;line-height:100px;text-align:center}
        .avatar{width:100px;height:100px;display:block}
    </style>
</head>
<body class="gray-bg">
<div id="app" >
    <el-container>
        <el-header style="height: auto;">
            <div id="searchDiv" style="background-color: #fff; padding: 10px 10px 0 10px;">
                <el-form inline>
                    <el-form-item style="width:100px;">
                        <el-select size="small" v-model="status" placeholder="用户状态">
                            <el-option
                                    v-for="item in statuses"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-input size="small" v-model="searchName" placeholder="关键字"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" type="primary" v-on:click="handleSearch">查询</el-button>
                        <el-button size="small" type="warning" v-on:click="handleClear">清空</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div style="background-color: #fff;margin-top:10px;padding: 10px;overflow: hidden;">
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
                <el-table :data="tableData" v-loading="loading" stripe style="width: 100%" >
                    <el-table-column prop="userName" label="登录账号" width="200"></el-table-column>
                    <el-table-column prop="realName" label="姓名" width="200"></el-table-column>
                    <el-table-column prop="userPhone" label="电话" width="200"></el-table-column>
                    <el-table-column label="角色" >
                        <template slot-scope="scope">
                            <el-tag type="info" v-for="item in scope.row.userRoles" size="small" style="margin-right:10px;">{{item.urRole.roleName}}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="loginNum" label="登录次数" width="80"></el-table-column>
                    <el-table-column prop="loginTime" label="最后登录" width="100">
                        <template slot-scope="scope">
                            <span>{{ formatDate(scope.row.loginTime) }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="online" label="状态" width="100">
                        <template slot-scope="scope">
                            <el-switch @change="handleStatus(scope.row)"
                                       v-model="scope.row.userStatus"
                                       active-color="#13ce66"
                                       inactive-color="#ff4949"
                                       :active-value="1"
                                       :inactive-value="0">
                            </el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="160" >
                        <template slot-scope="scope">
                            <el-button-group>
                                <el-button type="success" icon="el-icon-edit" size="small" title="编辑" v-on:click="handleEdit(scope.row)"></el-button>
                                <el-button type="primary" icon="el-icon-setting" size="small" title="设置角色" v-on:click="handleRole(scope.row)"></el-button>
                                <el-button type="danger" icon="el-icon-key" size="small" title="重置密码" v-on:click="handleReset(scope.row)"></el-button>
                            </el-button-group>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
        </el-main>
        <el-footer style="text-align: right;">
            <el-pagination
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[10, 20, 50, 100, 200]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </el-footer>
    </el-container>
    <el-dialog title="编辑用户信息" :visible.sync="editDialogVisible" :close-on-click-modal="false">
        <el-form>
            <el-form-item label="头像" label-width="100px">
                <el-upload class="avatar-uploader"
                           :action="uploadUrl"
                           :show-file-list="false"
                           :on-success="handleAvatarSuccess"
                           :before-upload="beforeAvatarUpload">
                    <img v-if="editObj.headImg" :src="editObj.headImg" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
            </el-form-item>
            <el-form-item label="姓名" label-width="100px">
                <el-input v-model="editObj.realName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="电话" label-width="100px">
                <el-input v-model="editObj.userPhone" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="登录账号" label-width="100px">
                <el-input v-model="editObj.userName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="登录密码" label-width="100px">
                <el-input disabled placeholder="新建用户默认密码为123456" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleSave" :loading="saveLoading">保 存</el-button>
        </div>
    </el-dialog>
    <el-dialog title="设置用户角色" :visible.sync="roleDialogVisible" :close-on-click-modal="false">
        <el-row >
            <tempplate>
                <el-select v-model="userRoles" multiple placeholder="请选择角色">
                    <el-option v-for="item in roles" :key="item.roleId" :label="item.roleName" :value="item.roleId"></el-option>
                </el-select>
            </tempplate>
        </el-row>
        <div slot="footer" class="dialog-footer">
            <el-button @click="handleCancleRole">取 消</el-button>
            <el-button type="primary" @click="handleSaveRole" :loading="saveLoading">保 存</el-button>
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
            loading:true,
            currentPage: 1,
            pageSize: 20,
            total: 1,
            searchName: '',
            status:null,
            statuses:[{name:'正常',value:1},{name:'封停',value:0}],
            tableLoading:true,
            tableData: [],
            saveLoading:false,
            editDialogVisible:false,
            selectDept:0,
            editObj:{"userId":0,"userName":"","headImg":"","realName":"","userPhone":""},
            roleDialogVisible:false,
            roles:[],
            userRoles:[],
            uploadUrl:"/user/upload",
            disabled:false
        },
        methods: {
            handleRefresh: function () {
                location.reload();
            },
            handleSearch: function () {
                this.currentPage=1;
                this.loadData();
            },
            handleClear: function () {
                this.status=null;
                this.searchName = '';
                this.loadData();
            },
            handleCreate: function () {
                this.editObj={"userId":0,"userName":"","headImg":"","realName":"","userPhone":""};
                this.editDialogVisible=true;
            },
            handleEdit: function (row) {
                var that =this;
                $.ajax({
                    url:"/user/load",
                    type:"post",
                    data:JSON.stringify({id: row.userId}),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            that.editObj=res.data;
                            if(null==that.editObj.headImg){
                                that.editObj.headImg="";
                            }
                            if(null==that.editObj.userDistrict){
                                that.editObj.userDistrict={};
                            }
                            if(null==that.editObj.userLabour){
                                that.editObj.userLabour={};
                            }else{
                                that.labours=[that.editObj.userLabour];
                            }
                            that.editDialogVisible=true;
                        }else{
                            that.$message.error(res.message);
                        }
                    }
                });
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
            formatDate: function (ms) {
                if (isNaN(ms)) {return '';}
                return formatDate(new Date(ms));
            },
            handleAvatarSuccess:function(res, file) {
                if(res.code=="success"){
                    this.editObj.headImg=res.data.fileName;
                }else{
                    this.$message.error(res.message);
                }
            },
            beforeAvatarUpload:function(file) {
                const isLtSize = file.size / 1024 / 1024 < 1;
                if (!isLtSize) {
                    this.$message.error('上传图片大小不能超过 1MB!');
                }
                return isLtSize;
            },
            handleRole: function (row) {
                this.editObj=row;
                if(null==this.editObj.userDistrict){
                    this.editObj.userDistrict={};
                }
                if(null==this.editObj.userLabour){
                    this.editObj.userLabour={};
                }
                this.userRoles=[];
                if(row.userRoles){
                    const  list = row.userRoles;
                    for (var i in list){
                        this.userRoles.push(list[i].urRole.roleId);
                    }
                }
                this.roleDialogVisible=true;
            },
            handleCancleRole:function () {
                this.roleDialogVisible = false;
                this.saveLoading=false;
                this.userRoles=[];
            },
            handleReset: function (row) {
                var that = this;
                this.$confirm('确定要将用户' + row.userName + '的密码重置为默认密码123456吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    $.ajax({
                        url:"/user/reset",
                        type:"post",
                        data:JSON.stringify({
                            id: row.userId
                        }),
                        headers:{"Content-Type":"application/json;charset=utf-8"},
                        dataType:"json",
                        success:function (res) {
                            if (res.code == "success") {
                                that.$message({type: 'success', message: res.message});
                            }else {
                                that.$message({type: 'error', message: res.message});
                            }
                        }
                    });
                });
            },
            handleStatus: function (row) {
                var that = this;
                this.$confirm('确定要' + (row.userStatus == 1 ? '启用' : '封停') + '该用户吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    $.ajax({
                        url:"/user/status",
                        type:"post",
                        data:JSON.stringify({
                            id: row.userId,
                            status: row.userStatus
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

            handleSave:function () {
                var that =this;
                if(null==that.editObj){
                    that.$message.error("无效操作！");
                    return false;
                }
                if(that.editObj.realName==""){
                    that.$message.error("姓名不能为空！");
                    return false;
                }
                if(that.editObj.userPhone==""){
                    that.$message.error("联系电话不能为空！");
                    return false;
                }
                if(that.editObj.userName==""){
                    that.$message.error("登录账号不能为空！");
                    return false;
                }
                that.saveLoading=true;
                $.ajax({
                    url:"/user/save",
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
            handleSaveRole:function () {
                var that =this;
                if(null==that.editObj){
                    that.$message.error("无效操作！");
                    return false;
                }
                if(this.userRoles.length==0){
                    that.$message.error("请选择角色！");
                    return false;
                }
                that.saveLoading=true;
                $.ajax({
                    url:"/user/rolesave",
                    type:"post",
                    data:JSON.stringify({userId:that.editObj.userId,roleIds:that.userRoles}),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        that.saveLoading=false;
                        if(res.code=="success"){
                            that.$message({message: '设置成功！',type: 'success'});
                            that.loadUserRoles(that.editObj.userId);
                            that.roleDialogVisible=false;

                        }else{
                            that.$message.error(res.message);
                        }
                    }
                });
            },
            loadData: function () {
                var that = this;
                that.loading=true;
                $.ajax({
                    url:"/user/page",
                    type:"post",
                    data:JSON.stringify({
                        currentPage: this.currentPage,
                        pageSize: this.pageSize,
                        status: this.status,
                        searchName: this.searchName
                    }),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        that.loading=false;
                        if(res.code=="success"){
                            that.tableData=res.data.list;
                            if(res.data.pageNo==1){
                                that.total=res.data.total;
                            }
                        }else{
                            that.tableData=[];
                        }
                    }
                });
            },
            loadUserRoles:function (userId) {
                var that =this;
                $.ajax({
                    url:"/user/role",
                    type:"post",
                    data:JSON.stringify({id: userId}),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            const list =res.data.list;
                            if(that.editObj){
                                that.editObj.userRoles=list;
                            }
                        }
                    }
                });
            },
            loadRoles: function () {
                var that = this;
                $.ajax({
                    url:"/role/all",
                    type:"post",
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            that.roles=res.data.list;
                        }
                    }
                });
            }
        },
        mounted: function () {
            this.loadData();
            this.loadRoles();
        }
    });
</script>
</body>
</html>