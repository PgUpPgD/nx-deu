package com.nx.collection.list;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class ArrayListC implements Serializable {

    public static void main(String[] args) {
        //java8的小bug
//        List<Integer> integerList = Arrays.asList(1,2,3);
//        System.out.println(integerList.toArray().getClass()==Object[].class); //false
//
//        List<Integer> list = new ArrayList<>();
//        System.out.println(list.toArray().getClass()==Object[].class); //true
//
//        List list2 = new ArrayList();
//        System.out.println(list.getClass()==list2.getClass()); //true
//
//        Object aClass = integerList.get(0).getClass();
//        System.out.println(aClass==Object.class);  //false

        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(6,0.75f);
        map.mappingCount();
        map.get("");
        map.remove("");
        for (int i = 1; i < 300; i++) {
            map.put("e" + i, 1);
            map.put("d" + i, 1);
            map.put("f" + i, 1);
            map.put("c" + i, 1);
            map.put("g" + i, 1);
            map.put("b" + i, 1);
            map.put("h" + i, 1);
            map.put("a" + i, 1);
        }
    }


}
