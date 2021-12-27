package com.nx.netty.buffer;


import java.nio.IntBuffer;
import java.security.SecureRandom;

public class Dome1 {
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(6);
        for (int i = 0; i < buffer.capacity(); i++) {
            int nextInt = new SecureRandom().nextInt(20);
            buffer.put(nextInt);
            //put read write 都会改变buffer的position位置
        }
        //让 limit = position;  position = 0;
        buffer.flip();
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}
