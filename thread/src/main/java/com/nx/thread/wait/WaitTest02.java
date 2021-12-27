package com.nx.thread.wait;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * wait 和 notify 一起使用， wait 所在的代码最好用 while(?)
 * 这样即使 notify 唤醒，也可以继续判断条件是否通过
 */
@Slf4j
public class WaitTest02 {
    private static final Object lock = new Object();
    private volatile static boolean ok = false;
    private volatile static boolean yes = false;

    public static void main(String[] args) throws Exception{
        log.debug("让出去玩。。。");
        new Thread(() ->{
            synchronized (lock){
                while (!ok){
                    log.debug("没车不去。。。");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            log.debug("既然接我那走吧。。。");
        },"t1").start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(() ->{
            synchronized (lock){
                while (!yes){
                    log.debug("没钱不去。。。");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            log.debug("既然免费那走吧。。。");
        },"t2").start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(() ->{
            log.debug("大哥来了。。。");
            synchronized (lock){
                log.debug("大哥有钱。。。");
                yes = true;
                //notify 是随机唤醒，不能指定叫醒那个需要钱的
                lock.notify();
                //notifyAll 全部唤醒，但如果之需要唤醒一个资源，会造成资源浪费
                lock.notifyAll();
            }
        },"t3").start();

        TimeUnit.SECONDS.sleep(1);
    }

}
