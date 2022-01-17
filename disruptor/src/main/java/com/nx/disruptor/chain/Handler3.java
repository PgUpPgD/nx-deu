package com.nx.disruptor.chain;

import com.lmax.disruptor.EventHandler;

public class Handler3 implements EventHandler<TradeEvent> {

	public void onEvent(TradeEvent event, long l, boolean b) throws Exception {
		System.out.println("Handler3:" + b);
	}

}
