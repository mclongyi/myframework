package com.odianyun.search.whale.req.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.model.req.BaseSearchRequest;
import com.odianyun.search.whale.api.model.req.FilterType;
import com.odianyun.search.whale.data.service.ConfigService;
import com.odianyun.search.whale.es.request.ESSearchRequest;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class FilterBuilder implements RequestBuilder {

	public static String SELFMERCHANTTYPE = "10";
	
	public static String ROOT_MERCHANT_TYPE = "0";
	
	public static String  IS_JINGPIN= "3";
	
	public static String O2O_MERCHANT_TYPE_STORE = "22";//门店
	public static String O2O_MERCHANT_TYPE_COMP = "21";//子公司
	public static String O2O_MERCHANT_TYPE_PLATM = "20";//平台
	
	@Autowired
	ConfigService configService;
	@Override
	public void build(ESSearchRequest esSearchRequest,
			BaseSearchRequest searchRequest) {
		int companyId=searchRequest.getCompanyId();
		SELFMERCHANTTYPE = configService.get("selfMerchantType", "10",companyId);
		IS_JINGPIN = configService.get("is_jingpin", "3",companyId);

		List<FilterType> filterTypes = searchRequest.getFilterTypes();
		
		BoolFilterBuilder boolFilter = new BoolFilterBuilder();

		boolean isO2O = false;
		if (CollectionUtils.isNotEmpty(filterTypes)){
			org.elasticsearch.index.query.FilterBuilder[] filterBuilders = 
					new org.elasticsearch.index.query.FilterBuilder[filterTypes
					.size()];
			//
			for (int i = 0; i < filterTypes.size(); i++) {
				FilterType ft = filterTypes.get(i);

				String fieldName = null;
				String fieldValue = null;
				if(ft == FilterType.IS_NEW) {
					fieldName = IndexFieldConstants.ISNEW;
					fieldValue = "1";
				}else if (ft == FilterType.HAS_STOCK) {
					fieldName = IndexFieldConstants.STOCK;
					fieldValue = "1";
				}else if (ft == FilterType.SELF_SUPPORT) {
					fieldName = IndexFieldConstants.MERCHANTTYPE;
					BoolFilterBuilder boolFilterBuilder=new BoolFilterBuilder();
					boolFilterBuilder.should(new TermFilterBuilder(fieldName, SELFMERCHANTTYPE));
					boolFilterBuilder.should(new TermFilterBuilder(fieldName, ROOT_MERCHANT_TYPE));
					filterBuilders[i] = boolFilterBuilder;
					continue;
				}else if (ft==FilterType.IS_JINGPIN){
					fieldName = IndexFieldConstants.SALETYPE;
					fieldValue = IS_JINGPIN;
				}else if(ft == FilterType.IS_O2O) {
					BoolFilterBuilder boolFilterBuilder=new BoolFilterBuilder();
					boolFilterBuilder.should(new TermFilterBuilder(IndexFieldConstants.MERCHANTTYPE, O2O_MERCHANT_TYPE_STORE));
					filterBuilders[i] = boolFilterBuilder;
					isO2O = true;
					continue;
				}
				filterBuilders[i] = new TermFilterBuilder(fieldName, fieldValue);

			}
			boolFilter.must(filterBuilders);
		}
		
		if(!isO2O) {
			BoolFilterBuilder boolFilterBuilder=new BoolFilterBuilder();
			boolFilterBuilder.mustNot(new TermFilterBuilder(IndexFieldConstants.MERCHANTTYPE, O2O_MERCHANT_TYPE_STORE));
			boolFilterBuilder.mustNot(new TermFilterBuilder(IndexFieldConstants.MERCHANTTYPE, O2O_MERCHANT_TYPE_COMP));
			boolFilterBuilder.mustNot(new TermFilterBuilder(IndexFieldConstants.MERCHANTTYPE, O2O_MERCHANT_TYPE_PLATM));
			boolFilter.must(boolFilterBuilder);
		}
		
		List<Long> excluedMpids = searchRequest.getExcludeMpIds();
		if(CollectionUtils.isNotEmpty(excluedMpids)){
			BoolFilterBuilder boolFilterBuilder=new BoolFilterBuilder();
			for(Long mpId : excluedMpids){
				boolFilterBuilder.mustNot(new TermFilterBuilder(IndexFieldConstants.ID, mpId));
			}
			boolFilter.must(boolFilterBuilder);
		}
		
		/*MerchantProductType merchantProductType = searchRequest.getMerchantProductType();
		if(null != merchantProductType){
			boolFilter.must(new TermFilterBuilder(IndexFieldConstants.TYPE, merchantProductType.getCode()));
		}*/
		List<Integer> types = searchRequest.getTypes();
		if(CollectionUtils.isNotEmpty(types)){
			BoolFilterBuilder boolFilterBuilder=new BoolFilterBuilder();
			for(Integer type:types){
				boolFilterBuilder.should(new TermFilterBuilder(IndexFieldConstants.TYPE, type));
			}
			boolFilter.must(boolFilterBuilder);
		}

		List<Integer> excludeTypes = searchRequest.getExcludeTypes();
		boolean isHiddenServiceProduct=configService.getBool("is_hidden_service_product",false,companyId);
		if(isHiddenServiceProduct&&CollectionUtils.isEmpty(searchRequest.getTypes())){
			if(excludeTypes==null){
				excludeTypes=new ArrayList<>();
			}
			excludeTypes.add(6);
		}
		if(CollectionUtils.isNotEmpty(excludeTypes)){
			BoolFilterBuilder boolFilterBuilder=new BoolFilterBuilder();
			for(Integer type:excludeTypes){
				boolFilterBuilder.mustNot(new TermFilterBuilder(IndexFieldConstants.TYPE, type));
			}
			boolFilter.must(boolFilterBuilder);
		}
	
		if(boolFilter.hasClauses()){
			esSearchRequest.setFilterBuilder(boolFilter);
		}
	}

}
