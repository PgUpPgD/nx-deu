package com.nx.nettychat.entity;

import com.nx.nettychat.protocol.IMMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

@Data
public class TranslatorDataWapper {
    private IMMessage imMessage;
    private ChannelHandlerContext ctx;
}
