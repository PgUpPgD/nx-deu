package com.nx.netty.buffer;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Demo2 {
    public static void main(String[] args) throws Exception {
        //文件输入流，从文件到缓存
        FileInputStream fileInputStream = new FileInputStream("D:\\Ideal\\nx-deu\\netty\\src\\main\\resources\\dome2.txt");
        //文件流通道
        FileChannel channel = fileInputStream.getChannel();
        //创建缓冲池
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //把文件内容读到缓存里 读完之后 read = -1
        int read = channel.read(byteBuffer);
        //重置position号和limit
        byteBuffer.flip();
        while (byteBuffer.remaining() > 0){
            System.out.println((char) byteBuffer.get());
        }
        fileInputStream.close();
    }
}
