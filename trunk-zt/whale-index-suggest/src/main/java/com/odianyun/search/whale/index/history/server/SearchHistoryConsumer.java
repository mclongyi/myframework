package com.odianyun.search.whale.index.history.server;

import com.odianyun.search.whale.data.manager.UpdateConsumer;
import org.apache.log4j.Logger;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.mq.common.consumer.ConsumerType;
import com.odianyun.mq.common.message.Destination;
import com.odianyun.mq.common.message.Message;
import com.odianyun.mq.consumer.BackoutMessageException;
import com.odianyun.mq.consumer.Consumer;
import com.odianyun.mq.consumer.ConsumerConfig;
import com.odianyun.mq.consumer.ConsumerFactory;
import com.odianyun.mq.consumer.MessageListener;
import com.odianyun.mq.consumer.impl.ConsumerFactoryImpl;
import com.odianyun.search.whale.common.util.NetUtil;
import com.odianyun.search.whale.index.api.model.req.HistoryWriteRequest;
import com.odianyun.search.whale.index.history.handler.HistoryLogHandler;

public class SearchHistoryConsumer {

	@Autowired
	HistoryLogHandler historyLogHandler;
	
	private static Logger log = Logger.getLogger(SearchHistoryConsumer.class);

	public static final String HISTORY = "history";

	public void startConsumerReload(String topic) {
		startConsumerReload(topic, null);
	}

	public void startConsumerReload(String topic, String nameSpace) {
		try {
			ConsumerFactory consumerFactory = ConsumerFactoryImpl.getInstance();
			ConsumerConfig config = new ConsumerConfig();
			config.setConsumerType(ConsumerType.CLIENT_ACKNOWLEDGE);
			config.setThreadPoolSize(10);
			Destination destination = null;
			if (StringUtils.isNotBlank(nameSpace)) {
				destination = Destination.topic(nameSpace, topic);
			} else {
				destination = Destination.topic(topic);
			}
			Consumer consumer = consumerFactory.createLocalConsumer(destination,
					UpdateConsumer.genConsumerIdWithSuffix(HISTORY), config);
			consumer.setListener(new MessageListener() {
				@Override
				public void onMessage(Message msg) throws BackoutMessageException {
					HistoryWriteRequest request = msg.transferContentToBean(HistoryWriteRequest.class);
					String keyword =	 request.getKeyword();
		    			String userId = request.getUserId();
		    			if(StringUtils.isBlank(userId) || StringUtils.isBlank(keyword)){
		    				log.warn("consume history userId | keyword is blacnk !!!"  );
		    				return;
		    			}
//	    				log.info("consume history userId :" +userId + " && keyword : "+ keyword  );
					try {
						logSearchHistory(request);
					} catch (Throwable e) {
						log.error("consume history failed: ", e);
					}
				}
			});
			consumer.start();
		} catch (Exception e) {
			log.error("start history comsumer failed==================================================", e);
		}
	}

	protected void logSearchHistory(HistoryWriteRequest request) {
		historyLogHandler.handle(request);
	}

}
