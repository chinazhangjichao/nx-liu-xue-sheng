package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.SysFunction;
import cn.zjc.app.service.SysFunctionService;
import cn.zjc.app.utils.ResponseData;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZJC
 * @decription: 系统功能管理
 * @date: 2022/1/15 14:14
 * @since JDK 1.8
 */
@RequestMapping("/function")
@RestController
public class SysFunctionController {

    @Autowired
    private SysFunctionService sysFunctionService;

    @ControllerLog(module = "系统功能管理",description = "加载功能树")
    @PreAuthorize(value = "hasAuthority('/roleright/save')")
    @PostMapping("/tree")
    public ResponseData tree() {
        ResponseData result = new ResponseData();
        List<SysFunction> list = this.sysFunctionService.queryAll();
        if (null != list && list.size() > 0) {
            result.setCode("success");
            result.setMessage("加载成功！");
            JSONObject data = new JSONObject();
            JSONArray ja = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                SysFunction obj = list.get(i);
                // 保存顶级部门
                if (null == obj.getFunParent()) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", obj.getFunId());
                    jo.put("label", obj.getFunName());
                    jo.put("children", treeChild(list, obj.getFunId()));
                    ja.add(jo);
                }
            }
            data.put("list", ja);
            result.setData(data);
        } else {
            result.setCode("failure");
            result.setMessage("加载失败，无数据！");
        }
        return result;
    }

    // 递归生成子节点
    private JSONArray treeChild(List<SysFunction> list, Integer parent) {
        JSONArray ja = new JSONArray();
        if (null == parent || parent <= 0)
            return ja;
        for (SysFunction o : list) {
            if (null != o.getFunParent() && o.getFunParent().equals(parent)) {
                JSONObject jo = new JSONObject();
                jo.put("id", o.getFunId());
                jo.put("label", o.getFunName());
                jo.put("children", treeChild(list, o.getFunId()));
                ja.add(jo);
            }
        }
        return ja;
    }
}
