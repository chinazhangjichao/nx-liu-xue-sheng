package cn.zjc.app.interceptor;

import cn.zjc.app.utils.Constants;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RoleRightInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		System.out.println("===============================================");
		System.out.println("...............interceptor..............");
		System.out.println("===============================================");
		HttpSession session = request.getSession();
		String contextPath = request.getContextPath();
		String uri = request.getRequestURI();
		uri = uri.substring(contextPath.length());
		if ("/admin/login".equalsIgnoreCase(uri) || "/admin/dologin".equalsIgnoreCase(uri) || "/admin/logout".equalsIgnoreCase(uri)) {
			// 登录与退出/对外接口 直接放行
			return true;
		} else {
			// 验证登录
			if (null == session.getAttribute(Constants.LOGINUSER)) {
				response.sendRedirect(request.getContextPath() + "/admin/login");
				return false;
			}
			return true;
		}
	}

}
