package com.nx.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 线程池提交任务
 * <T> Future<T> submit(Callable<T> task);
 * Future<?> submit(Runnable task);
 * <T> Future<T> submit(Runnable task, T result);
 */
@Slf4j
public class ThreadPool5 {

    /**
     * 1、submit(Callable<T> task )
     * Callable 允许有返回值 返回的是Future
     *
     * Future.get() 可以的到这个返回值
     *
     * 2、Future.get()  会阻塞
     */
    public static void main(String[] args) throws Exception{
        //核心线程和最大线程=1
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Future<String> futureTeak = executorService.submit(() -> {
            log.debug("1");
            TimeUnit.SECONDS.sleep(2);
            return "success";
        });
        log.debug("start");
        log.debug("result=[{}]",futureTeak.get());

        log.debug("end");
    }
}
