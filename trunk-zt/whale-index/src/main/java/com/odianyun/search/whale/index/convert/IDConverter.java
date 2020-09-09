package com.odianyun.search.whale.index.convert;

import java.util.List;

import com.odianyun.search.whale.index.api.common.UpdateType;

public interface IDConverter {
	
	List<Long> convert(List<Long> ids,int companyId) throws Exception;
}
