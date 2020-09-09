package com.odianyun.search.whale.analysis.post;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.odianyun.search.whale.analysis.ISegment;
import com.odianyun.search.whale.analysis.PostTokenFilter;
import com.odianyun.search.whale.analysis.TokenContext;
import com.odianyun.search.whale.analysis.TokenContextUtils;

/**
 * 带有后处理的分词器
 *
 * @author jing liu
 *
 */
public class PostTokenFilterSegment extends DelegateSegment {

	private List<PostTokenFilter> filters = new LinkedList<PostTokenFilter>();

	public PostTokenFilterSegment(ISegment delegate, List<PostTokenFilter> filters) {
		super(delegate);
		this.filters = filters;
	}

	public void add(PostTokenFilter filter) {
		this.filters.add(filter);
	}

	@Override
	public List<String> segment(String text) throws Exception {
		List<String> tokens = this.delegate.segment(text);

		TokenContext ctx = TokenContextUtils.get();

		for (PostTokenFilter f : this.filters) {
			Set<String> result = new HashSet<String>();
			for (String t : tokens) {
				result.addAll(f.action(ctx, t));
			}
			tokens = new LinkedList<String>(result);
			result.clear();
		}

		return tokens;
	}

}
