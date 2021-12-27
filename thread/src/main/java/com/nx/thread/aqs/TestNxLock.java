package com.nx.thread.aqs;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class TestNxLock {

    @Test
    public void test01() throws Exception{
        log.debug("1");
        Thread t1 = new Thread(() -> {
            log.debug("2");
            //park 阻塞线程
            LockSupport.park();
            log.debug("4");
        });
        //start 只是告诉cpu当前线程可以调度了，有没有调度看cpu分配（随机）
        t1.start();
        TimeUnit.SECONDS.sleep(2);
        log.debug("3");
        LockSupport.unpark(t1);
    }

    @Test
    public void test02() throws Exception{
        CustomLock lock = new CustomLock();
        log.debug("start");
        Thread t1 = new Thread(() -> {
            try {
                lock.lock();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 30; i++) {
                log.debug("t1");
            }
            lock.unlock();
        }, "t1");

        Thread t2 = new Thread(() -> {
            try {
                lock.lock();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 30; i++) {
                log.debug("t2");
            }
            lock.unlock();
        }, "t2");
        t1.start();
        t2.start();
        //让主程序睡眠，不然主程序运行完，程序就终止了
        TimeUnit.SECONDS.sleep(3);
    }
}
