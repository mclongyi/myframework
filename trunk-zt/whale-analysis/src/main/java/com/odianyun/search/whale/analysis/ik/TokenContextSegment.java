package com.odianyun.search.whale.analysis.ik;

import java.util.List;

import com.odianyun.search.whale.analysis.ISegment;
import com.odianyun.search.whale.analysis.TokenContextImpl;
import com.odianyun.search.whale.analysis.TokenContextUtils;
import com.odianyun.search.whale.analysis.post.DelegateSegment;

/**
 * 添加Token Context 机制
 *
 * @author jing liu
 *
 */
public class TokenContextSegment extends DelegateSegment {

	public TokenContextSegment(ISegment delegate) {
		super(delegate);
	}

	@Override
	public List<String> segment(String text) throws Exception {
		try {
			TokenContextImpl ctx = new TokenContextImpl();
			ctx.setRawInput(text);

			TokenContextUtils.set(ctx);

			return super.segment(text);
		} finally {
			TokenContextUtils.clear();
		}
	}
}
