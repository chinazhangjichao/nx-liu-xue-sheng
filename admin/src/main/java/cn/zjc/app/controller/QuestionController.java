package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.*;
import cn.zjc.app.service.MajorService;
import cn.zjc.app.service.QuestionService;
import cn.zjc.app.utils.AppUtil;
import cn.zjc.app.utils.Constants;
import cn.zjc.app.utils.ImageUtil;
import cn.zjc.app.utils.ResponseData;
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
 * @decription: 考题管理
 * @date: 2023/02/04 09:55
 * @since JDK 1.8
 */

@Controller
@RequestMapping("/question")
public class QuestionController {

    @Resource
    private QuestionService questionService;




    @ControllerLog(module = "考题管理",description = "进入模块")
    @PreAuthorize(value = "hasAuthority('/question/index')")
    @GetMapping(value = "/index")
    public String index() {
        return "questionindex";
    }


    @ControllerLog(module = "考题管理",description = "分页查询")
    @PreAuthorize(value = "hasAuthority('/question/page')")
    @PostMapping("/page")
    @ResponseBody
    public ResponseData page(@RequestBody Map params) {
        Integer currentPage = AppUtil.getIntegerParam(params, "currentPage");
        Integer pageSize = AppUtil.getIntegerParam(params, "pageSize");
        String keyword = AppUtil.getStringParam(params, "keyword");
        Integer course = AppUtil.getIntegerParam(params, "course");
        Integer exam = AppUtil.getIntegerParam(params, "exam");
        Integer test= AppUtil.getIntegerParam(params, "test");
        Integer type = AppUtil.getIntegerParam(params, "type");

        JSONObject data = new JSONObject();
        data.put("currentPage", currentPage);
        data.put("pageSize", pageSize);
        long total = 0;
        if (currentPage == 1) {
            total = this.questionService.queryCount(course,exam,test,type, keyword);
            data.put("total", total);
            if (total == 0) {
                return new ResponseData("failure", "无数据", data);
            }
        }
        List<Question> objList = this.questionService.queryByPage(currentPage, pageSize, course,exam,test,type, keyword);
        if (AppUtil.isEmpty(objList)) {
            return new ResponseData("failure", "无数据", data);
        } else {
            data.put("list", toArray(objList));
            return new ResponseData("success", "加载成功", data);
        }
    }

    @ControllerLog(module = "考题管理",description = "查询详情")
    @PreAuthorize(value = "hasAuthority('/question/load')")
    @PostMapping("/load")
    @ResponseBody
    public ResponseData load(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params, "id");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效的ID！");
        }
        Question obj = this.questionService.queryById(id);
        if (null != obj) {
            return new ResponseData("success", "加载成功", obj);
        } else {
            return new ResponseData("failure", "加载失败");
        }
    }

    @ControllerLog(module = "考题管理",description = "启用/停用")
    @PreAuthorize(value = "hasAuthority('/question/status')")
    @PostMapping(value = "/status")
    @ResponseBody
    public ResponseData status(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params,"id");
        Integer exam = AppUtil.getIntegerParam(params,"exam");
        Integer test = AppUtil.getIntegerParam(params,"test");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效的ID！");
        }
        if(null!=exam){
            if(exam!=0 && exam!=1){
                return new ResponseData("error", "请求参数不正确！");
            }
        }
        if(null!=test){
            if(test!=0 && test!=1){
                return new ResponseData("error", "请求参数不正确！");
            }
        }
        if (this.questionService.saveOrUpdate(new Question(id, exam,test))>0) {
            return new ResponseData("success", "操作成功！");
        } else {
            return new ResponseData("failure", "操作失败！");
        }
    }

    @ControllerLog(module = "考题管理",description = "删除")
    @PreAuthorize(value = "hasAuthority('/question/delete')")
    @PostMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params, "id");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效的ID！");
        }
        if (this.questionService.delete(id) > 0) {
            return new ResponseData("success", "删除成功！");
        } else {
            return new ResponseData("failure", "删除失败！");
        }
    }



    @ControllerLog(module = "考题管理",description = "保存")
    @PreAuthorize(value = "hasAuthority('/question/save')")
    @PostMapping("/save")
    @ResponseBody
    public ResponseData save(@RequestBody Question obj, HttpSession session) {
        if (null == obj) {
            return new ResponseData("error", "无效操作！");
        }
        if (!StringUtils.hasLength(obj.getName())) {
            return new ResponseData("error", "题目名称不能为空！");
        }
        if(null==obj.getType() || obj.getType()<=0){
            return new ResponseData("error", "考题类型不能为空！");
        }
        //单选或判断题：至少有两个选项
        if(obj.getType()==1 || obj.getType()==3){
            if(!StringUtils.hasLength(obj.getOption1()) || !StringUtils.hasLength(obj.getOption2())){
                return new ResponseData("error", "单选或判断题至少有两个选项！");
            }
        }
        if(obj.getType()==2){
            if(!StringUtils.hasLength(obj.getOption1()) || !StringUtils.hasLength(obj.getOption2())|| !StringUtils.hasLength(obj.getOption3())){
                return new ResponseData("error", "多选题至少有三个选项！");
            }
        }
        if (!StringUtils.hasLength(obj.getAnswer())) {
            return new ResponseData("error", "参考答案不能为空！");
        }
        if (null==obj.getScore() || obj.getScore()<=0 || obj.getScore()>=100) {
            return new ResponseData("error", "默认分值设置有误！");
        }
        SysUser loginUser = (SysUser) session.getAttribute(Constants.LOGINUSER);
        obj.setModifyTime(System.currentTimeMillis());
        obj.setModifyUser(loginUser);
        if (this.questionService.saveOrUpdate(obj) > 0) {
            return new ResponseData("success", "保存成功！");
        } else {
            return new ResponseData("failure", "保存失败！");
        }
    }
    @ControllerLog(module = "考题管理",description = "上传素材")
    @PreAuthorize(value = "hasAuthority('/question/upload')")
    @PostMapping("/upload")
    @ResponseBody
    public ResponseData uploadimage(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpSession session) {
        JSONObject json = new JSONObject();
        String realPath = request.getServletContext().getRealPath("/");
        String uploadPath = "/upload/questions/";
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

    @ControllerLog(module = "考题管理",description = "导入")
    @PreAuthorize(value = "hasAuthority('/question/import')")
    @PostMapping("/import")
    @ResponseBody
    public ResponseData importfile(MultipartFile file, HttpSession session) {
        String realPath = session.getServletContext().getRealPath("/");
        String uploadPath = "/upload/questions/temp/";
        File upload = new File(realPath + uploadPath);
        if (!upload.exists()) {
            upload.mkdirs();
        }
        long originalSize = file.getSize();
        String name = file.getOriginalFilename();
        String ext = name.substring(name.lastIndexOf("."));
        if (!".xls".equalsIgnoreCase(ext) && !".xlsx".equalsIgnoreCase(ext) ) {
            return new ResponseData("error", "只能上传xls或xlsx格式！");
        }
        SysUser loginUser = (SysUser)session.getAttribute(Constants.LOGINUSER);
        String fileName = UUID.randomUUID() + ext;
        // 保存
        try {
            //上传
            File targetFile = new File(upload, fileName);
            file.transferTo(targetFile);

            //导入
            StringBuilder tip = new StringBuilder();
            //List<CourseInfor> courseInfors= this.courseInforService.queryAll(null);
            List<Question> objList  = new ArrayList<>();
            List<ArrayList<String>> lists = ExcelRead.readExcel(targetFile);
            if(null!=lists && lists.size()>0){

                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat df2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int i = 0;
                for (ArrayList<String> row: lists) {
                    i++;
                    if(row.size()<7){
                        continue;
                    }
                    if(!StringUtils.hasLength(row.get(1))){
                        continue;
                    }
                    Question obj = new Question();
                    String courseName  =row.get(0).trim();
//                    for (Major m:majors) {
//                        if(m.getMajorName().equals(deptName)){
//                            obj.setWorkDept(dept);
//                            break;
//                        }
//                    }
                    obj.setName(row.get(1).trim());
                    obj.setOption1(row.get(2).trim());
                    obj.setOption2(row.get(3).trim());
                    obj.setOption3(row.get(4).trim());
                    obj.setOption4(row.get(5).trim());
                    obj.setOption5(row.get(6).trim());
                    obj.setOption6(row.get(7).trim());
                    obj.setAnswer(row.get(8).trim());
                    obj.setAnalysis(row.get(9).trim());



                    String type = row.get(10).trim();
                    if(type.indexOf("单选")!=-1){
                        obj.setType(1);
                    }else if(type.indexOf("多选")!=-1){
                        obj.setType(2);
                    }else if(type.indexOf("判断")!=-1){
                        obj.setType(31);
                    }else if(type.indexOf("问答")!=-1){
                        obj.setType(4);
                    }else{
                        obj.setType(5);
                    }
                    try{
                        obj.setScore(new Double(row.get(11)));
                    }catch (Exception e){
                        obj.setScore(0.0);
                    }
                    try{
                        obj.setIsExam(new Integer(row.get(12)));
                    }catch (Exception e){
                        obj.setIsExam(0);
                    }
                    try{
                        obj.setIsTest(new Integer(row.get(13)));
                    }catch (Exception e){
                        obj.setIsTest(0);
                    }

                    obj.setModifyTime(System.currentTimeMillis());
                    obj.setModifyUser(loginUser);
                    objList.add(obj);
                }
            }
            if(this.questionService.saveList(objList)>0){
                return new ResponseData("success", "导入成功",tip.toString());
            }else{
                return new ResponseData("failure", "导入失败");
            }
        } catch (Exception e) {
            return new ResponseData("failure", "导入失败：" + e.getMessage());
        }
    }

    @ControllerLog(module = "考题管理",description = "导出")
    @PreAuthorize(value = "hasAuthority('/question/export')")
    @RequestMapping(value = "/export")
    public void export(@RequestParam(defaultValue = "1") Integer currentPage,@RequestParam(defaultValue="0") Integer course,Integer type,Integer exam,Integer test, String keyword,HttpSession session,
                       HttpServletResponse response) {
        List<Question> objList = this.questionService.queryByPage(currentPage,10000,course,exam,test,type,keyword);
        List<ExportQuestion> viewList = new ArrayList<ExportQuestion>();

        if (null != objList) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Question mx : objList) {
                ExportQuestion exportMx = new ExportQuestion();
                exportMx.setCourse(mx.getCourse().getCourseName());
                exportMx.setName(mx.getName());
                exportMx.setOption1(mx.getOption1());
                exportMx.setOption1(mx.getOption1());
                exportMx.setOption1(mx.getOption1());
                exportMx.setOption1(mx.getOption1());
                exportMx.setOption1(mx.getOption1());
                exportMx.setOption1(mx.getOption1());
                exportMx.setAnswer(mx.getAnswer());
                exportMx.setAnalysis(mx.getAnalysis());
                exportMx.setScore(mx.getScore());
                exportMx.setType(mx.getType()==1?"单选":mx.getType()==2?"多选":mx.getType()==3?"判断":mx.getType()==4?"问答":"其他");
                exportMx.setIsExam(mx.getIsExam());
                exportMx.setIsTest(mx.getIsTest());

                viewList.add(exportMx);

            }
        }
        if (null != viewList && viewList.size() > 0) {
            OutputStream outputStream = null;
            try {
                ExportExcel<ExportQuestion> ex = new ExportExcel<ExportQuestion>();
                DateFormat df = new SimpleDateFormat("yyyyMMdd");
                try {
                    response.setHeader("Content-Disposition", "attachment;filename=" + new String("考题信息.xls".getBytes(), "ISO-8859-1"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                response.setContentType("application/x-excel;charset=UTF-8");
                outputStream = response.getOutputStream();
                String[] headers = { "课程", "题目","选项1", "选项2", "选项3", "选项4","选项5", "选项6", "答案","解析","题型","分值","是否考试题", "是否自测题"};
                HSSFWorkbook workBook = ex.exportExcel("会员列表", headers, viewList);
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
    private JSONArray toArray(List<Question> objList) {
        JSONArray jsonArray = new JSONArray();
        for (Question obj : objList) {
            JSONObject jo = new JSONObject();
            jo.put("id", obj.getId());
            jo.put("name", obj.getName());
            jo.put("type", obj.getType());
            jo.put("course", obj.getCourse());
            jo.put("score", obj.getScore());
            jo.put("isExam", obj.getIsExam());
            jo.put("isTest", obj.getIsTest());
            jo.put("modifyTime", obj.getModifyTime());
            jsonArray.add(jo);
        }
        return jsonArray;
    }
}
