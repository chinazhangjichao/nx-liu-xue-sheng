package cn.zjc.app.service;

import cn.zjc.app.bean.AdvertInfor;

import java.util.List;

/**
 * @author ZJC
 * @decription: 广告/banner 业务逻辑接口
 * @date: 2022/1/25 13:32
 * @since JDK 1.8
 */
public interface AdvertInforService {
    /**
     * 保存或更新
     * 
     * @param obj
     * @return
     */
    int saveOrUpdate(AdvertInfor obj);

    /**
     * 根据id删除
     * 
     * @param id
     * @return
     */
    int delete(Integer id);

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    int deleteMulti(Integer[] ids);

    /**
     * 根据id查询
     * 
     * @param id
     * @return
     */
    public AdvertInfor queryById(Integer id);

    /**
     * 分页查询
     * 
     * @param pageNo
     * @param pageSize
     * @param type
     *            广告类型/广告位
     * @param online
     *            是否在线
     * @return
     */
    public List<AdvertInfor> queryByPage(int pageNo, int pageSize, Integer type, String keyword, Integer online);

    /**
     * 查询记录数
     *
     * @return
     */
    public int queryCount(Integer type, String keyword, Integer online);
}
