package org.demo.learn.dto;

import lombok.Data;
import org.demo.learn.enums.StrategyEnum;

import java.util.List;

/**
 * @author luwt-a
 * @date 2024/9/7
 */
@Data
public class StrategyContext<T> {

    private List<StrategyEnum> strategies;

    private List<T> data;
}
