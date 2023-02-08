package cn.zjc.app.service;

import cn.zjc.app.bean.AdvertType;

import java.util.List;

/**
 * @author ZJC
 * @decription: 广告类别 业务逻辑操作接口
 * @date: 2022/1/25 13:32
 * @since JDK 1.8
 */
public interface AdvertTypeService {

    /**
     * 保存或修改
     * 
     * @param obj
     * @return
     */
    public int saveOrUpdate(AdvertType obj);

    /**
     * 删除
     * 
     * @param id
     * @return
     */
    public int delete(Integer id);

    /**
     * 根据Id查询
     * 
     * @param id
     * @return
     */
    public AdvertType queryById(Integer id);

    /**
     * 查询所有
     * 
     * @return
     */
    public List<AdvertType> queryAll();

}
