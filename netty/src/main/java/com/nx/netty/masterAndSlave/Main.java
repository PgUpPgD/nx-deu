package com.nx.netty.masterAndSlave;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            TCPReactor reactor = new TCPReactor(1333);
//                reactor.run();
            Thread thread = new Thread(reactor);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}