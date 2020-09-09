package com.odianyun.search.whale.data.model;

import com.odianyun.search.whale.index.api.common.IndexConstants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BusinessProduct {

	private Long id;

	private Long productId;

	private String tag_words;

	private String categoryId_search;

	private Long categoryId;

	private String navCategoryId_search;

	private String categoryName_search;

	private String categoryNameBuff;

	private Long brandId;

	private String brandId_search;

	private String brandName_search;

	//导购属性和系列属性id
	private Set<Long> attrValueIds=new HashSet<Long>();
	//导购属性和系列属性ID--拼接字符串
	private String attrValueId_search;
	//导购属性和系列属性值
	private Set<String> attrValueSet=new HashSet<String>();
	//导购属性和系列属性值--拼接字符串
	private String attrValue_search;

	private Long merchantId;

	private String coverProvinceId;

	private Integer is_deleted;

	private String create_time;

	private Double price;

	//原价
	private Double orgPrice;

	private String ean_no;

	private String merchantName_search;

	private String merchantCategoryId_search;

	private Integer isNew;

	private Integer stock=0;

	private Integer hasPic=IndexConstants.HAS_PIC;

	private String chinese_name;

	private String english_name;

	private Integer is_available;

	private String code;

	private String productCode;

	private List<Long> merchant_cate_tree_node_ids;

//	private Long categoryTreeNodeId;

	private String subtitle;//副标题(商家自定义名称)

	private Long companyId;

	private String merchant_categoryId;

	private String picUrl;

	private Integer type;

	private String tax;

	private Long merchantSeriesId;

	private String first_shelf_time;//首次上架时间

	private String seriesAttrValueIdSearch;
	//普通商品 默认值-1  系列品主品为1  非主品为0
	private Integer isMainSeries = -1;
	// 默认值-1  0:普通商品;1:系列主码(会取消);2:系列子码;3:系列虚码
	// 区别于isMainSeries 不考虑库存 是否上架  是商品原本的系列品属性
	//值等于 typeOfProduct
//	private Integer isMain = -1;
	private Integer typeOfProduct;

	private Integer merchantType;

	//商品销售数量
	private Long volume4sale = 0l;

	//真实商品销售数量
	private Long realVolume4sale = 0l;

	//季节性权重
	private int seasonWeight=0;

	//计量单位:5kg/箱
	private String calculation_unit;

	//产品规格
	private String standard;

	//商家所属的四级区域code
	private String areaCode;

	private Integer sale_type;

	//商家状态，本来便利o2o的上架商品对应的商家还需要是审核通过的才行，
	//b2c等不需要考虑
	private boolean merchant_status=true;

	private String updateTime;

	//好评率
	private Integer positiveRate = 0;
	//评分
	private Double rate = 0d;
	//评论数
	private Integer ratingCount = 0;

	private Integer managementState = 0;
	// 促销id
	private String promotionId_search;

	// 促销类型
	private String promotionType_search;


	private Set<String> subCodeSet = new HashSet<>();

	private String scriptIds;//角标ids
	//商品销售区域
	private String saleAreaCodes;
	//商品销售区域父区域
	private String searchAreaCodes;

	//父商家商品id
	private Long refId;

	//父商家id
	private Long parentMerchantId;

	//子商家id
	private String subMerchantIds;

	//第三方商品编码
	private String thirdCode;

	//是否是分销商品
	private int isDistributionMp=0;
	//商品历史分佣总金额
	private double commodityCommission=0;

	//好评数量
	private Integer positiveCount;

	private List<PointsMallProduct> pointsMallProductList;
	
	//父品id-外卖新增
	private Long seriesParentId;

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
	
	public Long getSeriesParentId() {
		return seriesParentId;
	}

	public void setSeriesParentId(Long seriesParentId) {
		this.seriesParentId = seriesParentId;
	}

	public Integer getManagementState() {
		return managementState;
	}

	public void setManagementState(Integer managementState) {
		this.managementState = managementState;
	}

	public String getMerchant_categoryId() {
		return merchant_categoryId;
	}

	public void setMerchant_categoryId(String merchant_categoryId) {
		this.merchant_categoryId = merchant_categoryId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getTag_words() {
		return tag_words;
	}

	public void setTag_words(String tag_words) {
		this.tag_words = tag_words;
	}

	/*public Long getCategoryTreeNodeId() {
		return categoryTreeNodeId;
	}

	public void setCategoryTreeNodeId(Long categoryTreeNodeId) {
		this.categoryTreeNodeId = categoryTreeNodeId;
	}*/

	public String getCategoryId_search() {
		return categoryId_search;
	}

	public void setCategoryId_search(String categoryId_search) {
		this.categoryId_search = categoryId_search;
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

	public String getAttrValueId_search() {
		return attrValueId_search;
	}

	public void setAttrValueId_search(String attrValueId_search) {
		this.attrValueId_search = attrValueId_search;
	}

	public String getAttrValue_search() {
		return attrValue_search;
	}

	public void setAttrValue_search(String attrValue_search) {
		this.attrValue_search = attrValue_search;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getCoverProvinceId() {
		return coverProvinceId;
	}

	public void setCoverProvinceId(String coverProvinceId) {
		this.coverProvinceId = coverProvinceId;
	}

	public Integer getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(Integer is_deleted) {
		this.is_deleted = is_deleted;
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

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getChinese_name() {
		return chinese_name;
	}

	public void setChinese_name(String chinese_name) {
		this.chinese_name = chinese_name;
	}

	public String getEnglish_name() {
		return english_name;
	}

	public void setEnglish_name(String english_name) {
		this.english_name = english_name;
	}

	public Integer getIs_available() {
		return is_available;
	}

	public void setIs_available(Integer is_available) {
		this.is_available = is_available;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public List<Long> getMerchant_cate_tree_node_ids() {
		return merchant_cate_tree_node_ids;
	}

	public void setMerchant_cate_tree_node_ids(List<Long> merchant_cate_tree_node_ids) {
		this.merchant_cate_tree_node_ids = merchant_cate_tree_node_ids;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
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

	public Integer getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(Integer merchantType) {
		this.merchantType = merchantType;
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

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public Integer getSale_type() {
		return sale_type;
	}

	public void setSale_type(Integer sale_type) {
		this.sale_type = sale_type;
	}

	public String getSeriesAttrValueIdSearch() {
		return seriesAttrValueIdSearch;
	}

	public void setSeriesAttrValueIdSearch(String seriesAttrValueIdSearch) {
		this.seriesAttrValueIdSearch = seriesAttrValueIdSearch;
	}

	public boolean isMerchant_status() {
		return merchant_status;
	}

	public void setMerchant_status(boolean merchant_status) {
		this.merchant_status = merchant_status;
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

	public Integer getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(Integer ratingCount) {
		this.ratingCount = ratingCount;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Set<String> getAttrValueSet() {
		return attrValueSet;
	}

	public Set<Long> getAttrValueIds() {
		return attrValueIds;
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

	public int getSeasonWeight() {
		return seasonWeight;
	}

	public void setSeasonWeight(int seasonWeight) {
		this.seasonWeight = seasonWeight;
	}
	/**
	 * @return the subCodeSet
	 */
	public Set<String> getSubCodeSet() {
		return subCodeSet;
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

	public List<PointsMallProduct> getPointsMallProductList() {
		return pointsMallProductList;
	}

	public void setPointsMallProductList(List<PointsMallProduct> pointsMallProductList) {
		this.pointsMallProductList = pointsMallProductList;
	}

	public String getCategoryNameBuff() {
		return categoryNameBuff;
	}

	public void setCategoryNameBuff(String categoryNameBuff) {
		this.categoryNameBuff = categoryNameBuff;
	}

	public Double getOrgPrice() {
		return orgPrice;
	}

	public void setOrgPrice(Double orgPrice) {
		this.orgPrice = orgPrice;
	}
	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
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

	public Integer getPositiveCount() {
		return positiveCount;
	}

	public void setPositiveCount(Integer positiveCount) {
		this.positiveCount = positiveCount;
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
