package com.nx.netty.heima.day01;

import com.nx.netty.heima.util.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class BossWorkEvent {
    public static void main(String[] args) throws IOException {
        new BossEventLoop().register();
    }

    static class BossEventLoop implements Runnable{
        private Selector boss;
        private WorkerEventLoop[] workers;
        private volatile boolean start = false;
        AtomicInteger index = new AtomicInteger();

        public void register() throws IOException{
            if (!start){
                ServerSocketChannel ssc = ServerSocketChannel.open();
                ssc.bind(new InetSocketAddress(8080));
                ssc.configureBlocking(false);
                boss = Selector.open();
                ssc.register(boss, SelectionKey.OP_ACCEPT);
                workers = initEventLoops();
                // 主线程关注所有的OP_ACCEPT事件
                new Thread(this, "boss").start();
                log.debug("boss start...");
                start = true;
            }
        }

        public WorkerEventLoop[] initEventLoops(){
            //Runtime.getRuntime().availableProcessors()
            //此获取的 cpu核心数，在docker中，依然获取的宿主机的cpu核心数，而该容器也许只分配了一核，内存等都可控制
            WorkerEventLoop[] loops = new WorkerEventLoop[2];
            for (int i = 0; i < loops.length; i++) {
                loops[i] = new WorkerEventLoop(i);
            }
            return loops;
        }

        @Override
        public void run() {
            while (true){
                try {
                    boss.select();
                    Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
                    while (iter.hasNext()){
                        SelectionKey key = iter.next();
                        if (key.isAcceptable()){
                            ServerSocketChannel ssck = (ServerSocketChannel) key.channel();
                            SocketChannel sc = ssck.accept();
                            sc.configureBlocking(false);
                            log.debug("{} connected", sc.getRemoteAddress());
                            // 除accept事件之外，交给worker线程
                            log.debug("worker register... {}", sc.getRemoteAddress());
                            workers[index.getAndIncrement() % workers.length].register(sc);
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    static class WorkerEventLoop implements Runnable {
        private Selector worker;
        private volatile boolean start = false;
        private int index;

        private final ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();

        public WorkerEventLoop(int index){
            this.index = index;
        }

        // 执行该方法的是boss线程
        public void register(SocketChannel sc) throws IOException{
            //保证只初始化 workers.length 个线程，不能每次连接进来都 new Thread 一个新线程
            if (!start){
                worker = Selector.open();
                log.debug("worker-" + index + " 初始化");
                new Thread(this, "worker-" + index).start();
                start = true;
            }
            // boss 向队列添加了任务，但该任务并没有立刻执行 ，
            // boss和worker两个线程之间，任务由队列传递给worker
            tasks.add(() ->{
                try {
                    sc.register(worker, SelectionKey.OP_READ);
                    worker.selectNow();
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
            // 让 worker.select() 解阻塞 即 worker的下一次select
            worker.wakeup();
        }

        @Override
        public void run() {
            while (true){
                try {
                    // worker线程 第一次初始化时，worker线程没有任何事件触发，会阻塞，
                    // 下面的 读事件注册 执行不了，所以需要在上面进行 worker.wakeup()
                    worker.select();
                    Runnable task = tasks.poll();
                    if (task != null){
                        // 相当于 sc.register(worker, SelectionKey.OP_READ);
                        log.debug("worker 的读事件注册了...");
                        task.run();
                    }
                    Iterator<SelectionKey> iter = worker.selectedKeys().iterator();
                    while (iter.hasNext()){
                        SelectionKey key = iter.next();
                        if (key.isReadable()) {
                            SocketChannel sc = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(128);
                            try {
                                int read = sc.read(buffer);
                                if (read == -1) {
                                    key.cancel();
                                    sc.close();
                                } else {
                                    buffer.flip();
                                    log.debug("{} message:", sc.getRemoteAddress());
                                    ByteBufferUtil.debugAll(buffer);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                key.cancel();
                                sc.close();
                            }
                        }
                        iter.remove();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
