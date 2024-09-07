package org.demo.learn.service;

import org.demo.learn.enums.StrategyEnum;

import java.util.List;

/**
 * @author luwt-a
 * @date 2024/9/7
 */
public interface StrategyService<R> {

    // 判断是否支持策略
    boolean support(StrategyEnum strategy);

    // 策略实现
    <T> List<R> handle(List<T> data);

}
