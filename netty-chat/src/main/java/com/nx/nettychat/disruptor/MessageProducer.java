package com.nx.nettychat.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.nx.nettychat.entity.TranslatorDataWapper;
import com.nx.nettychat.protocol.IMMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * 生产者
 */
public class MessageProducer {
    private RingBuffer<TranslatorDataWapper> ringBuffer;

    public MessageProducer(RingBuffer<TranslatorDataWapper> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendData(IMMessage message , ChannelHandlerContext ctx){
        long sequence = ringBuffer.next();
        try{
            TranslatorDataWapper event = ringBuffer.get(sequence);
            event.setImMessage(message);
            event.setCtx(ctx);
        }finally {
            ringBuffer.publish(sequence);
        }
    }
}
