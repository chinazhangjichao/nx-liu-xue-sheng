package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.SysUser;
import cn.zjc.app.service.SysUserService;
import cn.zjc.app.utils.Constants;
import cn.zjc.app.utils.Endecrypt;
import cn.zjc.app.utils.ResponseData;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Calendar;

/**
 * 后台：系统首页
 * 
 * @author ZJC
 * 
 */
@Controller
public class IndexController {
	@Autowired
	private SysUserService sysUserService;

	@ControllerLog(module = "系统首页",description = "进入模块")
	@GetMapping(value = "/index")
	public String index(Model model) {
		return "index";
	}

	@ControllerLog(module = "个人密码管理",description = "进入模块")
	@GetMapping(value = "/changepwd")
	public String changepwd(Model model) {
		return "changepwd";
	}


	@ControllerLog(module = "个人密码管理",description = "修改密码")
	@ResponseBody
	@PostMapping("/changepwd")
	public ResponseData changepwdsave(String oldPwd, String newPwd, String confirmPwd, HttpSession session) {
		SysUser user = (SysUser) session.getAttribute(Constants.LOGINUSER);
		if (!StringUtils.hasLength(oldPwd)) {
			return new ResponseData("error", "请填写原密码！");
		}

		if (!StringUtils.hasLength(newPwd)) {
			return new ResponseData("error", "请填写新密码！");
		}
		if (!confirmPwd.equals(newPwd)) {
			return new ResponseData("error", "两次输入的密码不一致！");
		}
		if (!user.getUserPwd().equalsIgnoreCase(Endecrypt.md5(oldPwd))) {
			return new ResponseData("error", "原密码验证失败！");
		}
		user.setUserPwd(Endecrypt.md5(newPwd));
		if (this.sysUserService.saveOrUpdate(user) == 1) {
			return new ResponseData("success", "密码修改成功！");
		} else {
			return new ResponseData("failure", "密码修改失败！");
		}
	}

	@GetMapping(value = "/norole")
	public String norole(Model model) {
		return "norole";
	}

	@GetMapping(value = "/calendar")
	public String canlendar(Model model) {
		return "calendar";
	}

	@GetMapping(value = "/welcome")
	public String welcome(Model model) {
		return "welcome";
	}

	@ResponseBody
	@RequestMapping(value = "/loadstatistic")
	public ResponseData loadstatistic(HttpSession session, @RequestParam(defaultValue = "0") Integer timeType) {
		Long startTime = 0L;
		Long endTime = 0L;

		if (timeType > 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			if (timeType == 1) { // 当天 startTime = calendar.getTimeInMillis();
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				endTime = calendar.getTimeInMillis();
			} else if (timeType == 2) { // 当月 calendar.add(Calendar.DAY_OF_MONTH, 1);
				endTime = calendar.getTimeInMillis();
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				startTime = calendar.getTimeInMillis();
			} else if (timeType == 3) { // 当年
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				endTime = calendar.getTimeInMillis();
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.MONTH, 0);
				startTime = calendar.getTimeInMillis();
			}

		}

		JSONObject data = new JSONObject();
		data.put("now",System.currentTimeMillis());
		return new ResponseData("success", "加载成功", data);
	}

}
