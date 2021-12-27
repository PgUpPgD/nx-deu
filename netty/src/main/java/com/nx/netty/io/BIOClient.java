package com.nx.netty.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

@Slf4j(topic = "e")
public class BIOClient {

    static byte[] bs = new byte[1024];

    public static void main(String[] args) throws IOException {

        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", 8080));
            System.out.println("conn success");
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("hello".getBytes());
            outputStream.flush();

            //阻塞
            Scanner scanner = new Scanner(System.in);
            scanner.next();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
