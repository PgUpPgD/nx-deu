package com.nx.thread.sync;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestSync {

    int i = 0;

    @Test
    public void test01() throws Exception{
        threadAdd();
        TimeUnit.SECONDS.sleep(1);
        log.info("i的值为{}", i);
    }

    public void threadAdd() throws Exception{
        Thread t1 = new Thread(this::add, "t1");
        Thread t2 = new Thread(this::add, "t2");
        Thread t3 = new Thread(this::add, "t3");

        t1.start();
//        t1.join();
        t2.start();
//        t2.join();
        t3.start();
//        t3.join();
    }

    //锁对象要定义为全局变量，如果是局部变量，jvm自己的优化会进行锁消除，相当于无锁
    private final Object lock = new Object();
    public void add() {
//        Object lock = new Object();
        synchronized (lock) {
            for (int j = 0; j <= 9999; j++) {
                i++;
            }
        }
    }

    public void test02(){
        //对这把锁计算hashCode
        int code = lock.hashCode();
        String s1 = Integer.toHexString(code);
        log.debug("code---[{}]", s1);

        //输出对象的内存结构
        String s = ClassLayout.parseInstance(lock).toPrintable(lock);
        log.debug(s);
        synchronized (lock){
            s = ClassLayout.parseInstance(lock).toPrintable(lock);
            log.debug(s);
        }
    }

    public void test03() throws Exception{
        //输出对象的内存结构
        String s = ClassLayout.parseInstance(lock).toPrintable(lock);
        log.debug(s);

        Thread t1 = new Thread(() -> {
            synchronized (lock){
                String s1 = ClassLayout.parseInstance(lock).toPrintable(lock);
                log.debug(s1);
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();

        Thread t2 = new Thread(() -> {
            synchronized (lock){
                String s2 = ClassLayout.parseInstance(lock).toPrintable(lock);
                log.debug(s2);
            }
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2");
        t2.start();
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void test04() throws Exception {
        TimeUnit.SECONDS.sleep(5);
        Object lock = new Object();
        String s = ClassLayout.parseInstance(lock).toPrintable(lock);
        log.debug(s);
    }

    // 使用@Test注解和main方法结果不一样，使用main
    public static void main(String[] args) throws Exception{
        new TestSync().test02();
        new TestSync().test03();
    }



}
