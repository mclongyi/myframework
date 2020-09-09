package com.odianyun.search.whale.index.api.common;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.odianyun.mq.common.ProtocolType;
import com.odianyun.mq.common.inner.exceptions.SendFailedException;
import com.odianyun.mq.common.message.Destination;
import com.odianyun.mq.producer.Producer;
import com.odianyun.mq.producer.ProducerConfig;
import com.odianyun.mq.producer.SendMode;
import com.odianyun.mq.producer.impl.ProducerFactoryImpl;

public class SearchUpdateSender {
	
	protected static Producer producer;

    protected static Logger logger = Logger.getLogger(SearchUpdateSender.class);
	
	static {
        ProducerFactoryImpl producerFactoryImpl = ProducerFactoryImpl.getInstance();
        //按需配置producer的参数
        ProducerConfig config = new ProducerConfig();
        config.setMode(SendMode.SYNC_MODE);
        config.setSyncRetryTimes(5);
        config.setThreadPoolSize(10);

        //创建指定topic的producer
        producer = producerFactoryImpl.createProducer(
                Destination.topic(IndexConstants.CACHE_TOPIC), config);
	}
	
    public static void sendUpdate(List<Long> ids,UpdateType updateType,int companyId) {
    	if(CollectionUtils.isEmpty(ids)){
        	logger.warn("send ids is : " + ids);
    		return;
    	}
        try {
            //发送消息，指定消息体的序列化方式为JSON
            producer.sendMessage(new UpdateMessage(ids,updateType,companyId), ProtocolType.JSON);
//            logger.info("send ids: "+ ids +" and updateType is :" + updateType);
        } catch (SendFailedException e) {
        	logger.error("send ids error: "+ e);
        }
    }
}
