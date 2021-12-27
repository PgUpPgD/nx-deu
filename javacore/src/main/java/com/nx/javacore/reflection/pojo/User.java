package com.nx.javacore.reflection.pojo;

import lombok.Data;


/**
 * 缺点：性能损耗，时间相差很大
 */
@Data
public class User  extends Person{
    public String idCard;
    private String address;

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            //使用构造函数创建对象
            User user = new User();
            //使用反射创建对象
//            getInstanceByRef();
        }

        long end = System.currentTimeMillis();
        System.out.println("总耗时"+(end-start));
    }

    private static void getInstanceByRef(){
        try {
            Class<?> clazz = Class.forName("com.nx.javacore.reflection.pojo.User");
            clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
