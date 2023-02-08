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
                        <el-cascader size="small"  v-model="searchTypes"  :options="types" :props="typeProps" clearable></el-cascader>
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
                    <el-table-column type="selection" width="50"></el-table-column>
                    <el-table-column prop="articleId" label="ID" ></el-table-column>
                    <el-table-column prop="articleTitle" label="文章名称" ></el-table-column>
                    <el-table-column prop="articleType.typeName" label="文章类别" width="200">
                        <template slot-scope="scope">
                            <span v-if="scope.row.articleType.typeParent">{{ scope.row.articleType.typeParent.typeName }} / </span><span>{{ scope.row.articleType.typeName }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="sendDate" label="发布日期" width="100">
                        <template slot-scope="scope">
                            <span>{{ formatDate(scope.row.sendDate) }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="isOnline" label="在线" width="80">
                        <template slot-scope="scope">
                            <el-tag type="success" v-if="scope.row.isOnline===1">是</el-tag>
                            <el-tag type="danger" v-else-if="scope.row.isOnline===0">否</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="isCommand" label="推荐" width="80">
                        <template slot-scope="scope">
                            <el-tag type="success" v-if="scope.row.isCommand===1">是</el-tag>
                            <el-tag type="danger" v-else-if="scope.row.isCommand===0">否</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="isTop" label="置顶" width="80">
                        <template slot-scope="scope">
                            <el-tag type="success" v-if="scope.row.isTop===1">是</el-tag>
                            <el-tag type="danger" v-else-if="scope.row.isTop===0">否</el-tag>
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
                        <img v-if="editObj.articleImg" :src="editObj.articleImg" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                <el-form-item label="标题" label-width="100px">
                    <el-input v-model="editObj.articleTitle" autocomplete="off" placeholder="请输入标题"></el-input>
                </el-form-item>
                <el-form-item label="类别" label-width="100px">
                    <el-cascader v-model="selectEditType"  :options="types" :props="typePropsId" @change="handleEditTypeChange"></el-cascader>
                </el-form-item>
                <el-form-item label="简介" label-width="100px">
                    <el-input v-model="editObj.articleDesc" autocomplete="off" placeholder="请输入一句话简介"></el-input>
                </el-form-item>
                <el-form-item label="标签" label-width="100px">
                    <el-input v-model="editObj.articleTag" autocomplete="off" placeholder="请输入搜索关键词"></el-input>
                </el-form-item>
                <el-form-item label="详情" label-width="100px">
                    <textarea id="editor" type="text/plain" style="height: 500px;">{{editObj.articleDetail}}</textarea>
                </el-form-item>
                <el-form-item label="阅读数" label-width="100px">
                    <el-input v-model="editObj.readNum" autocomplete="off" placeholder="请输入阅读数"></el-input>
                </el-form-item>
                <el-form-item label="排序号" label-width="100px">
                    <el-input v-model="editObj.sortNum" autocomplete="off" placeholder="请输入排序号"></el-input>
                </el-form-item>
                <el-form-item label="状态" label-width="100px">
                    <el-checkbox v-model="editObj.isOnline">是否展示</el-checkbox>
                    <el-checkbox v-model="editObj.isCommand">是否推荐首页</el-checkbox>
                    <el-checkbox v-model="editObj.isTop">是否置顶</el-checkbox>
                </el-form-item>
                <el-form-item label="发布日期" label-width="100px">
                    <el-date-picker v-model="editObj.sendDate" type="date" placeholder="选择发布日期"></el-date-picker>
                </el-form-item>
                <el-form-item label="附件" label-width="100px">
                    <el-upload
                            :action="uploadFiles"
                            :on-remove="handleFileRemove"
                            :before-remove="beforeFileRemove"
                            :on-success="handleFileSuccess"
                            :before-upload="beforeFileUpload"
                            multiple
                            :file-list="fileList">
                        <el-button size="small" type="primary">点击上传附件</el-button>
                        <div slot="tip" class="el-upload__tip">允许上传jpg,png,doc,docx,pdf,xls,xlsx,ppt,pptx格式的文件，且不超过50MB</div>
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
            searchTypes:[0],
            types:[],
            typeProps:{ checkStrictly: true, label:"typeName",value:"typeNo"},
            typePropsId:{checkStrictly: false,  label:"typeName",value:"typeId"},
            tableData: [],
            saveLoading:false,
            editDialogVisible:false,
            ueditor:null,
            selectEditType:[0],
            editObj:{articleId:0,articleImg:"",articleTitle:"",articleTag:"",articleDesc:"",articleDetail:"",sendDate:new Date(),isTop:false,isOnline:true,isCommand:true,readNum:0,sortNum:0,articleType:{typeId:0}},
            selectIds: [],
            uploadUrl:"/article/uploadimg",
            uploadFiles:"/article/uploadfiles",
            fileList:[],
            articleFiles:[]
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
                this.searchTypes= [];
                this.keyword = '';
                this.loadData();
            },
            handleSelectionChange: function (val) {
                this.selectIds=[];
                for (var i = 0; i < val.length; i++) {
                    this.selectIds[i] = val[i].articleId;
                }
            },
            handleCreate: function () {
                this.editDialogVisible=true;
                if(null==this.ueditor){
                    this.ueditor=UE.getEditor('editor');
                }
                this.editObj={articleId:0,articleImg:"",articleTitle:"",articleTag:"",articleDesc:"",articleDetail:"",sendDate:new Date(),isTop:false,isOnline:true,isCommand:true,readNum:0,sortNum:0,articleType:{typeId:0}};
                this.fileList=[];
                this.articleFiles=[];
                this.selectEditType=0;
                this.$nextTick(function () {
                    this.ueditor.setContent(this.editObj.articleDetail);
                });
            },
            handleEdit: function (row) {
                if(null==this.ueditor){
                    this.ueditor=UE.getEditor('editor');
                }
                this.fileList=[];
                this.articleFiles=[];
                var that =this;
                $.ajax({
                    url:"/article/load",
                    type:"post",
                    data:JSON.stringify({id: row.articleId}),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            that.editDialogVisible=true;
                            if(null==that.ueditor){
                                that.ueditor=UE.getEditor('editor');
                            }
                            that.editObj=res.data;
                            that.editObj.isCommand=that.editObj.isCommand==1?true:false;
                            that.editObj.isTop=that.editObj.isTop==1?true:false;
                            that.editObj.isOnline=that.editObj.isOnline==1?true:false;
                            that.editObj.sendDate = new Date(that.editObj.sendDate);
                            that.articleFiles =that.editObj.articleFiles;
                            that.fileList=that.editObj.articleFiles;
                            if(that.editObj.articleType){
                                that.selectEditType=[that.editObj.articleType.typeParent.typeId,that.editObj.articleType.typeId];
                            }else{
                                that.selectEditType=[0];
                            }
                            that.$nextTick(function () {
                                that.ueditor.setContent(that.editObj.articleDetail);
                            });
                        }else{
                            that.$message.error(res.message);
                        }
                    }
                });
            },
            handleEditTypeChange:function (v) {
                this.selectEditType=v;
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
                    this.editObj.articleImg=res.data.fileName;
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
            handleFileRemove(file, fileList) {
                var obj=null;
                if(file.id){
                    obj=file;
                }else if(file.response.data.code=='success'){
                    obj=file.response.data;
                }
                if(obj){
                    var that =this;
                    $.ajax({
                        url:"/article/file/delete",
                        type:"post",
                        data:JSON.stringify(obj),
                        headers:{"Content-Type":"application/json;charset=utf-8"},
                        dataType:"json",
                        success:function (res) {
                            if(res.code=="success"){
                                that.fileList=fileList;
                                var idx = -1;
                                for(var i in that.articleFiles){
                                    if(that.articleFiles[i].url==obj.url){
                                        idx=i;
                                        break;
                                    }
                                }
                                that.articleFiles.splice(idx,1);
                            }else{
                                that.$message.error(res.message);
                            }
                        }
                    });
                }else{
                    this.fileList=fileList;
                }
            },
            beforeFileRemove(file, fileList) {
                return this.$confirm(`确定移除 ${ file.name }？`);
            },
            handleFileSuccess:function(res, file,fileList) {
                if(res.code=="success"){
                    this.articleFiles.push(res.data);
                }else{
                    this.$message.error(res.message);
                }
            },
            beforeFileUpload:function(file) {
                const isLtSize = file.size / 1024 / 1024 < 50;
                if (!isLtSize) {
                    this.$message.error('附件大小不能超过50MB!');
                }
                return isLtSize;
            },
            handleSave:function () {
                var that =this;
                if(null==that.editObj){
                    that.$message.error("无效操作！");
                    return false;
                }
                if(that.editObj.articleTitle==""){
                    that.$message.error("文章标题不能为空！");
                    return false;
                }
                if(that.selectEditType.length<=0){
                    that.$message.error("请选择文章类别！");
                    return false;
                }
                that.editObj.articleDetail=that.ueditor.getContent();
                if(that.editObj.articleDetail==""){
                    that.$message.error("文章详情不能为空！");
                    return false;
                }
                var  regNum=/^\d{1,10}$/;
                if(!regNum.test(that.editObj.sortNum)){
                    that.$message.error("排序号只能为数字！");
                    return false;
                }
                if(!regNum.test(that.editObj.readNum)){
                    that.$message.error("阅读次数只能为数字！");
                    return false;
                }
                if(null==that.editObj.sendDate){
                    that.$message.error("发布日期不能为空！");
                    return false;
                }
                that.editObj.articleType.typeId=that.selectEditType[that.selectEditType.length-1];
                that.editObj.isCommand=that.editObj.isCommand?1:0;
                that.editObj.isTop=that.editObj.isTop?1:0;
                that.editObj.isOnline=that.editObj.isOnline?1:0;
                that.editObj.articleFiles=that.articleFiles;
                if(typeof(that.editObj.sendDate )!='number'){
                    that.editObj.sendDate = this.editObj.sendDate.getTime();
                }
                that.saveLoading=true;
                $.ajax({
                    url:"/article/save",
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
                        url:"/article/delete",
                        type:"post",
                        data:JSON.stringify({"id":row.articleId}),
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
                        url:"/article/multidelete",
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
                                            if(that.tableData[i].articleId==that.selectIds[j]){
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
                    url:"/article/page",
                    type:"post",
                    data:JSON.stringify({
                        currentPage: this.currentPage,
                        pageSize: this.pageSize,
                        searchType: this.searchTypes.length>0?this.searchTypes[this.searchTypes.length-1]:0,
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
                    url:"/articletype/all",
                    type:"post",
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            that.types=res.data.list;
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