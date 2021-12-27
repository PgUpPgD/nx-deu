package com.nx.thread.interupt;

import lombok.extern.slf4j.Slf4j;

/**
 * isInterrupted()
 * 返回是否被打断过，不重置
 */
@Slf4j
public class InteruptTest3 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 20 ; i++) {
                log.debug("i=[{}]",i);
            }
        }, "t1");
        t1.start();
        t1.interrupt();
        log.debug("在t1线程中调用第一次  Thread.isInterrupted()=[{}]",t1.isInterrupted());
        log.debug("在t1线程中调用第一次  Thread.isInterrupted()=[{}]",t1.isInterrupted());
    }
}
