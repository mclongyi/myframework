package com.odianyun.search.whale.data.service;

import java.util.List;

import com.odianyun.search.whale.data.model.AttributeValue;


public interface AttributeValueService{
	
	public AttributeValue getAttributeValue(Long valueId,int companyId); 
	
	public Boolean isAttributeValueGroup(Long valueId,int companyId);

	public List<Long> getAttributeValueIdsByAttrNameId(Long attrNameId, int companyId);

}
