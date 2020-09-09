package com.odianyun.search.whale.api.model.req;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.odianyun.search.whale.api.model.ManagementType;
import com.odianyun.search.whale.api.model.MerchantProductType;
import com.odianyun.search.whale.api.model.PriceRange;
import com.odianyun.search.whale.api.model.geo.Point;
import com.odianyun.search.whale.api.model.selectionproduct.TypeOfProductFilter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseSearchRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//关键词
	private String keyword;
	
	//平台类目id
	private List<Long> categoryIds;
	
	//导购属性值id,请使用attrItems替代
	@Deprecated
	private List<Long> attrValueIds;
	
	//<导购属性项id, List<导购属性值id>>
	private Map<Long, List<Long>> attrItemValuesMap;
	
	//品牌id
	private List<Long> brandIds;
	
	//覆盖省份
	private List<Integer> coverProvinceIds;
	
	//排序方式,请使用sortTypeList
	@Deprecated
	private SortType sortType;
	//支持单级和多级排序
	private List<SortType> sortTypeList = new ArrayList<SortType>();
	
	//刷选过滤
	private List<FilterType> filterTypes;

	//商家id
	Long merchantId;

	List<Long> merchantIdList = new ArrayList<Long>();
	
	//价格区间(含促销价相关)
	private PriceRange priceRange;

	//原价格区间
	private PriceRange originalPriceRange;
	
	//分页start,第一页为0*count=0，第二页为1*count，第三页2*count
	private int start=0;
	
	//每页的个数，eg:每页为10，第一页为10,第二页依然为10，。。。
	private int count=10;
	
	//公司id，eg:卓仕、宜和等
	private Integer companyId;
	
	//请求时间
	private Date requestTime;
	
	//搜索响应时间
	private long costTime;
	
	//请求类型
	private int requestType = 0;
	
	//互联网搜索用户唯一id
	private String userId;
	//---------------------------------------------------
	//结果集排除mpId
	private List<Long> excludeMpIds;
	//结果集排除merchantId
	private List<Long> excludeMerchantIds;
	//结果集排除前台类目Id
	private List<Long> excludeNavCategoryIds;
	//结果集排除后台类目id
	private List<Long> excludeCategoryIds;
	//结果集排除品牌id
	private List<Long> excludeBrandIds;
	//结果集排除商品类型
	private List<Integer> excludeTypes;

	//-----------------------------------------------------------
	//是否聚类结果
	private boolean isAggregation=true;
    //是否做零少结果处理
	private boolean isZeroResponseHandler=true;
    //是否做热词推荐处理
	private boolean isRecommendWordsHandler=true;
	//-----------------------------------------------------------
	// 商品状态 通过审核 已上架 已下架
	private ManagementType managementState = ManagementType.ON_SHELF;
	// 商品类型
	@Deprecated
	private MerchantProductType merchantProductType;
	// 商品类型
	private Integer type;
	// 商品类型
	private List<Integer> types=new ArrayList<Integer>();

	// 置顶的商品
	private List<Long> topMerchantProductIdList = new ArrayList<Long>();;

	//商品销售区域 扩容
	private List<Long> saleAreaCode;

	//促销Id
	private List<Long> promotionIdList;

	// 促销类型
	private List<Integer> promotionTypeList;

	//ean码，扫码购
	private List<String> eanNos;

	//新品
	private Integer isNew;
	//今日新品
	//private Integer isTodayNew;


	//是否过滤分销商品
	private Boolean isDistributionMp=null;

	//经纬度
	private Point point;

	//分词是否使用智能模式,默认使用智能模式
	private Boolean useSmart = true;
	//分词后的搜索结果取交集还是并集:true 并集,false,交集
	private Boolean isMergeResult = false;
	//分词后准备用来搜索的词源
	private List<String> tokens = new ArrayList<>();

	private TypeOfProductFilter typeOfProductFilter = new TypeOfProductFilter();
	
	private ClientRequestInfo clientInfo = new ClientRequestInfo();
	
	public BaseSearchRequest(Integer companyId){
		this.companyId=companyId;
	}

	public void setSortType(SortType sortType) {
		this.sortType = sortType;
		this.sortTypeList.add(sortType);
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
		this.types.add(type);
	}
	public Boolean getDistributionMp() {
		return isDistributionMp;
	}

	public void setDistributionMp(Boolean distributionMp) {
		isDistributionMp = distributionMp;
	}
	public void setMerchantId(Long merchantId) {
		if(merchantIdList == null){
			merchantIdList = new ArrayList<Long>();
		}
		this.merchantId=merchantId;
		merchantIdList.add(merchantId);
	}
}
