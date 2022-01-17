package com.nx.disruptor.chain;


import com.lmax.disruptor.EventHandler;

public class Handler1 implements EventHandler<TradeEvent> {

    @Override
    public void onEvent(TradeEvent tradeEvent, long l, boolean b) throws Exception {
        System.out.println("Handler1:" + b);
    }
}
