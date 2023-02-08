package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.utils.Constants;
import cn.zjc.app.utils.DataBaseUtil;
import cn.zjc.app.utils.ResponseData;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 后台：数据库管理
 *
 * @author 张吉超
 * @date 2020-04-16 13:59
 */
@Controller
@RequestMapping("/db")
public class DataBaseController {

    @ControllerLog(module = "数据库管理", description = "进入模块")
    @RequestMapping("/index")
    public String dbindex(Model model) {
        return "dbindex";
    }

    /**
     * 加载信息
     *
     * @return
     */
    @ControllerLog(module = "数据库管理", description = "分页查询备份")
    @ResponseBody
    @RequestMapping("/page")
    public ResponseData dbpage(HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        String root = request.getSession().getServletContext().getRealPath("/");

        File bakDir = new File(root + "dbbak");
        if (!bakDir.exists()) {
            bakDir.mkdirs();
        }
        File[] fileList = bakDir.listFiles();

        if (null != fileList && fileList.length > 0) {
            responseData.setCode("success");
            responseData.setMessage("加载成功！");
        } else {
            responseData.setCode("failure");
            responseData.setMessage("加载失败，无数据！");
        }
        JSONObject data = new JSONObject();
        data.put("list", toArray(fileList));
        responseData.setData(data);
        return responseData;
    }

    /**
     * 删除
     *
     * @param name
     * @return
     */
    @ControllerLog(module = "数据库管理", description = "删除备份")
    @ResponseBody
    @RequestMapping("/delete")
    public ResponseData dbdelete(String name, HttpServletRequest request) {
        String root = request.getSession().getServletContext().getRealPath("/");
        File bakDir = new File(root + "dbbak");
        if (!bakDir.exists()) {
            bakDir.mkdirs();
        }
        if (StringUtils.isEmpty(name)) {
            return new ResponseData("error", "无效操作，没有文件！");
        }
        if (StringUtils.isEmpty(name)) {
            return new ResponseData("success", "无效的操作，备份不存在！");
        }
        File file = new File(bakDir, name);
        if (file.delete()) {
            return new ResponseData("success", "删除成功！");
        } else {
            return new ResponseData("failure", "删除失败！");
        }
    }

    @ControllerLog(module = "数据库管理", description = "备份数据库")
    @ResponseBody
    @RequestMapping("/backup")
    public ResponseData dbbackup(HttpServletRequest request) {
        ServletContext application = request.getSession().getServletContext();
        String root = application.getRealPath("/");
        File bakDir = new File(root + "dbbak");
        if (!bakDir.exists()) {
            bakDir.mkdirs();
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(new Date());
        File[] fileList = bakDir.listFiles();
        int count = 0;
        for (File file : fileList) {
            if (today.equals(df.format(new Date(file.lastModified())))) {
                count++;
            }
        }
        int limit = null == application.getAttribute(Constants.DBBACKUPLIMIT) ? 6 : Integer
                .parseInt((String) application.getAttribute(Constants.DBBACKUPLIMIT));
        if (count >= limit) {
            return new ResponseData("error", "今日备份次数已达到上限！");
        }
        String fileName = root + "dbbak\\" + System.currentTimeMillis() + ".sql";

        if (DataBaseUtil.backup(fileName)) {
            return new ResponseData("success", "数据库备份成功！");
        } else {
            return new ResponseData("failure", "数据库备份失败！");
        }
    }

    @ControllerLog(module = "数据库管理", description = "备份数据库")
    @ResponseBody
    @RequestMapping("/restore")
    public ResponseData restore(HttpServletRequest request, String name) {
        String root = request.getSession().getServletContext().getRealPath("/");
        File bakDir = new File(root + "dbbak");
        if (!bakDir.exists()) {
            bakDir.mkdirs();
        }
        if (StringUtils.isEmpty(name)) {
            return new ResponseData("error", "无效操作，没有备份文件！");
        }
        File datafile = new File(bakDir, name);
        if (!datafile.exists()) {
            return new ResponseData("error", "备份文件不存在！");
        }
        if (DataBaseUtil.restore(bakDir + "\\" + name)) {
            return new ResponseData("success", "数据库还原成功！");
        } else {
            return new ResponseData("failure", "数据库还原失败！");
        }
    }

    /**
     * 把集合变成json数组
     *
     * @param objList
     * @return
     */
    private JSONArray toArray(File[] objList) {
        JSONArray jsonArray = new JSONArray();
        for (File obj : objList) {
            JSONObject jo = new JSONObject();
            jo.put("fileName", obj.getName());
            jo.put("fileTime", obj.lastModified());
            jsonArray.add(jo);
        }
        return jsonArray;
    }
}
