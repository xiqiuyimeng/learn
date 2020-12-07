package com.demo.learn.annotation;

import java.lang.annotation.*;

/**
 * @author luwt
 * @date 2020/9/4.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyFile {

    String value() default "";

//    String name() default "";
//
//    boolean required() default true;
//
//    String defaultValue() default ValueConstants.DEFAULT_NONE;


}
