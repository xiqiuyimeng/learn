package org.demo.learn.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.annotation.*;

/**
 * @author luwt
 * @date 2020/9/4.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyFile {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean required() default true;

    String defaultValue() default ValueConstants.DEFAULT_NONE;

}
