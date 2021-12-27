package com.nx.nettychat.hander;

import com.nx.nettychat.processor.MsgProcessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于检测channel的心跳handler
 * 继承ChannelInboundHandlerAdapter，从而不需要实现channelRead0方法
 */
@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    int readIdleTimes =  0;
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        ChannelId channelId = ctx.channel().id();
        // 不是socket通信的channel，我们就不需要处理
        if(!MsgProcessor.onlineUserSet.contains(channelId)){
            return;
        }
        // 判断evt是否是IdleStateEvent（用于触发用户事件，包含 读空闲/写空闲/读写空闲 ）
        if (!(evt instanceof IdleStateEvent)) {
            return;
        }
        IdleStateEvent event = (IdleStateEvent)evt;// 强制类型转换
        String eventType = null;
        switch (event.state()){
            case READER_IDLE:
                eventType = "读空闲";
                readIdleTimes ++;
                break;
            case WRITER_IDLE:
                eventType ="写空闲";
                break;
            case ALL_IDLE:
                eventType = "读写空闲";
                break;
        }

        log.info(ctx.channel().remoteAddress() + "超时事件：" + eventType);
        if(readIdleTimes > 3){
            log.info("channel关闭前，users的数量为：" + MsgProcessor.onlineUsers.size());
            Channel channel = ctx.channel();
            // 关闭无用的channel，以防资源浪费
            channel.close();
            log.info("channel关闭后，users的数量为：" + MsgProcessor.onlineUsers.size());
        }
    }
}
