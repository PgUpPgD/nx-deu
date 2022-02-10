package com.nx.rocketmq.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private String name;
    private int age;

    public User(){}
    public User(String name, int age){
        this.name = name;
        this.age = age;
    }
}
