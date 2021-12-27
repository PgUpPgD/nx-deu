package com.nx.thread.ReadWriteLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
读读并发，读写互斥
 */
@Slf4j(topic = "e")
public class RwLock {

    //读写锁
    private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    static Lock r = rwl.readLock();
    static Lock w = rwl.writeLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            w.lock();
            try {
                log.debug("t1...");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            w.unlock();
        }, "t1");

        Thread t2 = new Thread(() -> {
            r.lock();
            log.debug("t2...");
            r.unlock();
        }, "t2");

        Thread t3 = new Thread(() -> {
            r.lock();
            log.debug("t3...");
            r.unlock();
        }, "t3");

        Thread t4 = new Thread(() -> {
            w.lock();
            try {
                log.debug("t4...");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            w.unlock();
        }, "t4");

        Thread t5 = new Thread(() -> {
            r.lock();
            log.debug("t5...");
            r.unlock();
        }, "t5");

        Thread t6 = new Thread(() -> {
            r.lock();
            log.debug("t6...");
            r.unlock();
        }, "t6");

        t1.start();
        TimeUnit.MILLISECONDS.sleep(500);
        t2.start();
        t3.start();
        TimeUnit.MILLISECONDS.sleep(500);
        t4.start();
        t5.start();
        t6.start();

    }

}
