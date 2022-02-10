package com.nx.rocketmq.order;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

/**
 * 有序队列
 */
public class OrderedProducer {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        producer.setNamesrvAddr("8.210.248.130:9876");

        // 若为全局有序，则设置Queue数量为1
        producer.setDefaultTopicQueueNums(1);

        producer.start();

        for (int i = 0; i < 20; i++) {
            // 为了演示简单，使用整型数作为orderId
            Integer orderId  = i;
            byte[] body = ("Hi," + i).getBytes();
            Message message = new Message("TopicA", "TagA", body);
            // 将orderId作为消息key
            message.setKeys(orderId.toString());
            // send()的第三个参数值会传递给选择器的select()的第三个参数
            // 该send()为同步发送
            SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message message, Object arg) {
                    // 以下是使用消息key作为选择的算法
                    String keys = message.getKeys();
                    Integer id = Integer.valueOf(keys);

                    // 以下是使用arg作为选择key的选择算法
//                    int ids = arg.hashCode() % mqs.size();

                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, orderId); //这个orderId会传到 Object arg，作为arg的参数

            System.out.println(sendResult);
        }
        producer.shutdown();
    }
}
