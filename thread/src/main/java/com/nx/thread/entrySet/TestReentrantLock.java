package com.nx.thread.entrySet;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class TestReentrantLock {

    static List<Thread> list = new ArrayList<>();
    static ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                lock.lock();
                log.debug("thread executed");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                lock.unlock();
            }, "t" + i);
            list.add(t);
        }
        log.debug("---启动顺序 调度顺序或者说获取锁的顺序讲道理是正序0--9----");
        //for 循环完毕之后，synchronized (lock)才会释放锁，所以list中的线程都会因为拿不到锁进入entrySet阻塞队列
        lock.lock();
        for (Thread t : list) {
            //这个打印主要是为了看到线程启动的顺序
            log.debug("{}-启动顺序--正序0-9", t.getName());
            t.start();
            //此处阻塞为了保证线程按照顺序启动，和拿不到锁顺序进入阻塞队列，和不受cpu自由调度的影响
            TimeUnit.MILLISECONDS.sleep(1);
        }
        lock.unlock();
        log.debug("-------执行顺序--正序0-9");

        /**
         * 从结果可以看到使用ReentrantLock来保护临界区的时候效果几乎和synchronized关键字相同，唯一不
         * 同的是当主线程释放锁之后去EntryList（EntryList其实是C++的队列，ReentrantLock其实不存在
         * EntryList这个队列，但是他有一个对象FairSync或者NonfairSync这个对象维护了一个队列类似
         * EntryList,文中为了方便都称之为EntryList吧）当中唤醒线程的时候是正序的（先进先出从）\
         * 参考AQS
         */
    }
}
