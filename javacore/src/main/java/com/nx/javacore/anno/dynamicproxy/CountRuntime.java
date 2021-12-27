package com.nx.javacore.anno.dynamicproxy;

public class CountRuntime implements ICountRuntime {

    @Override
    public void method01() {
        System.out.println("method01");
    }

    @RunTime
    @Override
    public void method02(String param) {
        for (int i=0;i<8000;i++) {
            if (i == 8000-1){
                System.out.println("hello " + param);
            }
        }
    }
}
