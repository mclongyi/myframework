package com.longyi.csjl.mq.rocket;

import com.alibaba.fastjson.JSON;
import com.longyi.csjl.exception.LongyiException;
import com.longyi.csjl.elasticsearch.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Service
@Slf4j
public class StudentProducter {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    private static final String topic="mclongyi-student-topic";

    public void sendMsg(Student student){
        String msg = JSON.toJSONString(student);
        log.info("发送消息");
        try{
            Message build = MessageBuilder.withPayload(msg).build();
            SendResult sendResult = rocketMQTemplate.syncSend(topic+":ly-mq", build);
            log.info("生产消息结果：{}", sendResult);
            if(!sendResult.getSendStatus().toString().equals("SEND_OK")){
                throw new LongyiException("50001","发送MQ消息失败");
            }
        }catch (Exception e){
            log.error("发送消息异常:",e);
        }
    }


}
