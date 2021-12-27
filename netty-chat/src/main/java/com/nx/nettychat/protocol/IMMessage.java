package com.nx.nettychat.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class IMMessage implements Serializable {

    private static final long serialVersionUID = 8763561286199081881L;
    private String addr; // IP地址以及端口
    private String cmd; // 命令类型 SYSTEM|LOGIN|LOGOUT|CHAT|FLOWER|KEEPALIVE
    private long time; // 命令发送时间
    private int online; // 当前在线人数
    private String sender;// 发送人
    private String receiver;// 接收人
    private String content; // 消息内容
    private String terminal; // 终端
    public IMMessage(){}

    public IMMessage(String cmd, long time, int online, String content){
        this.cmd = cmd;
        this.time = time;
        this.online = online;
        this.content = content;
    }

    public IMMessage(String cmd, String terminal, long time, String sender){
        this.cmd = cmd;
        this.time = time;
        this.sender = sender;
        this.terminal = terminal;
    }


    public IMMessage(String cmd, long time, String sender, String content){
        this.cmd = cmd;
        this.time = time;
        this.sender = sender;
        this.content = content;
    }

    @Override
    public String toString() {
        return "IMMessage{" +
                "addr='" + addr + '\'' +
                ", cmd='" + cmd + '\'' +
                ", time=" + time +
                ", online=" + online +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
