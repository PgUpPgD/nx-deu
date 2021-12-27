package com.nx.thread.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "e")
public class MyTask implements Runnable{

    private String name;

    public MyTask(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public void run() {
        log.debug("{}--的具体逻辑",name);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
