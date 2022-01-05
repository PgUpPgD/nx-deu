package com.nx.netty.heima.day01;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ReadBorder {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true){
            Socket socket = serverSocket.accept();
            InputStream in = socket.getInputStream();
            byte[] bytes = new byte[4];
            while (true){
                int read = in.read(bytes);
                if (read == -1){
                    break;
                }
                System.out.println(new String(bytes));
            }
        }
    }
}
