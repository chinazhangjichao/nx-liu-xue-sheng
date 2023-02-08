package cn.zjc.app.listener;

import cn.zjc.app.bean.SysDictionary;
import cn.zjc.app.service.SysDictionaryService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

@WebListener
public class StartListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//加载系统参数
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		if(null!=wac){
			SysDictionaryService dicService = (SysDictionaryService) wac.getBean("sysDictionaryService");
			List<SysDictionary> list = dicService.queryAll();
			if (null != list && list.size() > 0) {
				for (SysDictionary dic : list) {
					sce.getServletContext().setAttribute(dic.getDicKey(), dic.getDicValue());
				}
			}
		}

	}
}
