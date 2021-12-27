package com.nx.nettychat.hander;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class ChatServerChannelInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        //处理消息的责任链
        ChannelPipeline pipeline = ch.pipeline();

        // ================支持http协议  start =========================
        //因为我们这里需要处理http请求所以，我们需要http编码器
        pipeline.addLast(new HttpServerCodec());//Inbound,Outbound
        //主要用于处理大数据流,比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的 ,加上这个handler我们就不用考虑这个问题了
        pipeline.addLast(new ChunkedWriteHandler());//Inbound,Outbound
        //主要是将同一个http请求或响应的多个消息对象变成一个 fullHttpRequest或者FullHttpResponse完整的消息对象
        //几乎在netty中的编程，都会使用到此hanler,组装报文
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));//Inbound

        /**
         * 自定义处理web页面，解析http协议的
         */
        pipeline.addLast(new HttpServerHandler()); //Inbound
        // ================支持http协议  end =========================


        // ====================== 增加心跳支持 start    ======================
        // 针对客户端，如果在1分钟时没有向服务端发送读写心跳(ALL)，则主动断开
        // 如果是读空闲或者写空闲，不处理
        // 正式环境 20， 40 ，60
        pipeline.addLast(new IdleStateHandler(4, 0, 0));
        // 自定义的空闲状态检测
        pipeline.addLast(new HeartBeatHandler());
        // ====================== 增加心跳支持 end    ======================


        // ====================== 解析WebSocket请求 start    ======================
        /**
         * websocket 服务器处理的协议，用于指定给客户端连接访问的路由 : /ws
         * 本handler会帮你处理一些繁重的复杂的事
         * 会帮你处理握手动作： handshaking（close, ping, pong） ping + pong = 心跳
         * 对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws")); //Inbound
        /**
         *  用户处理websocket通讯的
         */
        pipeline.addLast(new WebSocketServerHandler()); //Inbound
        // ================== 解析WebSocket请求 end ==========================
    }
}
