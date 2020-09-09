package com.odianyun.search.whale.index.business.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import com.odianyun.search.whale.data.model.MerchantProductAttributeValue;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.odianyun.search.whale.data.model.AttributeValue;
import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.ProductAttributeValue;
import com.odianyun.search.whale.data.service.AttributeValueService;
import com.odianyun.search.whale.data.service.ProductAttributeService;
import com.odianyun.search.whale.index.business.process.base.BaseProductAttributeProcessor;
import com.odianyun.search.whale.index.common.ProcessorApplication;

public class ProductAttributeProcessor extends BaseProductAttributeProcessor{

	ProductAttributeService productAttributeService;

	AttributeValueService attributeValueService;

	private Set<Integer> guideAttrbuteTypes=new HashSet<Integer>();
	
	public ProductAttributeProcessor(){
		attributeValueService = (AttributeValueService) ProcessorApplication.getBean("attributeValueService");
		productAttributeService = (ProductAttributeService) ProcessorApplication.getBean("productAttributeService");
		//基本1系列2导购4
		guideAttrbuteTypes.add(4);
		guideAttrbuteTypes.add(5);
		guideAttrbuteTypes.add(6);
		guideAttrbuteTypes.add(7);
	}
	
	@Override
	public void calcProductAttribute(Map<Long, BusinessProduct> map,int companyId) throws Exception {
		/*List<Long> productIds = new ArrayList<Long>();
		for(Map.Entry<Long, BusinessProduct> entry : map.entrySet()){
			productIds.add(entry.getValue().getProductId());
		}

		Map<Long, List<ProductAttributeValue>> productAttributeMap = productAttributeService.
				 queryProductAttributeValuesTable(productIds,companyId);
		if(productAttributeMap == null || productAttributeMap.size() == 0){
			return;
		}*/

		List<Long> mpIds = new ArrayList<Long>(map.keySet());
		Map<Long,List<MerchantProductAttributeValue>> mpavMap=
				productAttributeService.queryMerchantProductAttributeValuesByTable(mpIds,companyId);


		for(Map.Entry<Long, BusinessProduct> entry : map.entrySet()){
			BusinessProduct businessProduct = entry.getValue();
			/*Long productId = businessProduct.getProductId();
			if(productId == null || productId == 0){
				continue;
			}
			List<ProductAttributeValue> pavList = productAttributeMap.get(productId);*/
			Long mpId = businessProduct.getId();
			if(mpId == null || mpId == 0){
				continue;
			}
			List<MerchantProductAttributeValue> mpAttributeValues = mpavMap.get(mpId);
			if(CollectionUtils.isNotEmpty(mpAttributeValues)){
				Set<Long> attrValueIdSet=businessProduct.getAttrValueIds();
				Set<String> attrValueSet =businessProduct.getAttrValueSet();
				for(MerchantProductAttributeValue mpav:mpAttributeValues){
					if(mpav.getAttrType()!=null && guideAttrbuteTypes.contains(mpav.getAttrType())){
						AttributeValue attrValue = attributeValueService.getAttributeValue(mpav.getAttrValueId(),companyId);
						if(attrValue==null || StringUtils.isBlank(attrValue.getAttrValue())) {
							continue;
						}
						attrValueIdSet.add(mpav.getAttrValueId());
						attrValueSet.add(attrValue.getAttrValue().trim());
					}
				}

			}
		}
	}

}
