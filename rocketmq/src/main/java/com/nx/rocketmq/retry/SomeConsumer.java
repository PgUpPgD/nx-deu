package com.nx.rocketmq.retry;

import com.alibaba.fastjson.JSONObject;
import com.nx.rocketmq.entity.User;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 消费者
 */
public class SomeConsumer {

    public static volatile boolean running = true;

    public static void main(String[] args) throws MQClientException {

//        // 定义一个pull 拉取消息的消费者
//        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("cg");
//        // 指定nameServer
//        consumer.setNamesrvAddr("8.210.248.130:9876");
//        // 指定从第一条消息开始消费
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//        // 指定消费topic与tag
//        consumer.subscribe("someTopic", "*");
//        // 指定采用“广播模式”进行消费，默认为“集群模式”
//        consumer.setMessageModel(MessageModel.BROADCASTING);
//        // 启动消费对象
//        consumer.start();
//        try {
//            // 循环开始消费消息
//            while (running){
//                List<MessageExt> list = consumer.poll();
//                list.forEach(t ->{
//                    byte[] body = t.getBody();
//                    String s = new String(body);
//                    User user = JSONObject.parseObject(s, User.class);
//                    System.out.println(user.getName() + "-----" + user.getAge());
//                });
//                System.out.printf("%s%n", list);
//            }
//        } finally {
//            consumer.shutdown();
//        }

        // 定义一个push消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("cg");
        // 指定nameServer
        consumer.setNamesrvAddr("8.210.248.130:9876");
        // 指定从第一条消息开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 指定消费topic与tag
        consumer.subscribe("someTopic", "*");
        // 顺序消息消费失败的消费重试时间间隔，默认为1000毫秒，其取值范围为[10, 30000]毫秒
        consumer.setSuspendCurrentQueueTimeMillis(100);
        // 修改消费重试次数
        consumer.setMaxReconsumeTimes(20);
        // 注册消息监听器
        consumer.registerMessageListener(new MessageListenerConcurrently(){
            // 一旦broker中有了其订阅的消息就会触发该方法的执行，
            // 其返回值为当前consumer消费的状态
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
                // 逐条消费消息
                for (MessageExt msg : list) {
                    byte[] body = msg.getBody();
                    String s = new String(body);
                    User user = JSONObject.parseObject(s, User.class);
                    System.out.println(user.toString());
                    if (user.getAge() > 3){
                        // 消息进入重试队列，在consumerOffset.json中记录创建的重试队列
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                // 返回消费状态：消费成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // 开启消费者消费
        consumer.start();
    }
}
