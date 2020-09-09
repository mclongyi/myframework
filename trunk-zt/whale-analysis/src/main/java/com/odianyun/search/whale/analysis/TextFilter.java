package com.odianyun.search.whale.analysis;

/**
 * 前置处理
 *
 * @author jing liu
 *
 */
public interface TextFilter {
	String filter(TokenContext ctx, String text);
}
