package com.odianyun.search.whale.manager;

import java.util.List;

import com.odianyun.search.whale.analysis.ISegment;
import com.odianyun.search.whale.analysis.user.SearchPolicy;

/**
 * 对IKSegment的管理，一方面保持IKSegment的单例模式，防止随处创建IKSegment实例，另一方面
 * 屏蔽实现和使用细节
 *
 * @author zengfenghua
 *
 */
public class IKSegmentManager {

	private static SearchPolicy policy;
	private static volatile ISegment segment;
	private static volatile ISegment maxSegment;

	static {
		try {
			policy = new SearchPolicy();
			segment = policy.get();
			maxSegment = policy.get(false);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	/**
	 * 热加载新资源
	 *
	 * @throws Exception
	 */
	public static void reload() throws Exception {
		policy.reload();
		segment = policy.get();
		maxSegment = policy.get(false);
	}

	public static List<String> segment(String text) throws Exception{
		return segment.segment(text);
	}

	public static List<String> segment(String text,boolean useSmart) throws Exception{
		if (useSmart){
			return segment.segment(text);
		}else {
			return maxSegment.segment(text);
		}
	}

}
