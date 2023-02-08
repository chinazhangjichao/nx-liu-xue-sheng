package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.AdvertInfor;
import cn.zjc.app.bean.SysUser;
import cn.zjc.app.service.AdvertInforService;
import cn.zjc.app.utils.Constants;
import cn.zjc.app.utils.ImageUtil;
import cn.zjc.app.utils.ResponseData;
import cn.zjc.app.utils.AppUtil;
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
 * @decription: 广告管理
 * @date: 2022/1/19 13:32
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/advert")
public class AdvertInforController {

	@Resource
	private AdvertInforService advertInforService;

	@ControllerLog(module = "广告管理",description = "进入模块")
	@PreAuthorize(value = "hasAuthority('/advert/index')")
	@GetMapping(value = "/index")
	public String index(){
		return "advertindex";
	}


	@ControllerLog(module = "广告管理",description = "分页查询")
	@PreAuthorize(value = "hasAuthority('/advert/page')")
	@PostMapping("/page")
	@ResponseBody
	public ResponseData page(@RequestBody Map params) {
		Integer currentPage = AppUtil.getIntegerParam(params, "currentPage");
		Integer pageSize = AppUtil.getIntegerParam(params, "pageSize");
		Integer type = AppUtil.getIntegerParam(params, "searchType");
		String keyword= AppUtil.getStringParam(params, "keyword");
		JSONObject data = new JSONObject();
		data.put("currentPage", currentPage);
		data.put("pageSize", pageSize);
		long total = 0;
		if (currentPage == 1) {
			total = this.advertInforService.queryCount(type,keyword, null);
			data.put("total", total);
			if (total == 0) {
				return new ResponseData("failure", "无数据", data);
			}
		}
		List<AdvertInfor> objList = this.advertInforService.queryByPage(currentPage, pageSize, type, keyword,null);
		if (AppUtil.isEmpty(objList)) {
			return new ResponseData("failure", "无数据", data);
		} else {
			data.put("list", toArray(objList));
			return new ResponseData("success", "加载成功", data);
		}
	}

	@ControllerLog(module = "广告管理",description = "删除")
	@PreAuthorize(value = "hasAuthority('/advert/delete')")
	@PostMapping("/delete")
	@ResponseBody
	public ResponseData delete(@RequestBody Map params) {
		Integer id = AppUtil.getIntegerParam(params, "id");
		if (!AppUtil.isVaildId(id)) {
			return new ResponseData("error", "无效的ID！");
		}
		if (this.advertInforService.delete(id) == 1) {
			return new ResponseData("success", "删除成功！");
		} else {
			return new ResponseData("failure", "删除失败！");
		}
	}

	@ControllerLog(module = "广告管理",description = "批量删除")
	@PreAuthorize(value = "hasAuthority('/advert/multidelete')")
	@PostMapping("/multidelete")
	@ResponseBody
	public ResponseData multidelete(@RequestBody Integer[] ids) {
		if (AppUtil.isEmpty(ids)) {
			return new ResponseData("error", "无效操作！");
		}
		if (this.advertInforService.deleteMulti(ids) > 0) {
			return new ResponseData("success", "批量删除成功！");
		} else {
			return new ResponseData("failure", "批量删除失败！");
		}
	}

	@ControllerLog(module = "广告管理",description = "上下/下线")
	@PreAuthorize(value = "hasAuthority('/advert/status')")
	@PostMapping("/status")
	@ResponseBody
	public ResponseData status(@RequestBody Map params) {
		Integer id = AppUtil.getIntegerParam(params, "id");
		Integer status = AppUtil.getIntegerParam(params, "status");
		if (!AppUtil.isVaildId(id)) {
			return new ResponseData("error", "无效的ID！");
		}
		if (this.advertInforService.saveOrUpdate(new AdvertInfor(id, status)) == 1) {
			return new ResponseData("success", "更新成功！");
		} else {
			return new ResponseData("failure", "更新失败！");
		}
	}

	@ControllerLog(module = "广告管理",description = "查询详情")
	@PreAuthorize(value = "hasAuthority('/advert/load')")
	@PostMapping("/load")
	@ResponseBody
	public ResponseData load(@RequestBody Map params) {
		Integer id = AppUtil.getIntegerParam(params,"id");
		if (!AppUtil.isVaildId(id)) {
			return new ResponseData("error", "无效操作！");
		}
		AdvertInfor obj= this.advertInforService.queryById(id);
		if (null != obj) {
			JSONObject data = new JSONObject();
			data.put("obj", obj);
			return new ResponseData("success", "加载成功", data);
		} else {
			return new ResponseData("failure", "加载失败");
		}
	}

	@ControllerLog(module = "广告管理",description = "保存")
	@PreAuthorize(value = "hasAuthority('/advert/save')")
	@PostMapping("/save")
	@ResponseBody
	public ResponseData save(@RequestBody AdvertInfor obj,HttpSession session) {
		if (null == obj) {
			return new ResponseData("error", "无效操作！");
		}
		if (!StringUtils.hasLength(obj.getAdvertName())) {
			return new ResponseData("error", "名称不能为空！");
		}
		if (!StringUtils.hasLength(obj.getAdvertImg())) {
			return new ResponseData("error", "图片不能为空！");
		}
		if (null == obj.getAdvertType() || null == obj.getAdvertType().getTypeId()) {
			return new ResponseData("error", "类型不能为空！");
		}
		SysUser loginUser = (SysUser)session.getAttribute(Constants.LOGINUSER);
		obj.setModifyTime(System.currentTimeMillis());
		obj.setModifyUser(loginUser.getRealName() + "-" + loginUser.getUserId());
		if (this.advertInforService.saveOrUpdate(obj) == 1) {
			return new ResponseData("success", "保存成功！");
		} else {
			return new ResponseData("failure", "保存失败！");
		}
	}

	@ControllerLog(module = "广告管理",description = "上传图片")
	@PreAuthorize(value = "hasAuthority('/advert/upload')")
	@PostMapping("/upload")
	@ResponseBody
	public ResponseData uploadimage(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpSession session) {
		JSONObject json = new JSONObject();
		String realPath = request.getServletContext().getRealPath("/");
		String uploadPath = "/upload/adverts/";
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
			if (originalSize < 1*1024 * 1024) {
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
			data.put("fileName",request.getServletContext().getAttribute(Constants.WEBSITE)+ uploadPath  + fileName);
			return new ResponseData("success", "上传成功", data);
		} catch (Exception e) {
			return new ResponseData("failure", "上传失败：" + e.getMessage());
		}
	}

	/**
	 * 把集合变成json数组（只保留有用数据，减少json大小）
	 *
	 * @param objList
	 * @return
	 */
	private JSONArray toArray(List<AdvertInfor> objList) {
		JSONArray jsonArray = new JSONArray();
		for (AdvertInfor obj : objList) {
			JSONObject jo = new JSONObject();
			jo.put("advertId", obj.getAdvertId());
			jo.put("advertName", obj.getAdvertName());
			jo.put("advertImg", obj.getAdvertImg());
			jo.put("advertType", obj.getAdvertType());
			jo.put("advertUrl", obj.getAdvertUrl());
			jo.put("isOnline", obj.getIsOnline());
			jo.put("modifyTime", obj.getModifyTime());
			jo.put("modifyUser", obj.getModifyUser());
			jsonArray.add(jo);
		}
		return jsonArray;
	}
}
