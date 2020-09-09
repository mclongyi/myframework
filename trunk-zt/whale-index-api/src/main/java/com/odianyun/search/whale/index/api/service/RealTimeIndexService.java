package com.odianyun.search.whale.index.api.service;

import java.util.List;

import com.odianyun.search.whale.index.api.common.IndexException;
import com.odianyun.search.whale.index.api.common.UpdateType;
import com.odianyun.search.whale.index.api.model.req.IndexApplyDTO;
import com.odianyun.soa.InputDTO;

public interface RealTimeIndexService {

	@Deprecated
	public void updateIndex(List<Long> ids, UpdateType updateType,int companyId) throws Exception;

	@Deprecated
	public void updateIndex(InputDTO<IndexApplyDTO> inputDto) throws Exception;

	void updateIndexStandard(InputDTO<IndexApplyDTO> inputDto) throws Exception;
}
