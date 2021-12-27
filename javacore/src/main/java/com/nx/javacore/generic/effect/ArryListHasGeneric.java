package com.nx.javacore.generic.effect;

/**
 * 加泛型
 * 1、保证类型安全，编译阶段类型检查
 * 2、避免类型转换硬编码
 * 3、调用代码重用性
 * @param <E>
 */
public class ArryListHasGeneric<E> {
    private Object[] elements= new Object[4];
    private int size;

    public Object get(int i){
        if (size>i) return elements[i];
        throw new IndexOutOfBoundsException();
    }

    public void add(E c){
        elements[size++]=c;
    }

    public static void main(String[] args) {
        ArryListHasGeneric<String> list = new ArryListHasGeneric<String>();
        //编译类型检查
//        list.add(1);
        list.add("a");
//        list.add(new File("d:/nxjy"));
        System.out.println(list.size);
        String str =  (String) list.get(2);

    }
}
