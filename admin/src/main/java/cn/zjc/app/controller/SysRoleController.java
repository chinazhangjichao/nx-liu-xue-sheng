package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.SysRole;
import cn.zjc.app.service.SysRoleService;
import cn.zjc.app.utils.ResponseData;
import cn.zjc.app.utils.AppUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ZJC
 * @decription: 系统角色服务
 * @date: 2022/1/13 13:36
 * @since JDK 1.8
 */
@RequestMapping("/role")
@Controller
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;
    @ControllerLog(module = "角色管理",description = "进入模块")
    @PreAuthorize(value = "hasAuthority('/role/index')")
    @GetMapping(value = "/index")
    public String index(){
        return "roleindex";
    }


    @ControllerLog(module = "角色管理",description = "查询所有角色")
    @PreAuthorize(value = "hasAuthority('/role/all')")
    @PostMapping("/all")
    @ResponseBody
    public ResponseData all(){
        List<SysRole> list = this.sysRoleService.queryAll();
        JSONObject data = new JSONObject();
        data.put("list",list);
        return  new ResponseData("success","加载成功！",data);
    }

    @ControllerLog(module = "角色管理",description = "加载角色详情")
    @PreAuthorize(value = "hasAuthority('/role/load')")
    @PostMapping("/load")
    @ResponseBody
    public ResponseData load(@RequestBody Map params){
        Integer id=null!=params.get("id")?(Integer) params.get("id"):null;
        if(!AppUtil.isVaildId(id)){
            return  new ResponseData("error","未知的角色ID！");
        }
        SysRole obj = this.sysRoleService.queryById(id);
        if (null == obj) {
            return new ResponseData("failure", "信息不存在！");
        } else {
            JSONObject data = new JSONObject();
            data.put("obj", obj);
            return new ResponseData("success", "加载成功！", data);
        }
    }

    @ControllerLog(module = "角色管理",description = "删除角色")
    @PreAuthorize(value = "hasAuthority('/role/delete')")
    @PostMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestBody Map params){
        Integer id=null!=params.get("id")?(Integer) params.get("id"):null;
        if(!AppUtil.isVaildId(id)){
            return  new ResponseData("error","未知的角色ID！");
        }
        if(id<=4){
            return  new ResponseData("error","不允许删除系统角色！");
        }
        if(this.sysRoleService.delete(id)>0){
            return  new ResponseData("success","操作成功！");
        }else{
            return  new ResponseData("failure","操作失败！");
        }
    }

    @ControllerLog(module = "角色管理",description = "保存角色")
    @PreAuthorize(value = "hasAuthority('/role/save')")
    @PostMapping("/save")
    @ResponseBody
    public ResponseData save(@RequestBody SysRole obj) {
        if (null == obj) {
            return new ResponseData("error", "无效操作，未知的角色信息！");
        }
        if (!StringUtils.hasLength(obj.getRoleName())) {
            return new ResponseData("error", "角色名称不允许为空！");
        }
        if (this.sysRoleService.saveOrUpdate(obj) > 0) {
            return new ResponseData("success", "操作成功！");
        } else {
            return new ResponseData("failure", "操作失败！");
        }
    }



}
