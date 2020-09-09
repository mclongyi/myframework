package com.odianyun.search.whale.api.model.req;

import com.odianyun.search.whale.api.model.geo.Point;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 前台搜索请求
 * 
 * @author zengfenghua
 *
 */
@Data
public class HotSearchRequest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private List<Long> merchantIdList;

	private int start = 0;

	private int count = 10;

	private Integer companyId;

	private Point point;

	public HotSearchRequest(Integer companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public List<Long> getMerchantIdList() {
		return merchantIdList;
	}

	public void setMerchantIdList(List<Long> merchantIdList) {
		this.merchantIdList = merchantIdList;
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

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
}