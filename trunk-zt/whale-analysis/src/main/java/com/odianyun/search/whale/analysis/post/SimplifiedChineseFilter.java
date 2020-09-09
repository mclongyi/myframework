package com.odianyun.search.whale.analysis.post;

import java.util.LinkedList;
import java.util.List;

import com.odianyun.search.whale.analysis.PostTokenFilter;
import com.odianyun.search.whale.analysis.TextFilter;
import com.odianyun.search.whale.analysis.TokenContext;
import com.odianyun.search.whale.analysis.chinese.ZHConverter;

/**
 * 繁体转简体
 *
 * @author jing.liu
 *
 */
public class SimplifiedChineseFilter implements PostTokenFilter, TextFilter {

	private static final ZHConverter CONVERTER = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);

	@Override
	public List<String> action(TokenContext ctx, String token) {
		List<String> ret = new LinkedList<String>();
		ret.add(CONVERTER.convert(token));
		return ret;
	}

	@Override
	public String filter(TokenContext ctx, String text) {
		return CONVERTER.convert(text);
	}

}
