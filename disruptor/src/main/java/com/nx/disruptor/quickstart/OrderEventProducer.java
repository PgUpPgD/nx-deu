package com.nx.disruptor.quickstart;

import com.lmax.disruptor.RingBuffer;

public class OrderEventProducer {

    private RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    public void sendData(long orderId){
        //1  在生产者发送消息的时候, 首先 需要从我们的ringBuffer里面 获取一个可用的序号
        long sequence = ringBuffer.next();
        try {
            //2 根据这个序号, 找到具体的 "OrderEvent" 元素 注意:此时获取的OrderEvent对象是一个没有被赋值的"空对象"
            OrderEvent event = ringBuffer.get(sequence);
            //3 进行实际的赋值处理
            event.setOrderId(orderId);
        }finally {
            //4 提交发布操作
            // 这样保证事件一定发布。
            // 如果我们使用RingBuffer.next()获取一个事件槽，
            // 那么一定要发布对应的事件。如果不能发布事件，那么就会引起Disruptor状态的混乱。
            // 尤其是在多个事件生产者的情况下会导致事件消费者失速，从而不得不重启应用才能会恢复。
            ringBuffer.publish(sequence);
        }
    }

}
