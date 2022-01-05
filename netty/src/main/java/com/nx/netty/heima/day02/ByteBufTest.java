package com.nx.netty.heima.day02;

import com.nx.netty.heima.util.ByteBufferUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

/**
 * ByteBuf  是对字节数据的封装
 *
 * - 直接内存创建和销毁的代价昂贵，但读写性能高（少一次内存复制），适合配合池化功能一起用
 * - 直接内存对 GC 压力小，因为这部分内存不受 JVM 垃圾回收的管理，但也要注意及时主动释放
 *
 * ByteBuf 优势
 * - 池化 - 可以重用池中 ByteBuf 实例，更节约内存，减少内存溢出的可能
 * - 读写指针分离，不需要像 ByteBuffer 一样切换读写模式
 * - 可以自动扩容
 * - 支持链式调用，使用更流畅
 * - 很多地方体现零拷贝，例如 slice、duplicate、CompositeByteBuf
 */
@SuppressWarnings("all")
public class ByteBufTest {
    public static void main(String[] args) {
        //池化基于直接内存 (堆内存) 的 ByteBuf  初始容量是 10
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        ByteBufferUtil.logUtil(buffer);

        //池化基于直接内存（系统内存）的 ByteBuf
        ByteBuf buffer1 = ByteBufAllocator.DEFAULT.directBuffer(10);
        ByteBufferUtil.logUtil(buffer1);

        /**
         * 池化的最大意义在于可以重用 ByteBuf，优点有
         *
         * - 没有池化，则每次都得创建新的 ByteBuf 实例，这个操作对直接内存代价昂贵，就算是堆内存，也会增加 GC 压力
         * - 有了池化，则可以重用池中 ByteBuf 实例，并且采用了与 jemalloc 类似的内存分配算法提升分配效率
         * - 高并发时，池化功能更节约内存，减少内存溢出的可能
         *
         * 池化功能是否开启，可以通过下面的系统环境变量来设置
         * -Dio.netty.allocator.type={unpooled|pooled}
         *
         * - 4.1 以后，非 Android 平台默认启用池化实现，Android 平台启用非池化实现
         * - 4.1 之前，池化功能还不成熟，默认是非池化实现
         */

        //先写入一字节 01 | 00 代表 true | false
//        buffer.writeBoolean(true);

        /**
         * 在写入 4 个字节
         */
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        ByteBufferUtil.logUtil(buffer);

        //再写入一个 int 整数，也是 4 个字节
        buffer.writeInt(5);
        ByteBufferUtil.logUtil(buffer);

        /**
         * 扩容 再写入一个 int 整数时，容量不够了（初始容量是 10），这时会引发扩容
         */
        buffer.writeInt(6);
        ByteBufferUtil.logUtil(buffer);

        /**
         *   读取 例如读了 4 次，每次一个字节
         */
        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
        //读过的内容，就属于废弃部分了，再读只能读那些尚未读取的部分
        ByteBufferUtil.logUtil(buffer);

        //如果需要重复读取 int 整数 5，怎么办？ 可以在 read 前先做个标记 mark
        buffer.markReaderIndex();
        System.out.println(buffer.readInt());
        ByteBufferUtil.logUtil(buffer);

//        buffer.retain();
//        boolean release = buffer.release();

        //这时要重复读取的话，重置到标记位置 reset
        buffer.resetReaderIndex();
        ByteBufferUtil.logUtil(buffer);

        /**
         * retain & release
         *
         * 由于 Netty 中有堆外内存的 ByteBuf 实现，堆外内存最好是手动来释放，而不是等 GC 垃圾回收
         *
         * - UnpooledHeapByteBuf 使用的是 JVM 内存，只需等 GC 回收内存即可
         * - UnpooledDirectByteBuf 使用的就是直接内存了，需要特殊的方法来回收内存
         * - PooledByteBuf 和它的子类使用了池化机制，需要更复杂的规则来回收内存
         *
         * Netty 这里采用了引用计数法来控制回收内存，每个 ByteBuf 都实现了 ReferenceCounted 接口
         *
         * - 每个 ByteBuf 对象的初始计数为 1
         * - 调用 release 方法计数减 1，如果计数为 0，ByteBuf 内存被回收
         * - 调用 retain 方法计数加 1，表示调用者没用完之前，其它 handler 即使调用了 release 也不会造成回收
         * - 当计数为 0 时，底层内存会被回收，这时即使 ByteBuf 对象还在，其各个方法均无法正常使用
         *
         * 谁是最后使用者，谁负责 release
         * 有时候不清楚 ByteBuf 被引用了多少次，但又必须彻底释放，可以循环调用 release 直到返回 true
         */


        /**
         * slice 切片
         * 【零拷贝】的体现之一，对原始 ByteBuf 进行切片成多个 ByteBuf，切片后的 ByteBuf 并没有发生内存复制，
         * 还是使用原始 ByteBuf 的内存，切片后的 ByteBuf 维护独立的 read，write 指针
         */
        ByteBuf origin = ByteBufAllocator.DEFAULT.buffer(10);
        origin.writeBytes(new byte[]{1,2,3,4});
        origin.readByte();
        ByteBufferUtil.logUtil(origin);

        // 调用 slice 进行切片，无参 slice 是从原始 ByteBuf 的 read index 到 write index 之间的内容进行切片，
        // 切片后的 max capacity 被固定为这个区间的大小，因此不能追加 write
        ByteBuf slice = origin.slice();
        ByteBufferUtil.logUtil(slice);

        //如果原始 ByteBuf 再次读操作（又读了一个字节）
        // 这时的 slice 不受影响，因为它有独立的读写指针
        origin.readByte();
        ByteBufferUtil.logUtil(origin);
        ByteBufferUtil.logUtil(slice);

        //如果 slice 的内容发生了更改
        //原始 ByteBuf 也会受影响，因为底层都是同一块内存
        slice.setByte(2, 5);
        ByteBufferUtil.logUtil(origin);
        ByteBufferUtil.logUtil(slice);

        /**
         * duplicate
         * 【零拷贝】的体现之一，就好比截取了原始 ByteBuf 所有内容，并且没有 max capacity 的限制，
         * 也是与原始 ByteBuf 使用同一块底层内存，只是读写指针是独立的
         */

        /**
         * copy
         * 会将底层内存数据进行深拷贝，因此无论读写，都与原始 ByteBuf 无关
         */

        /**
         * CompositeByteBuf
         * 【零拷贝】的体现之一，可以将多个 ByteBuf 合并为一个逻辑上的 ByteBuf，避免拷贝
         *
         * 有两个 ByteBuf 如下
         * CompositeByteBuf 是一个组合的 ByteBuf，它内部维护了一个 Component 数组，每个 Component 管理一个 ByteBuf，记录了这个 ByteBuf 相对于整体偏移量等信息，代表着整体中某一段的数据。
         *
         * - 优点，对外是一个虚拟视图，组合这些 ByteBuf 不会产生内存复制
         * - 缺点，复杂了很多，多次操作会带来性能的损耗
         */
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer(5);
        buf1.writeBytes(new byte[]{1,2,3,4,5});
        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer(5);
        buf2.writeBytes(new byte[]{6,7,8,9,10});

        CompositeByteBuf buf3 = ByteBufAllocator.DEFAULT.compositeBuffer();
        buf3.addComponents(true, buf1, buf2);
        ByteBufferUtil.logUtil(buf3);

        /**
         * Unpooled
         * Unpooled 是一个工具类，类如其名，提供了非池化的 ByteBuf 创建、组合、复制等操作
         *
         * 这里仅介绍其跟【零拷贝】相关的 wrappedBuffer 方法，可以用来包装 ByteBuf
         */
        ByteBuf buf4 = ByteBufAllocator.DEFAULT.buffer(5);
        buf4.writeBytes(new byte[]{1,2,3,4,5});
        ByteBuf buf5 = ByteBufAllocator.DEFAULT.buffer(5);
        buf5.writeBytes(new byte[]{6,7,8,9,10});

        //当包装 ByteBuf 个数超过一个时, 底层使用了 CompositeByteBuf
        ByteBuf buf6 = Unpooled.wrappedBuffer(buf4, buf5);
        System.out.println("buf6");
        ByteBufferUtil.logUtil(buf6);
    }
}
