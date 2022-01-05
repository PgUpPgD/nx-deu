package com.nx.netty.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * bio--block io 阻塞io
 * 阻塞带来的问题
 * .accept()阻塞  没有人连接，阻塞在这里
 * .read()阻塞   当前的人连接了，却不发消息，阻塞，会出现多个人连接，
 *              但其中一个人不发消息，则全阻塞在这里
 */
@Slf4j(topic = "e")
public class BIOServer {

    static byte[] bs = new byte[1024];

    public static void main(String[] args) throws IOException {
        /**
         * 实例化一个serverSocket
         * serverSocket 只是一个监听器，监听有谁来连接我
         */
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true){
            //通过监听器创建和客户端对应的socket 此 socket 才是真正和客户端通讯的socket
            Socket socket = serverSocket.accept();
            socket.getInputStream().read(bs);
            String content = new String(bs);
            log.debug("content--[{}]", content);
        }

    }
}
