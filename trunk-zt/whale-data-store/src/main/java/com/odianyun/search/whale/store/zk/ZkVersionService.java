package com.odianyun.search.whale.store.zk;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.odianyun.search.whale.common.thread.NamedThreadFactory;
import com.odianyun.search.whale.common.util.EnvUtil;
import com.odianyun.search.whale.common.util.FileUtils;
import com.odianyun.search.whale.store.conf.ConfigCenterUtil;
import com.odianyun.search.whale.store.zk.model.VersionData;

public class ZkVersionService {
	
	private static final Logger log = Logger.getLogger(ZkVersionService.class);
	
	private static String zk_quorums = ConfigCenterUtil.get("zk_quorums", "172.16.2.133:2181");
	
	private static String localPrefixPath = ConfigCenterUtil.get("localPrefixPath", "/data/index/version");
	
	private static String zk_path_prefix;

	private int connectCount = 0;
	
	private ZkClient zkClient;
	
	private Map<String, List<VersionChangeListener>> versionListeners = new HashMap<String, List<VersionChangeListener>>();
	
	private Map<String,VersionData> versionDataMap = new HashMap<String,VersionData>();

	private Map<String,Set<String>> versonMap = new HashMap<String,Set<String>>();
	
	public final static String INDEX = "index";
	
	public final static String SEARCH = "search";

	static{
		String env = EnvUtil.getEnv();
//		String env = "test";
		zk_path_prefix = "/" + env + "/";
	}
	
	public static ZkVersionService instance = new ZkVersionService();
	
	private Gson gson = new Gson();
	
	/**
	 * 配置更改listener调度线程池。
	 * maxThread：10
	 * queue：100
	 * 队列达到上限后，任务直接丢弃
	 */
	private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
			2, 10, 60L, TimeUnit.SECONDS,
             new LinkedBlockingQueue<Runnable>(100),
			new NamedThreadFactory("zkVersion-listener"),
			new ThreadPoolExecutor.AbortPolicy());
	
	private ZkVersionService(){
		this.zkClient = new ZkClientService(zk_quorums);
	}
	
	private synchronized void checkConnect(){
		if(connectCount<5){
			connectCount++;
			zkClient.connect();		
		}
	}
	
	private synchronized void checkZkPathExists(String versionZkPath) throws InterruptedException{
		if (zkClient.getNode(versionZkPath, null) == null) {
			log.info("Init zk path: " + versionZkPath);
			zkClient.createPersistentNode(versionZkPath);
		}
	}
	
	private synchronized VersionData loadVersion(final String versionZkPath) {		
		byte[] data = null;
		int count=0;
		final String localVersionPath = localPrefixPath + versionZkPath.substring(zk_path_prefix.length());

		while(count++<3){
			boolean succ=true;
			try {
				checkConnect();
				checkZkPathExists(versionZkPath);
				data = zkClient.getNode(versionZkPath,
						new ZkClient.NodeListener() {
							@Override
							public void onNodeDeleted(String id) {
								log.info("onNodeDeleted triggered:" + versionZkPath);
								versionDataMap.remove(versionZkPath);
								Set<String> versions = versonMap.get(versionZkPath);
								versonMap.remove(versionZkPath);
								if(versions!=null && versions.size()>0){
									for(String version : versions){
										versionDataMap.remove(versionZkPath +"/"+ version);
									}
								}
								delete(localVersionPath);
								
							}

							@Override
							public void onNodeDataChanged(String id) {
								log.info("onNodeDataChanged triggered:" + versionZkPath);
								loadVersion(versionZkPath);
								List<VersionChangeListener> listeners = versionListeners.get(versionZkPath);
								if (listeners != null) {
									for (final VersionChangeListener listener : listeners) {
										threadPool.submit(new Runnable() {
											@Override
											public void run() {
												listener.onVersionChange(versionDataMap.get(versionZkPath));
											}
										});
									}
								}
							}

							@Override
							public void onNodeCreated(String id) {
							}
						});
			} catch (Throwable e) {
				log.error("can't get config from zk path:" + versionZkPath, e);
				succ=false;
			}
			if(succ){
				break;
			}
		}		
		if (data != null && data.length>0) {
			String tempData = new String(data);
			VersionData versionData = gson.fromJson(tempData, VersionData.class);
			versionDataMap.put(versionZkPath, versionData);
			backup(versionData, localVersionPath);//备份数据
		} else {//zk读取数据失败，尝试从本地读取
			VersionData dataStr = (VersionData) recovery(VersionData.class, localVersionPath);
			if (dataStr != null) {
				versionDataMap.put(versionZkPath, dataStr);
			}
		}
		log.info("loadConfig configZkPath:" + versionZkPath+",data:"+ versionDataMap.get(versionZkPath));
		return versionDataMap.get(versionZkPath);
	}
	
	private boolean backup(Object obj, String path) {
		try {
			String jsonFile = gson.toJson(obj);
			boolean succ =  FileUtils.updateFile(path, jsonFile);
			log.info("backup data to " + path + " over ,data=" + obj);
			return succ;
		} catch (Exception e) {
			log.error("backup config.", e);
			return false;
		}
	} 
	
	private Object recovery(Class<?> Obj, String path) {
		String fileContent = null;
		try {
			fileContent = FileUtils.readFileContent(path);
		} catch (IOException e) {
			log.error("recovery config.", e);
			return null;
		}
		
		if (StringUtils.isEmpty(fileContent)) {
			return null;
		}
		Object data =  gson.fromJson(fileContent, Obj);
		log.info("recovery data from " + path + " over ,data=" + data);
		return data;
	}

	/**
	 * 
	 * @param prefixPath
	 * @return
	 */
	public Set<String> listZkVersionNodes(String prefixPath) {
//		String configZkPath = getVersionPath(prefixPath, null);
		String configZkPath = prefixPath;

		int count=0;
		while(count++<3){
			boolean succ=true;
			try {				
				checkConnect();
				checkZkPathExists(configZkPath);
				Set<String> pathSet = zkClient.listNodes(configZkPath, null);
				log.info("listZkConfigPaths sucessful configZkPath:"+configZkPath);
				return pathSet;
			} catch (Exception e) {
				log.error("listZkConfigPaths error,configZkPath:"+configZkPath, e);
				succ=false;
			}
			if(succ){
				break;
			}
		}	
		return null;
	}
	
	public Set<String> listIndices() {
		String versionPath = getVersionPath(INDEX,"");
		return getVersion(versionPath);

//		return getVersion(INDEX);
	}
	
	public Set<String> getDataVersions(String indexName) {
		String versionPath = getVersionPath(INDEX,indexName);
		return getVersion(versionPath);

//		return getVersion(INDEX+"/"+indexName);
	}
	
	public VersionData getDataVersionData(String indexName, String dataVersion) {
		String versionPath = getVersionPath(INDEX,indexName+"/"+dataVersion);
		return getVersionData(versionPath);
	}
	
	public Set<String> getIndexVersion(String indexName, String dataVersion){
		String versionPath = getVersionPath(INDEX,indexName+"/"+dataVersion);
		return getVersion(versionPath);

//		return getVersion(INDEX+"/"+indexName+"/"+dataVersion);
	}
	
	public VersionData getIndexVersionData(String indexName, String dataVersion, String indexVersion){
		String versionPath = getVersionPath(INDEX,indexName+"/"+dataVersion+"/"+indexVersion);
		return getVersionData(versionPath);
	}
	
	private Set<String> getVersion(String versionPath){
		if (versonMap.containsKey(versionPath)) {
			return versonMap.get(versionPath);
		}
		synchronized (this) {
			if (versonMap.containsKey(versionPath)) {
				return versonMap.get(versionPath);
			}
			Set<String> versionData = listZkVersionNodes(versionPath);
			versonMap.put(versionPath, versionData);
			return versionData;
		}
	}
	
	private VersionData getVersionData(String versionPath) {
		if (versionDataMap.containsKey(versionPath)) {
			return versionDataMap.get(versionPath);
		} 
		synchronized (this) {
			if (versionDataMap.containsKey(versionPath)) {
				return versionDataMap.get(versionPath);
			}
			VersionData versionData = loadVersion(versionPath);
			versionDataMap.put(versionPath, versionData);
			return versionData;
		}
	}

	
	public void updateZkVersion(String indexName, String dataVersion, String indexVersion, VersionData versionData){
		if(StringUtils.isBlank(indexName)){
			return;
		}
		if(StringUtils.isBlank(dataVersion)){
			updateZKVersion(INDEX, indexName, null);
			return;
		}
		if(StringUtils.isBlank(indexVersion)){
			updateZKVersion(INDEX+"/"+indexName,dataVersion,versionData);
			return;
		}
		updateZKVersion(INDEX+"/"+indexName+"/"+dataVersion,indexVersion,versionData);
		
	}
	
	public void updateZkVersion(String key, String version, VersionData versionData){
		if(StringUtils.isBlank(key)){
			return;
		}
		if(StringUtils.isBlank(version)){
			updateZKVersion(INDEX, key, null);
			return;
		}
		updateZKVersion(INDEX+"/"+key, version, versionData);
		
	}
	
	public void deleteZKVersion(String indexName, String dataVersion, String indexVersion) {
		if(StringUtils.isBlank(indexName)){
			return;
		}
		if(StringUtils.isBlank(dataVersion)){
			deleteZKVersion(INDEX, indexName);
			return;
		}
		if(!StringUtils.isBlank(indexVersion)){
			deleteZKVersion(INDEX+"/"+indexName,dataVersion);
			return;
		}
		deleteZKVersion(INDEX+"/"+indexName+"/"+dataVersion,indexVersion);
	}

	
	private synchronized void deleteZKVersion(String path, String versionName) {
		String versionZkPath = getVersionPath(path, versionName);
		int count=0;
		while(count++<3){
			boolean succ=true;
			try {
				checkConnect();
				checkZkPathExists(versionZkPath);
				//TODO 递归或是只删除当前节点？
//				zkClient.deleteNode(versionZkPath);
				zkClient.deleteNodeRecursively(versionZkPath);
				log.info("deleteZKConfig sucessful configZkPath:"+versionZkPath);
			} catch (Exception e) {
				log.error("Delete zk config error,configZkPath:"+versionZkPath, e);
				succ=false;
			}
			if(succ){
				break;
			}
		}		
	}
	
	private synchronized void updateZKVersion(String path, String versionName, VersionData versionData) {
		String versionZkPath = getVersionPath(path, versionName);
		int count=0;
		while(count++<3){
			boolean succ=true;
			try {				
				checkConnect();
				checkZkPathExists(versionZkPath);
				String data = gson.toJson(versionData);
				zkClient.setOrCreatePersistentNode(versionZkPath, data.getBytes());
				log.info("updateZKVersion sucessful configZkPath:"+versionZkPath+",data:"+data);
			} catch (Throwable e) {
				log.error("Update zk version error,configZkPath:"+versionZkPath+",data:" + versionData, e);
				succ=false;
			}
			if(succ){
				break;
			}
		}		
	}
	
	private synchronized String getVersionPath(String versionZkPath, String versionName) {
		versionZkPath = deleteHeadTailSlash(versionZkPath);
		if(StringUtils.isBlank(versionZkPath) && StringUtils.isBlank(versionName)){
			return zk_path_prefix.substring(0, zk_path_prefix.length()-1);
		}
		if(StringUtils.isBlank(versionZkPath)) {
			return zk_path_prefix + versionName;
		}
		if(StringUtils.isBlank(versionName)) {
			return zk_path_prefix + versionZkPath;
		}
		
		return zk_path_prefix + versionZkPath + "/" + versionName;
	}
	
	private synchronized String deleteHeadTailSlash(String path){
		if(StringUtils.isBlank(path) || path.equals("/"))
			return "";
		
		if (path.startsWith("/")) {
			path = path.substring(1, path.length());
		}
		
		if (path.lastIndexOf('/') == path.length() - 1) {
			 path = path.substring(0, path.length() - 1);
		}
		
		return path;
	}
	
	private void delete(String path) {
		try {
			FileUtils.deleteFile(new File(path));
			log.info("delete file, path= " + path);
		} catch (Exception e) {
			log.error("backup config.", e);
		}
	} 
	
	public interface VersionChangeListener{
		public void onVersionChange(VersionData data);
	}
	
	public static void main(String[] args) {
		System.setProperty("global.config.path", "/Users/fishcus/JavaDev/data/env");

		String indexName = "b2c_alias";
		String dataVersionStr = "data_20160715";
		String indexVersionStr = "index_20160715";
		ZkVersionService.instance.updateZkVersion(indexName, "", "", null);
		
		Set<String> indices = ZkVersionService.instance.listIndices();
		System.out.println("indices:  "+indices);
		System.out.println("--------------------------------------");

		VersionData dataVersion = new  VersionData();
		dataVersion.setVersion(dataVersionStr);
		dataVersion.setCount(100l);
		dataVersion.setIsSuccess(true);
		ZkVersionService.instance.updateZkVersion(indexName, dataVersionStr, "", dataVersion);
		
		Set<String> dataVersions = ZkVersionService.instance.getDataVersions(indexName);
		System.out.println("dataVersions: :  "+dataVersions);
		dataVersion = ZkVersionService.instance.getDataVersionData(indexName, dataVersionStr);
		System.out.println("dataVersion data: "+dataVersion);
		System.out.println("--------------------------------------");

		VersionData indexVersion = new  VersionData();
		indexVersion.setVersion(dataVersionStr);
		indexVersion.setCount(100l);
		indexVersion.setIsSuccess(true);
		ZkVersionService.instance.updateZkVersion(indexName, dataVersionStr, indexVersionStr, indexVersion);
		Set<String> indexVersions = ZkVersionService.instance.getIndexVersion(indexName, dataVersionStr);
		System.out.println("indexVersions" + indexVersions);
		
		indexVersion = ZkVersionService.instance.getIndexVersionData(indexName, dataVersionStr, indexVersionStr);
		System.out.println("indexVsersion data: "+indexVersion);
		System.out.println("--------------------------------------");

	}

}
