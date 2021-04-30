package com.demo.learn.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author luwt
 * @date 2021/4/29.
 * 统计用户访问量
 */
public class CountDownLatchTest {

    static AtomicInteger count = new AtomicInteger(0);

    // 模拟用户访问的方法
    public static void request() throws InterruptedException {
        // 模拟耗时5毫秒
        TimeUnit.MILLISECONDS.sleep(5);
        // 访问量 +1， 当用户访问一次，计数器加1
        count.addAndGet(1);
    }

    public static void main(String[] args) throws InterruptedException{
        // 开始时间
        long startTime = System.currentTimeMillis();
        // 最大的线程数 100，模拟100个用户同时访问
        int threadSize = 100;
        CountDownLatch latch = new CountDownLatch(threadSize);

        for (int i = 0; i < threadSize; i++) {
            new Thread(() -> {
                try {
                    // 模拟每个用户访问10次
                    for (int j = 0; j < 10; j++) {
                        request();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 跑完一个线程，计数器减1，计数器值为0时，表示所有线程都结束
                    latch.countDown();
                }
            }).start();
        }
        // 线程都结束以后，继续执行主线程
        latch.await();

        long endTime = System.currentTimeMillis();
        System.out.println("耗时：" + (endTime - startTime) + " count = " + count);
    }

}
