package com.odianyun.search.whale.analysis.ik;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import com.odianyun.search.backend.api.service.DictFromDbToDiskServiceClient;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;

import com.odianyun.search.whale.analysis.ISegment;

/**
 * 支持自动加载刷新  默认1小时一次
 *
 * 此功能应该极少使用。
 * 原因有二：如果是由于分词导致的，那么必然有次全量。数据量大量后全量成本非常高。
 * 如果真有人工参与进来，那在此流程中触发一下重新加载是更适合的行为。
 *
 * 坏处：增加不必要的负荷，没有从根本上解决问题。
 * 好处：有利于实时索引更新修正。
 *
 * @author jing liu
 *
 */
public class AutoReloadableISegmentFactory extends ReloadableISegmentFactory {
	
	static Logger log = Logger.getLogger(AutoReloadableISegmentFactory.class);

	private static final int CORE = 1;
	private static final String NAME = "auto reloader thread";

	private final List<WeakReference<SegmentHolder>> history = new LinkedList<WeakReference<SegmentHolder>>();
	private final AutoReloader reloader = new AutoReloader();
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(CORE, new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, NAME);
			t.setDaemon(true);
			return t;
		}
	});

	public AutoReloadableISegmentFactory() throws Exception {
		super();
		this.scheduler.scheduleAtFixedRate(this.reloader, 1L, 1L, TimeUnit.HOURS);
	}

	private class AutoReloader implements Runnable {

		@Override
		public void run() {
			try {
				DictFromDbToDiskServiceClient.getInstance().exportDictFromDbToDisk();
				AutoReloadableISegmentFactory.this.reload();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			synchronized (AutoReloadableISegmentFactory.this.history) {
				Iterator<WeakReference<SegmentHolder>> iter = AutoReloadableISegmentFactory.this.history.iterator();
				while (iter.hasNext()) {
					WeakReference<SegmentHolder> holder = iter.next();
					SegmentHolder strong = holder.get();
					//reload by user
					if (strong == null) {
						iter.remove();
					//update inplace
					} else {
						Analyzer a = AutoReloadableISegmentFactory.super.createAnalyzer(strong.isSmart());
						ISegment delgate = AutoReloadableISegmentFactory.super.createSegment(a, strong.isSmart());
						strong.update(delgate);
					}
				}
			}
		}
	}

	private static class SegmentHolder implements ISegment {

		private volatile ISegment delegate;
		private boolean smart;

		public SegmentHolder(ISegment delegate, boolean isSmart) {
			this.delegate = delegate;
			this.smart = isSmart;
		}

		@Override
		public List<String> segment(String text) throws Exception {
			return this.delegate.segment(text);
		}

		public void update(ISegment newDelegate) {
			this.delegate = newDelegate;
		}

		public boolean isSmart() {
			return this.smart;
		}
	}

	@Override
	protected ISegment createSegment(Analyzer a, boolean isSmart) {
		ISegment delgate = super.createSegment(a, isSmart);
		SegmentHolder holder = new SegmentHolder(delgate, isSmart);

		synchronized (this.history) {
			this.history.add(new WeakReference<SegmentHolder>(holder));
		}

		return holder;
	}
}
