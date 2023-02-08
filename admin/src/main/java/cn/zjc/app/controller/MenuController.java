package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.Menu;
import cn.zjc.app.service.MenuService;
import cn.zjc.app.utils.AppUtil;
import cn.zjc.app.utils.Constants;
import cn.zjc.app.utils.ResponseData;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;


/**
 * @author ZJC
 * @decription: 网站菜单管理
 * @date: 2023/02/03 09:35
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/menu")
public class MenuController {

	@Autowired
	private MenuService menuService;

	@ControllerLog(module = "网站菜单管理",description = "进入模块")
	@PreAuthorize(value = "hasAuthority('/menu/index')")
	@GetMapping(value = "/index")
	public String index(){
		return "menuindex";
	}

	@ControllerLog(module = "网站菜单管理",description = "保存")
	@PreAuthorize(value = "hasAuthority('/menu/save')")
	@PostMapping("/save")
	@ResponseBody
	public ResponseData save(@RequestBody Menu obj) {
		if (null == obj) {
			return new ResponseData("error",  "无效操作！");
		}
		if (!StringUtils.hasLength(obj.getMenuName())) {
			return new ResponseData("error",  "名称不能为空！");
		}
		if (!StringUtils.hasLength(obj.getMenuEnglish())) {
			return new ResponseData("error",  "英文名称不能为空！");
		}
		if(null==obj.getMenuParent() ||  !AppUtil.isVaildId(obj.getMenuParent().getMenuId())) {
			obj.setMenuParent(new Menu(0));
		}
		if (this.menuService.saveOrUpdate(obj) == 1) {
			return new ResponseData("success", "保存成功！");
		} else {
			return new ResponseData("failure", "保存失败！");
		}
	}

	@ControllerLog(module = "网站菜单管理",description = "删除")
	@PreAuthorize(value = "hasAuthority('/menu/delete')")
	@PostMapping("/delete")
	@ResponseBody
	public ResponseData delete(@RequestBody Map params) {
		Integer id = AppUtil.getIntegerParam(params,"id");
		if (!AppUtil.isVaildId(id)) {
			return new ResponseData("error", "无效操作！");
		}
		if (this.menuService.delete(id) >0) {
			return new ResponseData("success", "操作成功！");
		} else {
			return new ResponseData("failure", "操作失败！");
		}
	}

	@ControllerLog(module = "网站菜单管理",description = "查询下级")
	@PreAuthorize(value = "hasAuthority('/menu/byparent')")
	@PostMapping("/byparent")
	@ResponseBody
	public ResponseData byparent(@RequestBody Map params) {
		Integer parent = AppUtil.getIntegerParam(params,"parent");
		if (!AppUtil.isVaildId(parent)) {
			return new ResponseData("error", "无效操作！");
		}
		List<Menu> objList = this.menuService.queryByParent(parent);
		if (null != objList && objList.size() > 0) {
			JSONObject data = new JSONObject();
			JSONArray ja = new JSONArray();
			for (Menu obj : objList) {
				JSONObject jo = new JSONObject();
				jo.put("menuId", obj.getMenuId());
				jo.put("menuName", obj.getMenuName());
				ja.add(jo);
			}
			data.put("list", ja);
			return new ResponseData("success", "加载成功", data);
		} else {
			return new ResponseData("failure", "加载失败");
		}
	}

	@ControllerLog(module = "网站菜单管理",description = "查询详情")
	@PreAuthorize(value = "hasAuthority('/menu/load')")
	@PostMapping("/load")
	@ResponseBody
	public ResponseData load(@RequestBody Map params) {
		Integer id = AppUtil.getIntegerParam(params,"id");
		if (!AppUtil.isVaildId(id)) {
			return new ResponseData("error", "无效操作！");
		}
		Menu obj= this.menuService.queryById(id);
		if (null != obj) {
			JSONObject data = new JSONObject();
			data.put("obj", obj);
			return new ResponseData("success", "加载成功", data);
		} else {
			return new ResponseData("failure", "加载失败");
		}
	}

	@ControllerLog(module = "网站菜单管理",description = "查询所有")
	@PreAuthorize(value = "hasAuthority('/menu/all')")
	@PostMapping("/all")
	@ResponseBody
	public ResponseData all() {
		List<Menu> objList = this.menuService.queryByParent(0);
		if (!AppUtil.isEmpty(objList)) {
			JSONObject data = new JSONObject();
			data.put("list", toArray(objList));
			return new ResponseData("success", "加载成功", data);
		} else {
			return new ResponseData("failure", "加载失败");
		}
	}



	@ControllerLog(module = "网站菜单管理",description = "上传图标")
	@PreAuthorize(value = "hasAuthority('/menu/save')")
	@PostMapping(value = "/upload")
	@ResponseBody
	public ResponseData uploadimage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		String realPath = request.getServletContext().getRealPath("/");
		String uploadPath = "/upload/menu/";
		File upload = new File(realPath + uploadPath);
		if (!upload.exists()) {
			upload.mkdirs();
		}
		long originalSize = file.getSize();
		if (originalSize > 200*1024) {
			return new ResponseData("error", "图片超过了最大限制200KB！");
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
			data.put("fileName", request.getServletContext().getAttribute(Constants.WEBSITE)+ uploadPath + fileName);
			return new ResponseData("success", "上传成功！", data);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseData("failure", "上传失败！");
		}
	}
	private JSONArray toArray(List<Menu> objList) {
		JSONArray jsonArray = new JSONArray();
		for (Menu obj : objList) {
			JSONObject jo = new JSONObject();
			jo.put("menuId", obj.getMenuId());
			jo.put("menuName", obj.getMenuName());
			jo.put("menuEnglish", obj.getMenuEnglish());
			jo.put("menuUrl", obj.getMenuUrl());
			jo.put("isMobile", obj.getIsMobile());
			jo.put("sortNum", obj.getSortNum());
			jo.put("menuIcon", obj.getMenuIcon());
			jo.put("menuParent", obj.getMenuParent());
			List<Menu> list =this.menuService.queryByParent(obj.getMenuId());
			if(!AppUtil.isEmpty(list)){
				jo.put("children", toArray(list));
			}
			jsonArray.add(jo);
		}
		return jsonArray;
	}

}
