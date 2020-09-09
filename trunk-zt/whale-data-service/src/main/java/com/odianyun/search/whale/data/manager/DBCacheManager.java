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
import com.odianyun.search.whale.data.service.impl.AbstractDBService;

/**
 * join缓存
 *
 * @author ody
 *
 */
public class DBCacheManager implements DBCacheManagerMBean {
	private Map<String, AbstractDBService> commandMap = new Hashtable<String, AbstractDBService>(); // 用于配置的单个加载10min一次。
	private static Logger log = Logger.getLogger(DBCacheManager.class);
	public static DBCacheManager instance = new DBCacheManager();
	public volatile boolean isScheduled = false;
	public volatile boolean isIniting = false;
	private Map<Integer, List<AbstractDBService>> internal_group_Commands = new Hashtable<Integer, List<AbstractDBService>>();

	private DBCacheManager() {
		JmxUtil.registerMBean(this);
	}

	public synchronized void registe(AbstractDBService command) {
		if (command != null) {
			if (command.getInterval() <= 0) {
				throw new RuntimeException(
						command.getName() + " command.getInterval()==" + command.getInterval() + ",<=0");
			}
			commandMap.put(command.getName(), command);
			List<AbstractDBService> commands = internal_group_Commands.get(command.getInterval());
			if (commands == null) {
				commands = new ArrayList<AbstractDBService>();
				internal_group_Commands.put(command.getInterval(), commands);
			}
			commands.add(command);
		}
		log.info("DBCacheManager registe  -------" + command.getName());
	}

	public synchronized void init() {
		log.info("DBCacheManager begin init -------");
		if (isIniting) {
			return;
		}
		isIniting = true;
		log.info("commandList size :" + commandMap.size());
		reloadCaches(commandMap.values());
		log.info("DBCacheManager end init -------");
		startScheedule();
	}

    private void startScheedule() {
		for (Entry<Integer, List<AbstractDBService>> entry : internal_group_Commands.entrySet()) {
			final Integer key = entry.getKey();
			final List<AbstractDBService> abstractDBServices = entry.getValue();
			Thread dbScheduleThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(key * 60 * 1000);
						} catch (InterruptedException e) {
							log.error(e.getMessage(), e);
						}
						for (AbstractDBService abstractDBService : abstractDBServices) {
							abstractDBService.reload();
						}
					}
				}
			});
			dbScheduleThread.setName("DB-ReloadThread-Timer-" + entry.getKey());
			dbScheduleThread.setDaemon(true);
			dbScheduleThread.start();
		}

	}

	public synchronized void reloadCaches(Collection<AbstractDBService> commandList) {
		ExecutorService cacheExec = null;
		try {
			if (commandList != null) {
				cacheExec = Executors.newFixedThreadPool(commandList.size());
				execReload(cacheExec, commandList);
			}
		} finally {
			if (cacheExec != null) {
				cacheExec.shutdownNow();
			}
		}

	}

	private void execReload(ExecutorService cacheExec, Collection<AbstractDBService> commandList) {
		CompletionService<Boolean> completionService = new ExecutorCompletionService<>(cacheExec);
		Set<Future<Boolean>> futures = new HashSet<Future<Boolean>>();
		for (final AbstractDBService command : commandList) {
			Future<Boolean> future = completionService.submit(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					command.reload();
					return Boolean.TRUE;
				}
			});
			futures.add(future);
		}

		while (futures.size() > 0) {
			try {
				Future<Boolean> future = completionService.poll(5, TimeUnit.MINUTES);
				if (future != null) {
					futures.remove(future);
				} else {
					break;
				}
			} catch (Throwable e) {
				log.error(e.getMessage(), e);
				break;
			}

		}

	}

	@Override
	public synchronized void reloadAll() {
		reloadCaches(commandMap.values());
	}

	@Override
	public synchronized void reload(String name) {
		AbstractDBService dbService = commandMap.get(name);
		if (dbService != null) {
			dbService.reload();
		} else {
			throw new RuntimeException(name + " is incorrect");
		}
	}
}
