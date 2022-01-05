package com.nx.netty.heima.day02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * NioEventLoopGroup 单个事件循环对象
 */
@Slf4j
public class Server01 {
    public static void main(String[] args) {
        new ServerBootstrap()
                //         线程池 + Selector
                .group(new NioEventLoopGroup())
                //         选择服务 socket 实现类 为 nio
                .channel(NioServerSocketChannel.class)
                // 接下来添加的处理器都是给 SocketChannel 用的，而不是给 ServerSocketChannel。
                // ChannelInitializer 处理器（仅执行一次），它的作用是待客户端 SocketChannel
                // 建立连接后，执行 initChannel 以便添加更多的处理器
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 解码 ByteBuf => String
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                log.debug("msg: " + msg);
                            }
                        });
                    }
                })
                // ServerSocketChannel 绑定的监听端口
                .bind(8080);
    }
}
