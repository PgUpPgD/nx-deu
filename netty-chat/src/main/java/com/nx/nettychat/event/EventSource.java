package com.nx.nettychat.event;

import java.util.Vector;

/**事件源类。表明谁触发了事件，用于作为EventObject类的构造参数，在listener中作路由*/
public class EventSource {
    private String who;
    Vector listeners=new Vector();
    public EventSource(String who){
        this.who=who;
    }
    public String getActioner(){
        return who;
    }
    public void addMyEventListener(MyEventListener listener){
        listeners.add(listener);
    }
    /*设定say方法能被MyEventListener对象监听到*/
    public void say(String words){
        System.out.println(this.getActioner()+"说："+words);
        for(int i=0;i<listeners.size();i++){
            MyEventListener listener=(MyEventListener) listeners.elementAt(i);
            /*发布事件。当然应该事先规划say方法事件能发布给哪些事件监听器。*/
            listener.onMyEvent(new EventClassOne(this));
        }
    }

    public static MyEventListener listener = null;
    public static void main(String[] args){

        listener = new MyEventListener();
        EventSource 小白 = new EventSource("小白");
        小白.addMyEventListener(listener);
        小白.say("今天天气不错");
        小白.say("适合出去走走");
    }
}
