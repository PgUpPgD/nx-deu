package com.nx.javacore.generic.type;

import java.util.Set;
import java.util.TreeSet;

/**
 * 无泛型参数传递给了一个可变泛型参数的方法  堆污染
 */
public class Pollution {
    public static void main(String[] args) {
        Set set = new TreeSet();
//        set.add("addd");
        varagMethod(set);
        set.add("abc");
    }

    private static void varagMethod(Set<Integer> set) {
        set.add(new Integer(100));
        System.out.println(set.size());
    }
}
