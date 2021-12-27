package com.nx.nettychat.util;

import com.nx.nettychat.protocol.IMMessage;
import com.nx.nettychat.protocol.MsgActionEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoderUtil {

    // 消息头 -
    // 消息头 ^\[(.*)\]  ^\[\]  以[开始以]结束  中间() 进行分组，中间是任意字符 .任意字符 *匹配任意次数
    // 消息体： (\s\-\s(.*))?  \s\-\s(.*) 空格 横杠 空格 开始后面是任意字符， 并且这个匹配可以有也可以没有所以用 ？
    private static Pattern pattern = Pattern.compile("^\\[(.*)\\](\\s\\-\\s(.*))?");

    public static IMMessage decode(String msg){
        if(StringUtils.isBlank(msg)){
            return null;
        }
        try{
            Matcher m = pattern.matcher(msg);
            // [命令][命令发送时间][命令发送人][终端类型] - 内容
            String header = "";
            String content = "";
            if(m.matches()){
                header = m.group(1);
                content = m.group(3);
            }
            String[] headers = header.split("\\]\\[");
            long time = 0;
            try{
                time = Long.parseLong(headers[1]);
            }catch (Exception e){
                System.err.println("时间转化出现异常：" + e);
            }
            String nickName = headers[2];
            //昵称最多是是个字
            nickName = nickName.length() < 10 ? nickName: nickName.substring(0,9);
            String cmd = headers[0];

            if(msg.startsWith("[" + MsgActionEnum.LOGIN.getName() + "]") ||
                    msg.startsWith("[" + MsgActionEnum.LOGOUT.getName() + "]")){
                return new IMMessage(cmd,headers[3],time,nickName);
            }else if(msg.startsWith("[" + MsgActionEnum.CHAT.getName() + "]")){
                return new IMMessage(cmd,time,nickName,content);
            }else if(msg.startsWith("[" + MsgActionEnum.FLOWER.getName() + "]")){
                return new IMMessage(cmd,headers[3],time,nickName);
            }else if(msg.startsWith("[" + MsgActionEnum.KEEPALIVE.getName() + "]") ||
                    msg.startsWith("[" + MsgActionEnum.KEEPALIVE.getName() + "]")){
                return new IMMessage(cmd,headers[3],time,nickName);
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /***
     * 将IMMessage对象编码成指定的自定义协议字符串
     * @param msg
     * @return
     */
    public static String encode(IMMessage msg){
        if(null == msg){
            return "";
        }
        String cmd = msg.getCmd();
        String sender = msg.getSender();
        String prex = "[" +  cmd +"]" + "[" + msg.getTime() +"]";
        if(MsgActionEnum.LOGIN.getName().equals(cmd) || MsgActionEnum.FLOWER.getName().equals(cmd)){
            prex += ("[" + sender + "][" + msg.getTerminal() + "]");
        }else if(MsgActionEnum.CHAT.getName().equals(cmd) ){
            prex += ("[" + sender + "]");
        }else if(MsgActionEnum.SYSTEM.getName().equals(cmd)){
            prex += ("[" + msg.getOnline() + "]");
        }
        if (StringUtils.isNotBlank(msg.getContent())) {
            prex += (" - " + msg.getContent());
        }
        return prex;
    }

}
