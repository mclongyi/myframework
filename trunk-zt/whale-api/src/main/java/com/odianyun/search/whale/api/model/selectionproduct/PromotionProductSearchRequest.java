package com.odianyun.search.whale.api.model.selectionproduct;

import java.util.ArrayList;
import java.util.List;

import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.PriceRange;
import com.odianyun.search.whale.api.model.req.SortType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PromotionProductSearchRequest implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2312313780353972623L;
	
	//eans
	private List<String> eans;
	//商品编码
	private List<String> codes;
	//后台类目
	private List<Long> categoryIds;
	//品牌
	private List<Long> brandIds;
	//商家id
	private List<Long> merchantIds;
	//商家名
	private List<String> merchantNames;
	//公司id
	private Integer companyId;
	//商品名
	private String merchantProductName;
	// 类目名
	private String categoryName;
	// 品牌名
	private String brandName;
	// 需要排除的品牌id
	private List<Long> excludeBrandIdList ;
	// 需要排除的类目id
	private List<Long> excludeCategoryIdList ;
	//产品code
	private List<String> productCodes;
	//产品id
	private List<Long> productIdList;
	// 商品id
	private List<Long> merchantProductIdList;

	private int start = 0;
	
	private int count = 10;
	// 商品状态 通过审核 已上架 已下架
	private ManagementType managementState = ManagementType.ON_SHELF;

	private boolean isCombine = false;

	private TypeOfProductFilter typeOfProductFilter = new TypeOfProductFilter();

	//支持单级和多级排序
	private List<SortType> sortTypeList = new ArrayList<SortType>();

	//父商家id
	private Long parentMerchantId;
	//子商家ids
	private List<Long> subMerchantIds;

	//第三方商品编码
	private List<String> thirdCodes;

	//是否过滤分销商品
	private Boolean isDistributionMp=null;

	//价格区间
	private PriceRange priceRange;

	// 商品类型
	private List<Integer> types=new ArrayList<Integer>();

	//结果集排除商品类型
	private List<Integer> excludeTypes;

	//原价格区间
	private PriceRange originalPriceRange;

	//merchant_type 包含与排除
	private List<Integer> merchantType=new ArrayList<Integer>();
	private List<Integer> excludeMerchantType = new ArrayList<>();



	public PromotionProductSearchRequest(Integer companyId,List<Long> merchantIds){
		this.companyId=companyId;
		this.merchantIds = merchantIds;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setTypeOfProductFilter(TypeOfProductFilter typeOfProductFilter) {
		this.typeOfProductFilter = typeOfProductFilter;
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

	public List<String> getEans() {
		return eans;
	}
	public void setEans(List<String> eans) {
		this.eans = eans;
	}
	public List<String> getCodes() {
		return codes;
	}
	public void setCodes(List<String> codes) {
		this.codes = codes;
	}
	public List<Long> getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(List<Long> categoryIds) {
		this.categoryIds = categoryIds;
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
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	public List<String> getMerchantNames() {
		return merchantNames;
	}
	public void setMerchantNames(List<String> merchantNames) {
		this.merchantNames = merchantNames;
	}
	public String getMerchantProductName() {
		return merchantProductName;
	}
	public void setMerchantProductName(String merchantProductName) {
		this.merchantProductName = merchantProductName;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public void setExcludeBrandIdList(List<Long> excludeBrandIdList) {
		this.excludeBrandIdList = excludeBrandIdList;
	}

	public void setExcludeCategoryIdList(List<Long> excludeCategoryIdList) {
		this.excludeCategoryIdList = excludeCategoryIdList;
	}

	public List<Long> getExcludeBrandIdList() {
		return excludeBrandIdList;
	}

	public List<Long> getExcludeCategoryIdList() {
		return excludeCategoryIdList;
	}

	public List<String> getProductCodes() {
		return productCodes;
	}

	public void setProductCodes(List<String> productCodes) {
		this.productCodes = productCodes;
	}

	public ManagementType getManagementState() {
		return managementState;
	}

	public void setManagementState(ManagementType managementState) {
		this.managementState = managementState;
	}

	public List<SortType> getSortTypeList() {
		return sortTypeList;
	}

	public void setSortTypeList(List<SortType> sortTypeList) {
		this.sortTypeList = sortTypeList;
	}

	public List<Long> getProductIdList() {
		return productIdList;
	}

	public void setProductIdList(List<Long> productIdList) {
		this.productIdList = productIdList;
	}

	public boolean isCombine() {
		return isCombine;
	}

	public void setCombine(boolean combine) {
		isCombine = combine;
	}

	public TypeOfProductFilter getTypeOfProductFilter() {
		return typeOfProductFilter;
	}

	public List<Long> getMerchantProductIdList() {
		return merchantProductIdList;
	}

	public void setMerchantProductIdList(List<Long> merchantProductIdList) {
		this.merchantProductIdList = merchantProductIdList;
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

	public PriceRange getPriceRange() {
		return priceRange;
	}

	public void setPriceRange(PriceRange priceRange) {
		this.priceRange = priceRange;
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
