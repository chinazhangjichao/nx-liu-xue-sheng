package cn.zjc.app.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;


/**
 * @author ZJC
 * @decription: 系统日志信息
 * @date: 2022/1/11 15:13
 * @since JDK 1.8
 */
@Data
@ToString
@AllArgsConstructor
public class SysLog implements Serializable {

    private static final long serialVersionUID = 8244761354511210623L;
    private Long logId;
    private Long logTime;
    private String logContent;
    private String logUser;
    private String logModule;
    private String logIp;
    private String controllerMethod;
    private String requestMethod;
    private String requestParams;
    private String system;
    private String browser;
    private String exception;
    /**
     * 操作标识0失败 1成功
     */
    private Integer logFlag;

    public SysLog() {
    }
}
