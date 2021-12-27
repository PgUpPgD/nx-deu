package com.nx.netty.oneReactor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

//            Selector abstractSelector = SelectorProvider.provider().openSelector();
//            ServerSocketChannel serverSocketChannel = SelectorProvider.provider().openServerSocketChannel();
//            System.out.println(abstractSelector.selectNow());
        try {
            TCPReactor reactor = new TCPReactor(1333);
            reactor.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
