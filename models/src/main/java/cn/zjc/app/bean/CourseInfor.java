package cn.zjc.app.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author ZJC
 * @decription:
 * @date: 2023-02-04 9:26
 * @since JDK 1.8
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfor {

    private Integer courseId;
    private String courseName;
    private String courseEnglish;

}
