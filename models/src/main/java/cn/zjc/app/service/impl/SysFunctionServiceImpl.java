package cn.zjc.app.service.impl;

import cn.zjc.app.bean.SysFunction;
import cn.zjc.app.dao.SysFunctionDao;
import cn.zjc.app.service.SysFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZJC
 * @decription: 系统功能业务操作
 * @date: 2022/1/12 13:22
 * @since JDK 1.8
 */
@Service
@Transactional
public class SysFunctionServiceImpl implements SysFunctionService {
    @Autowired
    private SysFunctionDao sysFunctionDao;

    @Override
    @Cacheable(value = "functions", key = "'all'")
    public List<SysFunction> queryAll() {
	return this.sysFunctionDao.queryAll();
    }

}
