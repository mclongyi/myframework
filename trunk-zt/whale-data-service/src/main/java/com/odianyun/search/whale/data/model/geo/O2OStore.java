package com.odianyun.search.whale.data.model.geo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.elasticsearch.common.geo.GeoPoint;

/**
 * O2O地理定位时索引的门店数据对象
 * @author yuqian
 *
 */
public class O2OStore {
	
	//需要保存的表名，作为参数传入sql
	private String tableName;

	private Long merchantId;
	
	private Long company_id;
	
	private Long shopId;
	
	private String location;//坐标
	
	private String polygon; //覆盖范围

	private String codeSearch;

	private String tag_words;//店铺名称  分词

	private Integer merchantFlag;//是否有门店 0:商家 1:门店

	private Integer businessState;

	private List<BusinessTime> businessTimes;

	private Integer hasInSiteService;

	private Long parentId;

	private String areaCodes;

	private Integer merchantType;

	public static final Map<String, String> resultMap = new HashMap<String, String>();
	
	static{
		resultMap.put("merchantId" ,"merchantId" );
        resultMap.put("shopId" ,"shopId" );
        resultMap.put("location" ,"location" );
        resultMap.put("polygon" ,"polygon" );
        resultMap.put("company_id","company_id");
		resultMap.put("codeSearch" ,"codeSearch" );
		resultMap.put("tag_words","tag_words");
		resultMap.put("merchantFlag","merchantFlag");
		resultMap.put("merchantType","merchantType");
	}
	
	public Integer getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(Integer merchantType) {
		this.merchantType = merchantType;
	}
	
	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPolygon() {
		return polygon;
	}

	public void setPolygon(String polygon) {
		this.polygon = polygon;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public static Map<String, String> getResultmap() {
		return resultMap;
	}

	public String getTag_words() {
		return tag_words;
	}

	public void setTag_words(String tag_words) {
		this.tag_words = tag_words;
	}

	public String getCodeSearch() {
		return codeSearch;
	}

	public void setCodeSearch(String codeSearch) {
		this.codeSearch = codeSearch;
	}

	public void setMerchantFlag(Integer merchantFlag) {
		this.merchantFlag = merchantFlag;
	}

	public Integer getMerchantFlag() {
		return merchantFlag;
	}

	public List<BusinessTime> getBusinessTimes() {
		return businessTimes;
	}

	public void setBusinessTimes(List<BusinessTime> businessTimes) {
		this.businessTimes = businessTimes;
	}

	public Integer getBusinessState() {
		return businessState;
	}

	public void setBusinessState(Integer businessState) {
		this.businessState = businessState;
	}

	public Integer getHasInSiteService() {
		return hasInSiteService;
	}

	public void setHasInSiteService(Integer hasInSiteService) {
		this.hasInSiteService = hasInSiteService;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getAreaCodes() {
		return areaCodes;
	}

	public void setAreaCodes(String areaCodes) {
		this.areaCodes = areaCodes;
	}
}
