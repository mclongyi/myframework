package com.odianyun.search.whale.analysis.ik;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.wltea.analyzer.lucene.IKAnalyzer;
import com.odianyun.search.whale.analysis.ChildFirstClassLoader;
import com.odianyun.search.whale.analysis.Constants;
import com.odianyun.search.whale.analysis.ISegment;
import com.odianyun.search.whale.analysis.ISegmentFactory;

/**
 * 支持热部署
 *
 * @author jing liu
 *
 */
public class ReloadableISegmentFactory implements ISegmentFactory {
	
	static Logger log = Logger.getLogger(ReloadableISegmentFactory.class);

	private static final String LOAD_CLASS = "org.wltea.analyzer.lucene.IKAnalyzer";
	private final URL url;

	private ChildFirstClassLoader loader;
	private Class<?> theClass;
	private Constructor<?> theCreator;

	private ChildFirstClassLoader preLoader;
	private Class<?> preTheClass;
	private Constructor<?> preTheCreator;

	public ReloadableISegmentFactory() throws Exception {
		this.url = IKAnalyzer.class.getProtectionDomain().getCodeSource().getLocation();
		this.reload();
	}

	/**
	 * 子类可以在这里创建不同的实例
	 *
	 * @param a
	 * @return
	 */
	protected ISegment createSegment(Analyzer a, boolean isSmart) {
		IKSegment seg = new IKSegment(a);
		return seg;
	}

	/**
	 * 创建分析器
	 *
	 * @param isSmart
	 * @return
	 */
	protected synchronized Analyzer createAnalyzer(boolean isSmart) {
    	String global_path=Constants.getGlobalPath();
		Settings settings=ImmutableSettings.builder().put("use_smart", isSmart)
				.put(Constants.IK_HOME, global_path + Constants.SEARCH_RESOURCE).build();
	    Environment environment = new Environment(settings);

	    Analyzer a = null;
		try {
			a = (Analyzer) this.theCreator.newInstance(null, settings, environment);
			//替换成功
			if (this.preLoader != null) {
				this.preLoader.close();
				this.preLoader = null;
				this.preTheClass = null;
				this.preTheCreator = null;
			}
		} catch (Exception e) {
			//替换失败
			if (this.preLoader != null) {
				try {
					this.loader.close();
				} catch (IOException ex) {
					log.error(e.getMessage(), e);
				}
				this.loader = this.preLoader;
				this.theClass = this.preTheClass;
				this.theCreator = this.preTheCreator;
			}

			throw new RuntimeException("create ik failed", e);
		}

		return a;
	}

	@Override
	public synchronized ISegment create(boolean isSmart) {
		Analyzer a = this.createAnalyzer(isSmart);
	    return this.createSegment(a, isSmart);
	}

	/**
	 * 调用此方法重新加载资源
	 *
	 * @throws Exception
	 */
	public synchronized void reload() throws Exception {
		this.preLoader = this.loader;
		this.preTheClass = this.theClass;
		this.preTheCreator = this.theCreator;

		//这个非常重要，在容器里面很有可能在定制的classloader里面加载
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		if (parent == null) {
			parent = ReloadableISegmentFactory.class.getClassLoader();
		}
		if (parent == null) {
			parent = ClassLoader.getSystemClassLoader();
		}

		this.loader = new ChildFirstClassLoader(new URL[] {this.url}, parent);
		this.theClass = this.loader.loadClass(LOAD_CLASS);
	    this.theCreator = this.theClass.getConstructor(new Class<?>[] {Settings.class, Settings.class, Environment.class} );
	}
}
