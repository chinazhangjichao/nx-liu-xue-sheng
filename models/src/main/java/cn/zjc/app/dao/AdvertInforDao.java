package cn.zjc.app.dao;

import cn.zjc.app.bean.AdvertInfor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author ZJC
 * @decription: 广告/banner图 数据库操作接口
 * @date: 2022/1/25 13:18
 * @since JDK 1.8
 */
@Mapper
public interface AdvertInforDao {
    /**
     * 保存
     * 
     * @param obj
     * @return
     */
    int save(AdvertInfor obj);

    /**
     * 更新
     * 
     * @param obj
     * @return
     */
    int update(AdvertInfor obj);

    /**
     * 根据id删除
     * 
     * @param id
     * @return
     */
    int delete(@Param("id") Integer id);

    /**
     * 批量删除
     * 
     * @param ids
     * @return
     */
    int deleteMulti(@Param("ids") Integer[] ids);

    /**
     * 根据id查询
     * 
     * @return
     */
    public AdvertInfor queryById(Integer id);

    /**
     * 分页查询
     * 
     * @param start
     * @param size
     * @param type
     *            广告位
     * @param online
     *            是否在线
     * @return
     */
    public List<AdvertInfor> queryByPage(@Param("start") int start, @Param("size") int size, @Param("type") Integer type, @Param("keyword") String keyword, @Param("online") Integer online);

    /**
     * 查询记录数
     * 
     * @param type
     * @param online
     * @return
     */
    public int queryCount(@Param("type") Integer type, @Param("keyword") String keyword, @Param("online") Integer online);

}
