package com.nx.thread.interupt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * interrupt()的使用  没测通
 * 给方法加上 throws InterruptedException，在代码块捕获
 */
@Slf4j
public class InteruptTest4 {

    static final Object lock = new Object();

    public static void main(String[] args) throws Exception{
        Thread t1 = new Thread(() -> {
            try {
                timeout2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        t1.interrupt();
        log.debug("interrupt");
    }

    public static void timeout() throws InterruptedException {
        log.debug("一些耗时操作");
        for (int s = 0; s < 10000000; s++) {
            for (int i = 0; i < 10000000; i++) {
                i++;
                for (int j = 0; j < 1000000000; j++) {
                    j++;
                }
            }
        }
        log.debug("耗时操作结束");
    }

    public static void timeout1() throws InterruptedException {
        log.debug("一些耗时操作");
        TimeUnit.SECONDS.sleep(5);
        log.debug("耗时操作结束");
    }

    public static void timeout2() throws InterruptedException {
        synchronized (lock){
            log.debug("waiting....");
            lock.wait();
        }
    }

}
