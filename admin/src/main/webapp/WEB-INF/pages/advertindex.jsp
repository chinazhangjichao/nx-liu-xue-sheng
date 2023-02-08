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
        .avatar-uploader-icon{font-size:28px;color:#8c939d;width:400px;height:100px;line-height:100px;text-align:center}
        .avatar{width:400px;height:100px;display:block}
    </style>
</head>
<body class="gray-bg">
<div id="app" >
    <el-container>
        <el-header style="height: auto;">
            <div id="searchDiv" style="background-color: #fff; padding: 10px 10px 0 10px;">
                <el-form inline>
                    <el-form-item>
                        <el-select size="small" v-model="searchType" placeholder="请选择">
                            <el-option
                                    v-for="item in types"
                                    :key="item.typeId"
                                    :label="item.typeName"
                                    :value="item.typeId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-input size="small" v-model="keyword" placeholder="关键字"></el-input>
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
                    <el-button size="small" type="danger" icon="el-icon-delete-solid" v-on:click="handleMultiDelete">批量刪除</el-button>
                </el-button-group>
                <el-button-group style="float: right;">
                    <el-button size="small" type="primary" icon="el-icon-refresh" v-on:click="handleRefresh">刷新</el-button>
                </el-button-group>
            </div>
        </el-header>
        <el-main>
            <template>
                <el-table :data="tableData"  stripe  v-loading="loading"  style="width: 100%" @selection-change="handleSelectionChange">
                    <el-table-column prop="advertId" type="selection" width="50"></el-table-column>
                    <el-table-column label="广告图" >
                        <template slot-scope="scope">
                            <el-link :href="scope.row.advertUrl" target="_blank">
                                <el-image style="width: 200px; height: 50px" :src="scope.row.advertImg" fit="scale-down"></el-image>
                            </el-link>
                        </template>
                    </el-table-column>
                    <el-table-column prop="advertName" label="名称" width="200"></el-table-column>
                    <el-table-column prop="advertType.typeName" label="类别" width="100"></el-table-column>
                    <el-table-column prop="isOnline" label="在线" width="80">
                        <template slot-scope="scope">
                            <el-tag type="success" v-if="scope.row.isOnline===1">是</el-tag>
                            <el-tag type="danger" v-else-if="scope.row.isOnline===0">否</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="200" fixed="right">
                        <template slot-scope="scope">
                            <el-button-group>
                                <el-button type="success" icon="el-icon-edit" size="small"  v-on:click="handleEdit(scope.row)">编辑</el-button>
                                <el-button type="danger" icon="el-icon-delete" size="small" v-on:click="handleDelete(scope.$index,scope.row)">删除</el-button>
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
    <template>
        <el-dialog title="编辑文章" :visible.sync="editDialogVisible" :fullscreen="true" :close-on-click-modal="false">
            <el-form>
                <el-form-item label="封面图" label-width="100px">
                    <el-upload class="avatar-uploader"
                               :action="uploadUrl"
                               :show-file-list="false"
                               :on-success="handleAvatarSuccess"
                               :before-upload="beforeAvatarUpload">
                        <img v-if="editObj.advertImg" :src="editObj.advertImg" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>

                <el-form-item label="类别" label-width="100px">
                    <el-select v-model="editObj.advertType.typeId" placeholder="请选择">
                        <el-option
                                v-for="item in types"
                                :key="item.typeId"
                                :label="item.typeName"
                                :value="item.typeId">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="标题" label-width="100px">
                    <el-input v-model="editObj.advertName" autocomplete="off" placeholder="请输入标题"></el-input>
                </el-form-item>
                <el-form-item label="链接地址" label-width="100px">
                    <el-input v-model="editObj.advertUrl" autocomplete="off" placeholder="请输入点击该广告时的跳转地址，没有请填写#"></el-input>
                </el-form-item>
                <el-form-item label="排序号" label-width="100px">
                    <el-input v-model="editObj.sortNum" autocomplete="off" placeholder="请输入排序号"></el-input>
                </el-form-item>
                <el-form-item label="状态" label-width="100px">
                    <el-checkbox v-model="editObj.isOnline">是否展示</el-checkbox>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleSave" :loading="saveLoading">保 存</el-button>
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
            loading:true,
            currentPage: 1,
            pageSize: 20,
            total: 1,
            keyword: '',
            searchType:0,
            types:[],
            tableData: [],
            saveLoading:false,
            editDialogVisible:false,
            editObj:{advertId:0,advertImg:"",advertName:"",advertUrl:"",articleDetail:"",isOnline:true,sortNum:0,advertType:{typeId:0}},
            selectIds: [],
            uploadUrl:"/advert/upload",
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
                this.searchType=0;
                this.keyword = '';
                this.loadData();
            },
            handleSelectionChange: function (val) {
                this.selectIds=[];
                for (var i = 0; i < val.length; i++) {
                    this.selectIds[i] = val[i].advertId;
                }
            },
            handleCreate: function () {
                this.editDialogVisible=true;
                this.saveLoading=false;
                this.editObj={advertId:0,advertImg:"",advertName:"",advertUrl:"#",articleDetail:"",isOnline:true,sortNum:0,advertType:{typeId:0}};
            },
            handleEdit: function (row) {
                this.saveLoading=false;
                var that =this;
                $.ajax({
                    url:"/advert/load",
                    type:"post",
                    data:JSON.stringify({id: row.advertId}),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            that.editDialogVisible=true;
                            that.editObj=res.data.obj;
                            that.editObj.isOnline=that.editObj.isOnline==1?true:false;
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
            handleAvatarSuccess(res, file) {
                if(res.code=="success"){
                    this.editObj.advertImg=res.data.fileName;
                }else{
                    this.$message.error(res.message);
                }
            },
            beforeAvatarUpload(file) {
                const isLtSize = file.size / 1024 / 1024 < 2;
                if (!isLtSize) {
                    this.$message.error('上传头像图片大小不能超过 2MB!');
                }
                return isLtSize;
            },
            handleSave:function () {
                var that =this;
                if(null==that.editObj){
                    that.$message.error("无效操作！");
                    return false;
                }
                if(that.editObj.advertImg==""){
                    that.$message.error("图片不能为空！");
                    return false;
                }

                if(that.editObj.advertType.typeId<=0){
                    that.$message.error("请选择类别！");
                    return false;
                }
                if(that.editObj.advertName==""){
                    that.$message.error("标题不能为空！");
                    return false;
                }
                if(that.editObj.advertUrl==""){
                    that.$message.error("链接地址不能为空，如不需要跳转请填写#");
                    return false;
                }
                var  regNum=/^\d{1,10}$/;
                if(!regNum.test(that.editObj.sortNum)){
                    that.$message.error("排序号只能为数字！");
                    return false;
                }
                that.editObj.isOnline=that.editObj.isOnline?1:0;
                that.saveLoading=true;
                $.ajax({
                    url:"/advert/save",
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
                this.$confirm("确认要删除该信息？", '提示', {confirmButtonText: '确定',cancelButtonText: '取消',type: 'warning'}).then(function () {
                    $.ajax({
                        url:"/advert/delete",
                        type:"post",
                        data:JSON.stringify({"id":row.advertId}),
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
                        url:"/advert/multidelete",
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
                                            if(that.tableData[i].advertId==that.selectIds[j]){
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
                that.loading=true;
                $.ajax({
                    url:"/advert/page",
                    type:"post",
                    data:JSON.stringify({
                        currentPage: this.currentPage,
                        pageSize: this.pageSize,
                        searchType: this.searchType,
                        keyword: this.keyword
                    }),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        that.loading=false;
                        if(res.code=="success"){
                            that.tableData=res.data.list;
                            if(res.data.currentPage==1){
                                that.total=res.data.total;
                            }
                        }else{
                            that.tableData=[];
                        }
                    }
                });
            },
            loadTypes: function () {
                var that = this;
                $.ajax({
                    url:"/adverttype/all",
                    type:"post",
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            that.types=res.data.list;
                            that.types.unshift({typeId:0,typeName:"请选择"})
                        }else{
                            that.types=[];
                        }
                    }
                });
            }
        },
        created: function () {
            this.loadData();
            this.loadTypes();
        }
    });
</script>
</body>
</html>