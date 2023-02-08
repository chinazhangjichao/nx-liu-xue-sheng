package cn.zjc.app.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription: 题库信息
 * @date: 2023-02-04 8:34
 * @since JDK 1.8
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Question implements Serializable {

    /**id*/
    private Integer id;
    /**所属课程*/
    private CourseInfor course;
    /**题目*/
    private String name;
    /**图片素材*/
    private String img;
    /**选项1*/
    private String option1;
    /**选项2*/
    private String option2;
    /**选项3*/
    private String option3;
    /**选项4*/
    private String option4;
    /**选项5*/
    private String option5;
    /**选项6*/
    private String option6;
    /**参考答案*/
    private String answer;
    /**答案解析*/
    private String analysis;
    /**题目类型：1单选2多选3判断4问答5其他*/
    private Integer type;
    /**分值*/
    private Double score;
    /**参与考试*/
    private Integer isExam;
    /**参与自测*/
    private Integer isTest;
    /**编辑时间*/
    private Long modifyTime;
    /**编辑人*/
    private SysUser modifyUser;

    public Question(Integer id) {
        this.id = id;
    }

    public Question(Integer id, Integer isExam, Integer isTest) {
        this.id = id;
        this.isExam = isExam;
        this.isTest = isTest;
    }
}
