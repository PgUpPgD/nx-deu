package com.nx.thread.interupt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 超时加锁
 * 一段时间内持续尝试获取锁
 */
@Slf4j
public class TryLockTest1 {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        new Thread(() ->{
            log.debug("start try");
            try {
                if (lock.tryLock(3, TimeUnit.SECONDS)){
                    log.debug("加锁成功");
                }else {
                    log.debug("加锁失败");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
