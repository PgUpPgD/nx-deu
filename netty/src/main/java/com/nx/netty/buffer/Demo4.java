package com.nx.netty.buffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Demo4 {
    public static void main(String[] args) throws IOException {
        FileOutputStream fileOutputStream=new FileOutputStream("dome4write.txt");
        FileInputStream fileInputStream=new FileInputStream("D:\\Ideal\\nx-deu\\netty\\src\\main\\resources\\dome4read.txt");
        FileChannel channelRead = fileInputStream.getChannel();
        FileChannel channelWrite = fileOutputStream.getChannel();
        //jvm堆内存，由于受GC影响，读写效率低
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //堆外内存，系统内存（少一次拷贝），读写效率高，但分配空间时效率低
//        ByteBuffer byteBuffer=ByteBuffer.allocateDirect(100);
        while (true){
            //清空缓存，到写模式，即写入到缓存
            byteBuffer.clear();
            //把文件读到缓存
            int readNumber = channelRead.read(byteBuffer);
            System.out.println(readNumber);
            if (-1 == readNumber){
                break;
            }
            //重置指针，到读模式，即从缓存中读取
            byteBuffer.flip();
            //缓存写出到文件，也是读缓存，其position和limit也会变
            channelWrite.write(byteBuffer);
        }
        fileOutputStream.close();
        fileInputStream.close();
    }
}
