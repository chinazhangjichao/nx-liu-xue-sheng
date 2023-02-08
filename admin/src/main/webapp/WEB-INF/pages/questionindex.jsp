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
        .avatar-uploader-icon{font-size:28px;color:#8c939d;width:720px;height:100px;line-height:100px;text-align:center}
        .avatar{width:720px;height:100px;display:block}
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
                        <el-select size="small" v-model="course" placeholder="课程" clearable>
                            <el-option v-for="item in courses" :key="item.courseId" :label="item.courseName" :value="item.courseId"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item style="width:120px;">
                        <el-select size="small" v-model="type" placeholder="题型" clearable>
                            <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item style="width:120px;">
                        <el-select size="small" v-model="test" placeholder="自测题" clearable>
                            <el-option v-for="item in tests" :key="item.id" :label="item.name" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item style="width:120px;">
                        <el-select size="small" v-model="exam" placeholder="考试题" clearable>
                            <el-option v-for="item in exames" :key="item.id" :label="item.name" :value="item.value"></el-option>
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
                    <el-table-column prop="course.courseName" label="课程" width="200"></el-table-column>
                    <el-table-column prop="name" label="题目" ></el-table-column>
                    <el-table-column prop="type" label="题型" ></el-table-column>
                    <el-table-column prop="score" label="分值" ></el-table-column>
                    <el-table-column prop="isExam" label="考试题" width="100">
                        <template slot-scope="scope">
                            <el-switch @change="handleStatus(scope.row)"
                                       v-model="scope.row.isExam"
                                       active-color="#13ce66"
                                       inactive-color="#ff4949"
                                       :active-value="1"
                                       :inactive-value="0">
                            </el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column prop="isTest" label="自测题" width="100">
                        <template slot-scope="scope">
                            <el-switch @change="handleStatus(scope.row)"
                                       v-model="scope.row.isTest"
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
        <el-dialog title="编辑考题信息" :visible.sync="editDialogVisible" :fullscreen="true" :close-on-click-modal="false">
            <el-form>
                <el-form-item label="课程" label-width="100px">
                    <el-select size="small" v-model="editObj.course.courseId" placeholder="课程" clearable>
                        <el-option v-for="item in courses" :key="item.courseId" :label="item.courseName" :value="item.courseId"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="题目" label-width="100px">
                    <el-input v-model="editObj.name" autocomplete="off" placeholder="请输入题目"></el-input>
                </el-form-item>
                <el-form-item label="题型" label-width="100px">
                    <el-select size="small" v-model="type" placeholder="题型" clearable>
                        <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="选项1" label-width="100px">
                    <el-input v-model="editObj.option1" autocomplete="off" placeholder="请输入选项1"></el-input>
                </el-form-item>
                <el-form-item label="选项2" label-width="100px">
                    <el-input v-model="editObj.option2" autocomplete="off" placeholder="请输入选项2"></el-input>
                </el-form-item>
                <el-form-item label="选项3" label-width="100px">
                    <el-input v-model="editObj.option3" autocomplete="off" placeholder="请输入选项3"></el-input>
                </el-form-item>
                <el-form-item label="选项4" label-width="100px">
                    <el-input v-model="editObj.option4" autocomplete="off" placeholder="请输入选项4"></el-input>
                </el-form-item>
                <el-form-item label="选项5" label-width="100px">
                    <el-input v-model="editObj.option5" autocomplete="off" placeholder="请输入选项5"></el-input>
                </el-form-item>
                <el-form-item label="选项6" label-width="100px">
                    <el-input v-model="editObj.option6" autocomplete="off" placeholder="请输入选项6"></el-input>
                </el-form-item>
                <el-form-item label="答案" label-width="100px">
                    <el-input v-model="editObj.answer" autocomplete="off" placeholder="请输入参考答案"></el-input>
                </el-form-item>
                <el-form-item label="解析" label-width="100px">
                    <el-input v-model="editObj.analysis" type="textarea" :rows="5" autocomplete="off" placeholder="请输入问题解析"></el-input>
                </el-form-item>
                <el-form-item label="图片素材" label-width="100px">
                    <el-upload class="avatar-uploader"
                               :action="uploadUrl"
                               :show-file-list="false"
                               :on-success="handleAvatarSuccess"
                               :before-upload="beforeAvatarUpload">
                        <img v-if="editObj.img" :src="editObj.img" class="avatar">
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
    <template>
        <el-dialog title="题目详情" :visible.sync="detailDialogVisible" :fullscreen="true" :close-on-click-modal="false">
            <el-descriptions :column="2" border>
                <el-descriptions-item label="课程" :span="2">{{editObj.course.courseName}}</el-descriptions-item>
                <el-descriptions-item label="题目" :span="2">{{editObj.name}}</el-descriptions-item>
                <el-descriptions-item label="选项1" :span="2">{{editObj.option1}}</el-descriptions-item>
                <el-descriptions-item label="选项2" :span="2">{{editObj.option2}}</el-descriptions-item>
                <el-descriptions-item label="选项3" :span="2">{{editObj.option3}}</el-descriptions-item>
                <el-descriptions-item label="选项4" :span="2">{{editObj.option4}}</el-descriptions-item>
                <el-descriptions-item label="选项5" :span="2">{{editObj.option5}}</el-descriptions-item>
                <el-descriptions-item label="选项6" :span="2">{{editObj.option6}}</el-descriptions-item>
                <el-descriptions-item label="答案" :span="2">{{editObj.major}}</el-descriptions-item>
                <el-descriptions-item label="解析" :span="2">{{editObj.email}}</el-descriptions-item>

                <el-descriptions-item label="图片素材" :span="2"><el-image style="width: 120px; height: 160px" :src="editObj.img" fit="scale-down"></el-image></el-descriptions-item>
                <el-descriptions-item label="题型">{{editObj.type==1?'单选':editObj.type==2?'多选':editObj.type==3?'判断':editObj.type==4?'问答':'其他'}}</el-descriptions-item>
                <el-descriptions-item label="分值">{{editObj.score}}</el-descriptions-item>
                <el-descriptions-item label="自测题">
                    <el-tag type="success" v-if="editObj.isTest==1">是</el-tag>
                    <el-tag type="danger" v-if="editObj.isTest==0">否</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="考试题">
                    <el-tag type="success" v-if="editObj.isExam==1">是</el-tag>
                    <el-tag type="danger" v-if="editObj.isExam==0">否</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="编辑人">{{editObj.modifyUser.realName}}</el-descriptions-item>
                <el-descriptions-item label="编辑时间">{{formatTime(editObj.modifyTime)}}</el-descriptions-item>
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
            type:null,
            types:[{name:'单选',value:1},{name:'多选',value:2},{name:'判断',value:3},{name:'问答',value:4},{name:'其他',value:5}],
            exam:null,
            exames:[{name:'是',value:1},{name:'否',value:0}],
            test:null,
            tests:[{name:'是',value:1},{name:'否',value:0}],
            course:null,
            courses:[],

            tableData: [],
            saveLoading:false,
            detailDialogVisible:false,
            editDialogVisible:false,
            editObj:{id:0,img:"",name:"",option1:"",option2:"",option3:"",option4:"",option5:"",option6:"",answer:"",analysis:"",type:1,score:0,isTest:1,isExam:1,course:{courseId:0},modifyUser:{}},
            uploadUrl:"/question/upload",
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
                this.editObj={id:0,img:"",name:"",option1:"",option2:"",option3:"",option4:"",option5:"",option6:"",answer:"",analysis:"",type:1,score:0,isTest:1,isExam:1,course:{courseId:0},modifyUser:{}};
            },
            handleEdit: function (row) {
                var that =this;
                $.ajax({
                    url:"/question/load",
                    type:"post",
                    data:JSON.stringify({id: row.id}),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            that.editDialogVisible=true;
                            that.editObj=res.data;
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

                var regNum=/^\d{1,10}$/;
                if(!regNum.test(that.editObj.sortNum)){
                    that.$message.error("正确填写排序号！");
                    return false;
                }
                if(null==that.editObj.course || that.editObj.name.courseId<=0){
                    that.$message.error("所属课程不能为空！");
                    return false;
                }
                if(that.editObj.name==""){
                    that.$message.error("题目不能为空！");
                    return false;
                }
                if(null==that.editObj.type){
                    that.$message.error("题目类型不能为空！");
                    return false;
                }
                if(that.editObj.type==1 || that.editObj.type==3){
                    if(that.editObj.option1=='' || that.editObj.option2==''){
                        that.$message.error("单选或判断题至少填写两个选项！");
                        return false;
                    }
                }
                if(that.editObj.type==2){
                    if(that.editObj.option1=='' || that.editObj.option2==''|| that.editObj.option3==''){
                        that.$message.error("多选题至少填写三个选项！");
                        return false;
                    }
                }
                if(that.editObj.answer==""){
                    that.$message.error("参考答案不能为空！");
                    return false;
                }
                if(that.editObj.score==""){
                    that.$message.error("分值不能为空！");
                    return false;
                }
                that.saveLoading=true;
                $.ajax({
                    url:"/question/save",
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
                        url:"/question/delete",
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
                $.ajax({
                    url:"/question/status",
                    type:"post",
                    data:JSON.stringify({id: row.id,exam: row.isExam,test: row.isTest}),
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
            },
            loadData: function () {
                var that = this;
                that.loading=true;
                $.ajax({
                    url:"/question/page",
                    type:"post",
                    data:JSON.stringify({
                        currentPage: this.currentPage,
                        pageSize: this.pageSize,
                        course: this.course,
                        type: this.type,
                        exam: this.exam,
                        test: this.test,
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
            loadCourses: function () {
                var that = this;
                that.loading=true;
                $.ajax({
                    url:"/course/page",
                    type:"post",
                    data:JSON.stringify({
                        currentPage: this.currentPage,
                        pageSize: this.pageSize,
                        status:1
                    }),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        that.loading=false;
                        if(res.code=="success"){
                            that.courses=res.data.list;
                        }else{
                            that.courses=[];
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