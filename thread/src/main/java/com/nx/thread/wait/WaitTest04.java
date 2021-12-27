package com.nx.thread.wait;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@SuppressWarnings("all")
public class WaitTest04 {
    private static final Object lock = new Object();
    private volatile static boolean ok = false;
    static List<Thread> list = new ArrayList<>();
    static int j = 0;
    public static void main(String[] args) throws Exception{
        log.debug("让出去玩。。。");
        new Thread(() ->{
            synchronized (lock){
                while (!ok){
                    log.debug("大佬没车不去。。。");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            log.debug("大佬去吧。。。");
        },"大佬").start();

        new Thread(() ->{
            synchronized (lock){
                while (!ok){
                    log.debug("小佬没车不去。。。");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            log.debug("小佬去吧。。。");
        },"小佬").start();

        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(() -> {
                synchronized (lock) {
                    log.debug("路人想去+1");
                }
            }, "t" + i);
            list.add(t);
        }

        TimeUnit.SECONDS.sleep(1);

        synchronized (lock){
            log.debug("大哥有车。。。");
            for (Thread t : list) {
                t.start();
                log.debug(t.getName() + "启动了");
                TimeUnit.SECONDS.sleep(1);
            }
            //notify 是随机唤醒，不能指定叫醒那个需要钱的
            ok = true;
            lock.notifyAll();
            TimeUnit.SECONDS.sleep(2);
        }

        /** synchronized 看执行顺序
         *  在线程wait后顺序进入waitSet，被 notifyAll() 全唤醒后先进的先出
         *  从waitSet出队之后，到entrySet最后排队，后进先出
         */
    }

}
