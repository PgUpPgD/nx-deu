package com.nx.netty.heima.day02;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * channel 的主要作用
 *
 * - close() 可以用来关闭 channel
 * - closeFuture() 用来处理 channel 的关闭
 *   - sync 方法作用是同步等待 channel 关闭
 *   - 而 addListener 方法是异步等待 channel 关闭
 * - pipeline() 方法添加处理器
 * - write() 方法将数据写入
 * - writeAndFlush() 方法将数据写入并刷出
 */
@Slf4j
public class ChannelTest {
    public static void main(String[] args) throws InterruptedException {
        // 客户端代码
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                /**
                 * 异步非阻塞
                 * 意味着不等连接建立，方法执行就返回了。因此 channelFuture 对象中不能【立刻】
                 * 获得到正确的 Channel 对象, 那么 channelFuture 对象不完整，没有对端信息
                 * channelFuture.channel().writeAndFlush 将没有作用
                 */
                .connect("localhost", 8080);

        //所以 需要 .sync() 来同步等待连接建立完成，等待 connect 返回
        //channelFuture.sync().channel().writeAndFlush(new Date() + ": hello world !");

        //第二种方法  等建立连接后，通过回调获取 future
        channelFuture.addListener((ChannelFutureListener) future ->{
            Channel channel = future.channel();
            System.out.println(channel);
        });
    }
}
