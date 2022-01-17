package com.nx.disruptor.chain;


import com.lmax.disruptor.EventHandler;

public class Handler2 implements EventHandler<TradeEvent> {

    @Override
    public void onEvent(TradeEvent tradeEvent, long l, boolean b) throws Exception {
        System.out.println("Handler2:" + b);
    }
}
