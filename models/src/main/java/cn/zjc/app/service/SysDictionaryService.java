package cn.zjc.app.service;

import cn.zjc.app.bean.SysDictionary;

import java.util.List;

/**
 * @author ZJC
 * @decription: 系统功能业务操作接口
 * @date: 2022/1/12 13:18
 * @since JDK 1.8
 */
public interface SysDictionaryService {


	/**
	 * 保存或更新参数
	 * @param obj
	 * @return
	 */
	public int saveOrUpdate(SysDictionary obj);

	/**
	 * 删除参数
	 * @param dicId
	 * @return
	 */
	public int delete(Integer dicId);
	/**
	 * 查询所有
	 * @return
	 */
	public List<SysDictionary> queryAll();

	/**
	 * 根据key查询字典值
	 * @param key
	 * @return
	 */

	public SysDictionary queryByKey(String key);

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public SysDictionary queryById(Integer id);


}
