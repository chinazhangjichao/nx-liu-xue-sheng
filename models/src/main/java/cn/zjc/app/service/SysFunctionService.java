package cn.zjc.app.service;

import cn.zjc.app.bean.SysFunction;

import java.util.List;

/**
 * @author ZJC
 * @decription: 系统功能业务操作接口
 * @date: 2022/1/12 13:18
 * @since JDK 1.8
 */
public interface SysFunctionService {
    /**
     * 查询所有
     * 
     * @return
     */
    public List<SysFunction> queryAll();

}
