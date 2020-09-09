package com.odianyun.search.whale.analysis.ik;

import java.util.LinkedList;
import java.util.List;

import com.odianyun.search.whale.analysis.ISegment;
import com.odianyun.search.whale.analysis.TextFilter;
import com.odianyun.search.whale.analysis.TokenContext;
import com.odianyun.search.whale.analysis.TokenContextImpl;
import com.odianyun.search.whale.analysis.TokenContextUtils;
import com.odianyun.search.whale.analysis.post.DelegateSegment;

/**
 * 前置处理功能的分词
 *
 * @author jing liu
 *
 */
public class TextFilterSegment extends DelegateSegment {

	private List<TextFilter> filters = new LinkedList<TextFilter>();

	public TextFilterSegment(ISegment delegate, List<TextFilter> filters) {
		super(delegate);
		this.filters.addAll(filters);
	}

	public void add(TextFilter filter) {
		this.filters.add(filter);
	}

	@Override
	public List<String> segment(String text) throws Exception {
		TokenContext ctx = TokenContextUtils.get();

		for (TextFilter f : this.filters) {
			text = f.filter(ctx, text);
		}

		if (ctx != null) {
			((TokenContextImpl) ctx).setInput(text);
		}

		return super.segment(text);
	}
}
