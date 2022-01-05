package com.nx.netty.heima.day01;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ReadBorderClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        OutputStream out = socket.getOutputStream();
        out.write("hello".getBytes());
        out.write("world".getBytes());
        out.write("你好".getBytes());
        out.flush();
        socket.close();
    }
}
