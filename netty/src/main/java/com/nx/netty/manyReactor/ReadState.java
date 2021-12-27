package com.nx.netty.manyReactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

public class ReadState implements HandlerState{

    private SelectionKey sk;

    @Override
    public void changeState(TCPHandler h) {
        h.setState(new WriteState());
    }

    @Override
    @SuppressWarnings("all")
    public void handle(TCPHandler h, SelectionKey sk, SocketChannel sc, ThreadPoolExecutor pool) throws IOException {
        this.sk = sk;
        // non-blocking下不可用Readers，因為Readers不支援non-blocking
        byte[] arr = new byte[1024];
        ByteBuffer buf = ByteBuffer.wrap(arr);
        //读取字符串
        int numBytes = sc.read(buf);
        if(numBytes == -1){
            System.out.println("[Warning!] A client has been closed.");
            h.closeChannel();
            return;
        }
        //将读取的字节文件转成字符串
        String str = new String(arr);
        if (!str.equals(" ")) {
            pool.execute(new WorkerThread(h, str));
            System.out.println(sc.socket().getRemoteSocketAddress().toString()
                    + " > " + str);
        }
    }

    //工作线程
    class WorkerThread implements Runnable {

        TCPHandler h;
        String str;

        public WorkerThread(TCPHandler h, String str) {
            this.h = h;
            this.str=str;
        }

        @Override
        public void run() {
            process(h, str);
        }
    }

    //执行逻辑处理
    synchronized void process(TCPHandler h, String str) {
        //改变状态(WORKING->SENDING)
        h.setState(new WriteState());
        //通过key改变注册事件
        this.sk.interestOps(SelectionKey.OP_WRITE);
        //使一个阻塞的selector操作立即返回
        this.sk.selector().wakeup();
    }
}
