package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.ExportStudentInfor;
import cn.zjc.app.bean.StudentInfor;
import cn.zjc.app.bean.SysUser;
import cn.zjc.app.service.StudentInforService;
import cn.zjc.app.utils.*;
import cn.zjc.app.utils.excel.ExcelRead;
import cn.zjc.app.utils.excel.ExportExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ZJC
 * @decription: 学生管理
 * @date: 2023/02/06 20:55
 * @since JDK 1.8
 */

@Controller
@RequestMapping("/student")
public class StudentInforController {

    @Resource
    private StudentInforService studentInforService;


    //==============================================
    //==================在线申请列表==================
    //==============================================
    @ControllerLog(module = "学生管理",description = "在线申请模块")
    @PreAuthorize(value = "hasAuthority('/student/apply/index')")
    @GetMapping(value = "/apply/index")
    public String applyIndex() {
        return "studentapply";
    }


    @ControllerLog(module = "学生管理",description = "在线申请列表")
    @PreAuthorize(value = "hasAuthority('/student/apply/page')")
    @PostMapping("/apply/page")
    @ResponseBody
    public ResponseData applyPage(@RequestBody Map params) {
        Integer currentPage = AppUtil.getIntegerParam(params, "currentPage");
        Integer pageSize = AppUtil.getIntegerParam(params, "pageSize");
        String firstName = AppUtil.getStringParam(params, "firstName");
        String lastName= AppUtil.getStringParam(params, "lastName");
        String name = AppUtil.getStringParam(params, "name");
        Integer status = AppUtil.getIntegerParam(params, "status");
        Integer timeType= AppUtil.getIntegerParam(params, "timeType");
        Integer type = AppUtil.getIntegerParam(params, "type");
        Long startTime= AppUtil.getLongParam(params, "startTime");
        Long endTime= AppUtil.getLongParam(params, "endTime");
        Long stuNo= AppUtil.getLongParam(params, "stuNo");

        JSONObject data = new JSONObject();
        data.put("currentPage", currentPage);
        data.put("pageSize", pageSize);
        long total = 0;
        if (currentPage == 1) {
            total = this.studentInforService.queryCount(stuNo,type,0,status,firstName,lastName,name,timeType,startTime,endTime);
            data.put("total", total);
            if (total == 0) {
                return new ResponseData("failure", "无数据", data);
            }
        }
        List<StudentInfor> objList = this.studentInforService.queryByPage(currentPage, pageSize, stuNo,type,0,status,firstName,lastName,name,timeType,startTime,endTime);
        if (AppUtil.isEmpty(objList)) {
            return new ResponseData("failure", "无数据", data);
        } else {
            data.put("list", toArray(objList));
            return new ResponseData("success", "加载成功", data);
        }
    }
    @ControllerLog(module = "学生管理",description = "审核")
    @PreAuthorize(value = "hasAuthority('/student/verify')")
    @PostMapping(value = "/verify")
    @ResponseBody
    public ResponseData verify(@RequestBody StudentInfor obj,HttpSession session) {
        if (null == obj) {
            return new ResponseData("error", "无效操作！");
        }
        if(null==obj.getStuVerify() || obj.getStuVerify()<0 || obj.getStuVerify()>2){
            return new ResponseData("error", "请求参数不正确！");
        }
        if(obj.getStuVerify()==2){
            if (!StringUtils.hasLength(obj.getVerifyRemark())) {
                return new ResponseData("error", "请填写拒绝原因！");
            }
        }
        SysUser loginUser = (SysUser) session.getAttribute(Constants.LOGINUSER);
        obj.setVerifyTime(System.currentTimeMillis());
        obj.setVerifyUser(loginUser);
        if (this.studentInforService.saveOrUpdate(obj)>0) {
            return new ResponseData("success", "操作成功！");
        } else {
            return new ResponseData("failure", "操作失败！");
        }
    }

    //==============================================
    //===================档案管理====================
    //==============================================
    @ControllerLog(module = "学生管理",description = "进入模块")
    @PreAuthorize(value = "hasAuthority('/student/index')")
    @GetMapping(value = "/index")
    public String index() {
        return "studentindex";
    }


    @ControllerLog(module = "学生管理",description = "分页查询")
    @PreAuthorize(value = "hasAuthority('/student/page')")
    @PostMapping("/page")
    @ResponseBody
    public ResponseData page(@RequestBody Map params) {
        Integer currentPage = AppUtil.getIntegerParam(params, "currentPage");
        Integer pageSize = AppUtil.getIntegerParam(params, "pageSize");
        String firstName = AppUtil.getStringParam(params, "firstName");
        String lastName= AppUtil.getStringParam(params, "lastName");
        String name = AppUtil.getStringParam(params, "name");
        Integer status = AppUtil.getIntegerParam(params, "status");
        Integer timeType= AppUtil.getIntegerParam(params, "timeType");
        Integer type = AppUtil.getIntegerParam(params, "type");
        Long startTime= AppUtil.getLongParam(params, "startTime");
        Long endTime= AppUtil.getLongParam(params, "endTime");
        Long stuNo= AppUtil.getLongParam(params, "stuNo");

        JSONObject data = new JSONObject();
        data.put("currentPage", currentPage);
        data.put("pageSize", pageSize);
        long total = 0;
        if (currentPage == 1) {
            total = this.studentInforService.queryCount(stuNo,type,1,status,firstName,lastName,name,timeType,startTime,endTime);
            data.put("total", total);
            if (total == 0) {
                return new ResponseData("failure", "无数据", data);
            }
        }
        List<StudentInfor> objList = this.studentInforService.queryByPage(currentPage, pageSize, stuNo,type,1,status,firstName,lastName,name,timeType,startTime,endTime);
        if (AppUtil.isEmpty(objList)) {
            return new ResponseData("failure", "无数据", data);
        } else {
            data.put("list", toArray(objList));
            return new ResponseData("success", "加载成功", data);
        }
    }

    @ControllerLog(module = "学生管理",description = "查询详情")
    @PreAuthorize(value = "hasAuthority('/student/load')")
    @PostMapping("/load")
    @ResponseBody
    public ResponseData load(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params, "id");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效的ID！");
        }
        StudentInfor obj = this.studentInforService.queryById(id);
        if (null != obj) {
            return new ResponseData("success", "加载成功", obj);
        } else {
            return new ResponseData("failure", "加载失败");
        }
    }

    @ControllerLog(module = "学生管理",description = "启用/停用")
    @PreAuthorize(value = "hasAuthority('/student/status')")
    @PostMapping(value = "/status")
    @ResponseBody
    public ResponseData status(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params,"id");
        Integer status = AppUtil.getIntegerParam(params,"status");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效的ID！");
        }
        if(null!=status){
            if(status!=0 && status!=1){
                return new ResponseData("error", "请求参数不正确！");
            }
        }
        StudentInfor obj = new StudentInfor(id);
        obj.setStuStatus(status);
        if (this.studentInforService.saveOrUpdate(obj)>0) {
            return new ResponseData("success", "操作成功！");
        } else {
            return new ResponseData("failure", "操作失败！");
        }
    }



    @ControllerLog(module = "学生管理",description = "删除")
    @PreAuthorize(value = "hasAuthority('/student/delete')")
    @PostMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params, "id");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效的ID！");
        }
        if (this.studentInforService.delete(id) > 0) {
            return new ResponseData("success", "删除成功！");
        } else {
            return new ResponseData("failure", "删除失败！");
        }
    }



    @ControllerLog(module = "学生管理",description = "保存")
    @PreAuthorize(value = "hasAuthority('/student/save')")
    @PostMapping("/save")
    @ResponseBody
    public ResponseData save(@RequestBody StudentInfor obj, HttpServletRequest request) {
        if (null == obj) {
            return new ResponseData("error", "无效操作！");
        }
        if (!StringUtils.hasLength(obj.getFirstName())) {
            return new ResponseData("error", "First Name不能为空！");
        }
        if (!StringUtils.hasLength(obj.getFirstName())) {
            return new ResponseData("error", "Last Name不能为空！");
        }
        if (!StringUtils.hasLength(obj.getCountry())) {
            return new ResponseData("error", "国籍不能为空！");
        }
        if (!StringUtils.hasLength(obj.getEmail())) {
            return new ResponseData("error", "电子邮箱不能为空！");
        }
        if (!StringUtils.hasLength(obj.getCertificateType())) {
            return new ResponseData("error", "证件类型不能为空！");
        }
        if (!StringUtils.hasLength(obj.getCertificateNo())) {
            return new ResponseData("error", "证件号码不能为空！");
        }
        if (null==obj.getStuType() || obj.getStuType()<=0) {
            return new ResponseData("error", "请求参数不正确！");
        }
        SysUser loginUser = (SysUser) request.getSession().getAttribute(Constants.LOGINUSER);
        obj.setModifyTime(System.currentTimeMillis());
        obj.setModifyIp(ServletUtil.getIpAddr(request));
        if(!AppUtil.isVaildId(obj.getStuId())){
            obj.setCreateTime(obj.getModifyTime());
            obj.setCreateIp(obj.getModifyIp());
            String stuNoPrefix = (String) request.getServletContext().getAttribute(Constants.STUNOPREFIX)+Calendar.getInstance().get(Calendar.YEAR);
            obj.setStuNo(new Long(stuNoPrefix));
            obj.setStuStatus(1);
            obj.setVerifyUser(loginUser);
            obj.setVerifyTime(obj.getModifyTime());
            obj.setVerifyIp(obj.getModifyIp());
            obj.setStuVerify(1);
            obj.setStuPwd(Endecrypt.md5(obj.getStuPwd()));
        }else{
            obj.setStuVerify(null);
            obj.setVerifyTime(null);
            obj.setVerifyUser(null);
            obj.setVerifyIp(null);
            obj.setStuStatus(null);
            obj.setStuPwd(null);
            obj.setCreateIp(null);
            obj.setCreateTime(null);
        }
        if (this.studentInforService.saveOrUpdate(obj) > 0) {
            return new ResponseData("success", "保存成功！");
        } else {
            return new ResponseData("failure", "保存失败！");
        }
    }
    @ControllerLog(module = "学生管理",description = "上传头像")
    @PreAuthorize(value = "hasAuthority('/student/save')")
    @PostMapping("/upload")
    @ResponseBody
    public ResponseData uploadimage(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpSession session) {
        JSONObject json = new JSONObject();
        String realPath = request.getServletContext().getRealPath("/");
        String uploadPath = "/upload/students/";
        File upload = new File(realPath + uploadPath);
        if (!upload.exists()) {
            upload.mkdirs();
        }
        long originalSize = file.getSize();
        String name = file.getOriginalFilename();
        String ext = name.substring(name.lastIndexOf("."));
        if (!".jpg".equalsIgnoreCase(ext) && !".jpeg".equalsIgnoreCase(ext) && !".png".equalsIgnoreCase(ext) && !".bmp".equalsIgnoreCase(ext)) {
            return new ResponseData("error", "只能上传jpg,jpeg,png,bmp格式！");
        }
        String tempName = "temp_" + System.currentTimeMillis() + ext;
        String fileName = UUID.randomUUID() + ext;
        // 保存
        try {
            if (originalSize < 500 * 1024) {
                File targetFile = new File(upload, fileName);
                file.transferTo(targetFile);
            } else {
                //先保存临时文件
                File targetFile = new File(upload, tempName);
                file.transferTo(targetFile);
                //压缩临时文件另存为正式文件
                if (ImageUtil.commpressPicForScale(targetFile.getAbsolutePath(), upload.getAbsolutePath() + "/" + fileName, 500, 0.8, 1920, 1080)) {
                    targetFile.delete();
                }
            }
            JSONObject data = new JSONObject();
            data.put("fileName", request.getServletContext().getAttribute(Constants.WEBSITE) + uploadPath + fileName);
            return new ResponseData("success", "上传成功", data);
        } catch (Exception e) {
            return new ResponseData("failure", "上传失败：" + e.getMessage());
        }
    }

    @ControllerLog(module = "学生管理",description = "导出")
    @PreAuthorize(value = "hasAuthority('/student/export')")
    @RequestMapping(value = "/export")
    public void export(@RequestParam(defaultValue = "1") Integer currentPage,Integer type,Integer verifyStatus,Integer status,Integer timeType, Long startTime,Long endTime,HttpSession session,
                       HttpServletResponse response) {
        List<StudentInfor> objList = this.studentInforService.queryByPage(currentPage,10000,null,type,verifyStatus,status,null,null,null,timeType,startTime,endTime);
        List<ExportStudentInfor> viewList = new ArrayList<ExportStudentInfor>();

        if (null != objList) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (StudentInfor mx : objList) {
                ExportStudentInfor exportMx = new ExportStudentInfor();
                exportMx.setStuNo(mx.getStuNo()+"");
                exportMx.setFirstName(mx.getFirstName());
                exportMx.setLastName(mx.getLastName());
                exportMx.setChineseName(mx.getChineseName());
                exportMx.setCountry(mx.getCountry());
                exportMx.setEmail(mx.getEmail());
                exportMx.setCertificateType(mx.getCertificateType());
                exportMx.setCertificateNo(mx.getCertificateNo());
                exportMx.setPhone(mx.getPhone());
                exportMx.setAddress(mx.getAddress());
                exportMx.setStuVerify(mx.getStuVerify()==0?"未审核":mx.getStuVerify()==1?"通过审核":"未通过审核");
                if(null!=mx.getStuVerify() && mx.getStuVerify()>0){
                    exportMx.setVerifyTime(df.format(new Date(mx.getVerifyTime())));
                    exportMx.setVerifyIp(mx.getVerifyIp());
                    exportMx.setVerifyRemark(mx.getVerifyRemark());
                    exportMx.setVerifyUser(mx.getVerifyUser().getRealName());
                }
                exportMx.setStuType(mx.getStuType()==1?"学历生":"非学历生");
                exportMx.setStuStatus(mx.getStuStatus()==0?"封停":"正常");
                viewList.add(exportMx);
            }
        }
        if (null != viewList && viewList.size() > 0) {
            OutputStream outputStream = null;
            try {
                ExportExcel<ExportStudentInfor> ex = new ExportExcel<ExportStudentInfor>();
                DateFormat df = new SimpleDateFormat("yyyyMMdd");
                try {
                    response.setHeader("Content-Disposition", "attachment;filename=" + new String("学生信息.xls".getBytes(), "ISO-8859-1"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                response.setContentType("application/x-excel;charset=UTF-8");
                outputStream = response.getOutputStream();
                String[] headers = { "学号", "姓","名", "中文名", "性别", "出生日期","电子邮箱", "国籍", "联系电话","联系地址","证件类型","证件号码","审核结果", "审核时间", "审核人", "审核IP", "审核备注", "学员状态", "学员类型", "创建时间", "创建IP", "登录时间", "登录IP"};
                HSSFWorkbook workBook = ex.exportExcel("学生信息", headers, viewList);
                workBook.write(outputStream);
                outputStream.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != outputStream)
                        outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 把集合变成json数组
     *
     * @param objList
     * @return
     */
    private JSONArray toArray(List<StudentInfor> objList) {
        JSONArray jsonArray = new JSONArray();
        for (StudentInfor obj : objList) {
            JSONObject jo = new JSONObject();
            jo.put("stuId", obj.getStuId());
            jo.put("stuNo", obj.getStuNo());
            jo.put("firstName", obj.getFirstName());
            jo.put("lastName", obj.getLastName());
            jo.put("chineseName", obj.getChineseName());
            jo.put("email", obj.getEmail());
            jo.put("country", obj.getCountry());
            jo.put("stuType", obj.getStuType());
            jo.put("stuVerify", obj.getStuVerify());
            jo.put("stuStatus", obj.getStuStatus());
            jo.put("createTime", obj.getCreateTime());
            jo.put("verifyTime", obj.getVerifyTime());
            jo.put("loginTime", obj.getLoginTime());
            jsonArray.add(jo);
        }
        return jsonArray;
    }
}
