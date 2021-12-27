package com.nx.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 shutdown
 1、线程池状态变为SHUTDOWN
 2、不会接收新任务 会出异常
 3、但已提交任务会执行完

 shutdownNow
 1、线程池状态变为 STOP
 2、不会接收新任务
 3、会将队列中的没有执行完成的任务返回，并用 interrupt 的方式中断正在执行的任务
    用List<Runnable> shutdownNow();接受返回的没有执行的任务

 awaitTermination(5,TimeUnit.SECONDS)
 表示主线程会最多等5s之后解阻塞，如果线程池在3秒就执行完成了；则只会等3s；
 如果线程池执行了10s；主线程只会等5s,然后继续向下执行
 */
@Slf4j(topic = "e")
public class ThreadPool8 {

    private static ExecutorService service;

    private ThreadPool8(){
        service = Executors.newFixedThreadPool(1);
    }

    public static ExecutorService getPool(){
        if (service == null){
            new ThreadPool8();
        }
        return service;
    }

    public static void addTask(Runnable runnable){
        service.submit(runnable);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        log.debug("start...");
        ExecutorService pool = ThreadPool8.getPool();

        new Thread(() ->{
            for (int i = 0; i < 10; i++) {
                int j = i;
                ThreadPool8.addTask(() ->{
                    log.debug("xxx---[{}]", j);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        },"t1").start();

        pool.awaitTermination(5,TimeUnit.SECONDS);
        log.debug("主线程解阻塞");
        log.debug("任务已经提交完，我开始shutdown");
//        pool.shutdown();

        List<Runnable> list = pool.shutdownNow();
        list.forEach((t)->{
            log.debug("线程池状态变为 STOP，返回的没有执行的任务");
            System.out.println(t);
        });
    }
}
