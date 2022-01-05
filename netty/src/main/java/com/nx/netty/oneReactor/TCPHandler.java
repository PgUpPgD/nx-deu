package com.nx.netty.oneReactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * 服务端处理接收的信息
 * wakeup()方法，同一个selector对象
 * 线程A 调用 selector.select() 阻塞
 * 线程B 调用 selector().wakeup() 解阻塞，让A直接返回
 * 线程B 先调用了 wakeup 线程A后 select ，A也直接返回
 */
public class TCPHandler implements Runnable {

    private final SelectionKey key;
    private final SocketChannel socket;
    private int state;

    public TCPHandler(SelectionKey key, SocketChannel socket) {
        this.key = key;
        this.socket = socket;
        state = 0; // 初始状态设定为 READING
    }

    @Override
    public void run() {
        try {
            if (state == 0)
                read(); //读取
            else
                send(); //发送
        } catch (IOException e) {
            System.out.println("[Warning!] A client has been closed.");
            closeChannel();
        }
    }

    /**
     * 注意：当客户端强制断开，服务端会产生读事件，socket.read(buf) 中socket此时已经为空，发生IOException
     * 客户端正常断开，服务端会产生读事件，int numBytes = socket.read(buf); = -1
     * 两种情况都需要  key.cancel();  socket.close(); 从Set<SelectionKey> keys 中移除key，不然会一直循环
     *
     * @throws IOException
     */
    private synchronized void read() throws IOException {
        //non-blocking下不可用Readers，因为Readers不支持non-blocking
        byte[] bytes = new byte[1024];
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        //读取字符串
        int numBytes = socket.read(buf);
        //numBytes = 0 表示没有发消息，= -1表示客户端正常.close关闭
        if (numBytes == -1){
            System.out.println("[Warning!] A client has been closed.");
            closeChannel();
            return;
        }
        String str = new String(bytes);
        if (!str.equals(" ")) {
            //逻辑处理
            process(str);
            System.out.println(socket.socket().getRemoteSocketAddress().toString() + " > " + str);
            state = 1; //改变状态
            //通过key改变通道的注册事件
            key.interestOps(SelectionKey.OP_WRITE);
            //使一个阻塞住的selector操作立刻返回
            key.selector().wakeup();
        }
    }

    private void send() throws IOException  {
        String str  = "Your message has sent to " + socket.socket().getLocalSocketAddress().toString() + "\r\n";
        //wrap自动把buf的position置为0，所以不需要再flip()
        ByteBuffer buf = ByteBuffer.wrap(str.getBytes());

        while (buf.hasRemaining()) {
            //给client回应字符串，发送buf的position位置 到limit位置之间的內容
            socket.write(buf);
        }

        state = 0; //改变状态
        //通过key改变通道注册事件
        key.interestOps(SelectionKey.OP_READ);
        //解阻塞
        key.selector().wakeup();
    }

    void process(String str) {
        // do process(decode, logically process, encode)..
        // ..
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void closeChannel() {
        try {
            key.cancel();
            socket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
