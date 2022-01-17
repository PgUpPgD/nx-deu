package com.nx.nettychat.hander;

import com.nx.nettychat.disruptor.MessageProducer;
import com.nx.nettychat.disruptor.RingBufferWorkerPoolFactory;
import com.nx.nettychat.processor.MsgProcessor;
import com.nx.nettychat.protocol.IMMessage;
import com.nx.nettychat.util.CoderUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    /**
     * 业务处理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //msg.text()获取客户端传输过来的消息
        IMMessage message = CoderUtil.decode(msg.text());
        //自已的应用服务应该有一个ID生成规则
        String producerId = "code:sessionId:001";
        MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
        messageProducer.sendData(message, ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
        ctx.channel().close();
    }

    /**
     * 关闭窗口则移除对应的channel
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        MsgProcessor.getInstance().removeChannelSendMsg(ctx.channel());
    }
}
