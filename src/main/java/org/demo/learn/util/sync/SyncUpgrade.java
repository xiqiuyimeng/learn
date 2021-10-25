package org.demo.learn.util.sync;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author luwt
 * @date 2021/5/11.
 * 锁升级：无锁 -> 偏向锁 -> 轻量锁 -> 重量锁
 */
public class SyncUpgrade {

    public static void main(String[] args) throws InterruptedException {
//        noLock();
//        SpecialBiasedLock();
//        biasedLock();
    }

    public static void noLock() {
        // 默认的无锁状态
        A a = new A();
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
    }

    public static void SpecialBiasedLock() {
        try {
            // jvm 默认延时加载偏向锁，延时默认时间4秒，可以通过 -XX:BiasedLockingStartupDelay 来设置，
            // 此时在延时过后，对象被设置为偏向锁状态，但是没有偏向任何线程，也就是匿名偏向
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        A a = new A();
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
    }

    public static void biasedLock() {
        // 偏向锁
        try {
            // 如果不等待加载偏向锁，在下面加synchronized时，会直接变成轻量锁
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        A a = new A();
        synchronized (a) {
            System.out.println(ClassLayout.parseInstance(a).toPrintable());
        }
    }



    static class A {

    }

}
