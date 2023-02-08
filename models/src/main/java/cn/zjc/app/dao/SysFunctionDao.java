package cn.zjc.app.dao;

import cn.zjc.app.bean.SysFunction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author ZJC
 * @decription: 系统功能数据库操作接口
 * @date: 2022/1/11 14:58
 * @since JDK 1.8
 */
@Mapper
public interface SysFunctionDao {

    /**
     * 查询所有
     * 
     * @return
     */
    public List<SysFunction> queryAll();

    /**
     * 根据多个functionId查询
     * @param ids
     * @return
     */
    public List<SysFunction> queryByIds(@Param("ids") String[] ids);

}
