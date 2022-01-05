package com.nx.netty.buffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Demo9 {

    //两个Channel传递数据
    public static void main(String[] args) throws IOException {
        FileChannel from = new FileInputStream("D:\\Ideal\\nx-deu\\netty\\src\\main\\resources\\dome2.txt").getChannel();
        FileChannel to = new FileOutputStream("dome2.txt").getChannel();
        //效率高，底层会利用操作系统的零拷贝进行优化，最大2G
        long size = from.size();
        //left 还剩余多少字节
        for (long left = size; left > 0;){
            //transferTo 会返回已传输的字节数
            left -= from.transferTo(size - left, left, to);
        }
    }
}
