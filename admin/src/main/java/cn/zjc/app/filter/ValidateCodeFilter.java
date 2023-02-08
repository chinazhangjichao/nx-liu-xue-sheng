package cn.zjc.app.filter;

import cn.zjc.app.utils.Constants;
import cn.zjc.app.utils.ResponseData;
import cn.zjc.app.utils.ServletUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ZJC
 * @decription:
 * @date: 2022-08-22 14:45
 * @since JDK 1.8
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if("/login".equals(request.getRequestURI()) && "post".equalsIgnoreCase(request.getMethod())){
            //校验验证码
            String imgCode = request.getParameter("imgCode");
            if(!StringUtils.hasLength(imgCode)){
                ServletUtil.responseJson(response, HttpStatus.OK,new ResponseData("failure","验证码不能为空！"));
                return;
            }
            String serverCode = (String)request.getSession().getAttribute(Constants.IMGCODE);
            if(!imgCode.equalsIgnoreCase(serverCode)){
                ServletUtil.responseJson(response, HttpStatus.OK,new ResponseData("failure","验证码不正确！"));
                return;
            }
            request.getSession().removeAttribute(Constants.IMGCODE);
        }
        filterChain.doFilter(request, response);

    }
}
