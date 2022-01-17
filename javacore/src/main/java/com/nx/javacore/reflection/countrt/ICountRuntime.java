package com.nx.javacore.reflection.countrt;

/**
 * 统计方法的执行时间
 * 1、不修改代码的具体实现
 * 2、可以使用注解
 * 分析
 * 待统计执行时间的方法上加注解
 * 有注解的方法就进行代码增强
 */
public interface ICountRuntime {

    void  method01();
    void  method02(String param);
}
