package com.odianyun.search.whale.suggest.check;

import java.util.List;

import com.odianyun.search.whale.suggest.model.QueryContext;
import com.odianyun.search.whale.suggest.model.QueryResult;

/**
 * Query rewrite Strategy
 * @author yuqian
 *
 */
public interface CheckStrategy {

	
	public List<QueryResult> reWrite(QueryContext context) throws Exception;
}
