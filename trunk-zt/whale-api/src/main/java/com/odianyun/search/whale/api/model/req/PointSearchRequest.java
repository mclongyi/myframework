package com.odianyun.search.whale.api.model.req;

import com.odianyun.search.whale.api.model.PointNumRange;
import lombok.Data;

import java.util.List;

/**
 * 积分商城搜索请求
 * 
 * @author ody
 *
 */
@Data
public class PointSearchRequest extends SearchRequest{

	public PointSearchRequest() {
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private List<PointType> pointTypeList;

	private PointNumRange pointNumRange;

	public PointSearchRequest(Integer companyId) {
		super(companyId);
	}

	public List<PointType> getPointTypeList() {
		return pointTypeList;
	}

	public void setPointTypeList(List<PointType> pointTypeList) {
		this.pointTypeList = pointTypeList;
	}

	public PointNumRange getPointNumRange() {
		return pointNumRange;
	}

	public void setPointNumRange(PointNumRange pointNumRange) {
		this.pointNumRange = pointNumRange;
	}
}
