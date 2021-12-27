package com.nx.thread.wait;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock lock = new ReentrantLock()
 * lock.newCondition() 可以创建一个队列，来装await的线程
 */
@Slf4j
public class WaitTest03 {
    private volatile static boolean ok = false;
    private volatile static boolean yes = false;
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition conditionw = lock.newCondition();
    private static Condition conditionm = lock.newCondition();

    @SneakyThrows
    public static void main(String[] args){
        log.debug("让出去玩。。。");
        new Thread(() ->{
            lock.lock();
            while (!ok){
                log.debug("没车不去。。。");
                try {
                    conditionw.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
            log.debug("既然接我那走吧。。。");
        },"t1").start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(() ->{
            lock.lock();
            while (!yes){
                log.debug("没钱不去。。。");
                try {
                    conditionm.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
            log.debug("既然免费那走吧。。。");
        },"t2").start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(() ->{
            lock.lock();
            log.debug("大哥来了。。。");
            log.debug("大哥有钱。。。");
            yes = true;
            //conditionm 是一个队列，signal()队列从头唤醒，signalAll()全部唤醒
            conditionm.signal();
            lock.unlock();
        },"t3").start();
        TimeUnit.SECONDS.sleep(1);
    }
}
