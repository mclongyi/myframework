package com.odianyun.search.whale.rest.util;

import java.io.IOException;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Created by Fu Yifan on 2017/8/24.
 */
public class MappingJackson2JsonpHttpMessageConverter extends
        MappingJackson2HttpMessageConverter {

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
        JsonGenerator jsonGenerator = this.getObjectMapper().getFactory().createGenerator(outputMessage.getBody(), encoding);

        try {
            // 对返回为String类型的数据做特殊处理
            if (object instanceof String) {
                outputMessage.getBody().write(((String) object).getBytes("UTF-8"));
            } else {
                // 不能抓到callback请求参数，写死
//                String jsonpCallback = "jsonpCallback";
//
//                jsonGenerator.writeRaw(jsonpCallback);
                this.getObjectMapper().writeValue(jsonGenerator, object);
                jsonGenerator.flush();
            }

        } catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

}
