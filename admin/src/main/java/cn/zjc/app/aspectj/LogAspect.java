package cn.zjc.app.aspectj;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.SysLog;
import cn.zjc.app.bean.SysUser;
import cn.zjc.app.service.SysLogService;
import cn.zjc.app.utils.Constants;
import cn.zjc.app.utils.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class LogAspect {
	@Resource
	private SysLogService sysLogService;

	/**
	 * 配置切入点（切 面 编 程）
	 */
	@Pointcut("@annotation(cn.zjc.app.annotation.ControllerLog)")
	public void pointcut() {

	}

	/**
	 * 配置正常通知
	 *
	 * @param joinPoint join point for advice
	 */
	@AfterReturning(pointcut = "pointcut()")
	public void afterReturning(JoinPoint joinPoint) throws Throwable {
		// 注解解析
		ControllerLog annotation = getAnnotation((ProceedingJoinPoint) joinPoint);
		HttpServletRequest request  = ServletUtil.getRequest();
		SysUser user = (SysUser) request.getSession().getAttribute(Constants.LOGINUSER);
		SysLog log  = new SysLog();
		log.setLogModule(annotation.module());
		log.setLogContent(annotation.description());
		log.setLogIp(ServletUtil.getIpAddr(request));
		if (null != user) {
			log.setLogUser(user.getRealName() + "-" + user.getUserId());
		} else {
			log.setLogUser("未登录");
		}
		log.setLogTime(System.currentTimeMillis());
		log.setRequestMethod(request.getMethod());
		log.setRequestParams(getParameterToJson((ProceedingJoinPoint) joinPoint));

		log.setControllerMethod(getMethodName(joinPoint));
		log.setBrowser(ServletUtil.getBrowser());
		log.setSystem(ServletUtil.getSystem());
		log.setLogFlag(1);
		this.sysLogService.save(log);
	}

	/**
	 * 配置异常通知
	 *
	 * @param joinPoint join point for advice
	 * @param e         exception
	 */
	@AfterThrowing(pointcut = "pointcut()", throwing = "e")
	public void afterThrowing(JoinPoint joinPoint, Throwable e) {
		// 注解解析
		// 注解解析
		ControllerLog annotation = getAnnotation((ProceedingJoinPoint) joinPoint);
		HttpServletRequest request  = ServletUtil.getRequest();
		SysUser user = (SysUser) request.getSession().getAttribute(Constants.LOGINUSER);
		SysLog log  = new SysLog();
		log.setLogModule(annotation.module());
		log.setLogContent(annotation.description());
		log.setLogIp(ServletUtil.getIpAddr(request));
		if (null != user) {
			log.setLogUser(user.getRealName() + "-" + user.getUserId());
		} else {
			log.setLogUser("未登录");
		}
		log.setLogTime(System.currentTimeMillis());
		log.setRequestMethod(request.getMethod());
		log.setRequestParams(getParameterToJson((ProceedingJoinPoint) joinPoint));

		log.setControllerMethod(getMethodName(joinPoint));
		log.setBrowser(ServletUtil.getBrowser());
		log.setSystem(ServletUtil.getSystem());
		log.setLogFlag(0);
		log.setException(this.getExceptionMessage(e));
		this.sysLogService.save(log);
	}

	/**
	 * 获得异常信息
	 * @param e
	 * @return
	 */
	public  String getExceptionMessage(Throwable e){
			StackTraceElement stackTraceElement = e.getStackTrace()[0];
			// 获取类名
			String className = stackTraceElement.getClassName();
			String filePath = stackTraceElement.getFileName();
			int lineNumber = stackTraceElement.getLineNumber();
			String methodName = stackTraceElement.getMethodName();
			return "类名:" + className + "，文件路径:" + filePath + ",行数:" + lineNumber + "方法名:" + methodName;
	}
	/**
	 * 获 取 注 解
	 */
	public ControllerLog getAnnotation(ProceedingJoinPoint point) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Class<? extends Object> targetClass = point.getTarget().getClass();
		ControllerLog targetLog = targetClass.getAnnotation(ControllerLog.class);
		if (targetLog != null) {
			return targetLog;
		} else {
			Method method = signature.getMethod();
			ControllerLog log = method.getAnnotation(ControllerLog.class);
			return log;
		}
	}

	/**
	 * 获取方法名
	 */
	public String getMethodName(JoinPoint point) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		// 方法路径
		return point.getTarget().getClass().getName()+"."+signature.getName()+"()";
	}

	/**
	 * 获 取 参数（转换json格式）
	 */
	public String getParameterToJson(ProceedingJoinPoint point) {
		List<Object> argList = new ArrayList<>();
		//参数值
		Object[] argValues = point.getArgs();
		//参数名称
		String[] argNames = ((MethodSignature)point.getSignature()).getParameterNames();
		if(argValues != null){
			for (int i = 0; i < argValues.length; i++) {
				if(argValues[i] instanceof HttpServletRequest || argValues[i] instanceof HttpServletResponse || argValues[i] instanceof HttpSession || argValues[i] instanceof MultipartFile){
					continue;
				}
				Map<String, Object> map = new HashMap<>();
				String key = argNames[i];
				map.put(key, argValues[i]);
				argList.add(map);
				map = null;
			}
		}
		if (argList.size() == 0) {
			return "";
		}
		return argList.size() == 1 ? JSONObject.toJSONString(argList.get(0))  : JSONObject.toJSONString(argList);
	}
}
