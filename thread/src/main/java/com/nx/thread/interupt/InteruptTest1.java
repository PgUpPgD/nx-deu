package com.nx.thread.interupt;

import lombok.extern.slf4j.Slf4j;

/**
 * 1、t1当中的for循环会执行吗？
 *   会执行---interrupt实际上不会打断执行的线程
 *   会打断正在阻塞的线程（sleep），以抛出异常的方式
 *
 * 2、t1.interrupted()
 *    会返回当前线程的打断标记
 */
@Slf4j
public class InteruptTest1 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 20 ; i++) {
                log.debug("i=[{}]",i);
            }
        }, "t1");
        t1.start();
        t1.interrupt();
        log.debug("----------mian--------");

        //返回的当前线程main（不是当前对象的那个线程） 为 false
        //interrupted 是静态方法 Thread.interrupted(),所以 t1.interrupted() 返回的是主线程main的打断标记
        log.debug("在主綫程當中調用  t1.interrupted()=[{}]",t1.interrupted());
        log.debug("在主綫程當中調用  t1.interrupted()=[{}]",t1.interrupted());
    }

}
