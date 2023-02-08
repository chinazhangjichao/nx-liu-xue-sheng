package cn.zjc.app.service.impl;

import cn.zjc.app.bean.Question;
import cn.zjc.app.dao.QuestionDao;
import cn.zjc.app.service.QuestionService;
import cn.zjc.app.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZJC
 * @decription: 师资队伍 业务逻辑操作
 * @date: 2023/02/04 09:32
 * @since JDK 1.8
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    @Resource
    private QuestionDao questionDao;

    @Override
    public int saveOrUpdate(Question obj) {
        if (null == obj)
            return 0;
        if (AppUtil.isVaildId(obj.getId())) {
            return this.questionDao.update(obj);
        } else {
            return this.questionDao.save(obj);
        }
    }

    @Override
    public int saveList(List<Question> objList) {
        if(null==objList || objList.isEmpty())
        return 0;
        return this.questionDao.saveList(objList);
    }

    @Override
    public int delete(Integer id) {
        if (!AppUtil.isVaildId(id))
            return 0;
        Question type = this.questionDao.queryById(id);
        if (null == type) {
            throw new IllegalArgumentException("数据不存在!");
        }

        //查询关联数据--确认是否能够删除

        return this.questionDao.delete(id);
    }

    @Override
    public Question queryById(Integer id) {
        if (!AppUtil.isVaildId(id))
            return null;
        return this.questionDao.queryById(id);
    }

    @Override
    public List<Question> queryByPage(Integer pageNo, Integer pageSize, Integer course, Integer exam, Integer test, Integer type, String name) {
        pageNo = null == pageNo ? 1 : pageNo;
        pageSize = null == pageSize ? 10 : pageSize;
        return questionDao.queryByPage((pageNo-1)*pageSize,pageSize,course,exam,test,type,name);
    }

    @Override
    public int queryCount(Integer course, Integer exam, Integer test, Integer type, String name) {
        return questionDao.queryCount(course, exam, test, type, name);
    }

}
