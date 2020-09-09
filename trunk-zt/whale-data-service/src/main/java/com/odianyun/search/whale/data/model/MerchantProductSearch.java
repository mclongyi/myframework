package com.odianyun.search.whale.data.model;

/**
 * 索引里用到的商品数据对象
 * @author yuqian
 *
 */
public class MerchantProductSearch {

	/**
	 * 持久化数据库时写入的表名
	 */
	private String tableName;

	private Long id;

	private Long productId;

	private String tag_words;

	private String categoryId_search;

	private Long categoryId;

	private String navCategoryId_search;

	private String categoryName_search;

	private String brandId_search;

	private String brandName_search;

	private String attrValueId_search;

	private String attrValue_search;

	private Long merchantId;

	private String coverProvinceId;

	private Integer is_deleted=0;

	private String create_time;

	private Double price;

	private Double orgPrice;

	private String ean_no;

	//商品code
	private String code;

	private String productCode;

	private String merchantName_search;

	private String merchantCategoryId_search;

	private Integer isNew;

	private Integer compositeSort;

	private Integer hasPic;

	private Integer stock;

	private Integer merchantType;//10:自营，11：第三方

	private String shopId;

	private String merchant_categoryId;

	private Long companyId;

	private String picUrl;

	private String productName;

	private Integer type;

	private String tax;

	private Long merchantSeriesId;

	private Integer isMainSeries;
	//普通商品 默认值-1  系列品主品为1  非主品为0 区别于isMainSeries 不考虑库存 是否上架  是商品原本的系列品属性
	private Integer typeOfProduct;

	private String seriesAttrValueIdSearch;

	//商品销数售量
	private Long volume4sale;

	//真实商品销售数量
	private Long realVolume4sale;

	//季节权重
	private int seasonWeight;

	//计量单位:5kg/箱
	private String calculation_unit;

	private String areaCode; //商家所属的四级区域code

	private Integer sale_type;

	//产品规格 
	private String standard;

	private String updateTime;

	//好评率
	private Integer positiveRate;
	//评分
	private Double rate;
	//评论数
	private Integer ratingCount;
	//是否上架
	private Integer managementState=0;
	// 促销id

	private String promotionId_search;

	// 促销类型
	private String promotionType_search;
	//首次上架时间
	private String first_shelf_time;

	private String scriptIds;

	private String saleAreaCodes;

	private String searchAreaCodes;
	//父商家商品id
	private Long refId_search;

	//父商家id
	private Long parentMerchantId;

	//子商家ids
	private String subMerchantIds;

	//第三方商品编码
	private String thirdCode;

	//是否是分销商品
	private int isDistributionMp=0;
	//商品历史分佣总金额
	private double commodityCommission=0;

	// 积分商城商品信息
	// 开始时间
	private String startTime;
	// 结束时间
	private String endTime;
	// 积分兑换 | 积分换购 类型
	private Integer priceType;
	// 积分价
	private Double pointPrice;
	// 商品 | 抵用券 类型
	private Integer pointType;

	// 积分规则是否默认
	private Integer isDefault;

	// 积分规则id
	private Long ruleId;
	
    //重量范围最小重量-外卖新增
	private Integer minSize;
	//重量范围最大重量-外卖新增
	private Integer maxSize;
	//产地国家
	private String placeOfOrigin;
	//产地国家logo
	private String placeOfOriginLogo;
	//券类型(18-Seibel券 19-虚拟商品券 20伊点卡)
	private Integer cardType;
	//绑定优惠卷/伊点卡id
	private Long cardId;

	private String subtitle;//副标题(商家自定义名称)

	public Integer getMinSize() {
		return minSize;
	}

	public void setMinSize(Integer minSize) {
		this.minSize = minSize;
	}

	public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTag_words() {
		return tag_words;
	}

	public void setTag_words(String tag_words) {
		this.tag_words = tag_words;
	}

	public String	getCategoryId_search() {
		return categoryId_search;
	}

	public void setCategoryId_search(String categoryId_search) {
		this.categoryId_search = categoryId_search;
	}

	public String getCategoryName_search() {
		return categoryName_search;
	}

	public void setCategoryName_search(String categoryName_search) {
		this.categoryName_search = categoryName_search;
	}

	public String getBrandId_search() {
		return brandId_search;
	}

	public void setBrandId_search(String brandId_search) {
		this.brandId_search = brandId_search;
	}

	public String getBrandName_search() {
		return brandName_search;
	}

	public void setBrandName_search(String brandName_search) {
		this.brandName_search = brandName_search;
	}

	public String getCoverProvinceId() {
		return coverProvinceId;
	}

	public void setCoverProvinceId(String coverProvinceId) {
		this.coverProvinceId = coverProvinceId;
	}


	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(Integer is_deleted) {
		this.is_deleted = is_deleted;
	}

	public String getAttrValueId_search() {
		return attrValueId_search;
	}

	public void setAttrValueId_search(String attrValueId_search) {
		this.attrValueId_search = attrValueId_search;
	}

	public String getAttrValue_search() {
		return attrValue_search;
	}

	public void setAttrValue_search(String attrValueName_search) {
		this.attrValue_search = attrValueName_search;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getNavCategoryId_search() {
		return navCategoryId_search;
	}

	public void setNavCategoryId_search(String navCategoryId_search) {
		this.navCategoryId_search = navCategoryId_search;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setEan_no(String ean_no) {
		this.ean_no = ean_no;
	}

	public String getEan_no() {
		return ean_no;
	}

	public String getMerchantName_search() {
		return merchantName_search;
	}

	public void setMerchantName_search(String merchantName_search) {
		this.merchantName_search = merchantName_search;
	}

	public String getMerchant_categoryId() {
		return merchant_categoryId;
	}

	public void setMerchant_categoryId(String merchant_categoryId) {
		this.merchant_categoryId = merchant_categoryId;
	}

	public String getMerchantCategoryId_search() {
		return merchantCategoryId_search;
	}

	public void setMerchantCategoryId_search(String merchantCategoryId_search) {
		this.merchantCategoryId_search = merchantCategoryId_search;
	}

	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}

	public Integer getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(Integer merchantType) {
		this.merchantType = merchantType;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public Long getMerchantSeriesId() {
		return merchantSeriesId;
	}

	public void setMerchantSeriesId(Long merchantSeriesId) {
		this.merchantSeriesId = merchantSeriesId;
	}

	public Integer getIsMainSeries() {
		return isMainSeries;
	}

	public void setIsMainSeries(Integer isMainSeries) {
		this.isMainSeries = isMainSeries;
	}

	public Long getVolume4sale() {
		return volume4sale;
	}

	public void setVolume4sale(Long volume4sale) {
		this.volume4sale = volume4sale;
	}

	public String getCalculation_unit() {
		return calculation_unit;
	}

	public void setCalculation_unit(String calculation_unit) {
		this.calculation_unit = calculation_unit;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Integer getSale_type() {
		return sale_type;
	}

	public void setSale_type(Integer sale_type) {
		this.sale_type = sale_type;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getSeriesAttrValueIdSearch() {
		return seriesAttrValueIdSearch;
	}
	public void setSeriesAttrValueIdSearch(String seriesAttrValueIdSearch) {
		this.seriesAttrValueIdSearch = seriesAttrValueIdSearch;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}



	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getPositiveRate() {
		return positiveRate;
	}

	public void setPositiveRate(Integer positiveRate) {
		this.positiveRate = positiveRate;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Integer getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(Integer ratingCount) {
		this.ratingCount = ratingCount;
	}

	public Integer getManagementState() {
		return managementState;
	}

	public void setManagementState(Integer managementState) {
		this.managementState = managementState;
	}

	/**
	 * @return the realVolume4sale
	 */
	public Long getRealVolume4sale() {
		return realVolume4sale;
	}

	/**
	 * @param realVolume4sale the realVolume4sale to set
	 */
	public void setRealVolume4sale(Long realVolume4sale) {
		this.realVolume4sale = realVolume4sale;
	}

	public int getSeasonWeight() {
		return seasonWeight;
	}

	public void setSeasonWeight(int seasonWeight) {
		this.seasonWeight = seasonWeight;
	}
	/**
	 * @return the compositeSort
	 */
	public Integer getCompositeSort() {
		return compositeSort;
	}

	/**
	 * @param compositeSort the compositeSort to set
	 */
	public void setCompositeSort(Integer compositeSort) {
		this.compositeSort = compositeSort;
	}
	/**
	 * @return the hasPic
	 */
	public Integer getHasPic() {
		return hasPic;
	}

	/**
	 * @param hasPic the hasPic to set
	 */
	public void setHasPic(Integer hasPic) {
		this.hasPic = hasPic;
	}

	/**
	 * @return the stock
	 */
	public Integer getStock() {
		return stock;
	}

	/**
	 * @param stock the stock to set
	 */
	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getPromotionId_search() {
		return promotionId_search;
	}

	public void setPromotionId_search(String promotionId_search) {
		this.promotionId_search = promotionId_search;
	}

	public String getPromotionType_search() {
		return promotionType_search;
	}

	public void setPromotionType_search(String promotionType_search) {
		this.promotionType_search = promotionType_search;
	}

	public Integer getTypeOfProduct() {
		return typeOfProduct;
	}

	public void setTypeOfProduct(Integer typeOfProduct) {
		this.typeOfProduct = typeOfProduct;
	}

	public String getScriptIds() {
		return scriptIds;
	}

	public void setScriptIds(String scriptIds) {
		this.scriptIds = scriptIds;
	}

	public String getSaleAreaCodes() {
		return saleAreaCodes;
	}

	public void setSaleAreaCodes(String saleAreaCodes) {
		this.saleAreaCodes = saleAreaCodes;
	}

	public String getSearchAreaCodes() {
		return searchAreaCodes;
	}

	public void setSearchAreaCodes(String searchAreaCodes) {
		this.searchAreaCodes = searchAreaCodes;
	}

	public String getFirst_shelf_time() {
		return first_shelf_time;
	}

	public void setFirst_shelf_time(String first_shelf_time) {
		this.first_shelf_time = first_shelf_time;
	}

	public String getThirdCode() {
		return thirdCode;
	}

	public void setThirdCode(String thirdCode) {
		this.thirdCode = thirdCode;
	}

	public int getIsDistributionMp() {
		return isDistributionMp;
	}

	public void setIsDistributionMp(int isDistributionMp) {
		this.isDistributionMp = isDistributionMp;
	}

	public double getCommodityCommission() {
		return commodityCommission;
	}

	public void setCommodityCommission(double commodityCommission) {
		this.commodityCommission = commodityCommission;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getPriceType() {
		return priceType;
	}

	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}

	public Double getPointPrice() {
		return pointPrice;
	}

	public void setPointPrice(Double pointPrice) {
		this.pointPrice = pointPrice;
	}

	public Integer getPointType() {
		return pointType;
	}

	public void setPointType(Integer pointType) {
		this.pointType = pointType;
	}

	public Double getOrgPrice() {
		return orgPrice;
	}

	public void setOrgPrice(Double orgPrice) {
		this.orgPrice = orgPrice;
	}

	public Long getRefId_search() {
		return refId_search;
	}

	public void setRefId_search(Long refId_search) {
		this.refId_search = refId_search;
	}

	public Long getParentMerchantId() {
		return parentMerchantId;
	}

	public void setParentMerchantId(Long parentMerchantId) {
		this.parentMerchantId = parentMerchantId;
	}

	public String getSubMerchantIds() {
		return subMerchantIds;
	}

	public void setSubMerchantIds(String subMerchantIds) {
		this.subMerchantIds = subMerchantIds;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;

	}

	public String getPlaceOfOrigin() {
		return placeOfOrigin;
	}

	public void setPlaceOfOrigin(String placeOfOrigin) {
		this.placeOfOrigin = placeOfOrigin;
	}

	public String getPlaceOfOriginLogo() {
		return placeOfOriginLogo;
	}

	public void setPlaceOfOriginLogo(String placeOfOriginLogo) {
		this.placeOfOriginLogo = placeOfOriginLogo;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}
}
