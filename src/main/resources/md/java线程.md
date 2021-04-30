#### # java线程

- 开启线程的方式：

  - 实现runnable接口：

    ~~~java
    @FunctionalInterface
    public interface Runnable {
        public abstract void run();
    }
    ~~~

    重写run方法就可以实现开启一个线程。run方法将被jvm调用。

  

