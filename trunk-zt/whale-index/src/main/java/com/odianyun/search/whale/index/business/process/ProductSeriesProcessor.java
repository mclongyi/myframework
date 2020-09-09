package com.odianyun.search.whale.index.business.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.odianyun.search.whale.data.model.*;
import com.odianyun.search.whale.data.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.index.business.process.base.BaseProductSeriesProcessor;
import com.odianyun.search.whale.index.common.ProcessorApplication;
import com.odianyun.search.whale.index.common.ProcessorConstants;

public class ProductSeriesProcessor extends BaseProductSeriesProcessor {

	static Logger logger = Logger.getLogger(ProductSeriesProcessor.class);

	MerchantSeriesService seriesService;

	ProductSeriesService productSeriesService;
	
	ProductAttributeService productAttributeService;

	AttributeValueService attributeValueService;
		
	ConfigService configService;
	
	
	public static boolean  IS_COMBINE_DISPLAY = true;

	public ProductSeriesProcessor() {
		configService = (ConfigService) ProcessorApplication.getBean("configService");
		seriesService = (MerchantSeriesService) ProcessorApplication.getBean("seriesService");
		productSeriesService = (ProductSeriesService) ProcessorApplication.getBean("productSeriesService");
		productAttributeService=(ProductAttributeService) ProcessorApplication.getBean("productAttributeService");
		attributeValueService = (AttributeValueService) ProcessorApplication.getBean("attributeValueService");

	}
	@Override
	public void calcSeriesType(Map<Long, BusinessProduct> businessMap, Map<Long, List<Long>> seriesMpMap,
			int companyId) throws Exception {
		IS_COMBINE_DISPLAY = configService.getBool("is_combine_display",true,companyId);
		if(seriesMpMap == null || seriesMpMap.size() == 0)
			return;
		List<Long> merchantSeriesIds = new ArrayList<Long>(seriesMpMap.keySet());
        //main_merchant_product_Id--->seriesId
//		Map<Long,Long> seriesMap = seriesService.getMerchantSeriesById(merchantSeriesIds,companyId);
        
		//seriesId--->main_merchant_product_Id
//		Map<Long,Long> seriesRevertMap = seriesService.getMerchantSeriesByIdRevert(merchantSeriesIds,companyId);
//		Map<Long,Long> seriesRevertMap = genSeriesRevertRelation(businessMap);

		
		Map<Long,List<MerchantSeriesAttribute>> seriesAttrbuteValues=seriesService.getMerchantProductAttrValues(merchantSeriesIds,companyId);
		if(seriesAttrbuteValues==null){
			seriesAttrbuteValues=new HashMap<Long,List<MerchantSeriesAttribute>>();
		}
		
		//查询所有的商品(包括主品对应的子品id)的属性
		List<Long> mpIds = new ArrayList<Long>(businessMap.keySet());
		/*for (Entry<Long, List<Long>> entry : seriesMpMap.entrySet()) {
			if (entry.getValue() != null) {
				mpIds.addAll(entry.getValue());
			}
		}*/

		Map<Long,List<MerchantProductAttributeValue>> mpavMap=
				productAttributeService.queryMerchantProductAttributeValuesByTable(mpIds,companyId);
		
//		calcDefaultSeriesMerchantProduct(businessMap,seriesMpMap,seriesRevertMap);
		
		for(Map.Entry<Long, BusinessProduct> entry : businessMap.entrySet()){
			Long merchantProductId = entry.getKey();
			BusinessProduct businessProduct = entry.getValue();
			Long seriesId=businessProduct.getMerchantSeriesId();
			Set<Long> attrValueIdSet =new HashSet<Long>();
			List<MerchantProductAttributeValue> mpavList=mpavMap.get(merchantProductId);
			if(CollectionUtils.isNotEmpty(mpavList)){
				for(MerchantProductAttributeValue mpav:mpavList){
					attrValueIdSet.add(mpav.getAttrValueId());
				}
			}
			List<MerchantSeriesAttribute> merchantSeriesAttributes=seriesAttrbuteValues.get(seriesId);
			if(CollectionUtils.isNotEmpty(merchantSeriesAttributes)){
				StringBuffer sb=new StringBuffer();
				for(MerchantSeriesAttribute merchantSeriesAttribute:merchantSeriesAttributes){
					Long attrNameId = merchantSeriesAttribute.getAttrNameId();
					List<Long> attValueIdList = attributeValueService.getAttributeValueIdsByAttrNameId(attrNameId,companyId);
					if(CollectionUtils.isNotEmpty(attValueIdList)){
						for(Long attValueId : attValueIdList){
							if(attrValueIdSet.contains(attValueId)){
								sb.append(attValueId+ ProcessorConstants.WORDCONNECT);
								AttributeValue attrValue = attributeValueService.getAttributeValue(attValueId,companyId);
								if(attrValue!=null && StringUtils.isNotBlank(attrValue.getAttrValue())) {
									businessProduct.getAttrValueSet().add(attrValue.getAttrValue());
								}
							}
						}
					}
				}
				businessProduct.setSeriesAttrValueIdSearch(sb.toString());
			}

			if(ProductType.VIRTUAL_CODE.getCode().equals(businessProduct.getTypeOfProduct())){
				calcVirtualMPPrice(seriesMpMap.get(seriesId),businessMap,merchantProductId);
			}

			if(IS_COMBINE_DISPLAY){
				calcOtherSeriesMerchantProduct(businessProduct,seriesMpMap,seriesAttrbuteValues,mpavMap,businessMap);
			}
		}
		
	}

	private Map<Long,Long> genSeriesRevertRelation(Map<Long, BusinessProduct> businessMap) {
		Map<Long,Long> seriesRevertMap = new HashMap<>();
		for(Map.Entry<Long, BusinessProduct> entry : businessMap.entrySet()){
			BusinessProduct businessProduct = entry.getValue();
			if(null != businessProduct){
				Integer typeOfProduct = businessProduct.getTypeOfProduct();
				if(typeOfProduct == null
						|| ProductType.NORMAL.getCode().equals(typeOfProduct)
						|| ProductType.SUB_CODE.getCode().equals(typeOfProduct)){
					continue;
				}
				Long merchantSeriesId = businessProduct.getMerchantSeriesId();
				Long mpId = seriesRevertMap.get(merchantSeriesId);
				if(mpId == null){
					seriesRevertMap.put(merchantSeriesId,businessProduct.getId());
				}else{
					BusinessProduct bp = businessMap.get(mpId);
					Integer top = bp.getTypeOfProduct();
					if(typeOfProduct > top){
						seriesRevertMap.put(merchantSeriesId,businessProduct.getId());
					}
				}
			}
		}
		return seriesRevertMap;
	}

	private void calcDefaultSeriesMerchantProduct(Map<Long, BusinessProduct> businessMap,
			Map<Long, List<Long>> seriesMpMap, Map<Long, Long> seriesMap) {
		Set<Long> defaultMpIdSet = new HashSet<Long>();
//		Set<Long> mainMpIdSet = new HashSet<Long>();

		for(Map.Entry<Long, List<Long>> entry : seriesMpMap.entrySet()){
			Long merchantSeriesId = entry.getKey();
			List<Long> mpIdList = entry.getValue();
			Long mainMpId = seriesMap.get(merchantSeriesId);
//			if(null != mainMpId && mainMpId != 0){
//				mainMpIdSet.add(mainMpId);
//			}
			Long defaultMpId = calcDefaultSeriesMpId(merchantSeriesId,mainMpId,mpIdList,businessMap);
			defaultMpIdSet.add(defaultMpId);
		}
		
		for(Map.Entry<Long, BusinessProduct> entry : businessMap.entrySet()){
			Long mpId = entry.getKey();
			BusinessProduct businessProduct = entry.getValue();
			if(defaultMpIdSet.contains(mpId)){
				businessProduct.setIsMainSeries(1);
			}else{
				businessProduct.setIsMainSeries(0);
			}
			
//			if(mainMpIdSet.contains(mpId)){
//				businessProduct.setIsMain(1);
//			}else{
//				businessProduct.setIsMain(0);
//			}
		}
		
		
	}
	private Long calcDefaultSeriesMpId(Long merchantSeriesId ,Long mainMpId,List<Long> mpIdList,
			Map<Long, BusinessProduct> businessMap) {
		List<Long> sortedMpIdList = new ArrayList<Long>();
		Long defaultMpId = 0l;
		if(null != mainMpId && mainMpId != 0){
			BusinessProduct businessProduct = businessMap.get(mainMpId);
			if(ProductType.VIRTUAL_CODE.getCode().equals(businessProduct.getTypeOfProduct())){
				calcVirtualMPPrice(mpIdList,businessMap,mainMpId);
				return mainMpId;
			}

			// 如果 mpIdList 不包含mainMpId 则 表示 主品状态status不是通过审核
			if(mpIdList.contains(mainMpId)){
				sortedMpIdList.add(mainMpId);
				mpIdList.remove(mainMpId);
				defaultMpId = mainMpId;
			}
		}
		
		Collections.sort(mpIdList, new Comparator<Long>() {
			@Override
			public int compare(Long o1, Long o2) {
				return o1.compareTo(o2);
			}
		});
		
		sortedMpIdList.addAll(mpIdList);
		
		if(CollectionUtils.isEmpty(sortedMpIdList)){
			return 0l;
		}
		if(defaultMpId == 0){
			defaultMpId = sortedMpIdList.get(0);
		}
		// 第一轮  第一个满足上架切有库存 为主品
		for(Long mpid : sortedMpIdList){
			BusinessProduct businessProduct = businessMap.get(mpid);
			Integer managementState = businessProduct.getManagementState();
			Integer stock = businessProduct.getStock();
			if(ManagementType.ON_SHELF.getCode().equals(managementState) 
					&& stock > 0 && hasPic(businessProduct)){
				return mpid;
			}
		}
		// 第二轮  第一个满足上架 为主品
		for(Long mpid : sortedMpIdList){
			BusinessProduct businessProduct = businessMap.get(mpid);
			Integer managementState = businessProduct.getManagementState();
			if(ManagementType.ON_SHELF.getCode().equals(managementState)
					&& hasPic(businessProduct)){
				return mpid;
			}
		}
		// 第三轮  第一个满足有库存 为主品
		for(Long mpid : sortedMpIdList){
			BusinessProduct businessProduct = businessMap.get(mpid);
			Integer stock = businessProduct.getStock();
			if(stock > 0 && hasPic(businessProduct)){
				return mpid;
			}
		}
		// 否则  默认主品为 原主品 或者mpid最小的
		return defaultMpId;
	}

	private void calcVirtualMPPrice(List<Long> mpIdList, Map<Long, BusinessProduct> businessMap, Long virtualMpId) {

		if(CollectionUtils.isNotEmpty(mpIdList)){
			double minPrice = Double.MAX_VALUE;
			double minOrgPrice = Double.MAX_VALUE;
			for(Long mpid : mpIdList){
				if(mpid.equals(virtualMpId)){
					continue;
				}
				BusinessProduct businessProduct = businessMap.get(mpid);
				Double price = businessProduct.getPrice();
				if(price != null && price != 0 && price < minPrice){
					minPrice = price;
				}
				Double orgPrice=businessProduct.getOrgPrice();
				if(orgPrice!=null&&orgPrice!=0&&orgPrice<minOrgPrice){
					minOrgPrice=orgPrice;
				}
			}
			if(minPrice < Double.MAX_VALUE){
				businessMap.get(virtualMpId).setPrice(minPrice);
			}
			if(minOrgPrice<Double.MAX_VALUE){
				businessMap.get(virtualMpId).setOrgPrice(minOrgPrice);
			}

		}
	}

	private boolean hasPic(BusinessProduct businessProduct) {
		boolean hasPic = true;
		if(StringUtils.isBlank(businessProduct.getPicUrl())){
			hasPic = false;
		}
		return hasPic;
	}

	/**
	 * 如果 主品未被索引或已下架 从子品中取一个设置为默认主品
	 * 设置条件： 1.已上架 2.mpid最小
	 * @param businessProduct
	 * @param seriesId2MerchantProductIds
	 * @param mpMap
	 * @param seriesMap
	 */
	private void calcDefaultSeriesMerchantProduct(BusinessProduct businessProduct, Map<Long, List<Long>> seriesId2MerchantProductIds,
			Map<Long, MerchantProduct> mpMap, Map<Long, Long> seriesMap,Set<Long> stockSet) {
		Long seriesId = businessProduct.getMerchantSeriesId();
		// 该系列所有商品mpid
		List<Long> mpIdList = new ArrayList<Long>(seriesId2MerchantProductIds.get(seriesId));
		// 该批次所有主品mpid
		List<Long> mainMpidList = new ArrayList<Long>(seriesMap.keySet());
		mpIdList.retainAll(mainMpidList);
		
		Long mainMpId = 0l;
		if(CollectionUtils.isNotEmpty(mpIdList)){
			mainMpId = mpIdList.get(0);
			MerchantProduct mp = mpMap.get(mainMpId);
			if(null != mp){
				Integer managementState = mp.getManagementState();
				// 索引商品中存在主品  且 主品上架 且  有库存 不需要设置默认主品
				if(ManagementType.ON_SHELF.getCode().equals(managementState) 
						&& stockSet.contains(mainMpId)){
					return;
				}
			}
		}
		
		mpIdList = new ArrayList<Long>(seriesId2MerchantProductIds.get(seriesId));
		// 移除主品mpid
		if(!mainMpId.equals(0)){
			mpIdList.remove(mainMpId);
		}
		
		// 移除下架或者没有库存商品mpid
		Iterator<Long> iterator = mpIdList.iterator();
		while(iterator.hasNext()){
			Long mpId = iterator.next();
			MerchantProduct mp = mpMap.get(mpId);
			if(null != mp){
				Integer managementState = mp.getManagementState();
				if(!ManagementType.ON_SHELF.getCode().equals(managementState) 
						|| !stockSet.contains(mpId)){
					iterator.remove();
				}
			}else{
				iterator.remove();
			}
		}
		if(CollectionUtils.isEmpty(mpIdList)){
			return;
		}		
		
		//按mpid大小升序排列 获取最小的mpid
		Collections.sort(mpIdList, new Comparator<Long>() {
			@Override
			public int compare(Long o1, Long o2) {
				return o1.compareTo(o2);
			}
		});
		
		Long minMpId = mpIdList.get(0);
		// 设置为默认主品
		if(businessProduct.getId().equals(minMpId)){
			businessProduct.setIsMainSeries(1);
		}
		
	}
	/**
	 * 把一个系列下的其他子码的系列属性值加到主码上,搜索子码的颜色也能搜索到
	 * @param businessProduct
	 * @param seriesId2MerchantProductIds
	 * @param seriesAttrbuteValues
     * @param mpavMap
     */
	private void calcOtherSeriesMerchantProduct(BusinessProduct businessProduct,Map<Long,List<Long>> seriesId2MerchantProductIds,
												Map<Long,List<MerchantSeriesAttribute>> seriesAttrbuteValues,Map<Long,List<MerchantProductAttributeValue>> mpavMap,
												Map<Long,BusinessProduct> mpMap){
		/*if(businessProduct.getIsMainSeries()!=1){
			return;
		}*/
		if(!ProductType.VIRTUAL_CODE.getCode().equals(businessProduct.getTypeOfProduct())){
			return;
		}
		List<Long> merchantProductIds= seriesId2MerchantProductIds.get(businessProduct.getMerchantSeriesId());
		if(CollectionUtils.isEmpty(merchantProductIds)){
             return;
		}
		// 只要系列品中任意一个有库存 ，主品库存不为0
//		calcMainSeriesStock(businessProduct,merchantProductIds,stockSet);
		
		int companyId = businessProduct.getCompanyId().intValue();
		Integer stock = businessProduct.getStock();
		Integer ratingCount = 0;
		Double highestRate = 0D;
		Integer totalPositiveCount = 0;
        for(Long merchantProductId:merchantProductIds){
			if(businessProduct.getId().equals(merchantProductId)){
				continue;
			}

			//计算评论相关数字
			Integer currentRatingCount = mpMap.get(merchantProductId).getRatingCount();
			Double currentRate = mpMap.get(merchantProductId).getRate();
			Integer currentPositiveCount = mpMap.get(merchantProductId).getPositiveCount();
			if(currentRatingCount != null){
				ratingCount += currentRatingCount;
			}
			if(currentRate != null && highestRate < currentRate){
				highestRate = currentRate;
			}
			if(currentPositiveCount != null){
				totalPositiveCount += currentPositiveCount;
			}

			Set<Long> attrValueIdSet =new HashSet<Long>();
			List<MerchantProductAttributeValue> mpavList=mpavMap.get(merchantProductId);
			if(CollectionUtils.isNotEmpty(mpavList)){
				for(MerchantProductAttributeValue mpav:mpavList){
					attrValueIdSet.add(mpav.getAttrValueId());
				}
			}
			BusinessProduct merchantProduct = mpMap.get(merchantProductId);
			if(null != merchantProduct){
				String code = merchantProduct.getCode();
				if(StringUtils.isNotBlank(code)){
					businessProduct.getSubCodeSet().add(code);
				}
				stock = stock | merchantProduct.getStock();
				businessProduct.setVolume4sale(businessProduct.getVolume4sale()+merchantProduct.getVolume4sale());
				businessProduct.setRealVolume4sale(businessProduct.getRealVolume4sale()+merchantProduct.getRealVolume4sale());
			}
			List<MerchantSeriesAttribute> attrValues=seriesAttrbuteValues.get(businessProduct.getMerchantSeriesId());
			if(CollectionUtils.isNotEmpty(attrValues)){
				for(MerchantSeriesAttribute merchantSeriesAttribute :attrValues){
					Long attrNameId = merchantSeriesAttribute.getAttrNameId();
					List<Long> attrValueIdList = attributeValueService.getAttributeValueIdsByAttrNameId(attrNameId, companyId);
					if(CollectionUtils.isNotEmpty(attrValueIdList)){
						for(Long attrValueId : attrValueIdList){
							if(attrValueIdSet.contains(attrValueId)){
								AttributeValue attrValue = attributeValueService.getAttributeValue(attrValueId,companyId);
								if(attrValue!=null && StringUtils.isNotBlank(attrValue.getAttrValue())) {
									businessProduct.getAttrValueSet().add(attrValue.getAttrValue());
									//2017.09.17  AttrValueIds应该删除系列属性id
									//businessProduct.getAttrValueIds().add(attrValue.getAttrValueId());
								}
							}
						}
						
					}
					
				}
			}
		}
		businessProduct.setStock(stock);
		businessProduct.setRatingCount(ratingCount);
		businessProduct.setRate(highestRate);
		businessProduct.setPositiveRate(ratingCount == 0 ? 0 : totalPositiveCount*100/ratingCount);
	}
	
	private void calcMainSeriesStock(BusinessProduct businessProduct, List<Long> merchantProductIds,
			Set<Long> stockSet) {
		Integer stock = businessProduct.getStock();
		if(stock == null || stock == 0){
	        for(Long merchantProductId : merchantProductIds){
	        	if(stockSet.contains(merchantProductId)){
	        		businessProduct.setStock(1);
	        		break;
	        	}
	        }
		}
		
	}

}
