package cn.zjc.app.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZJC
 * @decription: 导出题库信息
 * @date: 2023-02-04 8:34
 * @since JDK 1.8
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExportQuestion implements Serializable {

    /**所属课程*/
    private String course;
    /**题目*/
    private String name;
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
    private String type;
    /**分值*/
    private Double score;
    /**参与考试*/
    private Integer isExam;
    /**参与自测*/
    private Integer isTest;

}
