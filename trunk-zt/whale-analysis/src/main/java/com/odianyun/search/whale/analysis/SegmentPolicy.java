package com.odianyun.search.whale.analysis;

/**
 * 分词策略
 *
 * @author jing.liu
 *
 */
public interface SegmentPolicy {
	/**
	 * 获取分词实例，线程安全，在下次reload前可以一直使用
	 *
	 * @return
	 * @throws Exception
	 */
	ISegment get() throws Exception;

	/**
	 *  获取分词实例
	 * @param isSmart true ik_smart ，false ik_max_word
	 * @return
	 * @throws Exception
	 */
	ISegment get(boolean isSmart) throws Exception;

	/**
	 * 重新加载资源，调用后必须用get获取新资源
	 *
	 * @throws Exception
	 */
	void reload() throws Exception;
}
