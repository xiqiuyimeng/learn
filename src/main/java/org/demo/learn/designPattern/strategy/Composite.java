package org.demo.learn.designPattern.strategy;

import org.demo.learn.dto.StrategyContext;
import org.demo.learn.enums.StrategyEnum;
import org.demo.learn.service.StrategyService;
import org.demo.learn.util.SpringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 外部调用此类，此类完成策略选择组装
 * @author luwt-a
 * @date 2024/9/7
 */
@Component
public class Composite<T, R> implements CommandLineRunner {

    private List<StrategyService> strategyServices = new ArrayList<>();

    private final Map<StrategyEnum, StrategyService<T, R>> strategyServiceMap = new HashMap<>();

    public List<R> strategyHandle(StrategyContext<T> context) {
        // 获取处理器
        List<StrategyService<T, R>> strategyServices = getStrategyServices(context.getStrategies());
        // 调用处理器处理数据
        // 返回结果
        return strategyServices.stream()
                .map(strategyService -> strategyService.handle(context.getData()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<StrategyService<T, R>> getStrategyServices(List<StrategyEnum> strategies) {
        List<StrategyService<T, R>> services = new ArrayList<>();
        for (StrategyEnum strategy : strategies) {
            StrategyService<T, R> strategyService = strategyServiceMap.get(strategy);
            if (Objects.isNull(strategyService)) {
                // 获取service
                Optional<StrategyService> serviceOptional = strategyServices.stream()
                        .filter(service -> service.support(strategy))
                        .findFirst();
                if (serviceOptional.isPresent()) {
                    strategyService = serviceOptional.get();
                    strategyServiceMap.put(strategy, strategyService);
                }
            }
            services.add(strategyService);
        }
        return services;
    }

    @Override
    public void run(String... args) throws Exception {
        strategyServices = SpringUtils.getBeans(StrategyService.class);
    }
}
