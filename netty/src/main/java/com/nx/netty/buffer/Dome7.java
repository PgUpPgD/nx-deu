package com.nx.netty.buffer;

import java.nio.ByteBuffer;

public class Dome7 {
    public static void main(String[] args) {
        ByteBuffer byteBuffer=ByteBuffer.allocate(10);  //jvm的内存
        for(int i=0;i<byteBuffer.capacity();i++){
            byteBuffer.put((byte)i);
        }
        //只读buffer
        ByteBuffer byteBuffer1 = byteBuffer.asReadOnlyBuffer();
        System.out.println(byteBuffer.getClass());
        System.out.println(byteBuffer1.getClass());
        byteBuffer1.flip();
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer1.position());
        for(int i=0;i<byteBuffer1.capacity();i++){
            System.out.println(byteBuffer1.get());
        }
    }
}
