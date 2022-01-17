package com.nx.disruptor.quickstart;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        OrderEventFactory eventFactory = new OrderEventFactory();
        //指定RingBuffer大小,必须是2的N次方
        int ringBufferSize = 1024 * 1024;
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        /**
         * 1 eventFactory: 消息(event)工厂对象
         * 2 ringBufferSize: 容器的长度
         * 3 pool: 线程池(建议使用自定义线程池) RejectedExecutionHandler
         * 4 ProducerType: 单生产者 还是 多生产者
         * 5 waitStrategy: 等待策略 ： 当队列数据满了或者没有数据时候，生产者和消费者的等待策略
         * BlockingWaitStrategy SleepingWaitStrategy YieldingWaitStrategy BusySpinWaitStrategy
         * 拒绝策略 从左往右对cpu的性能要求增大，一般使用sleep，yield，不用busy。
         */
        //1.实例化disruptor对象
        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                eventFactory,
                ringBufferSize,
                pool,
                ProducerType.SINGLE,
                new SleepingWaitStrategy());
        //2. 添加消费者的监听(构建disruptor 与 消费者的一个关联关系)
        disruptor.handleEventsWith(new OrderEventHandler());
        //3. 启动disruptor
        disruptor.start();
        //4. 获取实际存储数据的容器: RingBuffer
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderEventProducer producer = new OrderEventProducer(ringBuffer);
        for (int i = 0; i < 5; i++) {
            producer.sendData(i);
        }
        //调用shutdown的时候，会等handler处理完数据后才执行完毕，shutdown
        disruptor.shutdown();
        pool.shutdown();
    }
}
