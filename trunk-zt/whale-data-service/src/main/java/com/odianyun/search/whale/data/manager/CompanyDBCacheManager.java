package com.odianyun.search.whale.data.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import com.odianyun.search.whale.common.util.JmxUtil;
import com.odianyun.search.whale.data.service.AbstractCompanyDBService;

public class CompanyDBCacheManager implements CompanyDBCacheManagerMBean{
	private Map<String, AbstractCompanyDBService> commandMap=new Hashtable<String, AbstractCompanyDBService>(); //用于配置的单个加载10min一次。
	private static Logger log = Logger.getLogger(CompanyDBCacheManager.class);
	public static CompanyDBCacheManager instance = new CompanyDBCacheManager();
	public volatile boolean isScheduled = false;
	public volatile boolean isIniting = false;
	private  Map<Integer, List<AbstractCompanyDBService>> internal_group_Commands=new Hashtable<Integer, List<AbstractCompanyDBService>>(); 
	private List<Integer> companyIds=new ArrayList<Integer>();
	
	private CompanyDBCacheManager(){
		JmxUtil.registerMBean(this);
	}
	
	public List<Integer> getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(List<Integer> companyIds) {
		this.companyIds = companyIds;
	}

	public synchronized void registe(AbstractCompanyDBService command){
		if(command != null){
			if(command.getInterval()<=0){
				throw new RuntimeException(command.getName()+" command.getInterval()=="+command.getInterval()+",<=0");
			}
			commandMap.put(command.getName(), command);
			List<AbstractCompanyDBService> commands=internal_group_Commands.get(command.getInterval());
			if(commands==null){
				commands=new ArrayList<AbstractCompanyDBService>();
				internal_group_Commands.put(command.getInterval(), commands);
			}
			commands.add(command);
		}
		log.info("CompanyDBCacheManager registe  -------" + command.getName());
	}
	
	public synchronized void init(List<Integer> companyIds){
		this.companyIds = companyIds;
		init();
	}
	
	public synchronized void init(){
		log.info("CompanyDBCacheManager begin init -------");
		if(isIniting){
			return;
		}
		isIniting = true;
		log.info("CompanyDBCacheManager commandList size :" + commandMap.size());
		reloadAll();
		log.info("CompanyDBCacheManager end init -------");
		startScheedule();
	}
	
	private void startScheedule() {
		for(Entry<Integer, List<AbstractCompanyDBService>> entry:internal_group_Commands.entrySet()){
			final Integer key=entry.getKey();
			final List<AbstractCompanyDBService> abstractDBServices=entry.getValue();
			Thread dbScheduleThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true){
						try {
							Thread.sleep(key*60*1000);
						} catch (InterruptedException e) {
							log.error(e.getMessage(), e);
						}
						for(AbstractCompanyDBService abstractDBService:abstractDBServices){
							reloadCache(abstractDBService,companyIds);
						}
					}		
				}
			});
			dbScheduleThread.setName("CompanyDB-ReloadThread-Timer-"+entry.getKey());
			dbScheduleThread.setDaemon(true);
			dbScheduleThread.start();
		}
		
	}
	
	public synchronized void reloadCaches(Collection<AbstractCompanyDBService> commandList,List<Integer> companyIds) {
		ExecutorService cacheExec=null;
		try{
			if(commandList!=null){
				cacheExec = Executors.newFixedThreadPool(commandList.size());
				execReload(cacheExec, commandList,companyIds);
			}
		}finally{
			if(cacheExec!=null){
				cacheExec.shutdownNow();
			}
		}
		
	}

	private void execReload(ExecutorService cacheExec,
			Collection<AbstractCompanyDBService> commandList,final List<Integer> companyIds) {
		CompletionService<Boolean> completionService=new ExecutorCompletionService<>(cacheExec);
		Set<Future<Boolean>> futures=new HashSet<Future<Boolean>>();
		for(final AbstractCompanyDBService command : commandList){
			Future<Boolean> future=completionService.submit(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					reloadCache(command, companyIds);
					return Boolean.TRUE;
				}
			});
			futures.add(future);
		}
		
		while(futures.size()>0){
			try {
				Future<Boolean> future=completionService.poll(5, TimeUnit.MINUTES);
				if(future!=null){
					futures.remove(future);
				}else{
					break;
				}
			} catch (Throwable e) {
				log.error(e.getMessage(), e);
				break;
			}
			
		}
	
	}
	
	private void reloadCache(AbstractCompanyDBService abStractDBService,List<Integer> companyIds){
		for(Integer companyId:companyIds){
			abStractDBService.reload(companyId);
		}
	}
	
	@Override
	public synchronized void reloadAll() {
		reloadCaches(commandMap.values(),companyIds);
	}

	@Override
	public synchronized void reload(int companyId) {
		List<Integer> companyIds=new ArrayList<Integer>();
		companyIds.add(companyId);
		reloadCaches(commandMap.values(),companyIds);
	}

	@Override
	public synchronized void reload(String name,int companyId) {
		AbstractCompanyDBService dbService=commandMap.get(name);
		if(dbService!=null){
			dbService.reload(companyId);
		}else{
			throw new RuntimeException(name+" is incorrect");
		}
	}

	@Override
	public synchronized void reload(String name, List<Long> ids,int companyId) {
		AbstractCompanyDBService dbService=commandMap.get(name);
		if(dbService!=null){
			dbService.reload(ids,companyId);
		}else{
			throw new RuntimeException(name+" is incorrect");
		}
		
	}
	
}
