package com.nx.javacore.reflection.countrt;

import com.nx.javacore.anno.dynamicproxy.ICountRuntime;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class CountRuntimeMain {

    // 使用代理的方式代码增强
    public static void main(String[] args) {
        //待增强的目标类
        ICountRuntime target = new CountRuntime();
       //JDK动态代理
        ICountRuntime proxy = (ICountRuntime) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler(){
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable { //代码增强
                        //获取目标类的目标方法
                        Class<? extends ICountRuntime> c = target.getClass();
                        //获取目标类的真实方法，如果方法中有@Runtime注解，那么就增强这个方法
                        Method m;
                        if (args==null){
                            m = c.getDeclaredMethod(method.getName());
                        }else{
                            Class[] paramArgs = new Class[args.length];
                            for (int i=0;i<args.length;i++){
                                paramArgs[i] = args[i].getClass();
                            }
                            m = c.getMethod(method.getName(),paramArgs);
                        }
                        RunTime annotation = m.getAnnotation(RunTime.class);
                        if(annotation!=null){
                            long start = System.currentTimeMillis();
                            //执行原方法
                            Object result = method.invoke(target, args);
                            long end = System.currentTimeMillis();
                            System.out.println("方法："+method.getName()+"执行时间为"+(end-start));
                            return "加注解已增强";
                        }
                        //没加注解
                        return method.invoke(target, args);
                    }
                }
        );

        proxy.method01();
        proxy.method02("samul");
    }
}
