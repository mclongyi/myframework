package com.odianyun.search.whale.index.full;

import com.odianyun.mq.common.ProtocolType;
import com.odianyun.mq.common.inner.exceptions.SendFailedException;
import com.odianyun.search.whale.index.api.common.SearchUpdateSender;
import com.odianyun.search.whale.index.api.common.UpdateMessage;

/**
 * Created by fishcus on 16/11/24.
 */
public class IndexUpdateSender extends SearchUpdateSender {

    public static void sendMeaasge(UpdateMessage message){
        if(message == null){
            logger.warn("send message is null ");
            return;
        }
        try {
            //发送消息，指定消息体的序列化方式为JSON
            producer.sendMessage(message, ProtocolType.JSON);
//            logger.info("send ids: "+ ids +" and updateType is :" + updateType);
        } catch (SendFailedException e) {
            logger.error("send ids error: "+ e);
        }
    }
}
