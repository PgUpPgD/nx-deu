package com.nx.disruptor.multi;

import com.lmax.disruptor.RingBuffer;

public class OrderProducer {

    private RingBuffer<Order> ringBuffer;

    public OrderProducer(RingBuffer<Order> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    public void sendData(String uuid){
        long sequence = ringBuffer.next();
        try {
            Order order = ringBuffer.get(sequence);
            order.setId(uuid);
        }finally {
            ringBuffer.publish(sequence);
        }
    }
}
