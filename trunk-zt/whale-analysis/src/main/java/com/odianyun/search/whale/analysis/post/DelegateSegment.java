package com.odianyun.search.whale.analysis.post;

import java.util.List;

import com.odianyun.search.whale.analysis.ISegment;

/**
 * 装饰模式
 *
 * @author jing liu
 *
 */
public class DelegateSegment implements ISegment {

	protected ISegment delegate;

	protected DelegateSegment(ISegment delegate) {
		this.delegate = delegate;
	}

	@Override
	public List<String> segment(String text) throws Exception {
		return this.delegate.segment(text);
	}

}
