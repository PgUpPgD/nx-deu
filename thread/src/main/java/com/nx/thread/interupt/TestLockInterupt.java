package com.nx.thread.interupt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * interrupt()打断不了一个正在排队的线程
 * lockInterruptibly() 可以打断
 */
@Slf4j
public class TestLockInterupt {

    public static void main(String[] args) throws Exception {
        Lock x = new ReentrantLock();
        x.lock();
        Thread t1 = new Thread(() -> {
            log.debug("尝试获取锁，应该拿不到，阻塞");
            try {
                x.lockInterruptibly();
            } catch (InterruptedException e) {
                log.debug("被打断了");
                e.printStackTrace();
            }finally {
                x.unlock();
            }
        }, "t1");
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        log.debug("t1拿不到，打断t1");
        t1.interrupt();
    }

//    public static void main(String[] args) throws Exception {
//        Object o = new Object();
//        new Thread(() ->{
//            synchronized (o){
//                log.debug("---t2-----");
//                try {
//                    TimeUnit.SECONDS.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        },"t2").start();
//        TimeUnit.SECONDS.sleep(1);
//        Thread t1 = new Thread(() -> {
//            log.debug("尝试获取锁，应该拿不到，阻塞");
//            synchronized (o){
//                log.debug("加锁成功");
//            }
//        }, "t1");
//        t1.start();
//        log.debug("t1拿不到，打断t1");
//        t1.interrupt();
//    }

}
