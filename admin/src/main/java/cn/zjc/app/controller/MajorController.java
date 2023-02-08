package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.Major;
import cn.zjc.app.bean.SysUser;
import cn.zjc.app.service.MajorService;
import cn.zjc.app.utils.AppUtil;
import cn.zjc.app.utils.ResponseData;
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
 * @decription: 专业管理
 * @date: 2023/02/03 13:35
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/major")
public class MajorController {

	@Autowired
	private MajorService majorService;
	
	@ControllerLog(module = "专业管理",description = "进入模块")
	@PreAuthorize(value = "hasAuthority('/major/index')")
	@GetMapping(value = "/index")
	public String index(){
		return "majorindex";
	}

	@ControllerLog(module = "专业管理",description = "保存")
	@PreAuthorize(value = "hasAuthority('/major/save')")
	@PostMapping("/save")
	@ResponseBody
	public ResponseData save(@RequestBody Major obj) {
		if (null == obj) {
			return new ResponseData("error",  "无效操作！");
		}
		if (!StringUtils.hasLength(obj.getMajorName())) {
			return new ResponseData("error",  "名称不能为空！");
		}
		if (!StringUtils.hasLength(obj.getMajorEnglish())) {
			return new ResponseData("error",  "英文名称不能为空！");
		}
		if (this.majorService.saveOrUpdate(obj) == 1) {
			JSONObject data = new JSONObject();
			data.put("obj",obj);
			return new ResponseData("success", "保存成功！",data);
		} else {
			return new ResponseData("failure", "保存失败！");
		}
	}

	@ControllerLog(module = "专业管理",description = "删除")
	@PreAuthorize(value = "hasAuthority('/major/delete')")
	@PostMapping("/delete")
	@ResponseBody
	public ResponseData delete(@RequestBody Map params) {
		Integer id = AppUtil.getIntegerParam(params,"id");
		if (!AppUtil.isVaildId(id)) {
			return new ResponseData("error", "无效操作！");
		}
		if (this.majorService.delete(id) >0) {
			return new ResponseData("success", "操作成功！");
		} else {
			return new ResponseData("failure", "操作失败！");
		}
	}

	@ControllerLog(module = "专业管理",description = "启用/停用")
	@PreAuthorize(value = "hasAuthority('/major/status')")
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
		if (this.majorService.saveOrUpdate(new Major(id, status))>0) {
			return new ResponseData("success", "操作成功！");
		} else {
			return new ResponseData("failure", "操作失败！");
		}
	}

	@ControllerLog(module = "专业管理",description = "查询所有")
	@PreAuthorize(value = "hasAuthority('/major/all')")
	@PostMapping("/all")
	@ResponseBody
	public ResponseData all(Integer status) {
		List<Major> objList = this.majorService.queryAll(status);
		if (!AppUtil.isEmpty(objList)) {
			JSONObject data = new JSONObject();
			data.put("list", objList);
			return new ResponseData("success", "加载成功", data);
		} else {
			return new ResponseData("failure", "加载失败");
		}
	}

}
