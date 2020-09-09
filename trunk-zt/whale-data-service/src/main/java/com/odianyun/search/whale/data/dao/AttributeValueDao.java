package com.odianyun.search.whale.data.dao;

import java.util.List;

import com.odianyun.search.whale.data.model.AttributeValue;


public interface AttributeValueDao {
	
	public List<AttributeValue> queryAllAttributeValue(int companyId) throws Exception;
	

}
