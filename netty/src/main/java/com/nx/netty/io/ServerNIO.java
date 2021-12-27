package com.nx.netty.io;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 传统BIO
 * 如果需要处理并发  是需要服务端实现多线程
 * 缺点：假如100个连接，只有10个是真正在和用户交互的，另外90个只是挂着，造成资源浪费
 */
@Slf4j(topic = "e")
public class ServerNIO {

    static byte[] bs = new byte[1024];
    static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        try {
            ServerSocket listener = new ServerSocket(8080);
            while (true){
                log.debug("waiting conn");
                Socket socket = listener.accept();
                log.debug("conn success");
                //实例化一个任务
                Task task = new Task(socket);
                executorService.execute(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Task implements Runnable{

        Socket socket;
        public Task(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                //获取输入流
                InputStream inputStream = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader buffer = new BufferedReader(reader);
                String content;
                //buffer.readLine()此处不发消息阻塞，不会影响后面其它线程的连接
                while ((content = buffer.readLine()) != null){
                    log.debug("content---{}", content);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
