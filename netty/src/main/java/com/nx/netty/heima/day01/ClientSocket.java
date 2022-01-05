package com.nx.netty.heima.day01;

import java.io.IOException;
import java.net.Socket;

public class ClientSocket {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost",8080);
            System.out.println(socket);
            socket.getOutputStream().write("hello world".getBytes());
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
