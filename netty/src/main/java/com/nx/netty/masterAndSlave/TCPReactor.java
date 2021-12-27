package com.nx.netty.masterAndSlave;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TCPReactor implements Runnable{

    private final ServerSocketChannel ssc;
    private final Selector selector;

    public TCPReactor(int port) throws IOException {
        selector = Selector.open();
        ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        SelectionKey sk = ssc.register(selector,SelectionKey.OP_ACCEPT); // ServerSocketChannel向selector註冊一個OP_ACCEPT事件，然後返回該通道的key
        sk.attach(new Acceptor(ssc)); // 給定key一個附加的Acceptor對象

        InetSocketAddress addr = new InetSocketAddress(port);
        ssc.socket().bind(addr); // 在ServerSocketChannel綁定監聽端口
    }

    @Override
    @SuppressWarnings("all")
    //主线程执行OP_ACCEPT监听
    public void run() {
        while (!Thread.interrupted()) { // 在線程被中斷前持續運行
            System.out.println("mainReactor waiting for new event on port: "
                    + ssc.socket().getLocalPort() + "...");
            try {
//                selector.select(2000);
//                selector.selectNow();
                if (selector.select() == 0) // 若沒有事件就緒則不往下執行
                    continue;
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<SelectionKey> selectedKeys = selector.selectedKeys(); // 取得所有已就緒事件的key集合
            Iterator<SelectionKey> it = selectedKeys.iterator();
            while (it.hasNext()) {
                dispatch((SelectionKey) (it.next())); // 根據事件的key進行調度
                it.remove();
            }
        }
    }

    private void dispatch(SelectionKey key) {
        Runnable r = (Runnable) (key.attachment()); // 根據事件之key綁定的對象開新線程
        if (r != null)
            r.run();
    }
}
