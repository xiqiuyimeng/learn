package org.demo.learn.designPattern.strategy;

import com.alibaba.fastjson.JSON;
import org.demo.learn.dto.StrategyContext;
import org.demo.learn.dto.StrategyParamDTO;
import org.demo.learn.dto.StrategyResultDTO;
import org.demo.learn.enums.StrategyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luwt-a
 * @date 2024/9/7
 */
@Component
public class StrategyUser {

    @Autowired
    private Composite<StrategyParamDTO, StrategyResultDTO> composite;

    /**
     * 策略1 调用
     */
    public List<StrategyResultDTO> handleStrategyFirst() {
        // 构建上下文
        List<StrategyParamDTO> paramDTOS = new ArrayList<>();
        StrategyParamDTO strategyParamDTO = new StrategyParamDTO();
        strategyParamDTO.setParamName("测试策略1");
        paramDTOS.add(strategyParamDTO);
        StrategyContext<StrategyParamDTO> context = this.buildFirstContext(paramDTOS);
        List<StrategyResultDTO> result = composite.strategyHandle(context);
        System.out.println(JSON.toJSONString(result));
        return result;
    }


    private StrategyContext<StrategyParamDTO> buildFirstContext(List<StrategyParamDTO> data) {
        StrategyContext<StrategyParamDTO> context = new StrategyContext<>();
        context.setData(data);
        List<StrategyEnum> strategies = new ArrayList<>(1);
        strategies.add(StrategyEnum.STRATEGY_FIRST);
        context.setStrategies(strategies);
        return context;
    }


    /**
     * 策略2 调用
     */
    public List<StrategyResultDTO> handleStrategySecond() {
        // 构建上下文
        List<StrategyParamDTO> secondPramDTOS = new ArrayList<>();
        StrategyParamDTO strategySecondPramDTO = new StrategyParamDTO();
        strategySecondPramDTO.setParamName("测试策略2");
        secondPramDTOS.add(strategySecondPramDTO);
        StrategyContext<StrategyParamDTO> context = this.buildSecondContext(secondPramDTOS);
        List<StrategyResultDTO> result = composite.strategyHandle(context);
        System.out.println(JSON.toJSONString(result));
        return result;
    }


    private StrategyContext<StrategyParamDTO> buildSecondContext(List<StrategyParamDTO> data) {
        StrategyContext<StrategyParamDTO> context = new StrategyContext<>();
        context.setData(data);
        List<StrategyEnum> strategies = new ArrayList<>(1);
        strategies.add(StrategyEnum.STRATEGY_SECOND);
        context.setStrategies(strategies);
        return context;
    }


    /**
     * 全部策略调用
     */
    public List<StrategyResultDTO> handleStrategyAll() {
        // 构建上下文
        List<StrategyParamDTO> paramDTOS = new ArrayList<>();
        StrategyParamDTO strategyParamDTO = new StrategyParamDTO();
        strategyParamDTO.setParamName("测试策略1");
        paramDTOS.add(strategyParamDTO);
        StrategyParamDTO strategyParamDTO2 = new StrategyParamDTO();
        strategyParamDTO2.setParamName("测试策略2");
        paramDTOS.add(strategyParamDTO2);
        StrategyContext<StrategyParamDTO> context = this.buildAllContext(paramDTOS);
        List<StrategyResultDTO> result = composite.strategyHandle(context);
        System.out.println(JSON.toJSONString(result));
        return result;
    }


    private StrategyContext<StrategyParamDTO> buildAllContext(List<StrategyParamDTO> data) {
        StrategyContext<StrategyParamDTO> context = new StrategyContext<>();
        context.setData(data);
        List<StrategyEnum> strategies = new ArrayList<>(1);
        strategies.add(StrategyEnum.STRATEGY_FIRST);
        strategies.add(StrategyEnum.STRATEGY_SECOND);
        context.setStrategies(strategies);
        return context;
    }

}
