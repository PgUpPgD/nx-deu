package com.nx.thread.interupt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

//实现一把死锁
@Slf4j
public class DeadLock {

    static final Object x = new Object();
    static final Object y = new Object();
    public static void main(String[] args) throws Exception{
        Thread t1 = new Thread(() -> {
            synchronized (x) {
                log.debug("x sync");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (y) {
                    log.debug("y sync");
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (y) {
                log.debug("y sync");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (x) {
                    log.debug("x sync");
                }
            }
        }, "t2");

        t1.start();
        t2.start();
    }

}
