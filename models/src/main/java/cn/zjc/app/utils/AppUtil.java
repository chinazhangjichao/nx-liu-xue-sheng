package cn.zjc.app.utils;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ZJC
 * @decription: 数字相关操作
 * @date: 2022/1/12 13:46
 * @since JDK 1.8
 */
public final class AppUtil {

    /**
     * 检查数字是否是有效ID
     * @param id
     * @return 如果数字不为空并且大于0，则返回true，否则返回false
     */
    public static  boolean isVaildId(Integer id){
        return null!=id && id>0;
    }

    /**
     * 检查数字是否是有效ID
     * @param id
     * @return 如果数字不为空并且大于0，则返回true，否则返回false
     */
    public static  boolean isVaildId(Long id){
        return null!=id && id>0;
    }

    /**
     * 判断集合是否为空
     * @param list
     * @return
     */
    public static  boolean isEmpty(List list){
        return null==list || list.isEmpty();
    }

    /**
     * 判断数组是否为空
     * @param array
     * @return
     */
    public static  boolean isEmpty(Object[] array){
        return null==array || array.length==0;
    }

    /**
     *
     * @param params
     * @param key
     * @return 如果读取不到，则返回null
     */
    public static Integer getIntegerParam(Map params, String key){
        return null==params.get(key)?null:(Integer) params.get(key);
    }

    /**
     * 根据key从map中读取整型数组参数
     * @param params
     * @param key
     * @return
     */
    public static List getListParams(Map params, String key){
        return null==params.get(key)?null:(List)params.get(key);
    }
    /**
     * 根据key从map中读取长整型参数
     * @param params
     * @param key
     * @return 如果读取不到，则返回null
     */
    public static Long getLongParam(Map params, String key){
        return null==params.get(key)?null:Long.valueOf(params.get(key).toString());

    }
    /**
     * 根据key从map中读取字符串参数
     * @param params
     * @param key
     * @return 如果读取不到，则返回null
     */
    public static String getStringParam(Map params, String key){
        return null==params.get(key)?null:(String) params.get(key);
    }

    /**
     * 获得文件扩展名
     * @param fileName
     * @return
     */
    public static  String getExtension(String fileName){
        if(!StringUtils.hasLength(fileName))
            throw  new IllegalArgumentException("无效的文件名称！");
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 返回指定格式的日期字符串
     * @param formatter
     * @return
     */
    public static String generateTimeStr(String formatter,Date date){
        return new SimpleDateFormat(formatter).format(date);
    }


}
