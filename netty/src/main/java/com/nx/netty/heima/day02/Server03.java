package com.nx.netty.heima.day02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class Server03 {

    public static void main(String[] args) {
        //创建服务器
        new ServerBootstrap()
                //  boss 1 accept  worker 2 read write
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                //  nio
                .channel(NioServerSocketChannel.class)
                //  nio socket
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = null;
                                if (msg instanceof ByteBuf){
                                    buf = (ByteBuf) msg;
                                    System.out.println(buf.toString(Charset.defaultCharset()));
                                }
                                ByteBuf response = ctx.alloc().buffer(16);
                                response.writeBytes(buf);
                                ctx.writeAndFlush(response);
                                if (buf != null){
                                    buf.release();
                                }
                            }
                        });
                    }
                })
                .bind(new InetSocketAddress(8080));
    }

}
