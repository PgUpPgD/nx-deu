package com.nx.netty.heima.day03.zb;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class LengthFieldBasedFrameDecoderTest2 {

    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
            new LengthFieldBasedFrameDecoder(
               1024,1,4,1,6
            ), new LoggingHandler(LogLevel.DEBUG)
        );

        // 4 个字节的内容长度， 实际内容
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        send(buffer, "Hello world");
        send(buffer, "Hi!");
        channel.writeInbound(buffer);
    }

    private static void send(ByteBuf buf, String content){
        //实际内容
        byte[] bytes = content.getBytes();
        //内容长度
        int length = bytes.length;
        buf.writeByte(1);       //例如给 length前面在添加 内容标记（如版本信息等。。。）lengthFieldOffset 读取时偏移掉
        buf.writeInt(length);    //lengthFieldLength  int 是4个字节，前4个字节表示 内容长度 length
        buf.writeByte(1);       //例如给 length后面在添加 内容标记（如版本信息等。。。）
        buf.writeBytes(bytes);
    }
    /**
     *          +-------------------------------------------------+
     *          |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
     * +--------+-------------------------------------------------+----------------+
     * |00000000| 00 00 00 0b 48 65 6c 6c 6f 20 77 6f 72 6c 64    |Hello world |
     * +--------+-------------------------------------------------+----------------+
     *
     *          +-------------------------------------------------+
     *          |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
     * +--------+-------------------------------------------------+----------------+
     * |00000000| 00 00 00 03 48 69 21                            |Hi!         |
     * +--------+-------------------------------------------------+----------------+
     *
     */
}
