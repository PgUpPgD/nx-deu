package com.nx.nettychat.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import com.nx.nettychat.entity.TranslatorDataWapper;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

@Data
public class RingBufferWorkerPoolFactory {

    private static RingBufferWorkerPoolFactory instance;

    private RingBufferWorkerPoolFactory(){}

    public static RingBufferWorkerPoolFactory getInstance(){
        if (instance == null){
            instance =  new RingBufferWorkerPoolFactory();
        }
        return instance;
    }

    // 存放生产者和消费者
    private static Map<String,MessageProducer> producers = new ConcurrentHashMap<>();
    private static Map<String,MessageConsumer> consumers = new ConcurrentHashMap<>();

    // 需要构建 ringBuffer缓存对象 sequenceBarrier环形链表屏障 workerPool消费者工作池
    private RingBuffer<TranslatorDataWapper> ringBuffer;
    private WorkerPool<TranslatorDataWapper> workerPool;
    private SequenceBarrier sequenceBarrier;

    public void initAndStart(ProducerType type, int bufferSize, WaitStrategy waitStrategy, MessageConsumer[] messageConsumers){
        //1.构建 ringBuffer
        this.ringBuffer = RingBuffer.create(
                type,
                TranslatorDataWapper::new,
                bufferSize,
                waitStrategy);
        //2.设置序号栅栏 sequenceBarrier
        this.sequenceBarrier = ringBuffer.newBarrier();
        //3.workerPool消费者工作池
        this.workerPool = new WorkerPool<>(this.ringBuffer, this.sequenceBarrier,
                new EventExceptionHandler(), messageConsumers);
        //4.把所有构建的消费者放到池中
        for (MessageConsumer mc : messageConsumers) {
            consumers.put(mc.getConsumerId(), mc);
        }
        //5.添加sequence
        this.ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        //6.启动工作线程池
        this.workerPool.start(
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        );
    }

    public MessageProducer getMessageProducer(String producerId){
        MessageProducer messageProducer = producers.get(producerId);
        if (messageProducer == null){
            messageProducer = new MessageProducer(this.ringBuffer);
            producers.put(producerId, messageProducer);
        }
        return messageProducer;
    }

    /**
     * 异常静态类
     */
    static class EventExceptionHandler implements ExceptionHandler<TranslatorDataWapper> {
        public void handleEventException(Throwable ex, long sequence, TranslatorDataWapper event) {
        }

        public void handleOnStartException(Throwable ex) {
        }

        public void handleOnShutdownException(Throwable ex) {
        }
    }
}

