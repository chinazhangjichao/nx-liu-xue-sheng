package cn.zjc.app.exception;

import cn.zjc.app.utils.ResponseData;
import cn.zjc.app.utils.ServletUtil;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 授权失败异常
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(AuthTokenException.class)
    public ResponseData authTokenExceptionHandler(AuthTokenException ex){
        return new ResponseData(HttpStatus.FORBIDDEN.value() + "",ex.getMessage());
    }

    /**
     * security访问被拒绝
     * @param ex
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Object accessDeniedExceptionHandler(AccessDeniedException ex, HttpServletRequest request, HttpServletResponse response){
        //判断请求是否为Ajax请求
        if (isAjax(request)) {
            ServletUtil.responseJson(response,HttpStatus.OK,new ResponseData(HttpStatus.FORBIDDEN.value() + "","权限不足！"));
            return null;
        } else {
            return "/norole";
        }
    }

    /**
     * 请求参数不正确异常
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseData illegalArgumentExceptionHandler(IllegalArgumentException ex){
        return new ResponseData(HttpStatus.BAD_REQUEST.value() + "",ex.getMessage());
    }
    /**
     * 对象属性封装不正确
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({InvalidPropertyException.class})
    public ResponseData illegalArgumentExceptionHandler(InvalidPropertyException ex){
        return new ResponseData(HttpStatus.BAD_REQUEST.value() + "",ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({ MissingServletRequestParameterException.class, HttpMessageNotReadableException.class,
            UnsatisfiedServletRequestParameterException.class, MethodArgumentTypeMismatchException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData badRequestException(Exception exception) {
        return new ResponseData(HttpStatus.BAD_REQUEST.value() + "", exception.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData exception(Throwable throwable) {
        throwable.printStackTrace();
        return new ResponseData(HttpStatus.INTERNAL_SERVER_ERROR.value()+"",throwable.getMessage());
    }

    public boolean isAjax(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null &&
                "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()))
                || request.getHeader("Content-Type").contains("multipart/form-data")
                ;
    }



}
