package com.nx.disruptor.performance;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DisruptorTest {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int size = 65536;
        Disruptor<DataEvent> disruptor = new Disruptor<>(new EventFactory<DataEvent>() {
            @Override
            public DataEvent newInstance() {
                return new DataEvent();
            }
        },
                size,
                executor,
                ProducerType.SINGLE,
                new YieldingWaitStrategy());
        DataEventHandler handler = new DataEventHandler();
        // 消费数据
        disruptor.handleEventsWith(handler);
        disruptor.start();
        new Thread(() -> {
            RingBuffer<DataEvent> ringBuffer = disruptor.getRingBuffer();
            for (long i = 0; i < Constants.EVENT_NUM_BAI; i++) {
                long seq = ringBuffer.next();
                DataEvent event = ringBuffer.get(seq);
                event.setId(i);
                event.setName("name:" + i);
                ringBuffer.publish(seq);
            }
        }).start();

        TimeUnit.SECONDS.sleep(3);
        disruptor.shutdown();
        executor.shutdown();
    }
}
