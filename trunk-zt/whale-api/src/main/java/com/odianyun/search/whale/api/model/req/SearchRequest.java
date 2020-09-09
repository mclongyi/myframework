package com.odianyun.search.whale.api.model.req;

import lombok.Data;

import java.util.List;

/**
 * 前台搜索请求
 * 
 * @author zengfenghua
 *
 */
@Data
public class SearchRequest extends BaseSearchRequest{

	public SearchRequest(Integer companyId, List<Long> navCategoryIds) {
		super(companyId);
		this.navCategoryIds = navCategoryIds;
	}

	public SearchRequest() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//导航类目id
	private List<Long> navCategoryIds;

	public SearchRequest(Integer companyId) {
		super(companyId);
	}

}
