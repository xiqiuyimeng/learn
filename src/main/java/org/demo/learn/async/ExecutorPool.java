package org.demo.learn.async;

import lombok.extern.slf4j.Slf4j;
import org.demo.learn.exception.BusinessException;
import org.demo.learn.exception.ExceptionEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 使用线程池，来实现异步处理，关于submit 方法和 execute方法：
 * execute 方法没有返回值，也就不能获取异步处理结果，但是可以通过将对象传入线程方法内，进行属性赋值或list添加元素来实现收集结果
 * submit 方法有返回值，可以接收异步结果，使用 submit().get()，获取结果，在这个过程中可以处理异常，如果子线程产生异常，
 * 会在调用 get() 方法的时候触发，可以在主线程进行 catch，以进行处理
 * @author luwt-a
 * @date 2022/8/1
 */
@Slf4j
@Component
public class ExecutorPool {

    @Resource
    ExecutorService executorService;

    public void test() {
        List<Future<?>> futures = new ArrayList<>();
        // 用CountDownLatch来保证两个线程任务都结束，主线程再进行下一步
        CountDownLatch latch = new CountDownLatch(2);
        try {
            futures.add(executorService.submit(() -> methodA(latch)));
            futures.add(executorService.submit(() -> methodB(latch)));
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("方法 AB执行结束");
        try {
            for (Future<?> future : futures) {
                // 获取线程执行结果，如果方法有返回值，get方法必须在线程池提交任务之后，
                // 单独取结果，否则会造成同步，而非异步效果
                future.get();
            }
        } catch (Exception e) {
            // 如果是线程内部的业务异常，再次抛出
            if (e.getCause() instanceof BusinessException) {
                throw (BusinessException) e.getCause();
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    private void methodA(CountDownLatch latch) {
        try {
            Thread.sleep(3000);
            log.info("methodA 方法执行");
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.UNDEFINED);
        } finally {
            latch.countDown();
        }
    }

    private void methodB(CountDownLatch latch) {
        try {
            Thread.sleep(1000);
            log.info("methodB 方法执行");
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.UNDEFINED);
        } finally {
            latch.countDown();
        }
    }

}
