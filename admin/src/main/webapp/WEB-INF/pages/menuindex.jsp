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
    <style type="text/css">
        .avatar-uploader .el-upload{border:1px dashed #d9d9d9;border-radius:6px;cursor:pointer;position:relative;overflow:hidden}
        .avatar-uploader .el-upload:hover{border-color:#409EFF}
        .avatar-uploader-icon{font-size:28px;color:#8c939d;width:100px;height:100px;line-height:100px;text-align:center}
        .avatar{width:100px;height:100px;display:block;line-height: 100px;}
    </style>
</head>
<body class="gray-bg">
<div id="app" >
    <el-container>
        <el-header style="height: auto;">
            <div style="background-color: #fff;margin-top:10px;padding: 10px;">
                <el-button-group size="small" >
                    <el-button size="small" type="success" icon="el-icon-plus" v-on:click="handleCreate">新建一级菜单</el-button>
                </el-button-group>
                <el-button-group style="float: right;">
                    <el-button size="small" type="primary" icon="el-icon-refresh" v-on:click="handleRefresh">刷新</el-button>
                </el-button-group>
            </div>
        </el-header>
        <el-main>
            <el-table
                    :data="tableData"
                    style="width: 100%;margin-bottom: 20px;"
                    row-key="menuId"
                    strip
                    default-expand-all
                    :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
                <el-table-column prop="menuName" label="菜单名称" width="180"></el-table-column>
                <el-table-column prop="menuEnglish" label="英文名称" width="180"></el-table-column>
                <el-table-column prop="menuUrl" label="跳转地址"></el-table-column>
                <el-table-column prop="sortNum" label="排序号" width="80"></el-table-column>
                <el-table-column prop="isMobile" label="菜单类型" width="80" >
                    <template slot-scope="scope">
                        <el-tag type="primary" size="small" v-if="scope.row.isMobile==1">手机端</el-tag>
                        <el-tag type="warning" size="small" v-else>电脑端</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="160" >
                    <template slot-scope="scope">
                        <el-button-group>
                            <el-button type="success" icon="el-icon-plus" size="small" title="添加子菜单" v-on:click="handleAppend(scope.row)"></el-button>
                            <el-button type="primary" icon="el-icon-edit" size="small" title="编辑" v-on:click="handleEdit(scope.row)"></el-button>
                            <el-button type="danger" icon="el-icon-delete" size="small" title="删除" v-on:click="handleDelete(scope.row)"></el-button>
                        </el-button-group>
                    </template>
                </el-table-column>
            </el-table>
        </el-main>
    </el-container>
    <template>
        <el-dialog title="编辑菜单" :visible.sync="editDialogVisible"  :fullscreen="true" :close-on-click-modal="false">
            <el-form>
                <el-form-item label="上级菜单" label-width="100px">
                    <el-input v-model="editObj.menuParent.menuName" disabled autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="排序号" label-width="100px">
                    <el-input v-model="editObj.sortNum" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="菜单名称" label-width="100px">
                    <el-input v-model="editObj.menuName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="英文名称" label-width="100px">
                    <el-input v-model="editObj.menuEnglish" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="跳转地址" label-width="100px">
                    <el-input v-model="editObj.menuUrl" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="菜单类型" label-width="100px">
                    <el-select size="small" v-model="editObj.isMobile" placeholder="菜单类型">
                        <el-option v-for="item in types" :key="item.value" :label="item.name" :value="item.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="图标" label-width="100px">
                    <el-upload class="avatar-uploader"
                               :action="uploadUrl" 
                               :show-file-list="false"
                               :on-success="handleAvatarSuccess"
                               :before-upload="beforeAvatarUpload">
                        <el-image v-if="editObj.menuIcon" :src="editObj.menuIcon" class="avatar">
                            <div slot="error" class="image-slot">
                                <i class="el-icon-picture-outline"></i>
                            </div>
                        </el-image>
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleSave" :loading="saveLoading">保 存</el-button>
            </div>
        </el-dialog>
    </template>
</div>
    <script src="/js/vue.min.js" type="text/javascript"></script>
    <script src="/js/element-ui.js" type="text/javascript"></script>
    <script src="/js/jquery-1.12.4.min.js" type="text/javascript"></script>
    <script src="/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
    new Vue({
        el: '#app',
        data: {
            tableData:[],
            types:[{value:0,name:"电脑端"},{value:1,name:"手机端"}],
            loading:true,
            saveLoading:false,
            editDialogVisible:false,
            editObj:{"menuId":0,"menuName":"","menuEnglish":"","menuIcon":"","sortNum":1,"isMobile":0,"menuUrl":"",menuParent:{"menuId":0,"menuName":""}},
            uploadUrl:"/menu/upload",
        },
        methods: {
            handleRefresh: function () {
                location.reload();
            },
            handleCreate: function () {
                this.editObj={"menuId":0,"menuName":"","menuEnglish":"","menuIcon":"","sortNum":1,"isMobile":0,"menuUrl":"",menuParent:{"menuId":0,"menuName":"一级菜单"}};
                this.editDialogVisible=true;
            },
            handleAppend: function (data) {
                this.editObj={"menuId":0,"menuName":"","menuEnglish":"","menuIcon":"","sortNum":1,"isMobile":0,"menuUrl":"",menuParent:{"menuId":0,"menuName":"一级菜单"}};
                this.editObj.menuParent={"menuId":data.menuId,"menuName":data.menuName};
                this.editDialogVisible=true;
            },
            handleAvatarSuccess(res, file) {
                if(res.code=="success"){
                    this.editObj.menuIcon=res.data.fileName;
                }else{
                    this.$message.error(res.message);
                }
            },
            beforeAvatarUpload(file) {
                const isLtSize = file.size / 1024  < 200;
                if (!isLtSize) {
                    this.$message.error('图片大小不能超过 200KB!');
                }
                return isLtSize;
            },
            handleEdit: function (data) {
                var that =this;
                $.ajax({
                    url:"/menu/load",
                    type:"post",
                    data:JSON.stringify({"id":data.menuId}),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            that.editObj=res.data.obj;
                            if(null==that.editObj.menuParent){
                                that.editObj.menuParent={"menuId":0,"menuName":"一级菜单"};
                            }
                            that.editDialogVisible=true;
                        }else{
                            that.$message.error(res.message);
                        }
                    }
                });

            },
            handleSave:function () {
                var that =this;
                if(null==that.editObj){
                    that.$message.error("无效操作！");
                    return false;
                }

                if(that.editObj.menuName==""){
                    that.$message.error("名称不能为空！");
                    return false;
                }
                if(that.editObj.menuEnglish==""){
                    that.$message.error("英文名称不能为空！");
                    return false;
                }
                if(that.editObj.menuUrl==""){
                    that.$message.error("跳转地址不能为空！");
                    return false;
                }
                that.saveLoading=true;
                $.ajax({
                    url:"/menu/save",
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
            handleDelete: function (data) {
                var that = this;
                this.$confirm("确认要删除类别【"+data.menuName+"】？", '提示', {confirmButtonText: '确定',cancelButtonText: '取消',type: 'warning'}).then(function () {
                    $.ajax({
                        url:"/menu/delete",
                        type:"post",
                        data:JSON.stringify({"id":data.menuId}),
                        headers:{"Content-Type":"application/json;charset=utf-8"},
                        dataType:"json",
                        success:function (res) {
                            if(res.code=="success"){
                                that.$message({message: '删除成功！',type: 'success'});
                                that.loadData();
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
                    url:"/menu/all",
                    type:"post",
                    dataType:"json",
                    success:function (res) {
                        that.loading=false;
                        if(res.code=="success"){
                            that.tableData=res.data.list;
                        }else{
                            that.tableData=[];
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