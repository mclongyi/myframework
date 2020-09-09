package com.odianyun.search.whale.common.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
/**
 * HttpClietUtil send Get or Post request
 * 
 * @author zengfenghua
 *
 */
public class HttpClientUtil {
	
	private static HttpClient httpClient=getHttpClient();
	
	public static final String http_resp_successful="{'operate':'successful'}";
	
	
	private static ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

        @Override
        public String handleResponse(
                final HttpResponse response) throws ClientProtocolException, IOException {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        }

    };
    
    
    private static ResponseHandler<JSONObject> responseJSONObjectHandler = new ResponseHandler<JSONObject>() {

        @Override
        public JSONObject handleResponse(
                final HttpResponse response) throws ClientProtocolException, IOException {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? new JSONObject(EntityUtils.toString(entity)) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        }

    };

	/**
	 * httpClient的get请求方式
	 * 
	 * @return String
	 * @throws Exception
	 */
	public static String doGet(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
		return httpClient.execute(httpGet,responseHandler);
	}
	
	/**
	 * httpClient的get请求方式
	 * 
	 * @return JSONObject
	 * @throws Exception
	 */
	public static JSONObject doGet2JSONObject(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
		return httpClient.execute(httpGet,responseJSONObjectHandler);
	}
	
	/**
	 * post请求
	 * 
	 * @param url
	 * @param json
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */

	public static String doPost(String url, JSONObject json) throws Exception {
		HttpPost post = new HttpPost(url);
		StringEntity s = new StringEntity(json.toString());
		s.setContentEncoding("UTF-8");
		s.setContentType("application/json");// 发送json数据需要设置contentType
		post.setEntity(s);
		return httpClient.execute(post,responseHandler);
	}
	
	public static String doPost(String url, String json) throws Exception {
		HttpPost post = new HttpPost(url);
		StringEntity s = new StringEntity(json.toString());
		post.setHeader("Content-type", "application/json");
		post.setEntity(s);
		return httpClient.execute(post,responseHandler);
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param json
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */

	public static JSONObject doPost2JSONObject(String url, JSONObject json) throws Exception {
		HttpPost post = new HttpPost(url);
		StringEntity s = new StringEntity(json.toString());
		s.setContentEncoding("UTF-8");
		s.setContentType("application/json");// 发送json数据需要设置contentType
		post.setEntity(s);
		return httpClient.execute(post,responseJSONObjectHandler);
	}
	
	public static String doPut(String url, String json) throws Exception {
		HttpPut put = new HttpPut(url);
		StringEntity s = new StringEntity(json);
		put.setHeader("Content-type", "application/json");
		put.setEntity(s);
		return httpClient.execute(put,responseHandler);
	}
	
	public static String doDelete(String url) throws Exception {
		HttpDelete delete = new HttpDelete(url);
		return httpClient.execute(delete,responseHandler);
	}

	private static HttpClient getHttpClient() {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.setMaxConnTotal(500);
		httpClientBuilder.setMaxConnPerRoute(100);
		SocketConfig.Builder builder = SocketConfig.custom().setSoTimeout(10000)
				.setTcpNoDelay(true);
		httpClientBuilder.setDefaultSocketConfig(builder.build());
		return httpClientBuilder.build();
	}


}
