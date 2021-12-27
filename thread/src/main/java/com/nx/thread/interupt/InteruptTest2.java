package com.nx.thread.interupt;

import lombok.extern.slf4j.Slf4j;

/**
 * 1、t1当中的for循环会执行吗？
 *  *   会执行---interrupt实际上不会打断执行的线程
 *
 * 2、t1.interrupted()
 *    会返回当前线程的打断标记 同时清除打断标记
 */
@Slf4j
public class InteruptTest2 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            //true
            log.debug("在t1线程中调用第一次  Thread.interrupted()=[{}]",Thread.interrupted());
            //false
            log.debug("在t1线程中调用第二次  Thread.interrupted()=[{}]",Thread.interrupted());
            for (int i = 0; i < 20 ; i++) {
                log.debug("i=[{}]",i);
            }
        }, "t1");
        t1.start();
        t1.interrupt();
        log.debug("----------mian--------");
    }


}
