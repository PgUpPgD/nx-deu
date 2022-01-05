package com.nx.netty.buffer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Demo3 {
    public static void main(String[] args) throws IOException {
        //文件输出流，从缓存到 文件
        FileOutputStream fileOutputStream = new FileOutputStream("dome3.txt");
        //文件输出流通道
        FileChannel channel = fileOutputStream.getChannel();
        //缓冲池
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //字符串转为byte数组，写入到缓存byteBuffer
        byte[] bytes = "hello world".getBytes();
        //写入到缓存
        byteBuffer.put(bytes);
        //重置指针
        byteBuffer.flip();
        //从通道写出至文件
        channel.write(byteBuffer);
        fileOutputStream.close();
    }
}
