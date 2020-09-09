package com.odianyun.search.whale.api.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MerchantProduct implements java.io.Serializable{

	public MerchantProduct() {
	}

	/**
	 *
	 */
	//private static final long serialVersionUID = -2575828692563059292L;

	private Long id;

	private Long productId;
	//商品名称，非产品名称

	private String productName;

	private String picUrl;

	private Double price;

	private Double pointPrice;

	private Long ruleId;

	private Integer pointType;

	private Integer priceType;

	private Integer stock;

	//商品类型 eg:(1普通商品、2卡券、5生鲜产品、6增值服务、7其他), 值的实际代表意义请根据表最新数据来判断
	private Integer type;

	//销售类型  eg:(1普通、2海购、3其他), 值的实际代表意义请根据表最新数据来判断
	private Integer sale_type;

	private String tax;

	private Long merchantSeriesId;

	private Long categoryId;

	private Long brandId;

	private String brandName;
	//商品code
	private String code;

	private Long companyId;

	private String subtitle;//副标题(商家自定义名称)

	//////////////////////////////////////
	//商家id
	private Long merchantId;
	//商家名称
	private String merchantName;
	//商家类型 10:自营  11:第三方商户
	private Integer merchantType;
	//店铺id
	private Long shopId;
	//店铺分类，对应shop_type表
	@Deprecated
	private Integer shopType;
	//店铺分类，对应shop_type表,数据库类型变为long型
	private Long newShopType;
	//店铺名称
	private String shopName;
	/////////////////////////////////////////

	//计量单位:kg
	private String calculation_unit;
	//产品规格 5kg/箱
	private String standard;

	//平台类目名称
	private String categoryName;
	//产品编码
	private String productCode;
	//产品ean码
	private String ean_no;

	private String updateTime;

	//商品销数售量
	private Long volume4sale;
	//真实商品销数售量
	private Long realVolume4sale;
	//商品评论数
	private Integer ratingCount;
	//商品评论
	private Double rate;
	//好评数量
	private Integer positiveRate;
	//商品角标list
	private List<MerProScript> merProScripts;

	private Long merchantCategoryId;

	private Integer typeOfProduct;

	private String thirdCode;

	private Map<Integer,List<Long>> promotionRelationMap;
	//对应虚品id
	private Long seriesParentId;
	//对应商家的商品mpId
	private Long refId;
	
	
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

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Integer getShopType() {
		return shopType;
	}

	public void setShopType(Integer shopType) {
		this.shopType = shopType;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getSale_type() {
		return sale_type;
	}

	public void setSale_type(Integer sale_type) {
		this.sale_type = sale_type;
	}

	public Integer getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(Integer merchantType) {
		this.merchantType = merchantType;
	}

	public String getCalculation_unit() {
		return calculation_unit;
	}

	public void setCalculation_unit(String calculation_unit) {
		this.calculation_unit = calculation_unit;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}


	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public void setEan_no(String ean_no) {
		this.ean_no = ean_no;
	}

	public String getEan_no() {
		return ean_no;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
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

	public Integer getPositiveRate() {
		return positiveRate;
	}

	public void setPositiveRate(Integer positiveRate) {
		this.positiveRate = positiveRate;
	}

	public List<MerProScript> getMerProScripts() {
		return merProScripts;
	}

	public void setMerProScripts(List<MerProScript> merProScripts) {
		this.merProScripts = merProScripts;
	}

	public Long getVolume4sale() {
		return volume4sale;
	}

	public void setVolume4sale(Long volume4sale) {
		this.volume4sale = volume4sale;
	}

	public Long getNewShopType() {
		return newShopType;
	}

	public void setNewShopType(Long newShopType) {
		this.newShopType = newShopType;
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

	public Map<Integer, List<Long>> getPromotionRelationMap() {
		return promotionRelationMap;
	}

	public void setPromotionRelationMap(Map<Integer, List<Long>> promotionRelationMap) {
		this.promotionRelationMap = promotionRelationMap;
	}

	public Long getMerchantCategoryId() {
		return merchantCategoryId;
	}

	public void setMerchantCategoryId(Long merchantCategoryId) {
		this.merchantCategoryId = merchantCategoryId;
	}

	public Integer getTypeOfProduct() {
		return typeOfProduct;
	}

	public void setTypeOfProduct(Integer typeOfProduct) {
		this.typeOfProduct = typeOfProduct;
	}

	public String getThirdCode() {
		return thirdCode;
	}

	public void setThirdCode(String thirdCode) {
		this.thirdCode = thirdCode;
	}

	public Long getSeriesParentId() {
		return seriesParentId;
	}

	public void setSeriesParentId(Long seriesParentId) {
		this.seriesParentId = seriesParentId;
	}

	public Double getPointPrice() {
		return pointPrice;
	}

	public void setPointPrice(Double pointPrice) {
		this.pointPrice = pointPrice;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getPointType() {
		return pointType;
	}

	public void setPointType(Integer pointType) {
		this.pointType = pointType;
	}

	public Integer getPriceType() {
		return priceType;
	}

	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
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

	@Override
	public String toString() {
		return "MerchantProduct{" +
				"id=" + id +
				", productId=" + productId +
				", productName='" + productName + '\'' +
				", picUrl='" + picUrl + '\'' +
				", price=" + price +
				", pointPrice=" + pointPrice +
				", ruleId=" + ruleId +
				", pointType=" + pointType +
				", priceType=" + priceType +
				", stock=" + stock +
				", type=" + type +
				", sale_type=" + sale_type +
				", tax='" + tax + '\'' +
				", merchantSeriesId=" + merchantSeriesId +
				", categoryId=" + categoryId +
				", brandId=" + brandId +
				", brandName='" + brandName + '\'' +
				", code='" + code + '\'' +
				", companyId=" + companyId +
				", merchantId=" + merchantId +
				", merchantName='" + merchantName + '\'' +
				", merchantType=" + merchantType +
				", shopId=" + shopId +
				", shopType=" + shopType +
				", newShopType=" + newShopType +
				", shopName='" + shopName + '\'' +
				", calculation_unit='" + calculation_unit + '\'' +
				", standard='" + standard + '\'' +
				", categoryName='" + categoryName + '\'' +
				", productCode='" + productCode + '\'' +
				", ean_no='" + ean_no + '\'' +
				", updateTime='" + updateTime + '\'' +
				", volume4sale=" + volume4sale +
				", realVolume4sale=" + realVolume4sale +
				", ratingCount=" + ratingCount +
				", rate=" + rate +
				", positiveRate=" + positiveRate +
				", merProScripts=" + merProScripts +
				", merchantCategoryId=" + merchantCategoryId +
				", typeOfProduct=" + typeOfProduct +
				", thirdCode='" + thirdCode + '\'' +
				", promotionRelationMap=" + promotionRelationMap +
				", seriesParentId=" + seriesParentId +
				", refId=" + refId +
				'}';
	}
}
