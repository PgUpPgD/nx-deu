package com.nx.netty.manyReactor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            TCPReactor reactor = new TCPReactor(1333);
            reactor.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
