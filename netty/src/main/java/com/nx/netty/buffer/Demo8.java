package com.nx.netty.buffer;

import java.nio.ByteBuffer;

public class Demo8 {

    public static void main(String[] args) {
        /**
         * 网络上有多条数据发送给服务端，数据之间使用 \n 进行分割
         * 但由于某种原因这些数据在接收时，被进行重新组合，例如
         * Hello, world\n
         * I`m zhangsan\n
         * How are you?\n
         * 变成了下面的两个 byteBuffer （黏包，半包）
         * Hello, world\nI`m zhangsan\nHo
         * w are you?\n
         * 现在要求，将错乱的数据恢复成原始的按 \n 分割的数据
         */
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello, world\nI`m zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
    }

    private static void split(ByteBuffer source){
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            //找到一条完整信息
            if (source.get(i) == '\n'){
                int length = i + 1 - source.position();
                //把完整的信息存入新的 ByteBuffer
                ByteBuffer target = ByteBuffer.allocate(length);
                //从source读 向target写
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                target(target);
            }
        }
        //把没读完的压在最前面，此时position指针指向下一个
        source.compact();
    }

    private static void target(ByteBuffer target){
        target.flip();
        while (target.hasRemaining()){
            System.out.print((char) target.get());
        }
        System.out.println();
    }

}
