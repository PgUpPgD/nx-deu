package com.nx.disruptor.chain;

import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // 构建一个线程池用于提交任务
        ExecutorService pool = Executors.newFixedThreadPool(3);
        //1、 构建Disruptor
        Disruptor<TradeEvent> disruptor = new Disruptor<TradeEvent>(
                new TradeEventFactory(),
                8,
                pool,
                ProducerType.SINGLE,
                //BlockingWaitStrategy SleepingWaitStrategy YieldingWaitStrategy BusySpinWaitStrategy
                //拒绝策略 从左往右对cpu的性能要求增大，一般使用sleep，yield，不用busy。
                new SleepingWaitStrategy()
        );
        // 2、 把消费者设置再Disruptor中的handleEventsWith
        /**
         * 2.1 使用串行操作
         */
        disruptor.handleEventsWith(new Handler1());
//                .handleEventsWith(new Handler2())
//                .handleEventsWith(new Handler3());
        /**
         * 2.2 并行操作  可以有两种方式进行添加
         *  1、handleEventWith方法 添加多个handler实现即可
         *  2、handleEventWith方法，分开调用即可
         */
//        disruptor.handleEventsWith(new Handler1(), new Handler2(),new Handler3());
//        disruptor.handleEventsWith(new Handler2());
//        disruptor.handleEventsWith(new Handler3());


        /**
         * 2.3 菱形操作
         *
         */
        // 2.3.1 菱形操作一
//        disruptor.handleEventsWith(new Handler1(),new Handler2())
//                .handleEventsWith(new Handler3());
        // 2.3.2 菱形操作二
//        EventHandlerGroup<TradeEvent> eventHandlerGroup = disruptor.handleEventsWith(new Handler1(), new Handler2());
//        eventHandlerGroup.then(new Handler3());
        // 2.4 多边形
//        Handler1 h1 = new Handler1();
//        Handler2 h2 = new Handler2();
//        Handler3 h3 = new Handler3();
//        Handler4 h4 = new Handler4();
//        Handler5 h5 = new Handler5();
//        disruptor.handleEventsWith(h1,h4);
//        disruptor.after(h1).handleEventsWith(h2);
//        disruptor.after(h4).handleEventsWith(h5);
//        disruptor.after(h2,h5).handleEventsWith(h3);
        // 3、启动disruptor
        disruptor.start();
        try {
            TradeEventProducer producer = new TradeEventProducer(disruptor.getRingBuffer());
            producer.sendData();
            //用err程序不结束
//            System.err.println("45");
        }finally {
//            TimeUnit.SECONDS.sleep(2);
            disruptor.shutdown();
            pool.shutdown();
        }


    }
}
