package com.nx.disruptor.multi;

import com.lmax.disruptor.ExceptionHandler;

public class EventExceptionHandler implements ExceptionHandler<Order> {
    // 在消费的时候出现异常
    @Override
    public void handleEventException(Throwable throwable, long sequence, Order order) {
        System.out.println("处理事件出现异常sequence:" + sequence + "  order:" +order);
        throwable.printStackTrace();
    }

    // 当启动的时候
    @Override
    public void handleOnStartException(Throwable throwable) {
        throwable.printStackTrace();
    }

    // 当消费结束的时候
    @Override
    public void handleOnShutdownException(Throwable throwable) {
        throwable.printStackTrace();
    }
}
