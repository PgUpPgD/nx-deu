package com.nx.javacore.generic;

/**
 * 类上的泛型可以是一个或者多个
 * @param <T>
 */
public class GenericClass<T> {
    private T first;
    private T sencond;

    public T fun1(T t){
        return null;
    }

    public <P> P fun2(){
        return null;
    }
}
