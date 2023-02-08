package cn.zjc.app.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 服务器响应数据
 *
 * @author 张吉超
 * @package cn.zjc.app
 * @date 2020-04-16 18:39
 * @copyright © 349655468@qq.com
 */
@Data
@ToString
public class ResponseData {
    private String code;
    private String message;
    private Object data;

    public ResponseData() {
    }

    public ResponseData(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseData(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }



}
