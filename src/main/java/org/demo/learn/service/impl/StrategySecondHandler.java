package org.demo.learn.service.impl;

import org.demo.learn.dto.StrategyParamDTO;
import org.demo.learn.dto.StrategyResultDTO;
import org.demo.learn.enums.StrategyEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luwt-a
 * @date 2024/9/7
 */
@Component
public class StrategySecondHandler extends AbstractStrategyHandler<StrategyParamDTO, StrategyResultDTO>{

    @Override
    protected StrategyEnum getSupportStrategy() {
        return StrategyEnum.STRATEGY_SECOND;
    }

    @Override
    protected List<StrategyResultDTO> doHandle(List<StrategyParamDTO> data) {
        // 第二个策略具体实现
        return data.stream().map(item -> {
            StrategyResultDTO result = new StrategyResultDTO();
            result.setName(getSupportStrategy().toString());
            result.setResult(item.getParamName());
            return result;
        }).collect(Collectors.toList());
    }
}
