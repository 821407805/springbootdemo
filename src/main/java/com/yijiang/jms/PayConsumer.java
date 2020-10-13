package com.yijiang.jms;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
public class PayConsumer {

    private DefaultMQPushConsumer consumer;

    private String consumerGroup = "pay_consumer_group";

    public  PayConsumer() throws MQClientException {

        consumer = new DefaultMQPushConsumer( consumerGroup );
        consumer.setNamesrvAddr( JmsConfig.NAME_SERVER );
        consumer.setConsumeFromWhere( ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET );

        // 默认是集群方式，可以更改为广播，但是广播方式不支持重试
        // 广播消费指的是：一条消息被多个consumer消费，即使这些consumer属于同一个ConsumerGroup,消息也会被ConsumerGroup中的每个Consumer都消费一次，广播消费中ConsumerGroup概念可以认为在消息划分方面无意义。
        // 集群消费模式：一个ConsumerGroup中的Consumer实例平均分摊消费消息。例如某个Topic有9条消息，其中一个ConsumerGroup有3个实例（可能是3个进程，或者3台机器），那么每个实例只消费其中部分，消费完的消息不能被其他实例消费。
        consumer.setMessageModel(MessageModel.CLUSTERING);

        consumer.subscribe(JmsConfig.TOPIC, "*");

        // 注：只有在消息模式为MessageModel.CLUSTERING集群模式时，Broker才会自动进行重试，广播消息是不会重试的
        // 当Consumer处理时间过长，在超时时间内没有返回给Broker消费状态，那么Broker也会自动重试
        // 重试时间间隔配置，默认每条消息最多重试16次
        // messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        consumer.setMaxReconsumeTimes(4);

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            MessageExt msg = msgs.get(0);
            int reconsumeTimes = msg.getReconsumeTimes();
            System.out.println("消费重试次数" + reconsumeTimes);
            try {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), new String(msgs.get(0).getBody()));
                String topic = msg.getTopic();
                String body = new String(msg.getBody(), "utf-8");
                String tags = msg.getTags();
                String keys = msg.getKeys();
                System.out.println("topic=" + topic + ", tags=" + tags + ", keys=" + keys + ", msg=" + body);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (UnsupportedEncodingException e) {
                if(reconsumeTimes >= 2){
                    // TODO 记录信息，人工处理
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                e.printStackTrace();
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });

       /* consumer.registerMessageListener( new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                try {
                Message msg = msgs.get(0);
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), new String(msgs.get(0).getBody()));

                String topic = msg.getTopic();
                String body = new String(msg.getBody(), "utf-8");
                String tags = msg.getTags();
                String keys = msg.getKeys();
                System.out.println("topic=" + topic + ", tags=" + tags + ", keys=" + keys + ", msg=" + body);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            }
        });*/

        consumer.start();
        System.out.println("consumer start ...");
    }

}
