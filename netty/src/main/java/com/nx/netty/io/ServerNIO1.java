package com.nx.netty.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 传统BIO
 * 如果需要处理并发  是需要服务端实现多线程
 * 缺点：假如100个连接，只有10个线程是真正在和用户交互的，另外90个线程只是挂着，造成资源浪费
 *
 * 解决？ 不阻塞
 * 让 accept() 和 read() 非阻塞
 * 优点：单线程解决多线程的并发问题
 * 服务端select维护一个列表，里面存的是socket对象，只需要遍历这个列表，就可以完成对各个socket
 * 事件的处理
 */
@Slf4j(topic = "e")
public class ServerNIO1 {

    //jdk 对 ServerSocket 的优化，非阻塞 ServerSocketChannel
    private static ServerSocketChannel listener;

    //存放历史客户端socket的容器,因为是while(true),不保留历史socket，会导致消息丢失
    static List<SocketChannel> list = new ArrayList<>();
    //
    static ByteBuffer readbuf = ByteBuffer.allocate(1024);

    public static void main(String[] args) {
        try {
            //监听有没有客户端连接
            listener = ServerSocketChannel.open();
            //设置非阻塞
            listener.configureBlocking(false);
            listener.bind(new InetSocketAddress("127.0.0.1", 8080));
            log.debug("server start");
            while (true){
                //和客户端通讯的socket 非阻塞
                SocketChannel socket = listener.accept();
                //没人连接
                if (socket == null){
                    log.debug("no conn");
                    TimeUnit.SECONDS.sleep(3);
                }
                //有人连接
                else {
                    log.debug("a client conn success");
                    //设置非阻塞
                    socket.configureBlocking(false);
                    list.add(socket);
                }
                queryData();
            }
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void queryData() throws IOException{
        for (SocketChannel socket : list){
            int read = socket.read(readbuf);
            if (read > 0){
                readbuf.flip();
                byte[] bytes = new byte[readbuf.remaining()];
                readbuf.get(bytes);
                String content = new String(bytes, StandardCharsets.UTF_8);
                log.debug("content---{}", content);
                readbuf.clear();
            }
        }
    }

}
