package com.nx.netty.heima.day02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * NioEventLoopGroup 2个事件循环对象 处理io事件
 * 服务器端两个 nio worker 工人
 */
@Slf4j
public class EventLoop {
    public static void main(String[] args) throws InterruptedException {
        // handler 也使用多线程
        DefaultEventLoopGroup normalWorkers = new DefaultEventLoopGroup(2);
        // 负责组装netty组件，启动服务器
        new ServerBootstrap()
                //server
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                //socket
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // LoggingHandler 方便看日志
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(normalWorkers, "myhandler",
                                new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf byteBuf = msg instanceof ByteBuf ? (ByteBuf)msg : null;
                                        if (byteBuf != null){
                                            byte[] buf = new byte[16];
                                            byteBuf.readBytes(buf, 0, byteBuf.readableBytes());
                                            log.debug(new String(buf));
                                        }
                                    }
                                });
                    }
                }).bind(8080).sync();
    }
}
