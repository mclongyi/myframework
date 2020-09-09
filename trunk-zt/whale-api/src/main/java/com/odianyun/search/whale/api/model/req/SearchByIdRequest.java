package com.odianyun.search.whale.api.model.req;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SearchByIdRequest implements Serializable {
	public SearchByIdRequest() {
	}

	private List<Long> mpIds;
	
	private Integer companyId;

	private int start = 0;

	private int count = Integer.MAX_VALUE;

	private List<SortType> sortTypeList = new ArrayList<SortType>();

	private boolean useCache = true;

	public SearchByIdRequest(List<Long> mpIds,Integer companyId){
		this.mpIds = mpIds;
		this.companyId = companyId;
	}
	/**
	 * @return the mpIds
	 */
	public List<Long> getMpIds() {
		return mpIds;
	}

	/**
	 * @param mpIds the mpIds to set
	 */
	public void setMpIds(List<Long> mpIds) {
		this.mpIds = mpIds;
	}
	/**
	 * @return the companyId
	 */
	public Integer getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
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

	public List<SortType> getSortTypeList() {
		return sortTypeList;
	}

	public void setSortTypeList(List<SortType> sortTypeList) {
		this.sortTypeList = sortTypeList;
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	@Override
	public String toString() {
		return "SearchByIdRequest{" +
				"mpIds=" + mpIds +
				", companyId=" + companyId +
				", start=" + start +
				", count=" + count +
				", sortTypeList=" + sortTypeList +
				", useCache=" + useCache +
				'}';
	}
}
