package com.nx.netty.buffer;

import java.nio.ByteBuffer;

public class Dome6 {
    public static void main(String[] args) {
        ByteBuffer byteBuffer=ByteBuffer.allocate(10);
        for(int i=0;i<byteBuffer.capacity();++i){
            byteBuffer.put((byte)i);
        }
        //设置指针和limit位置
        byteBuffer.position(2);
        byteBuffer.limit(8);
        //slice()方法用于创建一个新的字节缓冲区，对resetBuffer的修改也会改变byteBuffer
        //新缓冲区position2 和 limit8从原来位置位移到 position = 0 limit = 6 的位置
        ByteBuffer resetBuffer = byteBuffer.slice();
        for(int i=0;i<resetBuffer.capacity();i++){
            byte anInt = resetBuffer.get();
            resetBuffer.put(i, (byte) (anInt*2));
        }

        byteBuffer.position(0);
        byteBuffer.limit(byteBuffer.capacity());
        while (byteBuffer.hasRemaining()){
            System.out.println(byteBuffer.get());
        }
    }
}
