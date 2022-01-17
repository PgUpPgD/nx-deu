package com.itcast.nettychathm;


import com.itcast.nettychathm.config.Config;
import com.itcast.nettychathm.message.LoginRequestMessage;
import com.itcast.nettychathm.message.Message;
import com.itcast.nettychathm.protocol.MessageCodecSharable;
import com.itcast.nettychathm.protocol.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;

public class NettyChatHmApplicationTest {

    // 测试 MessageCodecSharable 编解码器 和 序列化
    public static void main(String[] args) {
        MessageCodecSharable code = new MessageCodecSharable();
        LoggingHandler logging = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(logging, code, logging);

        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
//        channel.writeOutbound(message);

        ByteBuf buf = messageToByteBuf(message);
        channel.writeInbound(buf);
    }

    public static ByteBuf messageToByteBuf(Message msg) {
        int algorithm = Config.getSerializerAlgorithm().ordinal();
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeBytes(new byte[]{1, 2, 3, 4});
        out.writeByte(1);
        out.writeByte(algorithm);
        out.writeByte(msg.getMessageType());
        out.writeInt(msg.getSequenceId());
        out.writeByte(0xff);
        byte[] bytes = Serializer.Algorithm.values()[algorithm].serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
        return out;
    }
}
