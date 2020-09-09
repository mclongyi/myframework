package com.odianyun.search.whale.api.model.resp;

import java.io.Serializable;
import java.util.List;

import com.odianyun.search.whale.index.api.model.req.HistoryResult;
import lombok.Data;

@Data
public class HistoryResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<HistoryResult> historyResult;

}
