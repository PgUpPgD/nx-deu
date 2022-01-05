package com.nx.netty.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
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

    private static ExecutorService executorService = Executors.newCachedThreadPool();

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
                System.out.println("run ...");
                //获取输入流
                InputStream inputStream = socket.getInputStream();
                byte[] bytes = new byte[1024];
                int read = inputStream.read(bytes);
                System.out.println(read + " content: " + new String(bytes));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
