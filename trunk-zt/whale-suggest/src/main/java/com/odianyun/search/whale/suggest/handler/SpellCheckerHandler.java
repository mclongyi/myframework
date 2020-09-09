package com.odianyun.search.whale.suggest.handler;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.req.SpellCheckerRequest;
import com.odianyun.search.whale.api.model.resp.SpellCheckerResponse;

/**
 * 处理拼写检查逻辑
 *
 * @author jing liu
 *
 */
public interface SpellCheckerHandler {
	/**
	 * 入口
	 *
	 * @param request 请求
	 * @return 结果
	 * @throws SearchException 出现异常抛出
	 */
	SpellCheckerResponse handle(SpellCheckerRequest request) throws SearchException;
}
