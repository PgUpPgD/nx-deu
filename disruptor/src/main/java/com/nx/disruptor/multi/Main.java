package com.nx.disruptor.multi;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

/**
 * 多生产者多消费者模式
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {

        // 1 创建RingBuffer
        // 这里和创建Disruptor有点区别，创建Disruptor需要指定线程池，如果是多生产多消费者
        // 模式就不需要线程池了
        RingBuffer<Order> ringBuffer = RingBuffer.create(ProducerType.MULTI,
                new EventFactory<Order>() {
                    @Override
                    public Order newInstance() {
                        return new Order();
                    }
                },
                1024 * 1024,
                new YieldingWaitStrategy());
        // 2 通过ringbuffer创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        // 3 创建多个消费者数组
        OrderConsumer[] consumers = new OrderConsumer[10];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new OrderConsumer("C: " + i);
        }
        // 4 构建多消费者工作池
        WorkerPool<Order> workerPool = new WorkerPool<>(
                ringBuffer,
                sequenceBarrier,
                new EventExceptionHandler(),
                consumers);

        // 5 设置多个消费者的sequence序号，用于单独统计消费者进度，并同步给ringbuffer中
        // 既然我们已经将多消费者交给workPool来管理，我们就需要从workPool中获取对应的工作序号
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        // 6 启动workPool
        workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

        CyclicBarrier latch = new CyclicBarrier(100);
        for(int i = 0; i < 100; i ++){
            OrderProducer producer = new OrderProducer(ringBuffer);
            new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                for(int j = 0 ;j < 100 ;j ++){
                    producer.sendData(UUID.randomUUID().toString());
                }
            }).start();
        }
        System.out.println("----------线程创建完毕，开始生产数据----------");
        Thread.sleep(5 * 1000);
        System.out.println("任务总数:" + consumers[2].getCount());
    }
}
