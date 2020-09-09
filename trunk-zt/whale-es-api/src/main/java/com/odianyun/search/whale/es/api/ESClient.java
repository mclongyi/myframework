package com.odianyun.search.whale.es.api;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ESClient {
	
//	static TransportClient client;
	
	private static String cluster_name=ESConfigUtil.get("cluster.name","ody-test-es-cluster");
	
	private static String node_ips=ESConfigUtil.get("node.ips","192.168.1.225,192.168.1.182:25555");
	
	public static String admin_url=ESConfigUtil.get("admin_url","http://192.168.1.225:25556");

	public static Integer es_port = 9300; 
	
	private static ConcurrentHashMap<String, TransportClient> clusterClientMap = new ConcurrentHashMap<String, TransportClient>();
	
	public static Client getClient() throws ElasticsearchException, UnknownHostException{
		return getClient(cluster_name, node_ips);
	}
	
	public static Client getClient(String clusterName, String nodeIps) throws ElasticsearchException, UnknownHostException{
		String[] nodeIpsArray = nodeIps.split(",");
		if(clusterClientMap.containsKey(clusterName)){
			return clusterClientMap.get(clusterName);
		}
		
		synchronized (ESClient.class) {
			/*if(client!=null){
				return client;
			}*/
			if(clusterClientMap.containsKey(clusterName)){
				return clusterClientMap.get(clusterName);
			}
			Settings settings = ImmutableSettings.settingsBuilder()
			        .put("cluster.name", clusterName).build();
			TransportClient client =  new TransportClient(settings);
//			Map<String,Integer> nodeMap = new HashMap<String,Integer>();
			List<EsNode> nodeList = new ArrayList<EsNode>();
			Integer port = null;
			int max = nodeIpsArray.length-1;
			for(int i=max;i>=0;i--){
				String[] ipPort = nodeIpsArray[i].split(":");
				if(null == ipPort || ipPort.length == 0){
					continue;
				}
				String ip = ipPort[0];
				if(ipPort.length == 2){
					port = Integer.valueOf(ipPort[1]);
				}else{
					if(null == port || port == 0){
						port = es_port;
					}
				}
//				nodeMap.put(ip, port);
				EsNode esNode = new EsNode(ip, port);
				nodeList.add(esNode);
			}
			
			for(EsNode esNode : nodeList){
				client.addTransportAddress(new InetSocketTransportAddress(
		        		InetAddress.getByName(esNode.getIp()), esNode.getPort()));
			}
			
			/*for(int i=0;i<nodeIpsArray.length;i++){
				client.addTransportAddress(new InetSocketTransportAddress(
		        		InetAddress.getByName(nodeIpsArray[i]), 9300));
			}  */
			
			clusterClientMap.put(clusterName, client);
		}
		return clusterClientMap.get(clusterName);
//		return client;
	}
	private static class EsNode{
		/**
		 * @return the ip
		 */
		public String getIp() {
			return ip;
		}
		/**
		 * @return the port
		 */
		public Integer getPort() {
			return port;
		}
		public EsNode(String ip,Integer port){
			this.ip = ip;
			this.port = port;
		}
		String ip;
		Integer port;
		
	}

}
