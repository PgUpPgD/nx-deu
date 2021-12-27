package com.nx.thread.wait;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * Object wait 方法是干什么的？
 * 1、是Object的方法，要求object是被sync加锁的对象
 * 如果 lock 没有加锁会出异常，为什么？
 *
 * 2、一旦锁对象调用wait方法，会升级为重量锁
 * 3、wait和sleep的区别，都是让当前线程阻塞，但wait方法会释放锁，
 *    主要做线程之间的交互，而sleep只是让线程单纯的阻塞
 */
@Slf4j
public class WaitTest01 {
    private static final Object lock = new Object();

    //-XX:BiasedLockingStartupDelay=0
    public static void main(String[] args) throws Exception{
        log.debug("start");
        String s1 = ClassLayout.parseInstance(lock).toPrintable(lock);
        log.debug(s1);
        Thread t1 = new Thread(() -> {
            synchronized (lock){
                try {
                    log.debug("t1 -- wait");
                    //直接升级为重量级锁，不好测，不是因为 t2 synchronized (lock)加锁才升级
                    //是一旦锁对象（即t1）调用wait方法，会升级为重量锁
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1");
        t1.start();

        TimeUnit.SECONDS.sleep(1);

        Thread t2 = new Thread(() -> {
            synchronized (lock){
                try {
                    String s3 = ClassLayout.parseInstance(lock).toPrintable(lock);
                    log.debug(s3);
                    lock.notify();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "t2");
        t2.start();
        TimeUnit.SECONDS.sleep(2);
    }

}
