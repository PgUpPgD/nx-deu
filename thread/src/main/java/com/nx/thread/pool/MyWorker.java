package com.nx.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 即是一个任务也是一个线程
 */
@Slf4j(topic = "e")
public class MyWorker implements Runnable {

    private MyQueue myQueue;
    //第一个任务进来时，实例化 MyWorker
    private Runnable firstTask;
    private Thread thread;

    public MyWorker(Runnable firstTask, MyQueue myQueue){
        this.firstTask = firstTask;
        this.myQueue = myQueue;
        this.thread = new Thread(this, "t" + MyThreadPool.atomicInteger.incrementAndGet());
    }

    public Thread getThread(){
        return thread;
    }

    @Override
    public void run() {
        //firstTask!=null 表示这次运行run方法是分配到了任务 不是自己取的
        //如果分配到了任务 则直接运行任务的run  firstTask.run();
        //如果firstTask==null 表示运行完了一次，至少是分配到的任务运行完了
        //需要去队列当中看看有没有任务 getTask()====两种获取任务的方法
        //1、获取不到则直接阻塞
        //2、获取不到之后超时后解阻塞 结束线程
        //假设队列当中有任务 则获取到赋值给 firstTask 接着执行 firstTask.run
        while (firstTask != null || (firstTask=getTask()) != null){
            firstTask.run();
            firstTask = null;
        }
        log.debug("没有拿到任务");
    }

    public MyTask getTask(){
        boolean allowCoreThreadTimeOut = MyThreadPool.allowCoreThreadTimeOut;
        try {
            return allowCoreThreadTimeOut ?
                    myQueue.poll(TimeUnit.SECONDS.toNanos(3)):
                    myQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void start(){
        thread.start();
    }

}
