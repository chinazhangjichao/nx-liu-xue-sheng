package cn.zjc.app.annotation;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ControllerLog {
	/**
	 * 模块
	 * @return
	 */
	String module() default "";

	/**
	 * 功能描述
	 * @return
	 */
	String description() default "";

}