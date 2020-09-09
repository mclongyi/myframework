package com.odianyun.search.whale.data.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MerchantProduct {
	
	private long id;
	
	private long product_id;
	
	private long merchant_id;
	
	private String chinese_name;
	
	private String english_name;
	
	private int is_deleted=0;
	
	private int is_available=1;
	
	private String create_time;
	
	private String code;
	
	private long merchant_cate_tree_node_id;
	
	private String subtitle;//副标题(商家自定义名称)
	
	private int isNew;

	private Date first_shelf_time;//首次上架时间
	
	private long company_id=0;
	
	private Integer type=0;
	
	private String tax="";
	
	private Long merchantSeriesId=0l;
	
	private Boolean isMainSeries;
	
	private Integer sale_type=0;
	
	private Integer managementState=0;

	private Long seriesParentId;

	private Integer typeOfProduct;

	private String thirdCode;
	
	//外卖增加-重量范围最小重量
	private Integer minSize;
	//外卖增加-重量范围最大重量
	private Integer maxSize;

	//外卖增加 20180907
	private Integer merchantType;

	//产地国家
	private String placeOfOrigin;
	//产地国家logo
	private String placeOfOriginLogo;
	//券类型(18-Seibel券 19-虚拟商品券 20伊点卡)
	private Integer cardType;
	//绑定优惠卷/伊点卡id
	private Long cardId;

	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("id", "id");
		resultMap.put("product_id", "product_id");
		resultMap.put("merchant_id", "merchant_id");
		resultMap.put("english_name", "english_name");
		resultMap.put("chinese_name", "chinese_name");
		resultMap.put("is_deleted", "is_deleted");
		resultMap.put("chinese_name", "chinese_name");
		resultMap.put("english_name", "english_name");
		resultMap.put("create_time", "create_time");
		resultMap.put("code", "code");
		resultMap.put("subtitle", "subtitle");
		resultMap.put("isNew", "isNew");
		resultMap.put("first_shelf_time", "first_shelf_time");
		resultMap.put("company_id", "company_id");
		resultMap.put("merchantSeriesId", "merchant_series_id");
		resultMap.put("tax", "tax");
		resultMap.put("type", "type");
		resultMap.put("sale_type", "sale_type");
		resultMap.put("thirdCode", "thirdCode");
		resultMap.put("minSize", "minSize");
		resultMap.put("maxSize", "maxSize");
		resultMap.put("placeOfOrigin", "placeOfOrigin");
		resultMap.put("placeOfOriginLogo", "placeOfOriginLogo");
	}
	public Integer getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(Integer merchantType) {
		this.merchantType = merchantType;
	}
	
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
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(long product_id) {
		this.product_id = product_id;
	}

	public long getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(long merchant_id) {
		this.merchant_id = merchant_id;
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

	public int getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
	}

	public int getIs_available() {
		return is_available;
	}

	public void setIs_available(int is_available) {
		this.is_available = is_available;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getMerchant_cate_tree_node_id() {
		return merchant_cate_tree_node_id;
	}

	public void setMerchant_cate_tree_node_id(long merchant_cate_tree_node_id) {
		this.merchant_cate_tree_node_id = merchant_cate_tree_node_id;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}
	
	public long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(long company_id) {
		this.company_id = company_id;
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

	public Boolean getIsMainSeries() {
		return isMainSeries;
	}

	public void setIsMainSeries(Boolean isMainSeries) {
		this.isMainSeries = isMainSeries;
	}

	public Integer getSale_type() {
		return sale_type;
	}

	public void setSale_type(Integer sale_type) {
		this.sale_type = sale_type;
	}

	@Override
	public String toString() {
		return "MerchantProduct [id=" + id + ", product_id=" + product_id
				+ ", merchant_id=" + merchant_id + ", chinese_name="
				+ chinese_name + ", english_name=" + english_name
				+ ", is_deleted=" + is_deleted + ", is_available="
				+ is_available + ", create_time=" + create_time + ", code="
				+ code + ", merchant_cate_tree_node_id="
				+ merchant_cate_tree_node_id + ", subtitle=" + subtitle
				+ ", isNew=" + isNew + ", company_id=" + company_id + ", type="
				+ type + ", tax=" + tax + ", merchantSeriesId="
				+ merchantSeriesId + ", isMainSeries=" + isMainSeries+ ",minSize=" + minSize + ",maxSize="+maxSize+"]";
	}
	
	public static Map<String, String> getResultmap() {
		return resultMap;
	}

	public Integer getManagementState() {
		return managementState;
	}

	public void setManagementState(Integer managementState) {
		this.managementState = managementState;
	}

	public Long getSeriesParentId() {
		return seriesParentId;
	}

	public void setSeriesParentId(Long seriesParentId) {
		this.seriesParentId = seriesParentId;
	}

	public Integer getTypeOfProduct() {
		return typeOfProduct;
	}

	public void setTypeOfProduct(Integer typeOfProduct) {
		this.typeOfProduct = typeOfProduct;
	}

	public Date getFirst_shelf_time() {
		return first_shelf_time;
	}

	public void setFirst_shelf_time(Date first_shelf_time) {
		this.first_shelf_time = first_shelf_time;
	}

	public String getThirdCode() {
		return thirdCode;
	}

	public void setThirdCode(String thirdCode) {
		this.thirdCode = thirdCode;
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
