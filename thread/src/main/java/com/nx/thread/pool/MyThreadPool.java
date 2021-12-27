package com.nx.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟线程池
 */
@Slf4j(topic = "e")
public class MyThreadPool {

    //线程安全的累加器，原子性自增长
    static AtomicInteger atomicInteger = new AtomicInteger(0);
    //队列 存放没有分配给线程的任务
    private MyQueue myQueue;
    //标识线程是否需要回收
    static boolean allowCoreThreadTimeOut = false;
    //支持线程的数量上限
    private int tsize;
    //所有的工作线程的集合
    private HashSet<MyWorker> set = new HashSet<>();

    public MyThreadPool(int tsize, int qsize){
        this.tsize = tsize;
        myQueue = new MyQueue(qsize);
    }

    public void info(){
        //打印队列的长度
        log.debug("set={}", set.size());
    }

    //向线程池提交一个任务 任务分配给一个线程
    public void execute(MyTask myTask){
        //如果正在工作的线程数量没有达到上限则直接实例化一个线程对象
        //然后把这个myTask 分配给这个线程 ---启动线程
        //把启动完成之后的线程 存放到set集合当中
        //同时 tsize+1
        if (set.size() < tsize){ //没到上限，可以new一个
            log.debug("线程池未满，new线程，分配任务 任务的名字--【{}】",myTask.getName());
            MyWorker worker = new MyWorker(myTask, myQueue);
            set.add(worker);
            worker.start();
        }else {
            log.debug("线程池已满，放到队列当中去，任务的名字--【{}】",myTask.getName());
            //把任务存到队列当中
            myQueue.offer(myTask);
        }
    }

    public static void main(String[] args) {
        MyThreadPool pool = new MyThreadPool(2, 2);
        for (int i = 0; i < 5; i++) {
            pool.execute(new MyTask("task-" + i));
        }
    }

}
