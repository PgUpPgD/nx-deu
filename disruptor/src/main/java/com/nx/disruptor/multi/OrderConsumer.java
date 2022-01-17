package com.nx.disruptor.multi;

import com.lmax.disruptor.WorkHandler;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

//消费者
public class OrderConsumer implements WorkHandler<Order> {

    private String consumerId;
    //统计总消费的消息，比对是否漏掉
    private static AtomicInteger count = new AtomicInteger(0);
    private Random random = new Random();
    public OrderConsumer(String consumerId){
        this.consumerId = consumerId;
    }

    @Override
    public void onEvent(Order order) {
        System.err.println("当前消费者：" + this.consumerId + ",消费消息ID:" + order.getId());
        count.incrementAndGet();
    }

    public int getCount(){
        return count.get();
    }
}
