package com.nx.disruptor.quickstart;

import com.lmax.disruptor.EventFactory;

public class OrderEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        // 返回一个空事件对象（Event）
        return new OrderEvent();
    }
}
