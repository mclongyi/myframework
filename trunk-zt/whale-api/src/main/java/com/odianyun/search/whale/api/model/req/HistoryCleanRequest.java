package com.odianyun.search.whale.api.model.req;

import java.io.Serializable;
import java.util.List;

import com.odianyun.search.whale.index.api.model.req.HistoryType;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class HistoryCleanRequest extends AbstractHistoryRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HistoryCleanRequest(Integer companyId, String userId, List<String> keywordList) {
		this(companyId,userId,keywordList,HistoryType.SEARCH);
	}
	
	public HistoryCleanRequest(Integer companyId, String userId, List<String> keywordList, HistoryType type) {
		super(companyId);
		this.keywordList = keywordList;
		this.setType(type);
		this.setUserId(userId);
	}
	//要被清除的历史记录list
	private List<String> keywordList;
}
