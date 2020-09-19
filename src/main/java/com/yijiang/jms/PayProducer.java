package com.yijiang.jms;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;

@Component
public class PayProducer {

    private String producerGroup = "pay_producer_group";

    private String nameServerAddr= JmsConfig.NAME_SERVER;

    private DefaultMQProducer producer;

    public  PayProducer(){
        producer = new DefaultMQProducer(producerGroup);
        //生者产重复发送次数
        producer.setRetryTimesWhenSendFailed(3);
        /*  指定NameServer地址，多个地址以 ; 隔开
        如 producer.setNamesrvAddr("192.168.100.141:9876;192.168.100.142:9876;192.168.100.149:9876");  */
        producer.setNamesrvAddr(nameServerAddr);
        start();
    }

    public DefaultMQProducer getProducer(){

        return this.producer;
    }

    /**
     * 对象在使用之前必须要调用一次，只能初始化一次
     */
    public void start(){
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }


    /**
     * 一般在应用上下文，使用上下文监听器，进行关闭
     */
    public void shutdown(){
        this.producer.shutdown();
    }


}
