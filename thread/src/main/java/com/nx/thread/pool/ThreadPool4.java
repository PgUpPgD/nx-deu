package com.nx.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1.固定线程数 核心线程=最大线程
 * 2.等待时间无限，不会死亡
 * 3.队列 LinkedBlockingQueue<Runnable>() 无界队列
 *      public LinkedBlockingQueue() {
 *         this(Integer.MAX_VALUE);
 *      }
 */
@Slf4j
public class ThreadPool4 {
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2,(t) ->{
            return new Thread(t,"T"+atomicInteger.incrementAndGet());
        });
        for (int i = 0; i < 100; i++) {
            service.execute(() ->{
                log.debug(Thread.currentThread().getName());
            });
        }
    }
}
