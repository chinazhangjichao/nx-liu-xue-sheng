package cn.zjc.app.controller;

import cn.zjc.app.annotation.ControllerLog;
import cn.zjc.app.bean.ArticleFile;
import cn.zjc.app.bean.ArticleInfor;
import cn.zjc.app.bean.SysUser;
import cn.zjc.app.service.ArticleFileService;
import cn.zjc.app.service.ArticleInforService;
import cn.zjc.app.utils.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author ZJC
 * @decription: 文章管理
 * @date: 2022/1/20 18:55
 * @since JDK 1.8
 */

@Controller
@RequestMapping("/article")
public class ArticleInforController {

    @Resource
    private ArticleInforService articleInforService;
    @Resource
    private ArticleFileService articleFileService;


    @ControllerLog(module = "文章管理",description = "进入模块")
    @PreAuthorize(value = "hasAuthority('/article/index')")
    @GetMapping(value = "/index")
    public String index() {
        return "articleindex";
    }


    @ControllerLog(module = "文章管理",description = "分页查询")
    @PreAuthorize(value = "hasAuthority('/article/page')")
    @PostMapping("/page")
    @ResponseBody
    public ResponseData page(@RequestBody Map params) {
        Integer currentPage = AppUtil.getIntegerParam(params, "currentPage");
        Integer pageSize = AppUtil.getIntegerParam(params, "pageSize");
        String keyword = AppUtil.getStringParam(params, "keyword");
        Integer type = AppUtil.getIntegerParam(params, "searchType");
        JSONObject data = new JSONObject();
        data.put("currentPage", currentPage);
        data.put("pageSize", pageSize);
        long total = 0;
        if (currentPage == 1) {
            total = this.articleInforService.queryCount(type, keyword);
            data.put("total", total);
            if (total == 0) {
                return new ResponseData("failure", "无数据", data);
            }
        }
        List<ArticleInfor> objList = this.articleInforService.queryByPage(currentPage, pageSize, type, keyword);
        if (AppUtil.isEmpty(objList)) {
            return new ResponseData("failure", "无数据", data);
        } else {
            data.put("list", toArray(objList));
            return new ResponseData("success", "加载成功", data);
        }
    }

    @ControllerLog(module = "文章管理",description = "查询详情")
    @PreAuthorize(value = "hasAuthority('/article/load')")
    @PostMapping("/load")
    @ResponseBody
    public ResponseData load(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params, "id");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效的ID！");
        }
        ArticleInfor obj = this.articleInforService.queryById(id);
        if (null != obj) {
            List<ArticleFile> articleFiles = this.articleFileService.queryByArticle(obj.getArticleId());
            obj.setArticleFiles(articleFiles);
            return new ResponseData("success", "加载成功", obj);
        } else {
            return new ResponseData("failure", "加载失败");
        }
    }

    @ControllerLog(module = "文章管理",description = "删除")
    @PreAuthorize(value = "hasAuthority('/article/delete')")
    @PostMapping("/delete")
    @ResponseBody
    public ResponseData delete(@RequestBody Map params) {
        Integer id = AppUtil.getIntegerParam(params, "id");
        if (!AppUtil.isVaildId(id)) {
            return new ResponseData("error", "无效的ID！");
        }
        if (this.articleInforService.delete(id) > 0) {
            return new ResponseData("success", "删除成功！");
        } else {
            return new ResponseData("failure", "删除失败！");
        }
    }

    @ControllerLog(module = "文章管理",description = "批量删除")
    @PreAuthorize(value = "hasAuthority('/article/multidelete')")
    @PostMapping("/multidelete")
    @ResponseBody
    public ResponseData multidelete(@RequestBody Integer[] ids) {
        if (AppUtil.isEmpty(ids)) {
            return new ResponseData("error", "无效操作！");
        }
        if (this.articleInforService.deleteMulti(ids) > 0) {
            return new ResponseData("success", "批量删除成功！");
        } else {
            return new ResponseData("failure", "批量删除失败！");
        }
    }

    @ControllerLog(module = "文章管理",description = "保存")
    @PreAuthorize(value = "hasAuthority('/article/save')")
    @PostMapping("/save")
    @ResponseBody
    public ResponseData save(@RequestBody ArticleInfor obj, HttpSession session) {
        if (null == obj) {
            return new ResponseData("error", "无效操作！");
        }
        if (!StringUtils.hasLength(obj.getArticleTitle())) {
            return new ResponseData("error", "文章名称不能为空！");
        }
        if (null == obj.getArticleType() || null == obj.getArticleType().getTypeId()) {
            return new ResponseData("error", "文章类型不能为空！");
        }
        if (!StringUtils.hasLength(obj.getArticleDetail())) {
            return new ResponseData("error", "文章详情不能为空！");
        }
        SysUser loginUser = (SysUser) session.getAttribute(Constants.LOGINUSER);
        obj.setModifyTime(System.currentTimeMillis());
        obj.setModifyUser(loginUser);
        obj.setIsCommand(null == obj.getIsCommand() ? 0 : obj.getIsCommand());
        obj.setIsTop(null == obj.getIsTop() ? 0 : obj.getIsTop());
        obj.setIsOnline(null == obj.getIsOnline() ? 1 : obj.getIsOnline());
        if (this.articleInforService.saveOrUpdate(obj) > 0) {
            return new ResponseData("success", "保存成功！");
        } else {
            return new ResponseData("failure", "保存失败！");
        }
    }
    @ControllerLog(module = "文章管理",description = "上传封面图")
    @PreAuthorize(value = "hasAuthority('/article/upload')")
    @PostMapping("/uploadimg")
    @ResponseBody
    public ResponseData uploadimage(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpSession session) {
        JSONObject json = new JSONObject();
        String realPath = request.getServletContext().getRealPath("/");
        String uploadPath = "/upload/articles/";
        File upload = new File(realPath + uploadPath);
        if (!upload.exists()) {
            upload.mkdirs();
        }
        long originalSize = file.getSize();
        String name = file.getOriginalFilename();
        String ext = name.substring(name.lastIndexOf("."));
        if (!".jpg".equalsIgnoreCase(ext) && !".jpeg".equalsIgnoreCase(ext) && !".png".equalsIgnoreCase(ext) && !".bmp".equalsIgnoreCase(ext)) {
            return new ResponseData("error", "只能上传jpg,jpeg,png,bmp格式！");
        }
        String tempName = "temp_" + System.currentTimeMillis() + ext;
        String fileName = UUID.randomUUID() + ext;
        // 保存
        try {
            if (originalSize < 500 * 1024) {
                File targetFile = new File(upload, fileName);
                file.transferTo(targetFile);
            } else {
                //先保存临时文件
                File targetFile = new File(upload, tempName);
                file.transferTo(targetFile);
                //压缩临时文件另存为正式文件
                if (ImageUtil.commpressPicForScale(targetFile.getAbsolutePath(), upload.getAbsolutePath() + "/" + fileName, 500, 0.8, 1920, 1080)) {
                    targetFile.delete();
                }
            }
            JSONObject data = new JSONObject();
            data.put("fileName", request.getServletContext().getAttribute(Constants.WEBSITE) + uploadPath + fileName);
            return new ResponseData("success", "上传成功", data);
        } catch (Exception e) {
            return new ResponseData("failure", "上传失败：" + e.getMessage());
        }
    }
    @ControllerLog(module = "文章管理",description = "上传附件")
    @PreAuthorize(value = "hasAuthority('/article/upload')")
    @PostMapping("/uploadfiles")
    @ResponseBody
    public ResponseData uploadfiles(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpSession session) {
        String realPath = request.getServletContext().getRealPath("/");
        String uploadPath = "/upload/articles/";
        File upload = new File(realPath + uploadPath);
        if (!upload.exists()) {
            upload.mkdirs();
        }
        long originalSize = file.getSize();
        if (originalSize > 50 * 1024 * 1024) {
            return new ResponseData("error", "附件超过了最大限制50MB！");
        }
        String name = file.getOriginalFilename();
        String ext = name.substring(name.lastIndexOf("."));
        if (!".jpg".equalsIgnoreCase(ext) && !".jpeg".equalsIgnoreCase(ext) && !".png".equalsIgnoreCase(ext)
                && !".doc".equalsIgnoreCase(ext) && !".docx".equalsIgnoreCase(ext) && !".pdf".equalsIgnoreCase(ext)
                && !".xls".equalsIgnoreCase(ext) && !".xlsx".equalsIgnoreCase(ext) && !".ppt".equalsIgnoreCase(ext)
                && !".pptx".equalsIgnoreCase(ext)
                ) {
            return new ResponseData("error", "不允许的附件格式！");
        }
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + ext;
        // 保存
        try {
            File targetFile = new File(upload, fileName);
            file.transferTo(targetFile);
            ArticleFile articleFile = new ArticleFile();
            articleFile.setCreateTime(System.currentTimeMillis());
            SysUser sysUser = (SysUser) session.getAttribute(Constants.LOGINUSER);
            articleFile.setCreateUser(new SysUser(sysUser.getUserId()));
            articleFile.setName(name);
            articleFile.setSize((int) originalSize);
            articleFile.setNo(uuid);
            articleFile.setType(ext.substring(1).toUpperCase());
            articleFile.setUrl(request.getServletContext().getAttribute(Constants.WEBSITE) + uploadPath + fileName);
            return new ResponseData("success", "上传成功", articleFile);
        } catch (Exception e) {
            return new ResponseData("failure", "上传失败：" + e.getMessage());
        }
    }
    @ControllerLog(module = "文章管理",description = "删除附件")
    @PreAuthorize(value = "hasAuthority('/article/upload')")
    @PostMapping("/file/delete")
    @ResponseBody
    public ResponseData filedelete(@RequestBody ArticleFile obj, HttpServletRequest request) {
        if (!StringUtils.hasLength(obj.getNo())) {
            return new ResponseData("error", "无效的文件！");
        }
        if (!StringUtils.hasLength(obj.getUrl())) {
            return new ResponseData("error", "无效的文件！");
        }

        if (AppUtil.isVaildId(obj.getId())) {
            this.articleFileService.delete(obj.getNo());
        }
        String host = (String) request.getServletContext().getAttribute(Constants.WEBSITE);
        String realPath = request.getServletContext().getRealPath("/");
        File file = new File(obj.getUrl().replace(host, realPath));
        if (file.exists()) {
            file.delete();
            return new ResponseData("success", "删除成功！");
        } else {
            return new ResponseData("failure", "删除失败！");
        }
    }

    /**
     * 把集合变成json数组
     *
     * @param objList
     * @return
     */
    private JSONArray toArray(List<ArticleInfor> objList) {
        JSONArray jsonArray = new JSONArray();
        for (ArticleInfor obj : objList) {
            JSONObject jo = new JSONObject();
            jo.put("articleId", obj.getArticleId());
            jo.put("articleTitle", obj.getArticleTitle());
            jo.put("articleImg", obj.getArticleImg());
            jo.put("articleType", obj.getArticleType());
            jo.put("isTop", obj.getIsTop());
            jo.put("isCommand", obj.getIsCommand());
            jo.put("isOnline", obj.getIsOnline());
            jo.put("isEn", obj.getIsEn());
            jo.put("sendDate", obj.getSendDate());
            jsonArray.add(jo);
        }
        return jsonArray;
    }
}
