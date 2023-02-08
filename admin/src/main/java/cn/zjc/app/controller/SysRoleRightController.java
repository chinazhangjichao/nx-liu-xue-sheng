package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.SysRoleRight;
import cn.zjc.app.bean.SysUser;
import cn.zjc.app.service.SysRoleRightService;
import cn.zjc.app.utils.Constants;
import cn.zjc.app.utils.ResponseData;
import cn.zjc.app.utils.AppUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author ZJC
 * @decription: 角色权限管理
 * @date: 2022/1/15 14:21
 * @since JDK 1.8
 */
@RequestMapping("/roleright")
@RestController
public class SysRoleRightController {

    @Autowired
    private SysRoleRightService sysRoleRightService;
    @ControllerLog(module = "权限管理",description = "查询角色权限")
    @PreAuthorize(value = "hasAuthority('/roleright/byrole')")
    @PostMapping("/byrole")
    public ResponseData byrole(@RequestBody Map params){
        Integer id = null==params.get("roleId")?0:(Integer) params.get("roleId");
        if(!AppUtil.isVaildId(id)){
            return new ResponseData("error","无效的角色ID");
        }
        List<SysRoleRight> list= this.sysRoleRightService.queryByRole(id);
        if(AppUtil.isEmpty(list)){
            return  new ResponseData("failure","未查询到该角色的权限信息");
        }else{
            JSONObject data = new JSONObject();
            data.put("list",list);
            return  new ResponseData("success","加载成功！",data);
        }
    }


    @ControllerLog(module = "权限管理",description = "查询登录用户权限")
    @PreAuthorize(value = "hasAuthority('/roleright/byuser')")
    @PostMapping("/byuser")
    public ResponseData byuser(HttpSession session){
        SysUser loginUser =  (SysUser)session.getAttribute(Constants.LOGINUSER);
        if(null==loginUser){
            return new ResponseData("failure","未登录!");
        }else{
            JSONObject data = new JSONObject();
            //data.put("list",loginUser.getAuthorities());
            return new ResponseData("success","加载成功!",data);
        }
    }

    @ControllerLog(module = "权限管理",description = "保存角色权限")
    @PreAuthorize(value = "hasAuthority('/roleright/save')")
    @PostMapping("/save")
    public ResponseData save(@RequestBody SysRoleRight[] objs) {
        if (AppUtil.isEmpty(objs)) {
            return new ResponseData("error", "权限信息不能为空！");
        }
        if (this.sysRoleRightService.saveList(Arrays.asList(objs)) >0) {
            return new ResponseData("success", "操作成功！");
        } else {
            return new ResponseData("failure", "操作失败！");
        }
    }

}
