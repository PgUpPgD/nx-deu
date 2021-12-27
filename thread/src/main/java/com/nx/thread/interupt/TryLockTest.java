package com.nx.thread.interupt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 超时加锁
 * 尝试一次失败便不再尝试
 */
@Slf4j
public class TryLockTest {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
//        lock.lock();
        new Thread(() ->{
            if (lock.tryLock()){
                log.debug("加锁成功");
            }
        }).start();
    }
}
