package com.nx.javacore.generic.border;

import java.util.ArrayList;

/**
 * 泛型通配符：边界的问题
 * 三种边界
 * 无界：？<?>
 * 上界： <? extends E>
 * 下界： <? super E>
 *
 *  编译检查 保证类型的安全
 *  避免强制转换的硬编码
 *  增加调用代码的重用性
 */
public class BoderDemo {

    /**
     * 无界通配符的使用
     */
    public void border01(ArrayList<?> arrayList){
        for (int i=0;i<arrayList.size();i++){
            System.out.println(arrayList.get(0));
        }
    }

    /**
     * 上界通配符的使用
     * <? extends Object> 无界通配符
     */
    public void border02(ArrayList<? extends Number> arrayList){
        for (int i=0;i<arrayList.size();i++){
            System.out.println(arrayList.get(0));
        }
    }

    /**
     * 下界通配符的使用
     */
    public void border03(ArrayList<? super Number> arrayList){
        for (int i=0;i<arrayList.size();i++){
            System.out.println(arrayList.get(0));
        }
    }

    public static void main(String[] args) {
        BoderDemo boderDemo = new BoderDemo();

        ArrayList<String> strList = new ArrayList<>();
        ArrayList<Object> objList = new ArrayList<>();
        ArrayList<Number> numberArrayList = new ArrayList<>();
        ArrayList<Double> doubleArrayList = new ArrayList<>();
        ArrayList<Integer> intArrayList = new ArrayList<>();

        //无界   啥都行
        boderDemo.border01(strList);
        boderDemo.border01(objList);
        boderDemo.border01(numberArrayList);
        boderDemo.border01(doubleArrayList);
        boderDemo.border01(intArrayList);

        //上界   ? extends Number  最大是 Number
        boderDemo.border02(numberArrayList);
        boderDemo.border02(doubleArrayList);
        //泛型编译检查报错
//        boderDemo.border02(strList);
//        boderDemo.border02(objList);

        //下界    ? super Number   最小是 Number
        boderDemo.border03(numberArrayList);
//        boderDemo.border03(doubleArrayList);
    }

}


