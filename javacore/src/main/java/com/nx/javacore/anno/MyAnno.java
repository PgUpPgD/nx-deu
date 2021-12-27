package com.nx.javacore.anno;

import java.lang.annotation.*;

/**
 * Java提供了一些预定义的注解
 * @Override：检测被该注解标注的方法是否是继承自父类(接口)的
 * @Deprecated：该注解标注的内容，表示已过时
 * @SuppressWarnings：压制警告，一般传递参数all如@SuppressWarnings("all")
 *
 * 元注解
 * public @interface 注解名称{
 *  属性列表;
 * }属性的返回值类型有：
 * 基本数据类型、String、枚举、注解、Class or an invocation of Class、以上类型的数组
 *
 *@Target:描述当前注解能够作用的位置
 *  ElementType.TYPE:可以作用在类上
 *  ElementType.METHOD:可以作用在方法上
 *  ElementType.FIELD:可以作用在成员变量上
 *
 *@Retention: 描述注解被保留到的阶段
 *  SOURCE:表示当前注解只在代码阶段有效
 *  CLASS:表示该注解会被保留到字节码阶段
 *  RUNTIME:表示该注解会被保留到运行阶段 JVM
 *  自定义的注解：RetentionPolicy.RUNTIME；SOURCE < CLASS < RUNTIME
 *
 *@Documented:描述注解是否被抽取到JavaDoc的api中
 *@inherited:描述注解是否可以被子类继承
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@Documented
public @interface MyAnno {

    //注解中定义的方法叫做属性
    int num() default 1;
    String[] str();
    ColorEnum myenum();
    MyAnno2 myanno();

}
