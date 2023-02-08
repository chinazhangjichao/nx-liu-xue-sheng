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
        .avatar-uploader-icon{font-size:28px;color:#8c939d;width:120px;height:160px;line-height:160px;text-align:center}
        .avatar{width:120px;height:160px;display:block}
        .el-form-item__content{line-height: 22px;}
    </style>
</head>
<body class="gray-bg">
<div id="app" >
    <el-container>
        <el-header style="height: auto;">
            <div id="searchDiv" style="background-color: #fff; padding: 10px 10px 0 10px;">
                <el-form inline>
                    <el-form-item>
                        <el-select size="small" v-model="status" placeholder="状态">
                            <el-option v-for="item in statuses" :key="item.id" :label="item.name" :value="item.value"></el-option>
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
                </el-button-group>
                <el-button-group style="float: right;">
                    <el-button size="small" type="primary" icon="el-icon-refresh" v-on:click="handleRefresh">刷新</el-button>
                </el-button-group>
            </div>
        </el-header>
        <el-main>
            <template>
                <el-table :data="tableData"  stripe  v-loading="loading"  style="width: 100%" >
                    <el-table-column prop="headImg" label="展示图" width="80">
                        <template slot-scope="scope">
                            <el-image style="width: 60px; height: 80px" :src="scope.row.headImg" fit="scale-down"></el-image>
                        </template>
                    </el-table-column>
                    <el-table-column prop="realName" label="姓名" width="150" ></el-table-column>
                    <el-table-column prop="duty" label="职称" ></el-table-column>
                    <el-table-column prop="major" label="专业" ></el-table-column>
                    <el-table-column prop="sortNum" label="排序号" width="80" ></el-table-column>
                    <el-table-column prop="online" label="状态" width="100">
                        <template slot-scope="scope">
                            <el-switch @change="handleStatus(scope.row)"
                                       v-model="scope.row.status"
                                       active-color="#13ce66"
                                       inactive-color="#ff4949"
                                       :active-value="1"
                                       :inactive-value="0">
                            </el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column prop="sendDate" label="最后编辑" width="100">
                        <template slot-scope="scope">
                            <span>{{ formatTime(scope.row.modifyTime) }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="150" fixed="right">
                        <template slot-scope="scope">
                            <el-button-group>
                                <el-button type="info" icon="el-icon-view" size="small"  v-on:click="handleDetail(scope.row)" title="查看详情"></el-button>
                                <el-button type="success" icon="el-icon-edit" size="small"  v-on:click="handleEdit(scope.row)"  title="编辑"></el-button>
                                <el-button type="danger" icon="el-icon-delete" size="small" v-on:click="handleDelete(scope.$index,scope.row)"  title="删除"></el-button>
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
        <el-dialog title="编辑教师资料" :visible.sync="editDialogVisible" :fullscreen="true" :close-on-click-modal="false">
            <el-form>
                <el-form-item label="展示头像" label-width="100px">
                    <el-upload class="avatar-uploader"
                               :action="uploadUrl" 
                               :show-file-list="false"
                               :on-success="handleAvatarSuccess"
                               :before-upload="beforeAvatarUpload">
                        <img v-if="editObj.headImg" :src="editObj.headImg" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                <el-form-item label="排序号" label-width="100px">
                    <el-input v-model="editObj.sortNum" autocomplete="off" placeholder="请输入排序号(升序显示)"></el-input>
                </el-form-item>
                <el-form-item label="姓名" label-width="100px">
                    <el-input v-model="editObj.realName" autocomplete="off" placeholder="请输入真实姓名"></el-input>
                </el-form-item>
                <el-form-item label="职称" label-width="100px">
                    <el-input v-model="editObj.duty" autocomplete="off" placeholder="请输入职称"></el-input>
                </el-form-item>
                <el-form-item label="专业" label-width="100px">
                    <el-input v-model="editObj.major" autocomplete="off" placeholder="请输入专业"></el-input>
                </el-form-item>
                <el-form-item label="电子邮箱" label-width="100px">
                    <el-input v-model="editObj.email" autocomplete="off" placeholder="请输入电子邮箱"></el-input>
                </el-form-item>
                <el-form-item label="简介" label-width="100px">
                    <el-input v-model="editObj.description" type="textarea" :rows="3" autocomplete="off" placeholder="请输入简介"></el-input>
                </el-form-item>
                <el-form-item label="详情" label-width="100px">
                    <textarea id="editor" type="text/plain" style="height: 500px;">{{editObj.detail}}</textarea>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleSave" :loading="saveLoading">保 存</el-button>
            </div>
        </el-dialog>
    </template>
    <template>
        <el-dialog title="师资详情" :visible.sync="detailDialogVisible" :fullscreen="true" :close-on-click-modal="false">
            <el-descriptions :column="2" border>
                <el-descriptions-item label="展示图" :span="2"><el-image style="width: 120px; height: 160px" :src="editObj.headImg" fit="scale-down"></el-image></el-descriptions-item>
                <el-descriptions-item label="排序号">{{editObj.sortNum}}</el-descriptions-item>
                <el-descriptions-item label="姓名">{{editObj.realName}}</el-descriptions-item>
                <el-descriptions-item label="职称">{{editObj.duty}}</el-descriptions-item>
                <el-descriptions-item label="专业">{{editObj.major}}</el-descriptions-item>
                <el-descriptions-item label="电子邮箱">{{editObj.email}}</el-descriptions-item>
                <el-descriptions-item label="状态">
                    <el-tag type="success" v-if="editObj.status==1">展示</el-tag>
                    <el-tag type="danger" v-if="editObj.status==0">不展示</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="简介" :span="2" >{{editObj.description}}</el-descriptions-item>
                <el-descriptions-item label="详情" :span="2"><div  v-html="editObj.detail"></div></el-descriptions-item>
                <el-descriptions-item label="操作人">{{editObj.modifyUser.realName}}</el-descriptions-item>
                <el-descriptions-item label="操作时间">{{formatTime(editObj.modifyTime)}}</el-descriptions-item>
            </el-descriptions>
            <div slot="footer" class="dialog-footer">
                <el-button @click="detailDialogVisible = false">取 消</el-button>
            </div>
        </el-dialog>
    </template>
</div>

<script type="text/javascript" src="/js/vue.min.js"></script>
<script type="text/javascript" src="/js/element-ui.js" ></script>
<script type="text/javascript" src="/js/jquery-1.12.4.min.js" ></script>
<script type="text/javascript" src="/js/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="/js/ueditor/ueditor.all.js"></script>
<script type="text/javascript" src="/js/common.js" ></script>
<script type="text/javascript">
    new Vue({
        el: '#app',
        data: {
            loading:true,
            currentPage: 1,
            pageSize: 20,
            total: 1,
            keyword: '',
            status:null,
            statuses:[{name:'展示',value:1},{name:'不展示',value:0}],
            tableData: [],
            saveLoading:false,
            detailDialogVisible:false,
            editDialogVisible:false,
            ueditor:null,
            editObj:{id:0,headImg:"",name:"",duty:"",major:"",email:"",sortNum:1,status:1,description:"",detail:"",modifyTime:0,modifyUser:{userId:0}},
            uploadUrl:"/teacher/upload",
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
                this.status= null;
                this.keyword = '';
                this.loadData();
            },
            handleCreate: function () {
                this.editDialogVisible=true;
                if(null==this.ueditor){
                    this.ueditor=UE.getEditor('editor');
                }
                this.editObj={id:0,headImg:"",name:"",duty:"",major:"",email:"",sortNum:1,status:1,description:"",detail:"",modifyTime:0,modifyUser:{userId:0}};
                this.$nextTick(function () {
                    this.ueditor.setContent(this.editObj.detail);
                });
            },
            handleEdit: function (row) {
                var that =this;
                $.ajax({
                    url:"/teacher/load",
                    type:"post",
                    data:JSON.stringify({id: row.id}),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            that.editDialogVisible=true;
                            if(null==that.ueditor){
                                that.ueditor=UE.getEditor('editor');
                            }
                            that.editObj=res.data;
                            that.$nextTick(function () {
                                that.ueditor.setContent(that.editObj.detail);
                            });
                        }else{
                            that.$message.error(res.message);
                        }
                    }
                });
            },
            handleDetail:function(row){
                var that =this;
                $.ajax({
                    url:"/teacher/load",
                    type:"post",
                    data:JSON.stringify({id: row.id}),
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
                const isLtSize = file.size / 1024 / 1024 < 10;
                if (!isLtSize) {
                    this.$message.error('上传图片大小不能超过 10MB!');
                }
                return isLtSize;
            },

            handleSave:function () {
                var that =this;
                if(null==that.editObj){
                    that.$message.error("无效操作！");
                    return false;
                }
                if(that.editObj.headImg==""){
                    that.$message.error("头像不能为空！");
                    return false;
                }
                var regNum=/^\d{1,10}$/;
                if(!regNum.test(that.editObj.sortNum)){
                    that.$message.error("正确填写排序号！");
                    return false;
                }
                if(that.editObj.realName==""){
                    that.$message.error("姓名不能为空！");
                    return false;
                }
                if(that.editObj.duty==""){
                    that.$message.error("职称不能为空！");
                    return false;
                }
                if(that.editObj.major==""){
                    that.$message.error("专业不能为空！");
                    return false;
                }
                that.editObj.detail=that.ueditor.getContent();
                that.saveLoading=true;
                $.ajax({
                    url:"/teacher/save",
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
                        url:"/teacher/delete",
                        type:"post",
                        data:JSON.stringify({"id":row.id}),
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
                this.$confirm('确定要' + (row.status == 1 ? '启用' : '停用') + '该数据吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    $.ajax({
                        url:"/teacher/status",
                        type:"post",
                        data:JSON.stringify({id: row.id,status: row.status}),
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
                that.loading=true;
                $.ajax({
                    url:"/teacher/page",
                    type:"post",
                    data:JSON.stringify({
                        currentPage: this.currentPage,
                        pageSize: this.pageSize,
                        status: this.status,
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
            }
        },
        created: function () {
            this.loadData();
        }
    });
</script>
</body>
</html>