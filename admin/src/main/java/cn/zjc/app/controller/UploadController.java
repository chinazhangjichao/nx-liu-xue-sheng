package cn.zjc.app.controller;

import cn.zjc.app.utils.Constants;
import cn.zjc.app.utils.ResponseData;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RequestMapping("/admin")
@Controller("adminUploadController")
public class UploadController {
	private String[] acceptImageType = { ".jpg", ".jpeg", ".png" };
	private String[] acceptVideoType = { ".mp4", ".mp3" };
	private String[] acceptFileType = { ".jpg", ".jpeg", ".png", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf" };

	private boolean checkImage(String ext) {
		for (String type : acceptImageType) {
			if (type.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkVideo(String ext) {
		for (String type : acceptVideoType) {
			if (type.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkFile(String ext) {
		for (String type : acceptFileType) {
			if (type.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}

	@RequestMapping(value = "/uploadimage")
	@ResponseBody
	public ResponseData uploadimage(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpSession session) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String realPath = request.getServletContext().getRealPath("/");
		String uploadPath = "/upload/" + df.format(new Date());
		File uploadDir = new File(realPath + uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		String originalName = file.getOriginalFilename();
		String ext = originalName.substring(originalName.lastIndexOf("."));
		if (!this.checkImage(ext)) {
			return new ResponseData("error", "只能上传jpg、jpeg、png格式的文件！");
		}
		String fileName = UUID.randomUUID() + ext;
		// 保存
		try {
			File targetFile = new File(uploadDir, fileName);
			file.transferTo(targetFile);
			JSONObject data = new JSONObject();
			data.put("fileName", request.getServletContext().getAttribute(Constants.WEBSITE) + uploadPath + "/" + fileName);
			return new ResponseData("success", "上传成功！", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseData("error", "上传失败！");
	}

	@RequestMapping(value = "/uploadvideo")
	@ResponseBody
	public ResponseData uploadvideo(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpSession session) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String realPath = request.getServletContext().getRealPath("/");
		String uploadPath = "/upload/" + df.format(new Date());
		File uploadDir = new File(realPath + uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		String originalName = file.getOriginalFilename();
		String ext = originalName.substring(originalName.lastIndexOf("."));
		if (!this.checkVideo(ext)) {
			return new ResponseData("error", "只能上传mp4、mp3格式的文件！");
		}
		String fileName = UUID.randomUUID() + ext;
		// 保存
		try {
			File targetFile = new File(uploadDir, fileName);
			file.transferTo(targetFile);
			JSONObject data = new JSONObject();
			data.put("fileName", request.getServletContext().getAttribute(Constants.WEBSITE) + uploadPath + "/" + fileName);
			return new ResponseData("success", "上传成功！", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseData("error", "上传失败！");
	}

	@RequestMapping(value = "/uploadfile")
	@ResponseBody
	public ResponseData uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpSession session) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String realPath = request.getServletContext().getRealPath("/");
		String uploadPath = "/upload/" + df.format(new Date());
		File uploadDir = new File(realPath + uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		String originalName = file.getOriginalFilename();
		String ext = originalName.substring(originalName.lastIndexOf("."));
		if (!this.checkFile(ext)) {
			return new ResponseData("error", "只能上传jpg、jpeg、png、doc、xls、ppt、pdf格式的文件！");
		}
		String fileName = UUID.randomUUID() + ext;
		// 保存
		try {
			File targetFile = new File(uploadDir, fileName);
			file.transferTo(targetFile);
			JSONObject data = new JSONObject();
			data.put("fileName", request.getServletContext().getAttribute(Constants.WEBSITE) + uploadPath + "/" + fileName);
			return new ResponseData("success", "上传成功！", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseData("error", "上传失败！");
	}

}
