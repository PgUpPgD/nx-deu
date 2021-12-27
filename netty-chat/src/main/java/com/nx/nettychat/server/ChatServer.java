package com.nx.nettychat.server;

import com.nx.nettychat.hander.ChatServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatServer implements DisposableBean {

    private int port = 20111;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap server;

    public ChatServer(){
        //主从线程模型的线程池
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        server = new ServerBootstrap();
        server.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                //BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
                //用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，
                //Java将使用默认值50
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChatServerChannelInitializer());
    }

    public void start() {
        try {
            server.bind(this.port).sync();
            log.info("netty websocket server 启动完毕 对应端口：{}",this.port);
        } catch (InterruptedException e) {
            e.printStackTrace();
            close();
            log.error("netty websocket server 启动失败...");
        }
    }

    public void close(){
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    // 我们想优雅的关闭netty所以我们再实现DisposableBean
    @Override
    public void destroy() {
        close();
    }

}
