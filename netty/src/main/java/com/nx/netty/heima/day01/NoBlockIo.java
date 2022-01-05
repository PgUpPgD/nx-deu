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
public class NoBlockIo {

    public static void main(String[] args) throws IOException {
        // 使用 nio 来理解非阻塞模式, 单线程
        // 0. ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 1. 创建了服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 非阻塞模式 使ssc.accept();不会阻塞
        ssc.configureBlocking(false);
        // 2. 绑定监听的端口
        ssc.bind(new InetSocketAddress(8080));
        // 3. socket 集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true){
            // 4. accept 建立与客户端连接， SocketChannel 用来与客户端之间通信
            // 非阻塞，线程还会继续运行，如果没有连接建立，但sc是null
            SocketChannel sc = ssc.accept();
            // 这条日志会刷屏，说明非阻塞
//            log.debug("没有阻塞 此时的 sc is：{}", sc);
            if(sc != null){
                log.debug("connected... {}", sc);
                // 非阻塞模式 socket.read(buffer);不会阻塞
                sc.configureBlocking(false);
                channels.add(sc);
            }
            for (SocketChannel socket : channels){
                // 5. 接收客户端发送的数据
                // 非阻塞，没有读到数据返回0，客户端正常断开返回-1，客户端断开进程抛出异常
                int read = socket.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    ByteBufferUtil.debugRead(buffer);
                    buffer.clear();
                    log.debug("after read...{}", socket);
                }
            }
        }
    }

}
