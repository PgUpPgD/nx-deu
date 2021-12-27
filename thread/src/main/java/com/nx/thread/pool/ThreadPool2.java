package com.nx.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j(topic = "e")
public class ThreadPool2 {
    /**
     * newSingleThreadExecutor()
     * 最大线程数和核心线程数=1 即单线程
     * LinkedBlockingQueue<Runnable>()  无界队列
     *
     * 问题：和自己new Thread() 的区别在哪里?
     *      和使用固定线程池 newFixedThreadPool(1) 的区别？
     *
     * 1、这种保证了单个线程
     *    Executors.newFixedThreadPool(1)  可以改变线程池的大小
     *    ExecutorService executorService = Executors.newFixedThreadPool(1);
     *         ThreadPoolExecutor executor = (ThreadPoolExecutor) executorService;
     *         executor.setCorePoolSize(n);
     *
     *    而newSingleThreadExecutor不可以改变
     *
     * 2、如果是单线程串行执行 出了异常会终止
     *  如果异常了会重新创建一个新线程去执行下面的任务
     *  上面的那个线程会终止
     */
    public static void main(String[] args) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            log.debug("1");
        });
        service.execute(() -> {
            log.debug("2");
            int a = 1 / 0;
        });
        System.out.println();
        service.submit(() -> {
            log.debug("3");
        });
        service.shutdown();
    }
}
