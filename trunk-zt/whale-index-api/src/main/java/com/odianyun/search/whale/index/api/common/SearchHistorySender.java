package com.odianyun.search.whale.index.api.common;

import com.odianyun.search.whale.index.api.model.req.HistoryType;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.odianyun.mq.common.ProtocolType;
import com.odianyun.mq.common.inner.exceptions.SendFailedException;
import com.odianyun.mq.common.message.Destination;
import com.odianyun.mq.producer.Producer;
import com.odianyun.mq.producer.ProducerConfig;
import com.odianyun.mq.producer.SendMode;
import com.odianyun.mq.producer.impl.ProducerFactoryImpl;
import com.odianyun.search.whale.index.api.model.req.HistoryWriteRequest;

public class SearchHistorySender {
	
	private static Producer producer;
	
	static Logger logger = Logger.getLogger(SearchHistorySender.class);
	
	static {
        ProducerFactoryImpl producerFactoryImpl = ProducerFactoryImpl.getInstance();
        //按需配置producer的参数
        ProducerConfig config = new ProducerConfig();
        config.setMode(SendMode.ASYNC_MODE);
        config.setSyncRetryTimes(3);
        config.setThreadPoolSize(100);

        //创建指定topic的producer
        producer = producerFactoryImpl.createProducer(
                Destination.topic(IndexConstants.SEARCH_HISTORY_TOPIC), config);
	}
	
    public static void sendHistory(HistoryWriteRequest request) {
        if (request.getMerchantId() != null && request.getMerchantId() != 0) {
            request.setType(HistoryType.MERCHANT);
        }
    		String keyword =	 request.getKeyword();
    		String userId = request.getUserId();
    		if(StringUtils.isBlank(userId) || StringUtils.isBlank(keyword)){
    			logger.warn("send userId | keyword is blacnk !!!"  );
    			return;
    		}
        try {
            //发送消息，指定消息体的序列化方式为JSON
            producer.sendMessage(request, ProtocolType.JSON);
//            logger.info("send userId :" +userId + " && keyword : "+ keyword  );
        } catch (SendFailedException e) {
        		logger.error("send ids error: "+ e);
        }
    }
}
