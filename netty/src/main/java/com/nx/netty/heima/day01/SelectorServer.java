package com.nx.netty.heima.day01;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class SelectorServer {
    public static void main(String[] args) {
        try {
            //监听连接
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.bind(new InetSocketAddress(8080));
            //创建事件池
            Selector selector = Selector.open();
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while (true){
                //count 是 selector 返回的当前有事件发生的个数
                int count = selector.select();
                log.debug("selector.select(): {}", count);
                if (count <= 0){
                    continue;
                }
                // 获取所有事件
                Set<SelectionKey> keys = selector.selectedKeys();
                // 遍历所有事件，逐一处理
                Iterator<SelectionKey> iter = keys.iterator();
                while (iter.hasNext()){
                    SelectionKey key = iter.next();
                    // 判断事件类型
                    if (key.isAcceptable()){
                        ServerSocketChannel ssck = (ServerSocketChannel)key.channel();
                        // 必须处理
                        SocketChannel sc = ssck.accept();
                        log.debug("socket:{}",sc);
                    }
                    // 处理完毕，必须将事件移除
                    iter.remove();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
