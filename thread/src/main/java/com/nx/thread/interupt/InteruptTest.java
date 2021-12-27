package com.nx.thread.interupt;

import lombok.extern.slf4j.Slf4j;

/**
 *1、t1当中的for循环会执行吗？
 *   会执行---interrupt实际上不会打断执行的线程
 *   会打断正在阻塞的线程，以抛出异常的方式
 */
@Slf4j
public class InteruptTest {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i <20 ; i++) {
                log.debug("i=[{}]",i);
            }
        }, "t1");
        t1.start();
        t1.interrupt();
        log.debug("interrupt");
    }
}
