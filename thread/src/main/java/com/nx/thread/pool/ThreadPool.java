package com.nx.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 演示 队列
 */
@Slf4j(topic = "e")
public class ThreadPool {

    //线程安全的累加器，原子性
    static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {
        /**
         * 1、核心线程数
         * 2、最大线程数
         * 3、空闲存活时间
         * 4、单位
         * 5、队列
         * 6、线程工厂 主要是为了名字
         *
         * 线程池是具备懒惰性的
         * 线程池被实例化的时候里面的线程的都不会启动
         * 在提交任务的时候创建线程；先创建核心线程,如果任务超出了核心线程则放到队列当中；
         * 如果队列当中也满了则创建空闲线程,如果超过最大线程，则按照拒绝策略，默认抛出异常
         */
        //队列   设置队列容量来存放任务
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                1,
                2,
                3,
                TimeUnit.SECONDS,
                queue,
                (t) ->{
                    return new Thread(t,"T"+atomicInteger.incrementAndGet());
                }
        );
        for (int i = 0; i < 4; i++) {
            pool.execute(new MyTask("task-" + i));
        }

        pool.shutdown();
    }

    @Slf4j(topic = "e")
    static class MyTask implements Runnable{
        String name;
        public MyTask(String name){
            this.name = name;
        }
        @Override
        public void run() {
            log.debug("task-Name[{}]",name);
        }
    }

}
