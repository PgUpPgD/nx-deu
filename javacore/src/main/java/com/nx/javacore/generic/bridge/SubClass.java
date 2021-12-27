package com.nx.javacore.generic.bridge;

//通过桥接来兼容1.5以前没有泛型的版本
public class SubClass implements SuperClass<String> {
    @Override
    public String m01(String param) {
        return param + "---";
    }

    public static void main(String[] args) {
        SuperClass subClass = new SubClass();
        subClass.m01("13ad");
        subClass.m01(new Object());
    }
}
