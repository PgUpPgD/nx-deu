package com.nx.netty.heima.day02;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class Future6 {

    // 异步处理任务失败
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

        eventExecutors.submit(() ->{
            System.out.println("1");
            try{
                //阻塞
                promise.await();
                /**
                 * 注意不能仅捕获 InterruptedException 异常
                 * 否则 死锁检查抛出的 BlockingOperationException 会继续向上传播
                 * 而提交的任务会被包装为 PromiseTask，它的 run 方法中会 catch 所有异常然后
                 * 设置为 Promise 的失败结果而不会抛出
                 */
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("2");
        });

        eventExecutors.submit(()->{
            System.out.println("3");
            try {
                promise.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("4");
        });
    }

}
