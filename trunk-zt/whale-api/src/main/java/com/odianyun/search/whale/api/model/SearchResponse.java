package com.odianyun.search.whale.api.model;

import com.odianyun.search.whale.common.cache.CacheInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class SearchResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//商品id的结果集
	public List<Long> merchantProductIds;

	//聚类计算的结果
	public Map<String,Map<String,Long>> facetResults;

	//导购属性结果
	public List<AttributeResult> attributeResult=new ArrayList<AttributeResult>();

	//类目树结果
	public List<CategoryTreeResult> categoryTreeResult=new ArrayList<CategoryTreeResult>();

	//前台类目树结果
	public List<CategoryTreeResult> navCategoryTreeResult=new ArrayList<CategoryTreeResult>();

	//品牌聚类结果
	public List<BrandResult> brandResult = new ArrayList<BrandResult>();

	//merchantProduct结果集
	public List<MerchantProduct> merchantProductResult = new ArrayList<MerchantProduct>();

	//zeroResultRecommendResult 纠错后的词语存储字段,纠错后的词语查询结果
	public ZeroResultRecommendResult zeroResultRecommendResult=new ZeroResultRecommendResult();

	//热词推荐列表
	public List<String> hotwordsRecommended = new ArrayList<String>();

	private KeywordResult keywordResult=new KeywordResult();

	public int companyId;
	
	//搜索到的总商品数，非返回数
	public long totalHit;
	
	//处理花费时间，毫秒
	public long costTime;

	//缓存的key值
	private CacheInfo cacheInfo;

	//商品角标map
	//private Map<Long,List<MerProScript>> superScriptMap;

	// 店铺信息结果
	public ShopResult shopResult;

	public long getTotalHit() {
		return totalHit;
	}

	public void setTotalHit(long totalHit) {
		this.totalHit = totalHit;
	}

	public List<Long> getMerchantProductIds() {
		return merchantProductIds;
	}

	public void setMerchantProductIds(List<Long> merchantProductIds) {
		this.merchantProductIds = merchantProductIds;
	}

	public Map<String, Map<String, Long>> getFacetResults() {
		return facetResults;
	}

	public void setFacetResults(Map<String, Map<String, Long>> facetResults) {
		this.facetResults = facetResults;
	}

	public List<AttributeResult> getAttributeResult() {
		return attributeResult;
	}

	public void setAttributeResult(List<AttributeResult> attributeResult) {
		this.attributeResult = attributeResult;
	}

	public List<CategoryTreeResult> getCategoryTreeResult() {
		return categoryTreeResult;
	}

	public void setCategoryTreeResult(List<CategoryTreeResult> categoryTreeResult) {
		this.categoryTreeResult = categoryTreeResult;
	}


	public List<BrandResult> getBrandResult() {
		return brandResult;
	}

	public void setBrandResult(List<BrandResult> brandResult) {
		this.brandResult = brandResult;
	}

	public List<MerchantProduct> getMerchantProductResult() {
		return merchantProductResult;
	}

	public List<String> getHotwordsRecommended() {
		return hotwordsRecommended;
	}

	public void setHotwordsRecommended(List<String> hotwordsRecommended) {
		this.hotwordsRecommended = hotwordsRecommended;
	}

	public ZeroResultRecommendResult getZeroResultRecommendResult() {
		return zeroResultRecommendResult;
	}

	public void setZeroResultRecommendResult(ZeroResultRecommendResult zeroResultRecommendResult) {
		this.zeroResultRecommendResult = zeroResultRecommendResult;
	}

	public void setMerchantProductResult(List<MerchantProduct> merchantProductResult) {
		this.merchantProductResult = merchantProductResult;
	}

	public List<CategoryTreeResult> getNavCategoryTreeResult() {
		return navCategoryTreeResult;
	}

	public void setNavCategoryTreeResult(List<CategoryTreeResult> navCategoryTreeResult) {
		this.navCategoryTreeResult = navCategoryTreeResult;
	}
	
	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	public CacheInfo getCacheInfo() {
		return cacheInfo;
	}

	public void setCacheInfo(CacheInfo cacheInfo) {
		this.cacheInfo = cacheInfo;
	}

	public KeywordResult getKeywordResult() {
		return keywordResult;
	}

	public ShopResult getShopResult() {
		return shopResult;
	}

	public void setShopResult(ShopResult shopResult) {
		this.shopResult = shopResult;
	}

	/*public Map<Long, List<MerProScript>> getSuperScriptMap() {
		return superScriptMap;
	}

	public void setSuperScriptMap(Map<Long, List<MerProScript>> superScriptMap) {
		this.superScriptMap = superScriptMap;
	}*/


	@Override
	public String toString() {
		return "SearchResponse{" +
				"merchantProductIds=" + merchantProductIds +
				", facetResults=" + facetResults +
				", attributeResult=" + attributeResult +
				", categoryTreeResult=" + categoryTreeResult +
				", navCategoryTreeResult=" + navCategoryTreeResult +
				", brandResult=" + brandResult +
				", merchantProductResult=" + merchantProductResult +
				", zeroResultRecommendResult=" + zeroResultRecommendResult +
				", hotwordsRecommended=" + hotwordsRecommended +
				", companyId=" + companyId +
				", totalHit=" + totalHit +
				", costTime=" + costTime +
				", cacheInfo=" + cacheInfo +
				'}';
	}
}
