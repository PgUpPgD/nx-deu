package com.itcast.nettychathm.server;

import com.itcast.nettychathm.protocol.MessageCodecSharable;
import com.itcast.nettychathm.protocol.ProtocolFrameDecoder;
import com.itcast.nettychathm.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer {

    public static void main(String[] args) {

        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup(4);
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        LoginRequestMessageHandler loginHandler = new LoginRequestMessageHandler();
        ChatRequestMessageHandler chatHandler = new ChatRequestMessageHandler();
        GroupCreateRequestMessageHandler groupCreateHandler = new GroupCreateRequestMessageHandler();
        GroupJoinRequestMessageHandler groupJoinHandler = new GroupJoinRequestMessageHandler();
        GroupMembersRequestMessageHandler groupMembersHandler = new GroupMembersRequestMessageHandler();
        GroupQuitRequestMessageHandler groupQuitHandler = new GroupQuitRequestMessageHandler();
        GroupChatRequestMessageHandler groupChatHandler = new GroupChatRequestMessageHandler();
        QuitHandler quitHandler = new QuitHandler();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(boss, worker)
                    //设置全连接 accept queue 队列 的大小
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //加入协议框架解码器，解决粘包半包
                            ch.pipeline().addLast(new ProtocolFrameDecoder());
                            //加入自带日志框架
                            ch.pipeline().addLast(loggingHandler);
                            //加入协议消息编解码器
                            ch.pipeline().addLast(messageCodec);
                            //用来判断是不是 读空闲时间过长，或 写空闲时间过长
                            //5s 内如果没有收到 channel 的数据，会触发一个 IdleState#READER_IDLE 事件
                            ch.pipeline().addLast(new IdleStateHandler(9, 0, 0));
                            //ChannelDuplexHandler 可以同时作为入站和出站处理器
                            ch.pipeline().addLast(new ChannelDuplexHandler() {
                                // 用来触发特殊事件
                                @Override
                                public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                    // 判断evt是否是IdleStateEvent（用于触发用户事件，包含 读空闲/写空闲/读写空闲 ）
                                    if (!(evt instanceof IdleStateEvent)) {
                                        return;
                                    }
                                    IdleStateEvent event = (IdleStateEvent) evt;
                                    // 触发了读空闲事件
                                    if (event.state() == IdleState.READER_IDLE) {
                                        log.debug("已经 9s 没有读到数据了");
                                        ctx.channel().close();
                                    }
                                }
                            });
                            //处理登录
                            ch.pipeline().addLast(loginHandler);
                            //个人发消息
                            ch.pipeline().addLast(chatHandler);
                            //创建群聊
                            ch.pipeline().addLast(groupCreateHandler);
                            //加入群聊
                            ch.pipeline().addLast(groupJoinHandler);
                            //获取组成员
                            ch.pipeline().addLast(groupMembersHandler);
                            //退出群聊
                            ch.pipeline().addLast(groupQuitHandler);
                            //群聊
                            ch.pipeline().addLast(groupChatHandler);
                            //退出
                            ch.pipeline().addLast(quitHandler);
                        }
                    });
            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
