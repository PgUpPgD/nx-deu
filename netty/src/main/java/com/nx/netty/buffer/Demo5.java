package com.nx.netty.buffer;

import java.nio.ByteBuffer;

public class Demo5 {
    public static void main(String[] args) {
        ByteBuffer buffer= ByteBuffer.allocate(100);
        buffer.putChar('a');  //2
        buffer.putInt(2); //4
        buffer.putLong(50000L); //8
        buffer.putShort((short) 2); //2
        buffer.putDouble(12.4);  //8
        System.out.println(buffer.position());
        buffer.flip();
        System.out.println(buffer.getChar());
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getShort());
        System.out.println(buffer.getDouble());
    }
}
