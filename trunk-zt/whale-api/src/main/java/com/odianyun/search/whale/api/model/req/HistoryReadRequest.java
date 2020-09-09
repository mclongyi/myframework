package com.odianyun.search.whale.api.model.req;

import java.io.Serializable;

import com.odianyun.search.whale.index.api.model.req.HistoryType;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class HistoryReadRequest extends AbstractHistoryRequest implements Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HistoryReadRequest(Integer companyId, String userId, int count) {
		this(companyId,userId,HistoryType.SEARCH,count);
	}
	
	public HistoryReadRequest(Integer companyId, String userId, HistoryType type, int count) {
		super(companyId);
		this.setType(type);
		this.setCount(count);
		this.setUserId(userId);
	}

}
