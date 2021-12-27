package com.nx.javacore.generic;

/**
 * 类上的泛型和方法上的泛型
 * @param <K>
 * @param <V>
 */
public class GenricMethod<K,V> {


    /**
     * 1、实体方法使用类上定义的泛型
     * @param k
     * @param v
     * @return
     */
    public K method1(K k,V v){
        return null;
    }

    public <T> T method02(){ //2、方法上定义泛型
        return null;
    }


    /**
     * 静态方法中不能直接使用类上定义的泛型
     * 静态方法中可以使用方法上定义的泛型
     * @return
     */
//    public static V method03(){
//        return null;
//    }

    public static <P> P method04(){
        return null;
    }

}
