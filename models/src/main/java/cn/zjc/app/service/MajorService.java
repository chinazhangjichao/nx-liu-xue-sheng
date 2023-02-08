package cn.zjc.app.service;

import cn.zjc.app.bean.Major;

import java.util.List;

/**
 * @author ZJC
 * @decription: 专业 业务逻辑操作接口
 * @date: 2023/02/03 14:32
 * @since JDK 1.8
 */
public interface MajorService {

    /**
     * 保存或修改
     * 
     * @param obj
     * @return
     */
    public int saveOrUpdate(Major obj);

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
    public Major queryById(Integer id);

    /**
     * 查询所有
     * 
     * @return
     */
    public List<Major> queryAll(Integer status);

}
