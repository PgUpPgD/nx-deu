package com.nx.netty.manyReactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public class WriteState implements HandlerState{

    @Override
    public void changeState(TCPHandler h) {
        h.setState(new ReadState());
    }

    @Override
    @SuppressWarnings("all")
    public void handle(TCPHandler h, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException {
        String str = "Your message has sent to "
                + sc.socket().getLocalSocketAddress().toString() + "\r\n";
        ByteBuffer buf = ByteBuffer.wrap(str.getBytes());

        while (buf.hasRemaining()) {
            //写给client
            sc.write(buf);
        }

        //改变状态(SENDING->READING)
        h.setState(new ReadState());
        //改变通道的监听事件
        sk.interestOps(SelectionKey.OP_READ);
        sk.selector().wakeup();
    }
}
