package com.odianyun.search.whale.index.business.process;

import com.odianyun.search.whale.data.model.BusinessProduct;
import com.odianyun.search.whale.data.model.MerchantProductSearch;
import com.odianyun.search.whale.index.business.process.base.BaseMerchantProductSearchProcessor;
import com.odianyun.search.whale.index.common.ProcessorConstants;

public class MerchantProductSearchProcessor extends BaseMerchantProductSearchProcessor{


	public MerchantProductSearchProcessor(){

	}

	@Override
	public MerchantProductSearch convert(BusinessProduct businessProduct,String updateTime) throws Exception {
		// TODO Auto-generated method stub
		if(businessProduct == null){
			return null;
		}
		MerchantProductSearch merchantProductSearch = doConvert(businessProduct,updateTime);
		return merchantProductSearch;
	}

	public static MerchantProductSearch doConvert(BusinessProduct businessProduct,String updateTime) {
		MerchantProductSearch merchantProductSearch = new MerchantProductSearch();
		merchantProductSearch.setId(businessProduct.getId());
		merchantProductSearch.setAttrValue_search(businessProduct.getAttrValue_search());
		StringBuffer valueIdStringBuffer=new StringBuffer();
		for(Long valueId:businessProduct.getAttrValueIds()){
			valueIdStringBuffer.append(valueId+ProcessorConstants.WORDCONNECT);
		}
		merchantProductSearch.setAttrValueId_search(valueIdStringBuffer.toString());
		merchantProductSearch.setBrandId_search(businessProduct.getBrandId_search());
		merchantProductSearch.setBrandName_search(businessProduct.getBrandName_search());
		merchantProductSearch.setCategoryId(businessProduct.getCategoryId());
		merchantProductSearch.setCategoryId_search(businessProduct.getCategoryId_search());
		merchantProductSearch.setCategoryName_search(businessProduct.getCategoryName_search());
		merchantProductSearch.setCoverProvinceId(businessProduct.getCoverProvinceId());
		merchantProductSearch.setCompanyId(businessProduct.getCompanyId());
		merchantProductSearch.setCreate_time(businessProduct.getCreate_time());
		merchantProductSearch.setEan_no(businessProduct.getEan_no());
		merchantProductSearch.setCode(businessProduct.getCode());
		merchantProductSearch.setIs_deleted(businessProduct.getIs_deleted());
		merchantProductSearch.setIsNew(businessProduct.getIsNew());
		merchantProductSearch.setMerchantCategoryId_search(businessProduct.getMerchantCategoryId_search());
		merchantProductSearch.setMerchantId(businessProduct.getMerchantId());
		merchantProductSearch.setMerchant_categoryId(businessProduct.getMerchant_categoryId());
		merchantProductSearch.setMerchantName_search(businessProduct.getMerchantName_search());
		merchantProductSearch.setNavCategoryId_search(businessProduct.getNavCategoryId_search());
		merchantProductSearch.setPicUrl(businessProduct.getPicUrl());
		merchantProductSearch.setProductName(businessProduct.getChinese_name());
		merchantProductSearch.setPrice(businessProduct.getPrice());
		merchantProductSearch.setOrgPrice(businessProduct.getOrgPrice());
		merchantProductSearch.setProductId(businessProduct.getProductId());
		merchantProductSearch.setCompositeSort(businessProduct.getHasPic()|businessProduct.getStock());
		merchantProductSearch.setTag_words(businessProduct.getTag_words());
		merchantProductSearch.setTax(businessProduct.getTax());
		merchantProductSearch.setMerchantSeriesId(businessProduct.getMerchantSeriesId());
		merchantProductSearch.setSeriesAttrValueIdSearch(businessProduct.getSeriesAttrValueIdSearch());
		merchantProductSearch.setType(businessProduct.getType());
		merchantProductSearch.setIsMainSeries(businessProduct.getIsMainSeries());
		merchantProductSearch.setTypeOfProduct(businessProduct.getTypeOfProduct());
		merchantProductSearch.setMerchantType(businessProduct.getMerchantType());
		merchantProductSearch.setVolume4sale(businessProduct.getVolume4sale());
		merchantProductSearch.setCalculation_unit(businessProduct.getCalculation_unit());
		merchantProductSearch.setAreaCode(businessProduct.getAreaCode());
		merchantProductSearch.setSale_type(businessProduct.getSale_type());
		merchantProductSearch.setStandard(businessProduct.getStandard());
		merchantProductSearch.setProductCode(businessProduct.getProductCode());
		merchantProductSearch.setUpdateTime(updateTime);
		merchantProductSearch.setRate(businessProduct.getRate());
		merchantProductSearch.setRatingCount(businessProduct.getRatingCount());
		merchantProductSearch.setPositiveRate(businessProduct.getPositiveRate());
		merchantProductSearch.setManagementState(businessProduct.getManagementState());
		merchantProductSearch.setRealVolume4sale(businessProduct.getRealVolume4sale());
		merchantProductSearch.setSeasonWeight(businessProduct.getSeasonWeight());
		merchantProductSearch.setStock(businessProduct.getStock());
		merchantProductSearch.setHasPic(businessProduct.getHasPic());
		merchantProductSearch.setPromotionId_search(businessProduct.getPromotionId_search());
		merchantProductSearch.setPromotionType_search(businessProduct.getPromotionType_search());
		merchantProductSearch.setScriptIds(businessProduct.getScriptIds());
		merchantProductSearch.setSaleAreaCodes(businessProduct.getSaleAreaCodes());
		merchantProductSearch.setSearchAreaCodes(businessProduct.getSearchAreaCodes());
		merchantProductSearch.setFirst_shelf_time(businessProduct.getFirst_shelf_time());
		merchantProductSearch.setThirdCode(businessProduct.getThirdCode());
		merchantProductSearch.setIsDistributionMp(businessProduct.getIsDistributionMp());
		merchantProductSearch.setCommodityCommission(businessProduct.getCommodityCommission());
		merchantProductSearch.setRefId_search(businessProduct.getRefId());
		merchantProductSearch.setParentMerchantId(businessProduct.getParentMerchantId());
		merchantProductSearch.setSubMerchantIds(businessProduct.getSubMerchantIds());

		//外卖新增查询项
		merchantProductSearch.setMinSize(businessProduct.getMinSize());
		merchantProductSearch.setMaxSize(businessProduct.getMaxSize());

		merchantProductSearch.setPlaceOfOrigin(businessProduct.getPlaceOfOrigin());
		merchantProductSearch.setPlaceOfOriginLogo(businessProduct.getPlaceOfOriginLogo());
		merchantProductSearch.setSubtitle(businessProduct.getSubtitle());
		merchantProductSearch.setCardType(businessProduct.getCardType());
		merchantProductSearch.setCardId(businessProduct.getCardId());
		return merchantProductSearch;
	}

}
