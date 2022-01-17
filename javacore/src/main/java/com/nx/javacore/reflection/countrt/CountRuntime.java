package com.nx.javacore.reflection.countrt;

import com.nx.javacore.anno.dynamicproxy.ICountRuntime;
import com.nx.javacore.anno.dynamicproxy.RunTime;

public class CountRuntime implements ICountRuntime {

    @Override
    public void method01() {
        for (int i=0;i<8;i++){
            System.out.println("method01");
        }
    }

    @RunTime
    @Override
    public void method02(String param) {
        System.out.println("hello "+param);
    }
}
