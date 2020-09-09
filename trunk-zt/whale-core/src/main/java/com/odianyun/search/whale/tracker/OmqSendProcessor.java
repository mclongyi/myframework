package com.odianyun.search.whale.tracker;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.misc.SearchRequestValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.odianyun.mq.common.ProtocolType;
import com.odianyun.mq.common.inner.exceptions.SendFailedException;
import com.odianyun.mq.common.message.Destination;
import com.odianyun.mq.producer.Producer;
import com.odianyun.mq.producer.ProducerConfig;
import com.odianyun.mq.producer.SendMode;
import com.odianyun.mq.producer.impl.ProducerFactoryImpl;
import com.odianyun.search.backend.model.RequestContext;
import com.odianyun.search.whale.api.model.req.SearchRequest;
import com.odianyun.search.whale.api.model.req.ShopSearchRequest;
import com.odianyun.search.whale.common.ConvertHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class OmqSendProcessor implements SearchProcessor {

	private Producer producer;
	private final String OMQ_NAMESPACE = "saas";
	private final String OMQ_TOPIC = "search_request_todb";
	private final String OMQ_FILTER_TYPE = "requestToDb";
    private final Logger logger = Logger.getLogger(OmqSendProcessor.class);
	
	public OmqSendProcessor() {
		createOmqProducer();
	}
	
    private void createOmqProducer() {
		
		ProducerFactoryImpl producerFactoryImpl = ProducerFactoryImpl.getInstance();
        ProducerConfig producerConfig = new ProducerConfig();
        producerConfig.setMode(SendMode.ASYNC_MODE);//异步发送
        producerConfig.setAsyncRetryTimes(3);//发送失败重复次数
        producerConfig.setThreadPoolSize(10);
        producer = producerFactoryImpl.createProducer(Destination.topic(OMQ_NAMESPACE, OMQ_TOPIC), producerConfig);
	}
    
    private void sendToOmq(Object request) {
		try {
            producer.sendMessage(request, OMQ_FILTER_TYPE, ProtocolType.JSON);
        } catch (SendFailedException ex) {
            logger.error("send request to omq error: " + ex, ex);
        }
	}


	@Override
	public void process(TrackContext context) {
		try {

			RequestContext requestLog;
			if(context.request instanceof SearchRequest) {//将request转化成log，并发送omq
				requestLog = ConvertHelper.ConvertToRequestContext((SearchRequest)context.request);
			}
			else {
				requestLog = ConvertHelper.ConvertToRequestContext((ShopSearchRequest)context.request);
			}

			String keyword = requestLog.getKeyword();
			if(StringUtils.isBlank(keyword) || keyword.equals(SearchRequestValidator.special_keyword) ){
				return;
			}
			keyword = filterString(keyword);
			if(StringUtils.isBlank(keyword)){
				return;
			}
			requestLog.setKeyword(keyword);
			requestLog.setHitCount((int)context.response.getTotalHit());
			requestLog.setCostTime((int)context.response.costTime);

			sendToOmq(requestLog);
			logger.info("搜索词的日志:" + requestLog);
		}
		catch(Exception ex) {
			logger.error("OmqSendProcessor error :" + ex.getMessage(), ex);
		}
	}


	static String filterString(String str) throws PatternSyntaxException {
		String regEx= "[`~!@#$%^&*()+=|{}':;',\"\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return  m.replaceAll("").trim();
	}
}
