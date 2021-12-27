package com.nx.thread.interupt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch( n )
 * 当 n==0 时， latch.await();解阻塞，通过 latch.countDown();来减 n 的值
 * 和 join的作用类似，join是让 t1.join() 等该执行完到t1线程死亡
 * CountDownLatch，是让该线程先运行，阻塞主线程。到n等于0后，解阻塞，
 * 不用等CountDownLatch的线程死亡
 */
@Slf4j
public class CountDownlatch {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        Thread t1 = new Thread(() -> {
            log.debug("t1");
            latch.countDown();
        }, "t1");

        Thread t2 = new Thread(() -> {
            log.debug("t2");
            latch.countDown();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t2.....");
        }, "t2");

        Thread t3 = new Thread(() -> {
            log.debug("t3");
            latch.countDown();
        }, "t3");

        t1.start();
        t2.start();
        t3.start();

        latch.await();
        log.debug("---------main-----------");
        System.out.println("t2--" + t2.isAlive());
        System.out.println("t3--" + t3.isAlive());
    }

}
