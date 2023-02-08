package cn.zjc.app.dao;

import cn.zjc.app.bean.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统日志数据库操作
 * 
 * @author ZJC
 * 
 */
@Mapper
public interface SysLogDao {
    /**
     * 查询记录总数
     * 
     * @return
     */
    public int queryCount(@Param("user") String user,@Param("module") String module,@Param("content") String content,@Param("status") Integer status, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 查询所有
     * 
     * @return
     */
    public List<SysLog> queryByPage(@Param("start") int start, @Param("pageSize") int pageSize, @Param("user") String user,@Param("module") String module,@Param("content") String content, @Param("status") Integer status,@Param("startTime") Long startTime,
                                    @Param("endTime") Long endTime);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysLog queryById(Integer id);

    /**
     * 保存信息
     * 
     * @param log
     * @return
     */
    public int save(SysLog log);

    /**
     * 删除日志
     * 
     * @param id
     * @return
     */
    public int delete(@Param("id") int id);

    /**
     * 批量删除日志
     * 
     * @param ids
     * @return
     */
    public int deleteMulti(@Param("ids") Integer[] ids);

}
