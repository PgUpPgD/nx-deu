package com.nx.javacore.generic.effect;

import java.io.File;

//不加泛型
public class ArryListNoGeneric {
    private Object[] elements= new Object[4];
    private int size;

    public Object get(int i){
        if (size>i) return elements[i];
        throw new IndexOutOfBoundsException();
    }

    public void add(Object c){
        elements[size++]=c;
    }

    public static void main(String[] args) {
        ArryListNoGeneric list = new ArryListNoGeneric();
        //通过编译，运行时报错
        list.add(1);
        list.add("a");
        list.add(new File("d:/nxjy"));
        System.out.println(list.size);
        String str =  (String) list.get(2);
    }
}
