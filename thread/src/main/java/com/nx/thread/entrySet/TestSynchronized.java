package com.nx.thread.entrySet;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestSynchronized {

    static List<Thread> list = new ArrayList<>();
    static Object lock = new Object();
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                synchronized (lock){
                    log.debug("thread executed");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, "t" + i);
            list.add(t);
        }
        log.debug("---启动顺序 调度顺序或者说获取锁的顺序讲道理是正序0--9----");
        //for 循环完毕之后，synchronized (lock)才会释放锁，所以list中的线程都会因为拿不到锁进入entrySet阻塞队列
        synchronized (lock){
            for (Thread t:list){
                //这个打印主要是为了看到线程启动的顺序
                log.debug("{}-启动顺序--正序0-9", t.getName());
                t.start();
                //此处阻塞为了保证线程按照顺序启动，和拿不到锁顺序进入阻塞队列，和不受cpu自由调度的影响
                TimeUnit.MILLISECONDS.sleep(1);
            }
            log.debug("-------执行顺序--正序9-0");
        }
        /** synchronized
         * 这里的结果可以说明JVM在从队列当中唤醒的时候是唤醒一个，而不是全部唤醒，因为如果是全部唤醒，那么这些
        线程的执行顺序肯定是乱的，只有唤醒一个，而且还是顺序唤醒才能保证执行顺序是具备规则的（t9-t0），
         而且是倒序唤醒的；那么这队列存在哪里呢？在java语言里使用synchronized关键字如果变成了
        一把重量锁，那么这个锁对象（本文当中的lock对象——Object lock =new Object()）
         会关联一个C++对象——ObjectMonitor对象；这个监视器对象当中记录了持有当前锁
        的线程，记录了锁被重入的次数，同时他还有一个属性EntryList用来关联那些因为拿不到锁而被阻塞的线程
         */
    }
}


