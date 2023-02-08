package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.SysUser;
import cn.zjc.app.bean.SysUserRole;
import cn.zjc.app.service.SysUserRoleService;
import cn.zjc.app.service.SysUserService;
import cn.zjc.app.utils.ResponseData;
import cn.zjc.app.utils.AppUtil;
import cn.zjc.app.utils.ServletUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author ZJC
 * @decription: 系统用户管理
 * @date: 2023/02/01 20:55
 * @since JDK 1.8
 */
@RequestMapping("/user")
@Controller
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @ControllerLog(module = "系统用户管理",description = "进入模块")
    @PreAuthorize(value = "hasAuthority('/user/index')")
    @GetMapping(value = "/index")
    public String index(){
        return "userindex";
    }

    @ControllerLog(module = "系统用户管理",description = "分页查询")
    @PreAuthorize(value = "hasAuthority('/user/page')")
    @PostMapping(value = "/page")
    @ResponseBody
    public ResponseData page(@RequestBody Map params) {
        Integer pageNo = AppUtil.getIntegerParam(params,"currentPage");
        Integer pageSize=  AppUtil.getIntegerParam(params,"pageSize");
        String searchName= AppUtil.getStringParam(params,"searchName");
        Integer status=   AppUtil.getIntegerParam(params,"status");

        pageNo = null==pageNo?1:pageNo;
        pageSize=null==pageSize?10:pageSize;
        JSONObject data = new JSONObject();
        data.put("pageNo", pageNo);
        data.put("pageSize", pageSize);
        List<SysUser> objList = this.sysUserService.queryByPage(pageNo, pageSize, searchName, status);
        long total = 0;
        if (pageNo == 1) {
            total = this.sysUserService.queryCount( searchName, status);
            data.put("total", total);
            if(total==0){
                return new ResponseData("failure","无数据！",data);
            }
        }
        if (AppUtil.isEmpty(objList)) {
            return new ResponseData("failure","无数据！",data);
        } else {
            data.put("list", toArray(objList));
            return new ResponseData("success","加载成功！",data);
        }

    }
    @ControllerLog(module = "系统用户管理",description = "删除用户")
    @PreAuthorize(value = "hasAuthority('/user/delete')")
    @PostMapping(value = "/delete")
    @ResponseBody
    public ResponseData delete(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params,"id");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效的用户ID！");
        }
        if (this.sysUserService.delete(id) >0) {
            return new ResponseData("success", "删除成功！");
        } else {
            return new ResponseData("failure", "删除失败！");
        }
    }

    @ControllerLog(module = "系统用户管理",description = "封停/解封")
    @PreAuthorize(value = "hasAuthority('/user/status')")
    @PostMapping(value = "/status")
    @ResponseBody
    public ResponseData status(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params,"id");
        Integer status = AppUtil.getIntegerParam(params,"status");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效的用户ID！");
        }
        if(status!=0 && status!=1){
            return new ResponseData("error", "无效的用户状态！");
        }
        if (this.sysUserService.saveOrUpdate(new SysUser(id, status))>0) {
            return new ResponseData("success", "操作成功！");
        } else {
            return new ResponseData("failure", "操作失败！");
        }
    }

    @ControllerLog(module = "系统用户管理",description = "重置密码")
    @PreAuthorize(value = "hasAuthority('/user/reset')")
    @PostMapping(value = "/reset")
    @ResponseBody
    public ResponseData reset(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params,"id");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效的用户ID！");
        }
        SysUser obj = new SysUser(id);
        obj.setUserPwd(this.passwordEncoder.encode("123456"));
        if (this.sysUserService.saveOrUpdate(obj) >0) {
            return new ResponseData("success", "密码重置成功！");
        } else {
            return new ResponseData("failure", "密码重置失败！");
        }
    }

    @ControllerLog(module = "系统用户管理",description = "查看详情")
    @PreAuthorize(value = "hasAuthority('/user/load')")
    @PostMapping("/load")
    @ResponseBody
    public ResponseData load(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params,"id");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效操作！");
        }
        SysUser obj= this.sysUserService.queryById(id);
        if (null != obj) {
            if(!StringUtils.hasLength(obj.getHeadImg())){
                obj.setHeadImg("");
            }
            return new ResponseData("success", "加载成功", obj);
        } else {
            return new ResponseData("failure", "加载失败");
        }
    }

    @ControllerLog(module = "系统用户管理",description = "保存用户")
    @PreAuthorize(value = "hasAuthority('/user/save')")
    @PostMapping(value = "/save")
    @ResponseBody
    public ResponseData save(@RequestBody SysUser obj, HttpServletRequest request) {
        ResponseData result = new ResponseData();
        if (null == obj) {
            return new ResponseData("error", "无效操作！");
        }
        if (!StringUtils.hasLength(obj.getUserName())) {
            return new ResponseData("error", "用户名不能为空！");
        }
        if (!StringUtils.hasLength(obj.getRealName())) {
            return new ResponseData("error", "姓名不能为空！");
        }
        if (!StringUtils.hasLength(obj.getUserPhone())) {
            return new ResponseData("error", "联系电话不能为空！");
        }
        if (null == obj.getUserId() || obj.getUserId() == 0) {
            obj.setCreateTime(System.currentTimeMillis());
            obj.setLoginNum(1);
            obj.setLoginIp(ServletUtil.getIpAddr(request));
            obj.setLoginTime(obj.getCreateTime());
            obj.setModifyTime(obj.getCreateTime());
            obj.setUserStatus(0);
            obj.setUserPwd(this.passwordEncoder.encode("123456"));
        } else {
            obj.setModifyTime(System.currentTimeMillis());
        }
        if (this.sysUserService.saveOrUpdate(obj) >0) {
            return new ResponseData("success", "保存成功！");
        } else {
            return new ResponseData("failure", "保存失败！");
        }
    }

    @ControllerLog(module = "系统用户管理",description = "上传用户头像")
    @PreAuthorize(value = "hasAuthority('/user/upload')")
    @PostMapping(value = "/upload")
    @ResponseBody
    public ResponseData upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String realPath = request.getServletContext().getRealPath("/");
        String uploadPath = "/upload/users/";
        File upload = new File(realPath + uploadPath);
        if (!upload.exists()) {
            upload.mkdirs();
        }
        long originalSize = file.getSize();
        if (originalSize > 500*1024) {
            return new ResponseData("error", "图片超过了最大限制500KB！");
        }
        String name = file.getOriginalFilename();
        String ext = name.substring(name.lastIndexOf("."));
        if(!".jpg".equalsIgnoreCase(ext) && !".jpeg".equalsIgnoreCase(ext) && !".png".equalsIgnoreCase(ext) && !".bmp".equalsIgnoreCase(ext)){
            return new ResponseData("error", "只能上传jpg,jpeg,png,bmp格式！");
        }
        String fileName = UUID.randomUUID() + ext;
        // 保存
        try {
            File targetFile = new File(upload, fileName);
            file.transferTo(targetFile);
            JSONObject data = new JSONObject();
            data.put("fileName", uploadPath + fileName);
            return new ResponseData("success", "上传成功！", data);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData("failure", "上传失败！");
        }
    }

    @ControllerLog(module = "系统用户管理",description = "查询用户角色")
    @PreAuthorize(value = "hasAuthority('/user/role')")
    @PostMapping("/role")
    @ResponseBody
    public ResponseData role(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params,"id");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效操作！");
        }
        List<SysUserRole> list = this.sysUserRoleService.queryByUser(id);
        if (!AppUtil.isEmpty(list)) {
            JSONObject data = new JSONObject();
            data.put("list", list);
            return new ResponseData("success", "加载成功", data);
        } else {
            return new ResponseData("failure", "加载失败");
        }
    }


    @ControllerLog(module = "系统用户管理",description = "设置用户角色")
    @PreAuthorize(value = "hasAuthority('/user/rolesave')")
    @PostMapping(value = "/rolesave")
    @ResponseBody
    public ResponseData rolesave(@RequestBody Map params, HttpServletRequest request) {
        Integer userId = AppUtil.getIntegerParam(params,"userId");
        List roleIds = AppUtil.getListParams(params,"roleIds");
        ResponseData result = new ResponseData();
        if (!AppUtil.isVaildId(userId)) {
            return new ResponseData("error", "无效的用户！");
        }
        if (AppUtil.isEmpty(roleIds)) {
            return new ResponseData("error", "请选择角色！");
        }
        if (this.sysUserRoleService.save(userId, roleIds) >0) {
            return new ResponseData("success", "角色设置成功！");
        } else {
            return new ResponseData("failure", "角色设置失败！");
        }
    }

    /**
     * 把集合变成json数组（只保留有用数据，减少json大小）
     *
     * @param objList
     * @return
     */
    private JSONArray toArray(List<SysUser> objList) {
        JSONArray jsonArray = new JSONArray();
        for (SysUser obj : objList) {
            JSONObject jo = new JSONObject();
            jo.put("userId", obj.getUserId());
            jo.put("userName", obj.getUserName());
            jo.put("realName", obj.getRealName());
            jo.put("userPhone", obj.getUserPhone());
            jo.put("headImg", obj.getHeadImg());
            jo.put("loginNum", obj.getLoginNum());
            jo.put("loginIp", obj.getLoginIp());
            jo.put("loginTime", obj.getLoginTime());
            jo.put("offlineTime", obj.getLogoutTime());
            jo.put("createTime", obj.getCreateTime());
            jo.put("modifyTime", obj.getModifyTime());
            jo.put("userRemark", obj.getUserRemark());
            jo.put("userStatus", obj.getUserStatus());
            jo.put("userRoles", this.sysUserRoleService.queryByUser(obj.getUserId()));
            jsonArray.add(jo);
        }
        return jsonArray;
    }



}
