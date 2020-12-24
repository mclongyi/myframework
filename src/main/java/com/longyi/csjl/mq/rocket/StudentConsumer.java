package com.longyi.csjl.mq.rocket;


import com.alibaba.fastjson.JSONObject;
import com.longyi.csjl.elasticsearch.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RocketMQMessageListener(topic = "mclongyi-student-topic", selectorExpression = "ly-mq", consumerGroup = "china-longyi")
public class StudentConsumer implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {


    @Override
    public void onMessage(MessageExt message) {
        log.info("消费消息，msgId:{}",message.getMsgId()+"消费次数，"+message.getReconsumeTimes());
        log.info("insertSku consumer message :{}", message);
        Student student = JSONObject.parseObject(message.getBody(), Student.class);
        log.info("消费信息为:{}",student.toString());
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        /**
         * 一个新的订阅组第一次启动从指定时间点开始消费<br>
         * 后续再启动接着上次消费的进度开始消费<br>
         * 时间点设置参见DefaultMQPushConsumer.consumeTimestamp参数
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
        //消费时间戳
        consumer.setConsumeTimestamp(UtilAll.timeMillisToHumanString3(System.currentTimeMillis()));

        consumer.setMaxReconsumeTimes(3);
    }
}
