package com.nx.nettychat.disruptor;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.WorkHandler;
import com.nx.nettychat.entity.TranslatorDataWapper;
import com.nx.nettychat.processor.MsgProcessor;
import com.nx.nettychat.protocol.IMMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

//消费者
@Slf4j
public class MessageConsumer implements WorkHandler<TranslatorDataWapper> {

    protected String consumerId;

    public MessageConsumer(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    @Override
    public void onEvent(TranslatorDataWapper event) throws Exception {
        IMMessage message = event.getImMessage();
        ChannelHandlerContext ctx = event.getCtx();
        //1.业务处理逻辑:
        log.info("业务逻辑处理：" + JSON.toJSONString(message));
        //2.回送响应信息:
        MsgProcessor.getInstance().dealMsg(ctx,message);
    }
}
