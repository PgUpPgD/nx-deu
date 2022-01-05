package com.nx.netty.heima.day02;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * EventLoop 除了可以处理 io 事件，同样可以向它提交普通任务
 * 可以用来执行耗时较长的任务
 */
@Slf4j
public class EventLoop1 {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup nioWorkers = new NioEventLoopGroup(2);
        log.debug("server start...");
        Thread.sleep(2000);
        //处理一般任务
        nioWorkers.execute(()->{
            log.debug("normal task...");
        });

        nioWorkers.execute(()->{
            log.debug("normal task2...");
        });

        //处理定时任务
        nioWorkers.scheduleAtFixedRate(() ->{
            log.debug("runing...");
        },0, 2, TimeUnit.SECONDS);
    }
}
