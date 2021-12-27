package com.nx.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程池提交任务 execute
 * 提交一个任务 没有返回值 只能接受Runnable
 * invokeAll  提交多个任务
 */
@Slf4j
public class ThreadPool6 {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService service = Executors.newFixedThreadPool(5);
        List<Future<String>> futures = service.invokeAll(
                Arrays.asList(
                        ()->{
                            log.debug("1");
                            return "1s";
                        },()->{
                            log.debug("2");
                            return "2s";
                        },()->{
                            log.debug("3");
                            return "3s";
                        },()->{
                            log.debug("4");
                            return "4s";
                        }
                )
        );
        log.debug("main start");

        futures.forEach(f->{
            try {
                log.debug(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        log.debug("main end");
    }
}
