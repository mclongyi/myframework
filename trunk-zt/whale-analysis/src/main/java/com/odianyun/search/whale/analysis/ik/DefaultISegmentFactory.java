package com.odianyun.search.whale.analysis.ik;

import com.odianyun.search.whale.analysis.ISegment;
import com.odianyun.search.whale.analysis.ISegmentFactory;

/**
 * 默认实现，从当前类加载器加载，不支持热更新
 *
 * @author jing liu
 *
 */
public class DefaultISegmentFactory implements ISegmentFactory {

	@Override
	public ISegment create(boolean isSmart) {
		return new IKSegment(isSmart);
	}

}
