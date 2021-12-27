package com.nx.netty.oneReactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 接受连接请求的线程 socket 和用户交互
 */
public class Acceptor implements Runnable{
    private final ServerSocketChannel ssc;
    private final Selector selector;

    public Acceptor(Selector selector, ServerSocketChannel ssc) {
        this.ssc=ssc;
        this.selector=selector;
    }

    @Override
    @SuppressWarnings("all")
    public void run() {
        try {
            //创建连接客户端的socket
            SocketChannel socket = ssc.accept();
            System.out.println(socket.socket().getRemoteSocketAddress().toString() + " is connected.");
            if (socket != null){
                socket.configureBlocking(false); //设置为非阻塞
                //SocketChannel向selector注册一个OP_READ事件，然后返回该通道的key
                SelectionKey key = socket.register(selector, SelectionKey.OP_READ);
                //使一个阻塞住的selector操作立即返回
                selector.wakeup();
                //给key一个附加对象
                key.attach(new TCPHandler(key, socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
