package com.odianyun.search.whale.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.data.dao.AttributeValueDao;
import com.odianyun.search.whale.data.model.AttributeValue;
import com.odianyun.search.whale.data.service.AbstractCompanyDBService;
import com.odianyun.search.whale.data.service.AttributeValueService;


public class AttributeValueServiceImpl extends AbstractCompanyDBService implements AttributeValueService{
	
	Map<Integer,AttributeValueCacheContext> attributeValueCacheContexts=new HashMap<Integer,AttributeValueCacheContext>();
	
	@Autowired
	AttributeValueDao attributeValueDao;

	@Override
	public AttributeValue getAttributeValue(Long valueId,int companyId) {
		AttributeValueCacheContext attributeValueCacheContext = attributeValueCacheContexts.get(companyId);
		if(attributeValueCacheContext != null){
			return attributeValueCacheContext.attributeValues.get(valueId);
		}
		return null;
	}

	@Override
	protected void tryReload(int companyId) throws Exception {
		AttributeValueCacheContext attributeValueCacheContext=new AttributeValueCacheContext();
		List<AttributeValue> valueList=attributeValueDao.queryAllAttributeValue(companyId);
		if(valueList!=null){
			Map<Long,AttributeValue> attributeValues_temp=new HashMap<Long,AttributeValue>();
			Map<Long,List<Long>> attributeNameId2ValueIdsMapTemp=new HashMap<Long,List<Long>>();

			Set<Long> parentValueIds_temp=new HashSet<Long>();
			for(AttributeValue av:valueList){
				attributeValues_temp.put(av.getAttrValueId(), av);
				Long parentId=av.getParentId();
				if(parentId!=null&&parentId!=0){
					parentValueIds_temp.add(parentId);				
				}
				Long attrNameId = av.getAttrNameId();
				List<Long> attributeValueIdList = attributeNameId2ValueIdsMapTemp.get(attrNameId);
				if(CollectionUtils.isEmpty(attributeValueIdList)){
					attributeValueIdList = new ArrayList<Long>();
					attributeNameId2ValueIdsMapTemp.put(attrNameId, attributeValueIdList);
				}
				attributeValueIdList.add(av.getAttrValueId());
			}
			attributeValueCacheContext.attributeValues=attributeValues_temp;
			attributeValueCacheContext.parentValueIds=parentValueIds_temp;
			attributeValueCacheContext.attributeNameId2ValueIdsMap = attributeNameId2ValueIdsMapTemp;
		}
		attributeValueCacheContexts.put(companyId, attributeValueCacheContext);
	}

	@Override
	public int getInterval() {
		return 60;
	}

	@Override
	public Boolean isAttributeValueGroup(Long valueId,int companyId) {
		AttributeValueCacheContext attributeValueCacheContext=attributeValueCacheContexts.get(companyId);
		return attributeValueCacheContext.parentValueIds.contains(valueId)
				&&attributeValueCacheContext.attributeValues.containsKey(valueId);
	}
	
	private static class AttributeValueCacheContext{
		
		Map<Long,AttributeValue> attributeValues=new HashMap<Long,AttributeValue>();
		Map<Long,List<Long>> attributeNameId2ValueIdsMap=new HashMap<Long,List<Long>>();

		Set<Long> parentValueIds=new HashSet<Long>();
		
	}

	@Override
	public List<Long> getAttributeValueIdsByAttrNameId(Long attrNameId, int companyId) {
		AttributeValueCacheContext attributeValueCacheContext = attributeValueCacheContexts.get(companyId);
		if(attributeValueCacheContext != null){
			return attributeValueCacheContext.attributeNameId2ValueIdsMap.get(attrNameId);
		}
		return new ArrayList<Long>();
	}

}
