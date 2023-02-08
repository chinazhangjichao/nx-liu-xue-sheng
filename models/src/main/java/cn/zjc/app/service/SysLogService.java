package cn.zjc.app.service;

import cn.zjc.app.bean.SysLog;

import java.util.List;

/**
 * @author ZJC
 * @decription: 系统日志业务操作接口
 * @date: 2022/1/12 13:22
 * @since JDK 1.8
 */
public interface SysLogService {

    /**
     * 根据条件查询总记录数
     *
     * @param user        操作人
     * @param module      操作模块
     * @param content 操作内容
     * @param startTime   操作起始时间
     * @param endTime     操作截止时间
     * @return
     */
    public int queryCount(String user, String module, String content, Integer status,Long startTime, Long endTime);

    /**
     * 分页查询
     *
     * @param pageNo      页码
     * @param pageSize    每页大小
     * @param user        操作人
     * @param module      操作模块
     * @param content 操作内容
     * @param startTime   起始时间
     * @param endTime     截止时间
     * @return
     */
    public List<SysLog> queryByPage(int pageNo, int pageSize, String user, String module, String content,Integer status, Long startTime, Long endTime);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    SysLog queryById(Integer id);
    /**
     * 保存或更新
     *
     * @param obj
     * @return
     */
    public int save(SysLog obj);

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    public int delete(Integer id);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    public int deleteMulti(Integer[] ids);

}
