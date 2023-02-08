package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.SysDictionary;
import cn.zjc.app.service.SysDictionaryService;
import cn.zjc.app.utils.ResponseData;
import cn.zjc.app.utils.AppUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author ZJC
 * @decription: 数据字典管理
 * @date: 2022/1/14 9:24
 * @since JDK 1.8
 */
@RequestMapping("/dictionary")
@Controller
public class SysDictionaryController {

    @Autowired
    private SysDictionaryService sysDictionaryService;

    @ControllerLog(module = "数据字典管理",description = "进入模块")
    @PreAuthorize(value = "hasAuthority('/dictionary/index')")
    @GetMapping(value = "/index")
    public String index(){
        return "dictionaryindex";
    }

    @ControllerLog(module = "数据字典管理",description = "查询所有")
    @PreAuthorize(value = "hasAuthority('/dictionary/all')")
    @PostMapping("/all")
    @ResponseBody
    public ResponseData all(){
        List<SysDictionary> list = this.sysDictionaryService.queryAll();
        if(AppUtil.isEmpty(list)){
            JSONObject data = new JSONObject();
            data.put("list",list);
            return  new ResponseData("failure","数据不存在！",data);
        }else{
            JSONObject data = new JSONObject();
            data.put("list",list);
            return  new ResponseData("success","加载成功！",data);
        }
    }

    @ControllerLog(module = "数据字典管理",description = "查询详情")
    @PreAuthorize(value = "hasAuthority('/dictionary/load')")
    @PostMapping("/load")
    @ResponseBody
    public ResponseData key(@RequestBody Map params){
        String key = null==params.get("key")?"":(String) params.get("key");
        if(!StringUtils.hasLength(key)){
            return new ResponseData("error", "无效的字典参数！");
        }
        SysDictionary obj= this.sysDictionaryService.queryByKey(key);
        if(null==obj){
            return  new ResponseData("failure","参数不存在！");
        }else{
            JSONObject data = new JSONObject();
            data.put("obj",obj);
            return  new ResponseData("success","加载成功！",data);
        }
    }

    @ControllerLog(module = "数据字典管理",description = "保存")
    @PreAuthorize(value = "hasAuthority('/dictionary/save')")
    @PostMapping("/save")
    @ResponseBody
    public ResponseData save(@RequestBody SysDictionary obj, HttpServletRequest request) {
        if (null==obj) {
            return new ResponseData("error", "参数不正确！");
        }
        if(!StringUtils.hasLength(obj.getDicKey())){
            return new ResponseData("error", "无效的字典参数！");
        }
        if(!StringUtils.hasLength(obj.getDicValue())){
            return new ResponseData("error", obj.getDicKey()+"的字典值无效！");
        }
        if (this.sysDictionaryService.saveOrUpdate(obj) > 0) {
            request.getServletContext().setAttribute(obj.getDicKey(),obj.getDicValue());
            return new ResponseData("success", "操作成功！");
        } else {
            return new ResponseData("failure", "操作失败！");
        }
    }

    @ControllerLog(module = "数据字典管理",description = "删除")
    @PreAuthorize(value = "hasAuthority('/dictionary/delete')")
    @PostMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestBody Map params,HttpServletRequest request) {
        Integer id = null==params.get("id")?0:(Integer) params.get("id");
        if (null==id) {
            return new ResponseData("error", "无效的字典！");
        }
        SysDictionary dictionary = this.sysDictionaryService.queryById(id);
        if(null==dictionary){
            return new ResponseData("error", "无效的字典！");
        }
        if (this.sysDictionaryService.delete(id) > 0) {
            request.getServletContext().removeAttribute(dictionary.getDicKey());
            return new ResponseData("success", "删除成功！");
        } else {
            return new ResponseData("failure", "删除失败！");
        }
    }

}
