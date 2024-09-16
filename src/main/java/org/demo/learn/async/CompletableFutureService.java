package org.demo.learn.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * 使用CompletableFuture来实现任务编排，带有async的方法为异步处理
 * 例如下面的test方法，可以实现 AB 方法并行，同时结束后，再取出 AB 的结果，执行 C，
 * 最终输出结果
 * @author luwt-a
 * @date 2022/8/1
 */
@Slf4j
@Component
public class CompletableFutureService {

    @Resource
    ExecutorService executorService;

    public void test() {
        // 异步执行
        CompletableFuture<Object> futureA = CompletableFuture.supplyAsync(this::methodA, executorService);
        CompletableFuture<Object> futureB = CompletableFuture.supplyAsync(this::methodB, executorService);
        // 等待AB执行完
//        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(futureA, futureB);
        // 除了可变参数直接传递，也可以放入list
        List<CompletableFuture<?>> futures = new ArrayList<>();
        futures.add(futureA);
        futures.add(futureB);
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        // 继续执行C
        CompletableFuture<String> result = completableFuture.thenApply(v -> {
            log.info((String) futureA.join());
            log.info((String) futureB.join());
            return methodC();
        });
        try {
            log.info("输出结果：{}", result.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    public void test2() {
        // 先执行A，然后拿到A的执行结果，异步执行BC
        CompletableFuture<String> futureA = CompletableFuture.supplyAsync(this::methodA, executorService);
        futureA.thenAccept(resA -> {
            CompletableFuture.supplyAsync(() -> methodD(resA), executorService);
            CompletableFuture.supplyAsync(() -> methodE(resA), executorService);
        });
    }

    public String methodA() {
        try {
            Thread.sleep(3000);
            log.info("methodA 方法执行");
            // 此处可以抛异常
//            int a = 1/0;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "methodA";
    }

    public String methodB() {
        try {
            Thread.sleep(2000);
            log.info("methodB 方法执行");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "methodB";
    }

    public String methodC() {
        try {
            Thread.sleep(2000);
            log.info("methodC 方法执行");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "methodC";
    }


    public String methodD(String s) {
        try {
            Thread.sleep(2000);
            log.info("methodD 方法执行" + s);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "methodD";
    }

    public String methodE(String s) {
        try {
            Thread.sleep(2000);
            log.info("methodE 方法执行" + s);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "methodE";
    }

}
