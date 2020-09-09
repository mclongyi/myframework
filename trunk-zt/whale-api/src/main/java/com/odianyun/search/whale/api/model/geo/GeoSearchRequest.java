package com.odianyun.search.whale.api.model.geo;

import com.odianyun.search.whale.api.model.req.MerchantFilterType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 地理位置搜索请求
 * 
 * @author zengfenghua
 *
 */
@Data
@NoArgsConstructor
public class GeoSearchRequest implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//经纬度
	private Point point;
	//地址，eg:上海市浦东新区张江路9999号
	private String address;
	//位置对于的区域信息
	private Area area;
	//公司id
	private Integer companyId;
	//返回个数,默认返回所有
	private Integer num=4;

	//门店标签code
	private String merchantCode;

	private int start = 0;

	private int count = 10;
	//排序方式，默认为距离排序
	private SortType sortType=SortType.DISTANCE;
	/**
	 * keyword和merchantType暂时为保留字段
	 */
	private String keyword;
	
	private String merchantType;

	// 是否过滤歇业商家
	private List<MerchantFilterType> filterList = new ArrayList<MerchantFilterType>();


	public GeoSearchRequest(Point point, Integer companyId) {
		super();
		this.point=point;
		this.companyId = companyId;
	}

	public GeoSearchRequest(String address, Integer companyId) {
		super();
		this.address = address;
		this.companyId = companyId;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public SortType getSortType() {
		return sortType;
	}

	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public static enum SortType{
		DISTANCE
	   //,RATING
	}

	public List<MerchantFilterType> getFilterList() {
		return filterList;
	}

	public void setFilterList(List<MerchantFilterType> filterList) {
		this.filterList = filterList;
	}
	

}
