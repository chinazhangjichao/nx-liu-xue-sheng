package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.ArticleFile;
import cn.zjc.app.bean.Major;
import cn.zjc.app.bean.Teacher;
import cn.zjc.app.bean.SysUser;
import cn.zjc.app.service.ArticleFileService;
import cn.zjc.app.service.TeacherService;
import cn.zjc.app.utils.AppUtil;
import cn.zjc.app.utils.Constants;
import cn.zjc.app.utils.ImageUtil;
import cn.zjc.app.utils.ResponseData;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author ZJC
 * @decription: 师资队伍管理
 * @date: 2023/02/03 21:55
 * @since JDK 1.8
 */

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private TeacherService teacherService;


    @ControllerLog(module = "师资队伍管理",description = "进入模块")
    @PreAuthorize(value = "hasAuthority('/teacher/index')")
    @GetMapping(value = "/index")
    public String index() {
        return "teacherindex";
    }


    @ControllerLog(module = "师资队伍管理",description = "分页查询")
    @PreAuthorize(value = "hasAuthority('/teacher/page')")
    @PostMapping("/page")
    @ResponseBody
    public ResponseData page(@RequestBody Map params) {
        Integer currentPage = AppUtil.getIntegerParam(params, "currentPage");
        Integer pageSize = AppUtil.getIntegerParam(params, "pageSize");
        String keyword = AppUtil.getStringParam(params, "keyword");
        Integer status = AppUtil.getIntegerParam(params, "status");
        JSONObject data = new JSONObject();
        data.put("currentPage", currentPage);
        data.put("pageSize", pageSize);
        long total = 0;
        if (currentPage == 1) {
            total = this.teacherService.queryCount(status, keyword);
            data.put("total", total);
            if (total == 0) {
                return new ResponseData("failure", "无数据", data);
            }
        }
        List<Teacher> objList = this.teacherService.queryByPage(currentPage, pageSize, status, keyword);
        if (AppUtil.isEmpty(objList)) {
            return new ResponseData("failure", "无数据", data);
        } else {
            data.put("list", toArray(objList));
            return new ResponseData("success", "加载成功", data);
        }
    }

    @ControllerLog(module = "师资队伍管理",description = "查询详情")
    @PreAuthorize(value = "hasAuthority('/teacher/load')")
    @PostMapping("/load")
    @ResponseBody
    public ResponseData load(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params, "id");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效的ID！");
        }
        Teacher obj = this.teacherService.queryById(id);
        if (null != obj) {
            return new ResponseData("success", "加载成功", obj);
        } else {
            return new ResponseData("failure", "加载失败");
        }
    }

    @ControllerLog(module = "师资队伍管理",description = "启用/停用")
    @PreAuthorize(value = "hasAuthority('/teacher/status')")
    @PostMapping(value = "/status")
    @ResponseBody
    public ResponseData status(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params,"id");
        Integer status = AppUtil.getIntegerParam(params,"status");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效的ID！");
        }
        if(status!=0 && status!=1){
            return new ResponseData("error", "请求参数不正确！");
        }
        if (this.teacherService.saveOrUpdate(new Teacher(id, status))>0) {
            return new ResponseData("success", "操作成功！");
        } else {
            return new ResponseData("failure", "操作失败！");
        }
    }

    @ControllerLog(module = "师资队伍管理",description = "删除")
    @PreAuthorize(value = "hasAuthority('/teacher/delete')")
    @PostMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params, "id");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效的ID！");
        }
        if (this.teacherService.delete(id) > 0) {
            return new ResponseData("success", "删除成功！");
        } else {
            return new ResponseData("failure", "删除失败！");
        }
    }



    @ControllerLog(module = "师资队伍管理",description = "保存")
    @PreAuthorize(value = "hasAuthority('/teacher/save')")
    @PostMapping("/save")
    @ResponseBody
    public ResponseData save(@RequestBody Teacher obj, HttpSession session) {
        if (null == obj) {
            return new ResponseData("error", "无效操作！");
        }
        if (!StringUtils.hasLength(obj.getHeadImg())) {
            return new ResponseData("error", "展示图片不能为空！");
        }
        if (!StringUtils.hasLength(obj.getRealName())) {
            return new ResponseData("error", "姓名不能为空！");
        }
        if (!StringUtils.hasLength(obj.getDuty())) {
            return new ResponseData("error", "职称/职务不能为空！");
        }
        SysUser loginUser = (SysUser) session.getAttribute(Constants.LOGINUSER);
        obj.setModifyTime(System.currentTimeMillis());
        obj.setModifyUser(loginUser);
        obj.setNo(UUID.randomUUID().toString());
        if (this.teacherService.saveOrUpdate(obj) > 0) {
            return new ResponseData("success", "保存成功！");
        } else {
            return new ResponseData("failure", "保存失败！");
        }
    }
    @ControllerLog(module = "师资队伍管理",description = "上传封面图")
    @PreAuthorize(value = "hasAuthority('/teacher/upload')")
    @PostMapping("/upload")
    @ResponseBody
    public ResponseData uploadimage(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpSession session) {
        JSONObject json = new JSONObject();
        String realPath = request.getServletContext().getRealPath("/");
        String uploadPath = "/upload/teachers/";
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

    /**
     * 把集合变成json数组
     *
     * @param objList
     * @return
     */
    private JSONArray toArray(List<Teacher> objList) {
        JSONArray jsonArray = new JSONArray();
        for (Teacher obj : objList) {
            JSONObject jo = new JSONObject();
            jo.put("id", obj.getId());
            jo.put("realName", obj.getRealName());
            jo.put("headImg", obj.getHeadImg());
            jo.put("status", obj.getStatus());
            jo.put("sortNum", obj.getSortNum());
            jo.put("duty", obj.getDuty());
            jo.put("email", obj.getEmail());
            jo.put("major", obj.getMajor());
            jo.put("modifyTime", obj.getModifyTime());
            jsonArray.add(jo);
        }
        return jsonArray;
    }
}
