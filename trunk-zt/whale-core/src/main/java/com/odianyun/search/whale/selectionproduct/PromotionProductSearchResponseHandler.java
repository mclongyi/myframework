package com.odianyun.search.whale.selectionproduct;

import java.util.*;

import com.odianyun.search.whale.data.model.ProductType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.filters.Filters.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;

import com.odianyun.search.whale.api.common.SearchException;
import com.odianyun.search.whale.api.model.MerchantProduct;
import com.odianyun.search.whale.api.model.selectionproduct.PromotionProduct;
import com.odianyun.search.whale.api.model.selectionproduct.PromotionProductSearchResponse;
import com.odianyun.search.whale.common.MerchantProductConvertor;
import com.odianyun.search.whale.index.api.common.IndexFieldConstants;

public class PromotionProductSearchResponseHandler {
	
	static Logger logger = Logger.getLogger(PromotionProductSearchResponseHandler.class);
	
	@Autowired
	MerchantProductConvertor merchantProductConvertor;
	
	public PromotionProductSearchResponse handle(SearchResponse searchResponse){
		PromotionProductSearchResponse promotionProductSearchResponse=new PromotionProductSearchResponse();
		try{
			SearchHits searchHits=searchResponse.getHits();
		    SearchHit[] searchHitArray=searchHits.getHits();
		    List<MerchantProduct> merchantProductList = new ArrayList<>();
//		    Set<PromotionProduct> productSet = new HashSet<>();
			Map<Long,Long> seriesMap = new HashMap<>();
			for(SearchHit hit:searchHitArray){
		    	Map<String, SearchHitField> data = hit.fields();
		    	MerchantProduct merchantProduct = merchantProductConvertor.convertFromSearchHitField(data);
				/*Object obj = data.get(IndexFieldConstants.IS_MAIN).getValues().get(0);
				merchantProduct.setTypeOfProduct((Integer)obj);*/
				Long productId = merchantProduct.getProductId();
		    	if(productId!=null&&productId!=0){
		    		merchantProductList.add(merchantProduct);
//		    		productSet.add(convertFromMerchantProduct(merchantProduct));
		    	}
				Integer typeOfProduct = merchantProduct.getTypeOfProduct();
				if(typeOfProduct != null &&  ProductType.VIRTUAL_CODE.getCode().equals(typeOfProduct)){
					Long merchantSeriesId = merchantProduct.getMerchantSeriesId();
					if(merchantSeriesId != null && merchantSeriesId != 0){
						seriesMap.put(merchantSeriesId,merchantProduct.getId());
					}
				}
		    }
			if(seriesMap.size() > 0){
				for(MerchantProduct mp : merchantProductList){
					Long merchantSeriesId = mp.getMerchantSeriesId();
					if(merchantSeriesId != null && merchantSeriesId != 0){
						Long seriesParentId = seriesMap.get(merchantSeriesId);
						if(seriesParentId != null && seriesParentId != 0){
							mp.setSeriesParentId(seriesParentId);
						}
					}
				}
			}
//		    promotionProductSearchResponse.setProducts(new ArrayList<PromotionProduct>(productSet));
		    promotionProductSearchResponse.setMerchantProducts(merchantProductList);
		    promotionProductSearchResponse.setTotalHit(searchHits.getTotalHits());
		    
		    /*InternalTerms internalTerms=(InternalTerms) searchResponse.getAggregations().asMap().get(IndexFieldConstants.PRODUCT_CODE);
			if (internalTerms != null) {
				List<Terms.Bucket> list = internalTerms.getBuckets();
				if(CollectionUtils.isNotEmpty(list)){
					promotionProductSearchResponse.setProductTotalHit(list.size());
				}
			}*/
		}catch(SearchException e){
			throw e;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new SearchException(e.getMessage(), e);
		}
	    return promotionProductSearchResponse;
	}

	private PromotionProduct convertFromMerchantProduct(MerchantProduct merchantProduct) {
		PromotionProduct product = new PromotionProduct();
		product.setEanNo(merchantProduct.getEan_no());
		product.setProductCode(merchantProduct.getProductCode());
		return product;
	}

}
