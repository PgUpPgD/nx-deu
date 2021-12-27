package com.nx.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * invokeAny  提交多个任务
 *  1、一次性提交所有的任务List
 *  不会执行所有任务
 *  如果有一个任务执行完了并且返回了则不会执行后面的了
 *
 *  2、阻塞主线
 */
@Slf4j(topic = "e")
public class ThreadPool7 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ExecutorService service = Executors.newFixedThreadPool(5);
        Object o = service.invokeAny(
                Arrays.asList(
                        ()->{
                            TimeUnit.SECONDS.sleep(3);
                            log.debug("1");
                            return "1s";
                        },()->{
                            TimeUnit.SECONDS.sleep(5);
                            log.debug("2");
                            return "2s";
                        },()->{
                            TimeUnit.SECONDS.sleep(2);
                            log.debug("3");
                            return "3s";
                        },()->{
                            TimeUnit.SECONDS.sleep(6);
                            log.debug("4");
                            return "4s";
                        }
                )
        );
        log.debug("main start");
        log.debug("result=[{}]",o);
        log.debug("main end");
    }
}
