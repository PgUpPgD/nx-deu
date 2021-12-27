package com.nx.javacore.reflection;

import com.nx.javacore.reflection.pojo.Person;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 获取Class文件的方式
 * 1、类名.class
 * 2、Class.forName //反射
 * 3、对象.getClass()
 * 4、通过类加载器.loadClass()
 */
public class CoreMain {

    public static void main(String[] args) throws Exception{
        //1、不会堆内初始化
        Class<Person> clazz01 = Person.class;
        System.out.println(clazz01);

        //2、会堆内初始化
        Class<?> clazz02 = Class.forName("com.nx.javacore.reflection.pojo.Person");
        System.out.println(clazz02);

        //3、会堆内初始化
        Class<? extends Person> clazz03 = new Person().getClass();
        System.out.println(clazz03);

        //4、不会堆内初始化
        Class<?> clazz04 = CoreMain.class.getClassLoader().loadClass("com.nx.javacore.reflection.pojo.Person");
        System.out.println(clazz04);

        /**
         * 基本信息操作
         */
        int modifier = clazz01.getModifiers();
        System.out.println("类修饰符: " + modifier);

        Package aPackage = clazz01.getPackage();
        System.out.println("类的包名: " + aPackage);

        String fullClassName = clazz01.getName();
        System.out.println("类的全路径名称: " + fullClassName);

        String simpleName = clazz01.getSimpleName();
        System.out.println("类的简单名称: " + simpleName);

        ClassLoader classLoader = clazz01.getClassLoader();
        System.out.println("类的加载器: " + classLoader);

        Class[] interfaces = clazz01.getInterfaces();
        System.out.println("类实现的接口列表: " + Arrays.toString(interfaces));

        Class<? super Person> superclass = clazz01.getSuperclass();
        System.out.println("类的父类: " + superclass);

        Annotation[] annotations = clazz01.getAnnotations();
        System.out.println("类的注解信息: " + Arrays.toString(annotations));

        /**
         * 属性操作
         */
        Person person = clazz01.newInstance();
        System.out.println("获取对象: " + person);
        Field[] fields = clazz01.getFields();
        System.out.println("获取类中所有的共有字段 包含继承的字段: " + Arrays.toString(fields));
        Field[] declaredFields = clazz01.getDeclaredFields();
        System.out.println("获取类中定义的字段 内部: " + Arrays.toString(declaredFields));
        Field nameField = clazz01.getDeclaredField("name");
        System.out.println("获取指定名称的类中定义的字段: " + nameField);
        int modifiers = nameField.getModifiers();
        System.out.println("获取字段的修饰符: " + modifiers);
        // 指定字段强制访问
        nameField.setAccessible(true);
        // 修改字段你的值
        nameField.set(person,"yqw");
        // 静态字段赋值
        Field numField = clazz01.getDeclaredField("num");
        numField.setAccessible(true);
        numField.set(null,666);
        System.out.println(person.toString());

        /**
         * 方法操作
         */
        Method[] methods = clazz01.getMethods();
        System.out.println("获取类中的所有的共有的方法 继承: " + Arrays.toString(methods));
        Method[] declaredMethods = clazz01.getDeclaredMethods();
        System.out.println("类中的定义的方法: " + Arrays.toString(declaredMethods));
        Method studyUp = clazz01.getMethod("studyUp", String.class);
        System.out.println("获取类中指定名称和参数的公有方法: " + studyUp);
        Method study = clazz01.getDeclaredMethod("study");
        System.out.println("获取类中定义的指定名称和参数的方法: " + study);
        int modifiers1 = studyUp.getModifiers();
        System.out.println("获取方法的修饰符: " + modifiers1);
        //强制执行
        studyUp.setAccessible(true);
        //执行方法
        studyUp.invoke(person, "you");
        study.setAccessible(true);
        //静态方法调用
        study.invoke(null);

        /**
         * 构造器操作
         */
        Constructor[] cons = clazz01.getConstructors();
        System.out.println("获取类中所有的公有构造器: " + Arrays.toString(cons));
        Constructor[] cons1 = clazz01.getDeclaredConstructors();
        System.out.println("获取类中所有的构造器: " + Arrays.toString(cons1));
        Constructor conNoParam= clazz01.getDeclaredConstructor();
        System.out.println("获取类中无参的构造器: " + conNoParam);
        Constructor con= clazz01.getDeclaredConstructor(String.class);
        System.out.println("获取类中有参构造: " + con);
        int modifers = con.getModifiers();
        System.out.println("获取构造器的修饰符: " + modifers);
        //构造器实例对象
        Person person1 = (Person)conNoParam.newInstance();
        //指定方法的强制访问
        con.setAccessible(true);
        //有参构造调用
        Person person2 = (Person)con.newInstance("abc");
        //class直接调用默认无参构造
        Person.class.newInstance();
    }

}



