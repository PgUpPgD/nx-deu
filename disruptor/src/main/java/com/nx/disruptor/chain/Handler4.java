package com.nx.disruptor.chain;

import com.lmax.disruptor.EventHandler;

public class Handler4 implements EventHandler<TradeEvent> {

	public void onEvent(TradeEvent event, long sequence, boolean endOfBatch) throws Exception {
		System.err.println("handler 4 : SET PRICE");
	//	Thread.sleep(1000);
		event.setPrice(17.0);
	}

}
