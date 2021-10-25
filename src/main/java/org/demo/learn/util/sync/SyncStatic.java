package org.demo.learn.util.sync;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author luwt
 * @date 2021/5/11.
 * synchronized 锁静态方法和非静态方法的区别
 */
public class SyncStatic {

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(5000);
        TestA testA = new TestA();
        System.out.println(ClassLayout.parseInstance(testA).toPrintable());
    }

}
