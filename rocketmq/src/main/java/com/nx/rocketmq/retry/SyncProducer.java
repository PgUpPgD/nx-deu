package com.nx.rocketmq.retry;

import com.alibaba.fastjson.JSONObject;
import com.nx.rocketmq.entity.User;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.IOException;

/**
 * 同步发送消息
 */
public class SyncProducer {

    public static void main(String[] args) throws MQClientException, IOException, RemotingException, InterruptedException, MQBrokerException {
        // 创建一个producer，参数为Producer Group名称
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        // 指定nameServer地址
        producer.setNamesrvAddr("8.210.248.130:9876");
        // 设置同步发送失败时重试发送的次数，默认为2次
        producer.setRetryTimesWhenSendFailed(3);
        // 设置发送超时时限为5s，默认3s
        producer.setSendMsgTimeout(5000);

        // 开启生产者
        producer.start();

        // 生产并发送100条消息
        for (int i = 0; i < 5; i++) {
            User user = new User("例" + i, i);
            String s = JSONObject.toJSONString(user);
            byte[] body = s.getBytes();
            Message msg = new Message("someTopic", "someTage", body);
            // 为消息指定key
            msg.setKeys("key-" + i);
            // 同步发送消息
            SendResult result = producer.send(msg);
            System.out.println(result);
        }
        // 关闭producer
        producer.shutdown();
    }

}
