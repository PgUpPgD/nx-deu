package com.nx.collection.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadAdd extends Thread {

    private List list;

    public ThreadAdd(List list) {
        this.list = list;
    }

    @Override
    public void run() {
        for (int i = 0; i <  100; i++) {
            System.out.println("循环执行:"+i);
            try {
                Thread.sleep(5);
                list.add(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * failFast 机制
     * 创建迭代器对象时 将全局的modCount赋值给迭代器的局部变量expectedModCount
     * 在迭代的过程中modCount！=expectedModCount快速抛出异常
     * @param args
     */
    public static void main(String[] args) {
//        List list = new ArrayList<>();
        List list = Collections.synchronizedList(new ArrayList<>());
        new ThreadAdd(list).start();
        new ThreadIterator(list).start();
    }
}
