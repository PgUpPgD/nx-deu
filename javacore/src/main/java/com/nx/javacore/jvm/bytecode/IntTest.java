package com.nx.javacore.jvm.bytecode;

/**
 * istort_2 操作码+操作数
 * 1、加载和存贮指令
 * [i,f,l,d.a]load store
 * bipush sipush ldc ldc_w ldc2_w... acontst_XXX
 * 2、对象的创建和访问
 *  new newarry,XXXarry
 *  get put。。
 *3、栈
 *  pop pop2
 *4、ifeq iflt ifle ifne。。。
 *  goto...
 * 5、invokevitrul
 *          interface
 *          static
 *          special
 */
public class IntTest {
    public static void main(String[] args) {
        int a = 1;
        int b = 200;
        int c = a+b;
        System.out.println(c);
    }

}

