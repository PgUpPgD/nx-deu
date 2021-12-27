package com.nx.javacore.reflection.pojo;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class Person implements Runnable {
    static {
        System.out.println("person...");
    }

    public Person(){}

    public Person(String name){
        this.name = name;
    }

    private static int num;
    private String name;

    private static void study(){
        System.out.println("good good study day day up。。。");
    }
    public void studyUp(String name){
        System.out.println(name + " good good study day day up。。。");
    }

    @Override
    public void run() {
        System.out.println("run...");
    }

    @Override
    public String toString() {
        return "Person{" +
                "num='" + num + '\'' +
                "name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) throws Exception{
        //获取Class对象
        Class<Person> clazz = Person.class;

        //动态创建对象
        Person person = clazz.newInstance();

        //调用方法
        Method studyMethod = clazz.getDeclaredMethod("study");
        studyMethod.invoke(person);
    }

}
