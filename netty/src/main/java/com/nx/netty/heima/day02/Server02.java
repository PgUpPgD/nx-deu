package com.nx.netty.heima.day02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * NioEventLoopGroup 单个事件循环对象
 */
@Slf4j
public class Server02 {
    public static void main(String[] args) throws InterruptedException {
        new ServerBootstrap()
                //        类似nio boss                         worker
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                //         选择服务 socket 实现类 为 nio
                .channel(NioServerSocketChannel.class)
                // 接下来添加的处理器都是给 SocketChannel 用的，而不是给 ServerSocketChannel。
                // ChannelInitializer 处理器（仅执行一次），它的作用是待客户端 SocketChannel
                // 建立连接后，执行 initChannel 以便添加更多的处理器
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = msg instanceof ByteBuf ? ((ByteBuf)msg) : null;
                                if (byteBuf != null){
                                    byte[] buf = new byte[16];
                                    ByteBuf len = byteBuf.readBytes(buf, 0, byteBuf.readableBytes());
                                    log.debug(new String(buf));
                                }
                            }
                        });
                    }
                })
                // ServerSocketChannel 绑定的监听端口
                .bind(8080).sync();
    }
}
