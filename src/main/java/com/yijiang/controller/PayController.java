package com.yijiang.controller;

import com.yijiang.domain.ProductOrder;
import com.yijiang.jms.JmsConfig;
import com.yijiang.jms.PayProducer;
import com.yijiang.jms.TransactionProducer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class PayController {

    @Autowired
    private PayProducer payProducer;
    private TransactionProducer transactionProducer;
    private static final String topic = JmsConfig.TOPIC;

    /**
     * @param
     * @return
     * @author jasonxiao
     * @description 同步发送mq  可设置延迟消息
     * @date 2020/9/17
     */
    @RequestMapping("/api/v1/pay_cb")
    public Object sync(String text) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        // key 唯一 保证 生产者重试，比如订单号
        Message message = new Message(topic, "taga", "6688", ("hello xdclass rocketmq = " + text).getBytes());
        // 设置延迟消息 "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
        // 2代表级别，代表 5s
        message.setDelayTimeLevel(2);
        SendResult sendResult = payProducer.getProducer().send(message);
        System.out.println(sendResult);

        return sendResult;
    }

    /**
     * @param
     * @return
     * @author jasonxiao
     * @description 异步发送mq 不会重试，只发送 1 次
     * @date 2020/9/17
     */
    @RequestMapping("/api/v1/pay_cbbb")
    public Object async(String text) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {

        Message message = new Message(JmsConfig.TOPIC, "taga", "66881", ("hello xdclass rocketmq = " + text).getBytes());

        //payProducer.getProducer().sendOneway(message);

        payProducer.getProducer().send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.printf("发送结果=%s, msg=%s ", sendResult.getSendStatus(), sendResult.toString());
            }

            @Override
            public void onException(Throwable e) {
                e.printStackTrace();
                //补偿机制，根据业务情况进行使用，看是否进行重试
            }
        });
        return new HashMap<>();
    }

    /**
     * @param
     * @return
     * @author jasonxiao
     * @description ONEWAY方式发送mq
     * @date 2020/9/17
     */
    @RequestMapping("/api/v1/oneWay")
    public Object oneWay(String text) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {

        Message message = new Message(JmsConfig.TOPIC, "taga", "66881", ("hello xdclass rocketmq = " + text).getBytes());
        payProducer.getProducer().sendOneway(message);

        return new HashMap<>();
    }

    /**
     * @param
     * @return
     * @author jasonxiao
     * @description MessageQueueSelector  同步, 应用在 顺序消息
     * @date 2020/9/17
     */
    @RequestMapping("/api/v1/syncMessageQueueSelector")
    public Object syncMessageQueueSelector(String text) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {

        Message message = new Message(JmsConfig.TOPIC, "taga", "66881", ("hello xdclass rocketmq = " + text).getBytes());
        SendResult sendResult = payProducer.getProducer().send(message, new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                int queueNum = Integer.parseInt(o.toString());
                return list.get(queueNum);
            }
        }, 0);
        System.out.printf("发送结果=%s, msg=%s ", sendResult.getSendStatus(), sendResult.toString());

        return sendResult;
    }

    /**
     * @param
     * @return
     * @author jasonxiao
     * @description MessageQueueSelector  异步, 不能保证顺序
     * @date 2020/9/17
     */
    @RequestMapping("/api/v1/asyncMessageQueueSelector")
    public Object asyncMessageQueueSelector(String text) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {

        Message message = new Message(JmsConfig.TOPIC, "taga", "66881", ("hello xdclass rocketmq = " + text).getBytes());

        payProducer.getProducer().send(message, new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                int queueNum = Integer.parseInt(o.toString());
                return list.get(queueNum);
            }
        }, 0, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.printf("发送结果=%s, msg=%s ", sendResult.getSendStatus(), sendResult.toString());
            }

            @Override
            public void onException(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        return new HashMap<>();
    }


    /**
     * @param
     * @return
     * @author jasonxiao
     * @description 案例：电商订单消息发送  同步, 应用在 顺序消息
     * @date 2020/9/17
     */
    @RequestMapping("/api/v1/syncMessageQueueSelector")
    public Object orderMessage(String text) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        List<ProductOrder> list = ProductOrder.getOrderList();

        for (int i = 0; i < list.size(); i++) {
            ProductOrder order = list.get(i);
            Message message = new Message(JmsConfig.ORDERLY_TOPIC, "",
                    order.getOrderId() + "", order.toString().getBytes());

            SendResult sendResult = payProducer.getProducer().send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Long id = (Long) arg;
                    long index = id % mqs.size();
                    return mqs.get((int) index);
                }
            }, order.getOrderId());
            System.out.printf("发送结果=%s, sendResult=%s ,orderid=%s, type=%s\n", sendResult.getSendStatus(),
                    sendResult.toString(), order.getOrderId(), order.getType());
        }
        return new HashMap<>();
    }
    /**
     * @author jasonxiao
     * @description 分布式事务MQ
     * @date 2020/9/19 
     * @param 
     * @return 
     */
    @RequestMapping("/api/v1/transaction")
    public Object transactionMQProducer( String tag, String otherParam ) throws Exception {

        Message message = new Message(JmsConfig.TOPIC, tag, tag+"_key",tag.getBytes());

        SendResult sendResult =  transactionProducer.getProducer().
                sendMessageInTransaction(message, otherParam);

        System.out.printf("发送结果=%s, sendResult=%s \n", sendResult.getSendStatus(), sendResult.toString());

        return new HashMap<>();
    }


}






