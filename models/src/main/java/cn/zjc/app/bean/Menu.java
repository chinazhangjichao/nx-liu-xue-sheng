package cn.zjc.app.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription: 网站自定义菜单
 * @date: 2023-02-03 9:05
 * @since JDK 1.8
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Menu implements Serializable {

    /**主键ID*/
    private Integer menuId;
    /**菜单名称*/
    private String menuName;
    /**英文名称*/
    private String menuEnglish;
    /**跳转地址*/
    private String menuUrl;
    /**上级菜单*/
    private Menu menuParent;
    /**显示图标*/
    private String menuIcon;
    /**排序号*/
    private Integer sortNum;
    /**是否手机端显示*/
    private Integer isMobile;

    public Menu(Integer menuId) {
        this.menuId = menuId;
    }
}
