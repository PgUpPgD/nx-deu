package com.nx.netty.heima.day01;

import com.nx.netty.heima.util.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class ReadServer {
    public static void main(String[] args) {
        try {
            ServerSocketChannel channel = ServerSocketChannel.open();
            channel.bind(new InetSocketAddress(8080));
            Selector selector = Selector.open();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_ACCEPT);
            while (true){
                int count = selector.select();
                log.debug("select count: {}", count);
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()){
                        ServerSocketChannel ssck = (ServerSocketChannel)key.channel();
                        SocketChannel sc = ssck.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                        log.debug("连接已建立: {}", sc);
                    }else if (key.isReadable()){
                        SocketChannel soc = (SocketChannel)key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int read = soc.read(buffer);
                        if (read == -1){
                            // 等于-1，客户端正常断开
                            key.cancel();
                            soc.close();
                        }else {
                            buffer.flip();
                            ByteBufferUtil.debugRead(buffer);
                        }
                    }
                    iterator.remove();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
