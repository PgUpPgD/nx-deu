package com.nx.netty.manyReactor;


import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {
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
            //生成真正的socket连接客户端
            SocketChannel sc= ssc.accept();
            System.out.println(sc.socket().getRemoteSocketAddress().toString() + " is connected.");

            if(sc!=null) {
                //设置非阻塞
                sc.configureBlocking(false);
                //SocketChannel向selector注册一个OP_READ事件，然后返回该通道的key
                SelectionKey sk = sc.register(selector, SelectionKey.OP_READ);
                selector.wakeup(); // 使一个阻塞住的selector操作立即返回
                sk.attach(new TCPHandler(sk, sc));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
