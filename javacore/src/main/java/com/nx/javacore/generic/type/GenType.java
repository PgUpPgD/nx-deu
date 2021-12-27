package com.nx.javacore.generic.type;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 泛型并不是数据的基本类型
 * 伪泛型，Java编译期间会将泛型擦除
 */
public class GenType {

    @Test
    public void m01(){
        List list = new ArrayList();
        List<String> slist = new ArrayList<>();

        System.out.println(list.getClass());
        System.out.println(slist.getClass());
        //true
        System.out.println(list.getClass() == slist.getClass());
    }

    @Test
    public void method02() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
//        list.add(Object.class);
        System.out.println(list.size());

        System.out.println("-----只在编译期进行类型检查--------");

        Class<? extends List> clazz = list.getClass();
        Method method = clazz.getDeclaredMethod("add", Object.class);
        method.invoke(list, new Object());
        System.out.println(list.size());
    }

}
