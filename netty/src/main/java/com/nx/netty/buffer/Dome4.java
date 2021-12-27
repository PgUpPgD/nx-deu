package com.nx.netty.buffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Dome4 {
    public static void main(String[] args) throws IOException {
        FileOutputStream fileOutputStream=new FileOutputStream("dome4write.txt");
        FileInputStream fileInputStream=new FileInputStream("D:\\Ideal\\nx-deu\\netty\\src\\main\\resources\\dome4read.txt");
        FileChannel channelRead = fileInputStream.getChannel();
        FileChannel channelWrite = fileOutputStream.getChannel();
        //jvm内存
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //堆外内存
//        ByteBuffer byteBuffer=ByteBuffer.allocateDirect(100);
        while (true){
            byteBuffer.clear();
            int readNumber = channelRead.read(byteBuffer);
            System.out.println(readNumber);
            if (-1 == readNumber){
                break;
            }
            byteBuffer.flip();
            channelWrite.write(byteBuffer);
        }
        fileOutputStream.close();
        fileInputStream.close();
    }
}
