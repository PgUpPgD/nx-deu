package com.nx.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "e")
public class MyQueue {

    //锁 存入/取出 都需要同步
    private final ReentrantLock lock = new ReentrantLock();
    //生产者线程 当队列已满 需要阻塞 阻塞到这个队列当中
    private Condition full = lock.newCondition();
    //消费者线程 队列消费空了 需要阻塞
    private Condition empty = lock.newCondition();
    //存放任务的队列
    private Deque<MyTask> deque = new ArrayDeque<>();
    //队列的元素上限 deque的长度不能大于 queueSize
    private int queueSize;

    public MyQueue(int queueSize){
        this.queueSize = queueSize;
    }

    /**
     * 往当前队列中存放task
     * 线程安全
     */
    public void offer(MyTask myTask){
        lock.lock();
        //判断队列的界限
        try {
            //控制队列的上限
            while (deque.size() == queueSize){
                log.debug("队列已满---阻塞{}",myTask.getName());
                full.await();
            }
            log.debug("队列未满，入队---success{}",myTask.getName());
            deque.addLast(myTask);
            //唤醒 执行任务的线程 有可能在队列为空的情况下陷入了阻塞状态
            empty.signal();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 永远阻塞
     * 从队列中取出任务
     */
    public MyTask take(){
        lock.lock();
        try {
            log.debug("取出一个任务");
            //如果队列为空则阻塞
            while (deque.size() == 0){
                log.debug("拿不到任务");
                log.debug("阻塞---永久阻塞");
                empty.await();
            }
            //获取一个任务 并且从队列中remove
            MyTask task = deque.removeFirst();
            //由于可以从队列中拿到任务 则需要唤醒提交任务的线程
            full.signal();
            return task;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return null;
    }

    /**
     * 超时阻塞,拿不到任务不立即阻塞，等待空闲时间
     * 从队列当中取出一个任务
     */
    public MyTask poll(long nanos) throws InterruptedException{
        lock.lockInterruptibly();
        try {
            log.debug("取出一个任务");
            while (deque.size() == 0){
                log.debug("拿不到任务");
                log.debug("阻塞---timeout 阻塞");
                if (nanos <= 0){
                    return null;
                }
                //纳秒
                empty.awaitNanos(nanos);
            }
            MyTask task = deque.removeFirst();
            //由于可以从队列中拿到任务 则需要唤醒提交任务的线程
            full.signal();
            return task;
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return null;
    }


}
