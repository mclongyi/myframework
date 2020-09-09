package com.odianyun.search.whale.api.model.series;

import com.odianyun.search.whale.api.model.ManagementType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeriesRequest implements java.io.Serializable{
	
	private Long seriesId;
	
	private Integer companyId;
	
	// 商品状态 通过审核 已上架 已下架
	private ManagementType managementState = ManagementType.ON_SHELF;

	public SeriesRequest(Long seriesId, Integer companyId) {
		super();
		this.seriesId = seriesId;
		this.companyId = companyId;
	}

	public Long getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(Long seriesId) {
		this.seriesId = seriesId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public ManagementType getManagementState() {
		return managementState;
	}

	public void setManagementState(ManagementType managementState) {
		this.managementState = managementState;
	}

	@Override
	public String toString() {
		return "SeriesRequest [seriesId=" + seriesId + ", companyId="
				+ companyId + "]";
	}
	

}
