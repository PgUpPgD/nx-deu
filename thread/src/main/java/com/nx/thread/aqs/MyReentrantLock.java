package com.nx.thread.aqs;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class MyReentrantLock {

    @Test
    public void test01() throws Exception{
        //true为公平锁
        Lock lock = new ReentrantLock(true);
        log.debug("start");
        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 30; i++) {
                log.debug("t1");
            }
            lock.unlock();
        }, "t1");

        Thread t2 = new Thread(() -> {
            lock.lock();
            for (int i = 0; i < 30; i++) {
                log.debug("t2");
            }
            lock.unlock();
        }, "t2");
        t1.start();
        t2.start();
        TimeUnit.SECONDS.sleep(5);
    }

}
