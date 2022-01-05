package com.nx.netty.heima.day02;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class Future1 {

    //同步处理任务成功
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);
        eventExecutors.execute(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("set success, {}",10);
            promise.setSuccess(10);
        });
        log.debug("start...");
        //getNow 获取任务结果，非阻塞，还未产生结果时返回 null
        log.debug("{}",promise.getNow()); // 还没有结果
        //get 是阻塞，等结果返回
        log.debug("{}",promise.get());
    }

}
