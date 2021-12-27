package com.nx.javacore.anno.example;

import lombok.Data;

import java.lang.reflect.Field;

@Data
public class Apple {

    @FruitName("Apple")
    private String appleName;

    @FruitProvider(id=1,name="山东富士")
    private String appleProvider;

    public static void main(String[] args) {
        Class<Apple> clazz = Apple.class;
        Field[] fields = clazz.getDeclaredFields();
        for(Field field:fields){
            if (field.isAnnotationPresent(FruitName.class)){
                FruitName fruitName = field.getAnnotation(FruitName.class);
                System.out.println("fruitName: " + fruitName.value());
            }
        }
    }
}
