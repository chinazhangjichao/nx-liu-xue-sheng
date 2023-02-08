package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.ArticleType;
import cn.zjc.app.service.ArticleTypeService;
import cn.zjc.app.utils.Constants;
import cn.zjc.app.utils.ResponseData;
import cn.zjc.app.utils.AppUtil;
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
 * @decription: 文章类别管理
 * @date: 2022/1/19 13:35
 * @since JDK 1.8
 */
@Controller("adminArticleTypeController")
@RequestMapping("/articletype")
public class ArticleTypeController {

	@Autowired
	private ArticleTypeService articleTypeService;

	@ControllerLog(module = "文章类别管理",description = "进入模块")
	@PreAuthorize(value = "hasAuthority('/articletype/index')")
	@GetMapping(value = "/index")
	public String index(){
		return "articletypeindex";
	}

	@ControllerLog(module = "文章类别管理",description = "保存")
	@PreAuthorize(value = "hasAuthority('/articletype/save')")
	@PostMapping("/save")
	@ResponseBody
	public ResponseData save(@RequestBody ArticleType obj) {
		if (null == obj) {
			return new ResponseData("error",  "无效操作！");
		}
		if (!Pattern.matches("[1-9]\\d",obj.getTypeNo().toString())) {
			return new ResponseData("error",  "类别编号只能为两位数字且不能以0开头！");
		}
		if (!StringUtils.hasLength(obj.getTypeName())) {
			return new ResponseData("error",  "类别名称不能为空！");
		}
		if(null==obj.getTypeParent() ||  !AppUtil.isVaildId(obj.getTypeParent().getTypeId())) {
			obj.setTypeParent(new ArticleType(0));
		}
		if(null!=obj.getTypeParent().getTypeNo() && obj.getTypeParent().getTypeNo()>0){
			obj.setTypeNo(Integer.valueOf(obj.getTypeParent().getTypeNo()+""+obj.getTypeNo()));
		}
		if (this.articleTypeService.saveOrUpdate(obj) == 1) {
			return new ResponseData("success", "保存成功！");
		} else {
			return new ResponseData("failure", "保存失败！");
		}
	}

	@ControllerLog(module = "文章类别管理",description = "删除")
	@PreAuthorize(value = "hasAuthority('/articletype/delete')")
	@PostMapping("/delete")
	@ResponseBody
	public ResponseData delete(@RequestBody Map params) {
		Integer id = AppUtil.getIntegerParam(params,"id");
		if (!AppUtil.isVaildId(id)) {
			return new ResponseData("error", "无效操作！");
		}
		if (this.articleTypeService.delete(id) >0) {
			return new ResponseData("success", "操作成功！");
		} else {
			return new ResponseData("failure", "操作失败！");
		}
	}

	@ControllerLog(module = "文章类别管理",description = "查询下级")
	@PreAuthorize(value = "hasAuthority('/articletype/byparent')")
	@PostMapping("/byparent")
	@ResponseBody
	public ResponseData byparent(@RequestBody Map params) {
		Integer parent = AppUtil.getIntegerParam(params,"parent");
		if (!AppUtil.isVaildId(parent)) {
			return new ResponseData("error", "无效操作！");
		}
		List<ArticleType> objList = this.articleTypeService.queryByParent(parent);
		if (null != objList && objList.size() > 0) {
			JSONObject data = new JSONObject();
			JSONArray ja = new JSONArray();
			for (ArticleType obj : objList) {
				JSONObject jo = new JSONObject();
				jo.put("typeId", obj.getTypeId());
				jo.put("typeName", obj.getTypeName());
				ja.add(jo);
			}
			data.put("list", ja);
			return new ResponseData("success", "加载成功", data);
		} else {
			return new ResponseData("failure", "加载失败");
		}
	}

	@ControllerLog(module = "文章类别管理",description = "查询详情")
	@PreAuthorize(value = "hasAuthority('/articletype/load')")
	@PostMapping("/load")
	@ResponseBody
	public ResponseData load(@RequestBody Map params) {
		Integer id = AppUtil.getIntegerParam(params,"id");
		if (!AppUtil.isVaildId(id)) {
			return new ResponseData("error", "无效操作！");
		}
		ArticleType obj= this.articleTypeService.queryById(id);
		if (null != obj) {
			JSONObject data = new JSONObject();
			data.put("obj", obj);
			return new ResponseData("success", "加载成功", data);
		} else {
			return new ResponseData("failure", "加载失败");
		}
	}

	@ControllerLog(module = "文章类别管理",description = "查询所有")
	@PreAuthorize(value = "hasAuthority('/articletype/all')")
	@PostMapping("/all")
	@ResponseBody
	public ResponseData all() {
		List<ArticleType> objList = this.articleTypeService.queryByParent(0);
		if (!AppUtil.isEmpty(objList)) {
			JSONObject data = new JSONObject();
			data.put("list", toArray(objList));
			return new ResponseData("success", "加载成功", data);
		} else {
			return new ResponseData("failure", "加载失败");
		}
	}



	@ControllerLog(module = "文章类别管理",description = "上传图标")
	@PreAuthorize(value = "hasAuthority('/articletype/upload')")
	@PostMapping(value = "/upload")
	@ResponseBody
	public ResponseData uploadimage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		String realPath = request.getServletContext().getRealPath("/");
		String uploadPath = "/upload/articletype/";
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
	private JSONArray toArray(List<ArticleType> objList) {
		JSONArray jsonArray = new JSONArray();
		for (ArticleType obj : objList) {
			JSONObject jo = new JSONObject();
			jo.put("typeId", obj.getTypeId());
			jo.put("typeName", obj.getTypeName());
			jo.put("typeNo", obj.getTypeNo());
			jo.put("typeImg", obj.getTypeImg());
			jo.put("typeParent", obj.getTypeParent());
			List<ArticleType> list =this.articleTypeService.queryByParent(obj.getTypeId());
			if(!AppUtil.isEmpty(list)){
				jo.put("children", toArray(list));
			}
			jsonArray.add(jo);
		}
		return jsonArray;
	}

}
