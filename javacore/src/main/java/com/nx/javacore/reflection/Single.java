package com.nx.javacore.reflection;

import java.lang.reflect.Constructor;

/**
 * 反射破坏单例
 */
public class Single {

    private static Single instance;

    private Single(){
        //判断 instance 是否存在可防止单例被反射破坏
        if (instance != null){
//            throw new RuntimeException("这是一个单例，不能重复创建...");
        }
    }

    public static Single getInstance(){
        if (instance == null){
            instance = new Single();
        }
        return instance;
    }

    public static void main(String[] args) throws Exception{
        Single instance = Single.getInstance();
        Single instance1 = Single.getInstance();

        System.out.println(instance == instance1);  //true

        Class<? extends Single> clazz = instance.getClass();
        Constructor<? extends Single> constructor = clazz.getDeclaredConstructor();
        //对私有的构造方法设置强制访问
        constructor.setAccessible(true);
        Single instance2 = constructor.newInstance();

        System.out.println(instance==instance2); //false
    }

}

