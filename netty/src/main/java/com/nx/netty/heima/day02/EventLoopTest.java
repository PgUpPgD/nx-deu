package com.nx.netty.heima.day02;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;

/**
 * 事件循环对象
 *
 * EventLoop 本质是一个单线程执行器（同时维护了一个 Selector），里面有 run 方法处理 ChannelTest 上源源不断的 io 事件。
 *
 * 它的继承关系比较复杂
 *
 * - 一条线是继承自 j.u.c.ScheduledExecutorService 因此包含了线程池中所有的方法
 * - 另一条线是继承自 netty 自己的 OrderedEventExecutor，
 *   - 提供了 boolean inEventLoop(Thread thread) 方法判断一个线程是否属于此 EventLoop
 *   - 提供了 parent 方法来看看自己属于哪个 EventLoopGroup
 *
 *
 * 事件循环组
 *
 * EventLoopGroup 是一组 EventLoop，ChannelTest 一般会调用 EventLoopGroup 的 register 方法来绑定其中一个 EventLoop，后续这个 ChannelTest 上的 io 事件都由此 EventLoop 来处理（保证了 io 事件处理时的线程安全）
 *
 * - 继承自 netty 自己的 EventExecutorGroup
 *   - 实现了 Iterable 接口提供遍历 EventLoop 的能力
 *   - 另有 next 方法获取集合中下一个 EventLoop
 */
public class EventLoopTest {

    public static void main(String[] args) {
        DefaultEventLoopGroup group = new DefaultEventLoopGroup(2);
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        for (EventExecutor eventLoop : group) {
            System.out.println(eventLoop);
        }
    }


}
