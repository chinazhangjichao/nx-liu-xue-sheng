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
                    <el-form-item  style="width:100px;">
                        <el-select size="small" v-model="timeType" placeholder="时间类型" clearable>
                            <el-option v-for="item in timeTypes" :key="item.id" :label="item.name" :value="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-date-picker size="small" v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']"></el-date-picker>
                    </el-form-item>
                    <el-form-item style="width:100px;">
                        <el-select size="small" v-model="type" placeholder="学生类型" clearable>
                            <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item style="width:100px;">
                        <el-select size="small" v-model="verifyStatus" placeholder="审核状态" clearable>
                            <el-option v-for="item in verifyStatuses" :key="item.id" :label="item.name" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item style="width:100px;">
                        <el-select size="small" v-model="status" placeholder="状态" clearable>
                            <el-option v-for="item in statuses" :key="item.id" :label="item.name" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-input size="small" v-model="stuNo" placeholder="学号"></el-input>
                    </el-form-item>
                    <el-form-item >
                        <el-input size="small" v-model="firstName" placeholder="姓"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input size="small" v-model="lastName" placeholder="名"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input size="small" v-model="name" placeholder="中文名"></el-input>
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
                    <el-table-column prop="stuNo" label="学号" ></el-table-column>
                    <el-table-column prop="firstName" label="姓" ></el-table-column>
                    <el-table-column prop="lastName" label="名" ></el-table-column>
                    <el-table-column prop="chineseName" label="中文名" ></el-table-column>
                    <el-table-column prop="country" label="国籍" ></el-table-column>
                    <el-table-column prop="stuType" label="学生类型" width="100">
                        <template slot-scope="scope">
                            <el-tag type="primary" v-if="scope.row.stuType==1">学历生</el-tag>
                            <el-tag type="warning" v-else>非学历生</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="stuStatus" label="状态" width="100">
                        <template slot-scope="scope">
                            <el-switch @change="handleStatus(scope.row)"
                                       v-model="scope.row.stuStatus"
                                       active-color="#13ce66"
                                       inactive-color="#ff4949"
                                       :active-value="1"
                                       :inactive-value="0">
                            </el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column prop="createTime" label="注册日期" width="100">
                        <template slot-scope="scope">
                            <span>{{ formatTime(scope.row.createTime) }}</span>
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
        <el-dialog title="编辑学员档案" :visible.sync="editDialogVisible" :fullscreen="true" :close-on-click-modal="false">
            <el-form>
                <el-form-item label="学生类型" label-width="100px">
                    <el-select v-model="editObj.stuType" placeholder="学生类型" >
                        <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="姓" label-width="100px">
                    <el-input v-model="editObj.firstName" ref="firstName" autocomplete="off" placeholder="请填写姓氏"></el-input>
                </el-form-item>
                <el-form-item label="名" label-width="100px">
                    <el-input v-model="editObj.lastName" ref="lastName" autocomplete="off" placeholder="请填写名字"></el-input>
                </el-form-item>
                <el-form-item label="中文名" label-width="100px">
                    <el-input v-model="editObj.chineseName" ref="chineseName" autocomplete="off" placeholder="请填写中文名字"></el-input>
                </el-form-item>
                <el-form-item label="国籍" label-width="100px">
                    <el-input v-model="editObj.country" ref="country" autocomplete="off" placeholder="请输入国籍"></el-input>
                </el-form-item>
                <el-form-item label="电子邮箱" label-width="100px">
                    <el-input v-model="editObj.email" ref="email" autocomplete="off" placeholder="请输入电子邮箱"></el-input>
                </el-form-item>
                <el-form-item label="证件类型" label-width="100px">
                    <el-input v-model="editObj.certificateType" ref="certificateType" autocomplete="off" placeholder="请输入证件类型"></el-input>
                </el-form-item>
                <el-form-item label="证件号码" label-width="100px">
                    <el-input v-model="editObj.certificateNo" ref="certificateNo" autocomplete="off" placeholder="请输入证件号码"></el-input>
                </el-form-item>
                <el-form-item label="性别" label-width="100px">
                    <el-select v-model="editObj.gender" placeholder="性别" >
                        <el-option v-for="item in genders" :key="item.id" :label="item.name" :value="item.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="出生日期" label-width="100px">
                    <el-date-picker v-model="editObj.birth" ref="birth" type="date" placeholder="出生日期"></el-date-picker>
                </el-form-item>
                <el-form-item label="电话" label-width="100px">
                    <el-input v-model="editObj.phone" ref="phone" autocomplete="off" placeholder="请输入联系电话"></el-input>
                </el-form-item>
                <el-form-item label="地址" label-width="100px">
                    <el-input v-model="editObj.address" ref="address" autocomplete="off" placeholder="请输入联系地址"></el-input>
                </el-form-item>
                <el-form-item label="登录密码" label-width="100px" v-if="editObj.stuId==0">
                    <el-input v-model="editObj.stuPwd" ref="stuPwd" autocomplete="off" placeholder="请设置登录密码"></el-input>
                </el-form-item>
                <el-form-item label="个性头像" label-width="100px">
                    <el-upload class="avatar-uploader"
                               :action="uploadUrl"
                               :show-file-list="false"
                               :on-success="handleAvatarSuccess"
                               :before-upload="beforeAvatarUpload">
                        <img v-if="editObj.stuImg" :src="editObj.stuImg" class="avatar">
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
        <el-dialog title="档案详情" :visible.sync="detailDialogVisible" :fullscreen="true" :close-on-click-modal="false">
            <el-descriptions :column="2" border>
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
                <el-descriptions-item label="编辑人">{{editObj.verifyUser.realName}}</el-descriptions-item>
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
            stuNo: '',
            firstName: '',
            lastName: '',
            name: '',
            tableData: [],
            timeType:null,
            timeTypes:[{name:'注册时间',value:1},{name:'审核时间',value:2},{name:'登录时间',value:3}],
            verifyStatus:null,
            verifyStatuses:[{name:'未审核',value:0},{name:'审核通过',value:1},{name:'审核未通过',value:2}],
            status:null,
            statuses:[{name:'正常',value:1},{name:'封停',value:0}],
            type:null,
            types:[{name:'学历生',value:1},{name:'非学历生',value:2}],
            dateRange:null,
            genders:[{name:'男',value:1},{name:'女',value:2}],
            saveLoading:false,
            detailDialogVisible:false,
            editDialogVisible:false,
            editObj:{stuId:0,stuNo:0,stuImg:"",firstName:"",lastName:"",chineseName:"",email:"",country:"",gender:1,birth:"",certificateType:"",certificateNo:"",stuType:2,stuStatus:0,stuVerify:1,verifyUser:{}},
            uploadUrl:"/student/upload",
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
                this.editObj={stuId:0,stuNo:0,stuImg:"",firstName:"",lastName:"",chineseName:"",email:"",country:"",gender:1,birth:"",certificateType:"",certificateNo:"",stuType:2,stuStatus:0,stuVerify:1,verifyUser:{}};
            },
            handleEdit: function (row) {
                var that =this;
                $.ajax({
                    url:"/student/load",
                    type:"post",
                    data:JSON.stringify({id: row.stuId}),
                    headers:{"Content-Type":"application/json;charset=utf-8"},
                    dataType:"json",
                    success:function (res) {
                        if(res.code=="success"){
                            that.editDialogVisible=true;
                            that.editObj=res.data;
                            if(that.editObj.birth){
                                that.editObj.birth = new Date(that.editObj.birth );
                            }
                            if(null==that.editObj.verifyUser){
                                that.editObj.verifyUser={};
                            }
                        }else{
                            that.$message.error(res.message);
                        }
                    }
                });
            },
            handleDetail:function(row){
                var that =this;
                $.ajax({
                    url:"/student/load",
                    type:"post",
                    data:JSON.stringify({id: row.stuId}),
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
                    this.editObj.stuImg=res.data.fileName;
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


                if(null==that.editObj.stuType){
                    that.$message.error("请选择学生类型！");
                    return false;
                }
                if(that.editObj.firstName==""){
                    that.$message.error("姓氏不能为空！");
                    that.$refs.firstName.focus();
                    return false;
                }
                if(that.editObj.lastName==""){
                    that.$message.error("名字不能为空！");
                    that.$refs.lastName.focus();
                    return false;
                }
                if(that.editObj.country==""){
                    that.$message.error("国籍不能为空！");
                    that.$refs.country.focus();
                    return false;
                }
                if(that.editObj.email==""){
                    that.$message.error("电子邮箱不能为空！");
                    that.$refs.email.focus();
                    return false;
                }
                if(that.editObj.certificateType==""){
                    that.$message.error("证件类型不能为空！");
                    that.$refs.certificateType.focus();
                    return false;
                }
                if(that.editObj.certificateNo==""){
                    that.$message.error("证件号码不能为空！");
                    that.$refs.certificateNo.focus();
                    return false;
                }
                if(null==that.editObj.gender){
                    that.$message.error("请选择性别！");
                    return false;
                }
                if(that.editObj.stuPwd==""){
                    that.$message.error("请设置登录密码！");
                    that.$refs.stuPwd.focus();
                    return false;
                }
                if(that.editObj.birth){
                    that.editObj.birth =that.editObj.birth.getTime();
                }
                that.saveLoading=true;
                $.ajax({
                    url:"/student/save",
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
                        url:"/student/delete",
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
                    url:"/student/status",
                    type:"post",
                    data:JSON.stringify({id: row.stuId,status: row.stuStatus}),
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
                    url:"/student/page",
                    type:"post",
                    data:JSON.stringify({
                        currentPage: this.currentPage,
                        pageSize: this.pageSize,
                        timeType: this.timeType,
                        type: this.type,
                        status: this.status,
                        verifyStatus: this.verifyStatus,
                        firstName: this.firstName,
                        lastName: this.lastName,
                        name: this.name,
                        startTime: null==this.dateRange?null:this.dateRange[0].getTime(),
                        endTime: null==this.dateRange?null:this.dateRange[1].getTime()
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