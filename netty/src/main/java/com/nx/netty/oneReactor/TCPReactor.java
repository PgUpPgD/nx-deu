package com.nx.netty.oneReactor;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 服务端启动 创建监听对象
 */
public class TCPReactor implements Runnable{

    private final ServerSocketChannel ssc;
    private final Selector selector;

    public TCPReactor(int port) throws IOException {
//            Selector abstractSelector = SelectorProvider.provider().openSelector();
//            ServerSocketChannel serverSocketChannel = SelectorProvider.provider().openServerSocketChannel();
        this.ssc = ServerSocketChannel.open();
        this.selector = Selector.open();
        //设置监听器为非阻塞
        ssc.configureBlocking(false);
        //绑定监听端口
        ssc.socket().bind(new InetSocketAddress(port));
        System.out.println("Waiting for new event on port: " + ssc.socket().getLocalPort() + "...");
        //ServerSocketChannel向selector事件池注册一个OP_ACCEPT事件，然后返回该通道的key
        SelectionKey key = ssc.register(selector, SelectionKey.OP_ACCEPT);
        //给key绑定一个附加对象
        key.attach(new Acceptor(selector, ssc));
    }

    @Override
    public void run() {
        //在线程被打断前执行
        while (!Thread.interrupted()){
            try {
                //selector.select(2000);//最多阻塞两秒
                //selector.selectNow();//立马返回 不会阻塞
                if (selector.select() == 0) {  //阻塞
                    Thread.sleep(100);
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //取得所有已就绪事件
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()){
                //根据事件key进行调度
                dispatch(it.next());
                it.remove();
            }
        }
    }

    private void dispatch(SelectionKey key) {
        //根据事件绑定的对象 new Acceptor(selector, ssc) 开线程
        Runnable r = (Runnable) (key.attachment());
        if (r != null)
            r.run();
    }
}
