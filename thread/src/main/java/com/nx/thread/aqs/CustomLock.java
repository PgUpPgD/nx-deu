package com.nx.thread.aqs;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/*
1、什么是锁？
目标：同步 多线程之间一前一后的执行

其实就是一个标识，如果这个标识改变成了某个状态我们就理解未获取锁（正常返回）
拿不到锁，就陷入阻塞（死循环）让这个方法不返回
 */
public class CustomLock {
    //赋值 status = 1 操作，不是原子性
    volatile int status = 0;
    //主要是为了调用CAS的方法 为了获得status的偏移量
    private static Unsafe unsafe = null;
    //CustomLock 当中的 status 变量的内存偏移量    不准确的理解：变量的内存地址，
    //即变量status的内存地址偏移了CustomLock对象的内存地址多少，从而确定 status 变量的内存地址
    private static long statusOffset;

    //获取unsafe对象
    static {
        Field singleoneInstanceFiele = null;
        try {
            singleoneInstanceFiele = Unsafe.class.getDeclaredField("theUnsafe");
            singleoneInstanceFiele.setAccessible(true);
            unsafe = (Unsafe)singleoneInstanceFiele.get(null);
            statusOffset = unsafe.objectFieldOffset(
                    com.nx.thread.aqs.CustomLock.class.getDeclaredField("status"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //加锁
    void lock()throws Exception{
        //cas 原子操作
        while (!compareAndSet(0,1)){
//            TimeUnit.SECONDS.sleep(3);
        }
    }

    boolean compareAndSet(int oldVal, int newVal){
        return unsafe.compareAndSwapInt(this, statusOffset, 0, 1);
    }

    //解锁
    void unlock(){
        status = 0;
    }
}
