package com.nx.netty.heima.day03.zb;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 *  maxFrameLength 最大长度，
 *  lengthFieldOffset 长度偏移，      4.在读取 1 的内容长度前 偏移掉 n 个字节
 *  lengthFieldLength 长度占用字节，  1.前 n 个字节，表示 传输的内容长度
 *  lengthAdjustment 长度调整，       3.在 1 后面在跳过 n 个字节，然后在读取 1 表示的 n 个长度为内容
 *  initialBytesToStrip 剥离字节数    2.去掉前面 n 个字节，表示前 n 个字节不是要展示的‘内容’（hello），不用展示
 */
public class LengthFieldBasedFrameDecoderTest {

    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
            new LengthFieldBasedFrameDecoder(
               1024,0,4,0,0
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
        buf.writeInt(length);    //lengthFieldLength  int 是4个字节，前4个字节表示 内容长度 length
        buf.writeBytes(bytes);
    }
    /**
     *          +-------------------------------------------------+
     *          |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
     * +--------+-------------------------------------------------+----------------+
     * |00000000| 00 00 00 0b 48 65 6c 6c 6f 20 77 6f 72 6c 64    |....Hello world |
     * +--------+-------------------------------------------------+----------------+
     *
     *          +-------------------------------------------------+
     *          |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
     * +--------+-------------------------------------------------+----------------+
     * |00000000| 00 00 00 03 48 69 21                            |....Hi!         |
     * +--------+-------------------------------------------------+----------------+
     *
     */
}
