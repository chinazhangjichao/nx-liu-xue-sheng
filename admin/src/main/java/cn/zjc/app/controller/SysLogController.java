package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.SysLog;
import cn.zjc.app.service.SysLogService;
import cn.zjc.app.utils.ResponseData;
import cn.zjc.app.utils.AppUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author ZJC
 * @decription: 系统操作日志管理
 * @date: 2022/1/17 10:55
 * @since JDK 1.8
 */
@Controller
@RequestMapping(value = "/log")
public class SysLogController {
    @Autowired
    private SysLogService sysLogService;

    @ControllerLog(module = "系统日志管理", description = "进入模块")
    @PreAuthorize(value = "hasAuthority('/log/index')")
    @GetMapping(value = "/index")
    public String index() {
        return "logindex";
    }

    @ControllerLog(module = "系统日志管理", description = "分页查询")
    @PreAuthorize(value = "hasAuthority('/log/page')")
    @PostMapping("/page")
    @ResponseBody
    public ResponseData page(@RequestBody Map params) {
        Integer pageNo = AppUtil.getIntegerParam(params, "currentPage");
        Integer pageSize = AppUtil.getIntegerParam(params, "pageSize");
        Integer status = AppUtil.getIntegerParam(params, "status");
        String user = AppUtil.getStringParam(params, "user");
        String module = AppUtil.getStringParam(params, "module");
        String content = AppUtil.getStringParam(params, "content");
        Long startTime = AppUtil.getLongParam(params, "startTime");
        Long endTime = AppUtil.getLongParam(params, "endTime");
        pageNo = null == pageNo ? 1 : pageNo;
        pageSize = null == pageSize ? 10 : pageSize;
        JSONObject data = new JSONObject();
        data.put("pageNo", pageNo);
        data.put("pageSize", pageSize);
        long total = 0;
        if (pageNo == 1) {
            total = this.sysLogService.queryCount(user, module, content, status, startTime, endTime);
            data.put("total", total);
            if (total == 0) {
                return new ResponseData("failure", "无数据！", data);
            }
        }
        List<SysLog> objList = this.sysLogService.queryByPage(pageNo, pageSize, user, module, content, status, startTime, endTime);
        if (AppUtil.isEmpty(objList)) {
            return new ResponseData("failure", "无数据！", data);
        } else {

            data.put("list", toArray(objList));
            return new ResponseData("success", "加载成功！", data);
        }
    }

    @ControllerLog(module = "系统日志管理", description = "查看详情")
    @PreAuthorize(value = "hasAuthority('/log/load')")
    @PostMapping("/load")
    @ResponseBody
    public ResponseData load(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params, "id");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效操作！");
        }
        SysLog obj = this.sysLogService.queryById(id);
        if (null != obj) {
            return new ResponseData("success", "加载成功！", obj);
        } else {
            return new ResponseData("failure", "加载失败！");
        }
    }

    @ControllerLog(module = "系统日志管理", description = "删除")
    @PreAuthorize(value = "hasAuthority('/log/delete')")
    @PostMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params, "id");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效操作！");
        }
        if (this.sysLogService.delete(id) > 0) {
            return new ResponseData("success", "删除成功！");
        } else {
            return new ResponseData("failure", "删除失败！");
        }
    }


    @ControllerLog(module = "系统日志管理", description = "批量删除")
    @PreAuthorize(value = "hasAuthority('/log/multidelete')")
    @PostMapping("/multidelete")
    @ResponseBody
    public ResponseData multidelete(@RequestBody Integer[] ids) {
        if (AppUtil.isEmpty(ids)) {
            return new ResponseData("error", "无效操作！");
        }
        if (this.sysLogService.deleteMulti(ids) > 0) {
            return new ResponseData("success", "批量删除成功！");
        } else {
            return new ResponseData("failure", "批量删除失败！");
        }
    }

    private JSONArray toArray(List<SysLog> objList) {
        JSONArray jsonArray = new JSONArray();
        for (SysLog obj : objList) {
            JSONObject jo = new JSONObject();
            jo.put("logId", obj.getLogId());
            jo.put("logModule", obj.getLogModule());
            jo.put("logContent", obj.getLogContent());
            jo.put("logTime", obj.getLogTime());
            jo.put("logUser", obj.getLogUser());
            jo.put("logIP", obj.getLogIp());
            jo.put("logFlag", obj.getLogFlag());
            jsonArray.add(jo);
        }
        return jsonArray;
    }


}
