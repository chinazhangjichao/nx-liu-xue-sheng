package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.AdvertType;
import cn.zjc.app.service.AdvertTypeService;
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
 * @decription: 广告类别管理
 * @date: 2022/1/19 13:35
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/adverttype")
public class AdvertTypeController {

	@Autowired
	private AdvertTypeService advertTypeService;
	@ControllerLog(module = "广告类别管理",description = "进入模块")
	@PreAuthorize(value = "hasAuthority('/adverttype/index')")
	@GetMapping(value = "/index")
	public String index(){
		return "adverttypeindex";
	}

	@ControllerLog(module = "广告类别管理",description = "保存")
	@PreAuthorize(value = "hasAuthority('/adverttype/save')")
	@PostMapping("/save")
	@ResponseBody
	public ResponseData save(@RequestBody AdvertType obj) {
		if (null == obj) {
			return new ResponseData("error",  "无效操作！");
		}
		if (!StringUtils.hasLength(obj.getTypeName())) {
			return new ResponseData("error",  "类别名称不能为空！");
		}
		if (this.advertTypeService.saveOrUpdate(obj) == 1) {
			JSONObject data = new JSONObject();
			data.put("obj",obj);
			return new ResponseData("success", "保存成功！",data);
		} else {
			return new ResponseData("failure", "保存失败！");
		}
	}

	@ControllerLog(module = "广告类别管理",description = "删除")
	@PreAuthorize(value = "hasAuthority('/adverttype/delete')")
	@PostMapping("/delete")
	@ResponseBody
	public ResponseData delete(@RequestBody Map params) {
		Integer id = AppUtil.getIntegerParam(params,"id");
		if (!AppUtil.isVaildId(id)) {
			return new ResponseData("error", "无效操作！");
		}
		if (this.advertTypeService.delete(id) >0) {
			return new ResponseData("success", "操作成功！");
		} else {
			return new ResponseData("failure", "操作失败！");
		}
	}

	@ControllerLog(module = "广告类别管理",description = "查询所有")
	@PreAuthorize(value = "hasAuthority('/adverttype/all')")
	@PostMapping("/all")
	@ResponseBody
	public ResponseData all() {
		List<AdvertType> objList = this.advertTypeService.queryAll();
		if (!AppUtil.isEmpty(objList)) {
			JSONObject data = new JSONObject();
			data.put("list", objList);
			return new ResponseData("success", "加载成功", data);
		} else {
			return new ResponseData("failure", "加载失败");
		}
	}

}
