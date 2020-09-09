package com.odianyun.search.whale.analysis;

import java.util.List;

/**
 * 分词后处理器
 *
 * @author jing liu
 *
 */
public interface PostTokenFilter {
	List<String> action(TokenContext ctx, String token);
}
