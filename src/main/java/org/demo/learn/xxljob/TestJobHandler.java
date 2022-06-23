package org.demo.learn.xxljob;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author luwt-a
 * @date 2022/6/23
 */
@Slf4j
@Component
public class TestJobHandler {

    @XxlJob("测试定时任务")
    public void testXxlJob() {
        log.info("测试定时任务");
    }

}
