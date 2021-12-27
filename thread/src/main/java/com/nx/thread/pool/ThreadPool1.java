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
public class ThreadPool1 {

    //线程安全的累加器，原子性
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private static ThreadPoolExecutor pool;
    public static void main(String[] args) throws InterruptedException {
        /**
         * 线程池是具备懒惰性的
         * 线程池被实例化的时候里面的线程的都不会启动
         * 在提交任务的时候创建线程；先创建核心线程,如果任务超出了核心线程则放到队列当中；
         * 如果队列当中也满了则创建空闲线程,如果超过最大线程，则按照拒绝策略，默认抛出异常
         *
         * 核心线程并不是某一固定线程,如t1第一个执行（5秒），然后t2执行（10秒），此时t1作
         * 为核心线程执行完了，t2还在继续，t1过了空闲时间，此时还有大于等于 1 个线程在工作
         * 那么t1被回收，t2执行完，只剩t2一个，保持核心线程数至少1个，t2此时是核心线程
         * 设置核心线程可回收，则都会回收
         */
        //队列   设置队列容量来存放任务
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
        pool = new ThreadPoolExecutor(
                1,
                3,
                3,
                TimeUnit.SECONDS,
                queue,
                (t) ->{
                    return new Thread(t,"T"+atomicInteger.incrementAndGet());
                }
        );
        for (int i = 0; i < 3; i++) {
            //提交任务给线程池
            pool.execute(new MyTask("task-" + i));
        }

        log.debug("查看工作线程数-{}",pool.getPoolSize());
        //设置核心线程可回收
        //pool.allowCoreThreadTimeOut(true);

        //阻塞5秒，测试时间 5 > 3 ,因为execute()只是提交任务，执行看cpu，而且任务执行也要时间
        //主线程会直接向下执行，所以睡眠时间稍长一点
        TimeUnit.SECONDS.sleep(5);
        log.debug("延迟大于空闲时间之后查看工作线程数-{}",pool.getPoolSize());
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
