package com.odianyun.search.whale.search.api.solrj;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.odianyun.search.whale.common.util.ConfigUtil;


public class SolrClient {
    private static HttpSolrServer client=null;
    private static String url=ConfigUtil.get("solr_url");
    private static String full_import_url=ConfigUtil.get("solr_full_import_url");
    
    public static HttpSolrServer getHttpSolrClient(){
    	if(client==null){
        	synchronized (SolrClient.class) {
        		if(client==null){
        			client = new HttpSolrServer(url);
                	client.setSoTimeout(5000);  // socket read timeout
                	client.setConnectionTimeout(5000);
                	client.setDefaultMaxConnectionsPerHost(100);
                	client.setMaxTotalConnections(100);
                	client.setFollowRedirects(false);  // defaults to false
                	client.setAllowCompression(true);
        		}
        		
			}
        	
        }
        return client;
    } 
    
    public static void execute_full_import() throws ClientProtocolException, IOException{
    	HttpSolrServer client=getHttpSolrClient();
        HttpClient httpClient=client.getHttpClient();
        HttpGet httpGet=new HttpGet(full_import_url);
        httpClient.execute(httpGet);
    }
}
