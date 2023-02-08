package cn.zjc.app.utils;

import com.alibaba.fastjson.JSONObject;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author ZJC
 * @decription: 数字相关操作
 * @date: 2022/1/12 13:46
 * @since JDK 1.8
 */
public final class ServletUtil {
    private static final UserAgentAnalyzer userAgentAnalyzer = UserAgentAnalyzer
            .newBuilder()
            .hideMatcherLoadStats()
            .withCache(10000)
            .withField(UserAgent.AGENT_NAME_VERSION)
            .build();

    public static void responseJson(HttpServletResponse response, HttpStatus status, Object data) {
        PrintWriter out = null;
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(status.value());
            out = response.getWriter();
            out.write(JSONObject.toJSONString(data));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) out.close();
        }
    }
    /**
     * 获得IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                try {
                    ipAddress = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 获取 HttpServletRequest 对象
     *
     * @return {@link HttpServletRequest}
     * */
    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * Request 请求方法(类型)
     *
     * @return {@link String}
     * */
    public static String getMethod(){
        return getRequest().getMethod();
    }

    /**
     * Request 请求头
     *
     * @param name 名称
     * @return {@link String}
     * */
    public static String getHeader(String name){
        return getRequest().getHeader(name);
    }

    /**
     * Request Agent
     *
     * @return {@link String}
     * */
    private static String getAgent(){
        return getHeader("User-Agent");
    }

    /**
     * Request 浏览器类型
     *
     * @return {@link String}
     * */
    public static String getBrowser(){
        String browser = "";
        String userAgent = getAgent();
        if (userAgent.contains("Firefox")) browser = "火狐浏览器";
        else if (userAgent.contains("Chrome")) browser = "谷歌浏览器";
        else if (userAgent.contains("Trident")) browser = "IE 浏览器";
        else browser = "未知浏览器";
        UserAgent.ImmutableUserAgent parse = userAgentAnalyzer.parse(userAgent);
        String value = parse.get(UserAgent.AGENT_NAME_VERSION).getValue();
        return browser + "(" + value + ")";
    }

    /**
     * Request 访问来源 ( 客户端类型 )
     *
     * @return {@link String}
     * */
    public static String getSystem(){
        String userAgent = getAgent();
        if (getAgent().toLowerCase().contains("windows" )) return "Windows";
        else if (userAgent.toLowerCase().contains("mac" )) return "Mac";
        else if (userAgent.toLowerCase().contains("x11" )) return "Unix";
        else if (userAgent.toLowerCase().contains("android" )) return "Android";
        else if (userAgent.toLowerCase().contains("iphone" )) return "IPhone";
        else return "UnKnown, More-Info: " + userAgent;
    }


}
