package com.nx.javacore.jvm.cloader;

/**
 * 总结：
 * 1、双亲委派
 * 2、自定义加载器
 * 3、破坏双亲委派 。。。
 * 4、类加载器的命名空间
 * --------------------------
 *
 * 1、每个类加载器都有自己的命名空间----由该类加载器以及所有父类加载器所加载的类组成
 * 2、同一个命名空间中，不会出现全类名相同的两个类对象
 * 3、在不同的命名空间中，有可能出现全类名相同的两个类对象
 * 4、同一个命名空间的类是相互可见的。
 *
 *
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("java.class.path")); //app加载路径
        System.out.println("-----------------------------------------------");
        System.out.println(System.getProperty("java.ext.dirs")); //ext加载路径
        System.out.println("-----------------------------------------------");
        System.out.println(System.getProperty("sun.boot.class.path")); //bootstrap加载路径

        //使用自定义加载器加载类
        SelfClassLoader selfClassLoader = new SelfClassLoader("D:\\nx\\", "selfClassLoader");
//        Class<?> dogClazz = selfClassLoader.loadClass("com.zh.layui.Schedu");
        //dog放在和com平级的target目录下
        Class<?> dogClazz = selfClassLoader.loadClass("Dog");
        Object dogObj = dogClazz.newInstance();

        System.out.println(dogObj.getClass().getClassLoader());
        System.out.println(dogObj.getClass().getClassLoader().getParent());
        System.out.println(dogObj.getClass().getClassLoader().getParent().getParent());
//        System.out.println(dogObj.getClass().getClassLoader().getParent().getParent().getParent());
        System.out.println("------------------------------------------------------------------------");
        SelfClassLoader selfClassLoader1 = new SelfClassLoader("D:\\nx\\", "selfClassLoader1");
        Class<?> dogClazz1 = selfClassLoader1.loadClass("Dog");
        Object dogObj1 = dogClazz1.newInstance();

        System.out.println(dogClazz==dogClazz1);
        System.out.println(dogObj==dogObj1);

        //破坏双亲委派... 沙箱机制


    }
}
