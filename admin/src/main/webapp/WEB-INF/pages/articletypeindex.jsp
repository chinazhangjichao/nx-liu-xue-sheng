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

        .el-tree-node__content{height:36px;line-height: 36px;}
        .el-tree-node__content .custom-tree-node{flex:1;display:flex;align-items:center;justify-content:space-between;font-size:14px;padding-right:8px}
        .el-tree-node__content .typeIcon{width: 36px;height: 36px;font-size: 16px;line-height: 36px;text-align: center;float: left;}
        .el-tree-node__content .typeIcon img{width: 30px;height: 30px;border-radius:4px;margin:3px;}

        .el-tree-node__children .typeIcon{width: 30px;height: 30px;font-size: 14px;line-height: 30px;text-align: center;float: left;}
        .el-tree-node__children .typeIcon img{width: 24px;height: 24px;border-radius:4px;margin:3px;}
    </style>
</head>
<body class="gray-bg">
<div id="app" >
    <el-container>
        <el-header style="height: auto;">
            <div style="background-color: #fff;margin-top:10px;padding: 10px;">
                <el-button-group size="small" >
                    <el-button size="small" type="success" icon="el-icon-plus" v-on:click="handleCreate">新建一级类别</el-button>
                </el-button-group>
                <el-button-group style="float: right;">
                    <el-button size="small" type="primary" icon="el-icon-refresh" v-on:click="handleRefresh">刷新</el-button>
                </el-button-group>
            </div>
        </el-header>
        <el-main>
            <el-tree v-loading="loading" :data="treeData"  node-key="typeId" default-expand-all :expand-on-click-node="false">
                <span class="custom-tree-node" slot-scope="{ node, data }">
                    <span>
                        <el-image v-if="data.typeImg" :src="data.typeImg" class="typeIcon">
                          <div slot="error" class="image-slot">
                            <i class="el-icon-picture-outline"></i>
                          </div>
                        </el-image>
                        <span>{{data.typeName }}({{data.typeNo }})</span>
                    </span>
                    <span>
                        <el-button-group>
                            <el-button type="success" icon="el-icon-plus" size="mini" @click="handleAppend(data)" v-if="null==data.typeParent" >添加子类</el-button>
                            <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleEdit(node,data)">编辑</el-button>
                            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(node, data)">删除</el-button>
                        </el-button-group>
                    </span>
                </span>
            </el-tree>
        </el-main>
    </el-container>
    <template>
        <el-dialog title="编辑类别" :visible.sync="editDialogVisible"  :fullscreen="true" :close-on-click-modal="false">
            <el-form>
                <el-form-item label="上级类别" label-width="100px">
                    <el-input v-model="editObj.typeParent.typeName" disabled autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="类别编号" label-width="100px">
                    <el-input v-model="editObj.typeNo" autocomplete="off" placeholder="请输入两位数字">
                        <template slot="prepend" v-if="editObj.typeParent.typeNo>0">{{editObj.typeParent.typeNo}}</template>
                    </el-input>
                </el-form-item>
                <el-form-item label="类别名称" label-width="100px">
                    <el-input v-model="editObj.typeName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="英文名称" label-width="100px">
                    <el-input v-model="editObj.typeEnglish" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="类别图标" label-width="100px">
                    <el-upload class="avatar-uploader"
                               :action="uploadUrl" 
                               :show-file-list="false"
                               :on-success="handleAvatarSuccess"
                               :before-upload="beforeAvatarUpload">
                        <el-image v-if="editObj.typeImg" :src="editObj.typeImg" class="avatar">
                            <div slot="error" class="image-slot">
                                <i class="el-icon-picture-outline"></i>
                            </div>
                        </el-image>
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                <el-form-item label="排序号" label-width="100px">
                    <el-input v-model="editObj.sortNum" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="类别描述" label-width="100px">
                    <el-input v-model="editObj.typeDesc" type="textarea" autocomplete="off"></el-input>
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
            treeData:[],
            loading:true,
            saveLoading:false,
            editDialogVisible:false,
            editObj:{"typeId":0,"typeName":"","typeEnglish":"","typeNo":"","typeImg":"","sortNum":1,"typeDesc":"",typeParent:{"typeId":0,"typeName":"",typeNo:0}},
            uploadUrl:"/articletype/upload",
        },
        methods: {
            handleRefresh: function () {
                location.reload();
            },
            handleCreate: function () {
                this.editObj={"typeId":0,"typeName":"","typeEnglish":"","typeNo":"","typeImg":"","sortNum":1,"typeDesc":"",typeParent:{"typeId":0,"typeName":"一级类别",typeNo:0}};
                this.editDialogVisible=true;
            },
            handleAppend: function (data) {
                this.editObj={"typeId":0,"typeName":"","typeEnglish":"","typeNo":"","typeImg":"","sortNum":1,"typeDesc":"",typeParent:{"typeId":0,"typeName":"一级类别",typeNo:0}};
                this.editObj.typeParent={"typeId":data.typeId,"typeName":data.typeName,"typeNo":data.typeNo};
                this.editDialogVisible=true;
            },
            handleAvatarSuccess(res, file) {
                if(res.code=="success"){
                    this.editObj.typeImg=res.data.fileName;
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
            handleEdit: function (node,data) {
                var that =this;
                $.ajax({
                    url:"/articletype/load",
                    type:"post",
                    data:JSON.stringify({"id":data.typeId}),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            that.editObj=res.data.obj;
                            if(null==that.editObj.typeParent){
                                that.editObj.typeParent={"typeId":0,"typeName":"一级类别","typeNo":0};
                            }else {
                                that.editObj.typeNo=that.editObj.typeNo%100;
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
                var regNo=/^[1-9]\d$/;
                if(!regNo.test(that.editObj.typeNo)){
                    that.$message.error("编号只能为两位数字且不能以0开头！");
                    return false;
                }
                if(that.editObj.typeName==""){
                    that.$message.error("名称不能为空！");
                    return false;
                }
                if(that.editObj.typeEnglish==""){
                    that.$message.error("英文名称不能为空！");
                    return false;
                }
                that.saveLoading=true;
                $.ajax({
                    url:"/articletype/save",
                    type:"post",
                    data:JSON.stringify(that.editObj),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        that.saveLoading=false;
                        if(res.code=="success"){
                            that.editDialogVisible=false;
                            that.loadTreeData();
                        }else{
                            that.$message.error(res.message);
                        }
                    }
                });
            },
            handleDelete: function (node, data) {
                var that = this;
                const parent = node.parent;
                const children = parent.data.children || parent.data;
                this.$confirm("确认要删除类别【"+data.typeName+"】？", '提示', {confirmButtonText: '确定',cancelButtonText: '取消',type: 'warning'}).then(function () {
                    $.ajax({
                        url:"/articletype/delete",
                        type:"post",
                        data:JSON.stringify({"id":data.typeId}),
                        headers:{"Content-Type":"application/json;charset=utf-8"},
                        dataType:"json",
                        success:function (res) {
                            if(res.code=="success"){
                                that.$message({
                                    message: '删除成功！',
                                    type: 'success'
                                });
                                const index = children.findIndex(d => d.typeId === data.typeId);
                                children.splice(index, 1);
                            }else{
                                that.$message.error(res.message);
                            }
                        }
                    });
                });
            },
            loadTreeData: function () {
                var that = this;
                that.loading=true;
                $.ajax({
                    url:"/articletype/all",
                    type:"post",
                    dataType:"json",
                    success:function (res) {
                        that.loading=false;
                        if(res.code=="success"){
                            that.treeData=res.data.list;
                        }else{
                            that.treeData=[];
                        }
                    }
                });
            }
        },
        created: function () {
            this.loadTreeData();
        }
    });
</script>
</body>
</html>