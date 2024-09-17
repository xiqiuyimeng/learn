package org.demo.learn.util.rateLimit;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

/**
 * 调用外部接口时，进行限流，防止大量调用外部接口，对方压力过大，服务异常
 * @author luwt-a
 * @date 2024/9/17
 */
@Slf4j
public class RateLimiterUtil {

    // 创建一个每秒限制 2 个的限流器
    private final RateLimiter rateLimiter = RateLimiter.create(2);

    public void callExternalApi() {
        // 调用之前，获取许可
        rateLimiter.acquire();

        // 模拟调用外部接口
        log.info("模拟调用外部接口");
    }

    public static void main(String[] args) {
        RateLimiterUtil rateLimiterUtil = new RateLimiterUtil();
        // 模拟多次调用，预计耗时 5 秒
        for (int i = 0; i < 10; i++) {
            rateLimiterUtil.callExternalApi();
        }
    }

}
