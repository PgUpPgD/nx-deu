package com.nx.netty.heima.day01;

import com.nx.netty.heima.util.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BlockIo {
    public static void main(String[] args) throws IOException {
        // 使用 nio 来理解阻塞模式, 单线程
        // 0. ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 1. 创建了服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 2. 绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        // 3. 连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true){
            // 4. accept 建立与客户端连接， SocketChannel 用来与客户端之间通信
            // 阻塞方法，线程停止运行
            SocketChannel sc = ssc.accept();
            channels.add(sc);
            for (SocketChannel socket : channels){
                // 5. 接收客户端发送的数据
                log.debug("before read... {}", socket);
                // 阻塞方法，线程停止运行
                socket.read(buffer);
                buffer.flip();
                ByteBufferUtil.debugRead(buffer);
                buffer.clear();
                log.debug("after read...{}", socket);
            }
        }
    }

}
