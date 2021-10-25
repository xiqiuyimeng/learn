package org.demo.learn.exception;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 业务异常断言接口，继承自基础异常类接口，
 * 提供两个断言方法，断言失败并抛出异常，
 * 由于接口默认方法只能通过实现类的对象来调用，
 * 所以this当前指代的是实现类 ExceptionEnum 的对象，
 * 这样可以实现用ExceptionEnum定义好的异常对象来调用断言，
 * 例如：ExceptionEnum.NPE.assertNonNull(object);
 * 简化判断
 * @author luwt
 * @date 2020/6/1.
 */
public interface BusinessExceptionAssert extends BaseExceptionInter {

    default void assertNonNull(Object object) {
        if (object == null
                || (object instanceof String && StringUtils.isBlank(String.valueOf(object)))
                || (object instanceof Collection && CollectionUtils.isEmpty((Collection<?>) object))
                || (object instanceof Map && MapUtils.isEmpty((Map<?, ?>) object))) {
            throw new BusinessException(this);
        }
    }

    default <T> void assertNonNull(T[] t) {
        if (ArrayUtils.isEmpty(t)) {
            throw new BusinessException(this);
        }
    }

    default void assertTrue(boolean bool) {
        if (!bool) {
            throw new BusinessException(this);
        }
    }
}
