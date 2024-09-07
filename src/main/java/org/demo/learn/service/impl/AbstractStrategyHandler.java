package org.demo.learn.service.impl;

import org.demo.learn.enums.StrategyEnum;
import org.demo.learn.service.StrategyService;

import java.util.List;

/**
 * @author luwt-a
 * @date 2024/9/7
 */
public abstract class AbstractStrategyHandler<R> implements StrategyService<R> {

    @Override
    public boolean support(StrategyEnum strategy) {
        return getSupportStrategy().equals(strategy);
    }

    @Override
    public <T> List<R> handle(List<T> data) {
        return this.doHandle(data);
    }

    protected abstract StrategyEnum getSupportStrategy();

    protected abstract <T> List<R> doHandle(List<T> data);
}
