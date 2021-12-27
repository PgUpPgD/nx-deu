package com.nx.thread.interupt;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch( n )
 * 当 n==0 时， latch.await();解阻塞，通过 latch.countDown();来减 n 的值
 * 和 join的作用类似，join是让 t1.join() 等该执行完到t1线程死亡
 * CountDownLatch，是让该线程先运行，阻塞主线程。到n等于0后，解阻塞，
 * 不用等CountDownLatch的线程死亡
 */
@Slf4j
public class CountDownlatch1 {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        String[] list = new String[5];
        for (int i = 0; i < 5; i++) {
            int k = i;
            Random random = new Random();
            int time = random.nextInt(100);
            Thread t = new Thread(() -> {
                for (int j = 0; j <= 100; j++) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String str = Thread.currentThread().getName() + "--" + j + "%";
                    list[k] = str;
                    System.out.print("\r" + "{" + Arrays.toString(list) + "}");
                }
                latch.countDown();
            }, "h" + i);
            t.start();
        }
        latch.await();
        System.out.println("\n" + "start");
    }

}
