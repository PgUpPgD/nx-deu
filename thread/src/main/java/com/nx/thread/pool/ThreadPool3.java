package com.nx.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1.缓存  体现在当一个线程执行完之后，不会立即死亡，在60s之内可以复用
 * 2.没有核心线程, 60s之后如果没有任务，那么线程池里的线程全部死亡
 * 3.最大线程个数 Integer.MAX_VALUE
 * 4.同步阻塞队列
 * 5.效率高    场景：任务非常多，执行时间短
 *
 * SynchronousQueue<Runnable>() 同步阻塞队列
 * 放入一个元素之后立马阻塞，知道这个元素被其他线程拿走了才会解阻塞
 * 他阻塞的是放入元素的这个线程 主线程，所以放入一个创建一个空闲线程拿走
 */
@Slf4j
public class ThreadPool3 {
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool((t) ->{
            return new Thread(t,"T"+atomicInteger.incrementAndGet());
        });
        for (int i = 0; i < 100; i++) {
            service.execute(() ->{
                log.debug(Thread.currentThread().getName());
            });
        }
    }

    /**
     * 同步阻塞队列 SynchronousQueue 队列容量1，拿走了才能放进去
     */
    static class SynchronousQueueTest {
        public static void main(String[] args) throws InterruptedException {

            SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();

            new Thread(()->{
                log.debug("start put 1");
                try {
                    synchronousQueue.put("1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("end put 1");

                log.debug("start put 2");
                try {
                    synchronousQueue.put("2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("end put 2");
            },"t1").start();

            TimeUnit.SECONDS.sleep(1);

            new Thread(()->{
                log.debug("start take 1");
                try {
                    synchronousQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("end take 1");
            },"t2").start();
        }
    }
}
