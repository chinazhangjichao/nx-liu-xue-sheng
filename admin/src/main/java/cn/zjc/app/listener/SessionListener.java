package cn.zjc.app.listener;

import cn.zjc.app.bean.SysUser;
import cn.zjc.app.service.SysUserService;
import cn.zjc.app.utils.Constants;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
@WebListener
public class SessionListener implements HttpSessionListener {
	@Override
	public void sessionCreated(HttpSessionEvent event) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent e) {
		ApplicationContext act = ContextLoader.getCurrentWebApplicationContext();
		HttpSession session = e.getSession();
		if (null != session.getAttribute(Constants.LOGINUSER)) {
			SysUserService userService = (SysUserService) act.getBean("sysUserService");
			SysUser loginUser = (SysUser) session.getAttribute(Constants.LOGINUSER);
			loginUser.setLogoutTime(System.currentTimeMillis());
			userService.updateOffline(loginUser);
		}
	}

}
