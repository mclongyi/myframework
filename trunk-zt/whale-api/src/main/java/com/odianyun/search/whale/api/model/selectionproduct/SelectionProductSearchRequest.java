package com.odianyun.search.whale.api.model.selectionproduct;

import java.util.ArrayList;
import java.util.List;

import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.PriceRange;
import com.odianyun.search.whale.api.model.req.SortType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SelectionProductSearchRequest implements java.io.Serializable{
	public SelectionProductSearchRequest() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7015852031116488738L;
	//关键词
	private String keyword;
	//产品编码
	private List<String> productCodes;
 	//商品编码
	private List<String> codes;
	//后台类目
	private List<Long> categoryIds;
	//商家类目
	private List<Long> merchantCategoryIds;
	//品牌
	private List<Long> brandIds;
	//商家id
	private List<Long> merchantIds;
	//价格区间
	private PriceRange priceRange;
	//商家所属区域,非覆盖区域
	private List<String> areaCodes;
	//公司id
	private Integer companyId;
	
	private String merchantName;
	//父商家关联商品id
	private List<Long> refIds;

	private int start = 0;
	
	private int count = 10;
	
	// 商品状态 通过审核 已上架 已下架
	private ManagementType managementState = ManagementType.ON_SHELF;
	
	//支持单级和多级排序
	private List<SortType> sortTypeList = new ArrayList<SortType>();

	//父商家id
	private Long parentMerchantId;
	//子商家ids
	private List<Long> subMerchantIds;

	//第三方商品编码
	private List<String> thirdCodes;

	private boolean isCombine = false;

    //是否过滤分销商品
	private Boolean isDistributionMp=null;

	// 商品类型
	private List<Integer> types=new ArrayList<Integer>();

	//结果集排除商品类型
	private List<Integer> excludeTypes;

	//原价格区间
	private PriceRange originalPriceRange;

	//merchant_type 包含与排除
	private List<Integer> merchantType=new ArrayList<Integer>();
	private List<Integer> excludeMerchantType = new ArrayList<>();

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<Integer> getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(List<Integer> merchantType) {
		this.merchantType = merchantType;
	}

	public List<Integer> getExcludeMerchantType() {
		return excludeMerchantType;
	}

	public void setExcludeMerchantType(List<Integer> excludeMerchantType) {
		this.excludeMerchantType = excludeMerchantType;
	}

	public boolean isCombine() {
		return isCombine;
	}

	public void setCombine(boolean combine) {
		isCombine = combine;
	}

	public List<SortType> getSortTypeList() {
		return sortTypeList;
	}

	public void setSortTypeList(List<SortType> sortTypeList) {
		this.sortTypeList = sortTypeList;
	}

	public SelectionProductSearchRequest(Integer companyId){
		this.companyId=companyId;
	}
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public List<Long> getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(List<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}
	public PriceRange getPriceRange() {
		return priceRange;
	}
	public void setPriceRange(PriceRange priceRange) {
		this.priceRange = priceRange;
	}
	public List<String> getAreaCodes() {
		return areaCodes;
	}
	public void setAreaCodes(List<String> areaCodes) {
		this.areaCodes = areaCodes;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public List<Long> getBrandIds() {
		return brandIds;
	}
	public void setBrandIds(List<Long> brandIds) {
		this.brandIds = brandIds;
	}
	public List<Long> getMerchantIds() {
		return merchantIds;
	}
	public void setMerchantIds(List<Long> merchantIds) {
		this.merchantIds = merchantIds;
	}
	public List<String> getCodes() {
		return codes;
	}
	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	public List<String> getProductCodes() {
		return productCodes;
	}

	public void setProductCodes(List<String> productCodes) {
		this.productCodes = productCodes;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ManagementType getManagementState() {
		return managementState;
	}

	public void setManagementState(ManagementType managementState) {
		this.managementState = managementState;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public List<Long> getRefIds() {
		return refIds;
	}

	public void setRefIds(List<Long> refIds) {
		this.refIds = refIds;
	}

	public Long getParentMerchantId() {
		return parentMerchantId;
	}

	public void setParentMerchantId(Long parentMerchantId) {
		this.parentMerchantId = parentMerchantId;
	}

	public List<Long> getSubMerchantIds() {
		return subMerchantIds;
	}

	public void setSubMerchantIds(List<Long> subMerchantIds) {
		this.subMerchantIds = subMerchantIds;
	}

	public List<String> getThirdCodes() {
		return thirdCodes;
	}

	public void setThirdCodes(List<String> thirdCodes) {
		this.thirdCodes = thirdCodes;
	}

	public Boolean getDistributionMp() {
		return isDistributionMp;
	}

	public void setDistributionMp(Boolean distributionMp) {
		isDistributionMp = distributionMp;
	}

	public List<Long> getMerchantCategoryIds() {
		return merchantCategoryIds;
	}

	public void setMerchantCategoryIds(List<Long> merchantCategoryIds) {
		this.merchantCategoryIds = merchantCategoryIds;
	}

	public List<Integer> getTypes() {
		return types;
	}

	public void setTypes(List<Integer> types) {
		this.types = types;
	}

	public List<Integer> getExcludeTypes() {
		return excludeTypes;
	}

	public void setExcludeTypes(List<Integer> excludeTypes) {
		this.excludeTypes = excludeTypes;
	}

	public PriceRange getOriginalPriceRange() {
		return originalPriceRange;
	}

	public void setOriginalPriceRange(PriceRange originalPriceRange) {
		this.originalPriceRange = originalPriceRange;
	}
}
