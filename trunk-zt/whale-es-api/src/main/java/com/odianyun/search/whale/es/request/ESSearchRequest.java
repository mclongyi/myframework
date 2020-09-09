package com.odianyun.search.whale.es.request;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;

public class ESSearchRequest {

	private String indexName;
	
	private String type;
	
	private SearchType searchType=SearchType.DFS_QUERY_THEN_FETCH;
	
	private QueryBuilder queryBuilder;
	
	private QueryBuilder aggregationQueryBuilder;
	
	private List<SortBuilder> sortBuilderList;
	
	private FilterBuilder filterBuilder;
	
	private int start=0;
	
	private int count=10;
	
	private int maxStart=10000;
	
	private int maxCount=500;
	
	private boolean explain=false;
	
	private List<String> fields;
	
	private List<String> facet_fields;
	
	private String[] includeFields;
	
	public String[] getIncludeFields() {
		return includeFields;
	}

	public void setIncludeFields(String[] includeFields) {
		this.includeFields = includeFields;
	}

	public ESSearchRequest(String indexName, String type){
		this(indexName,type,null);
	}

	public ESSearchRequest(String indexName, String type,
			QueryBuilder queryBuilder) {
		super();
		this.indexName = indexName;
		this.type = type;
		this.queryBuilder = queryBuilder;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SearchType getSearchType() {
		return searchType;
	}

	public void setSearchType(SearchType searchType) {
		this.searchType = searchType;
	}

	public QueryBuilder getQueryBuilder() {
		return queryBuilder;
	}

	public void setQueryBuilder(QueryBuilder queryBuilder) {
		this.queryBuilder = queryBuilder;
	}

	public QueryBuilder getAggregationQueryBuilder() {
		return aggregationQueryBuilder;
	}

	public void setAggregationQueryBuilder(QueryBuilder aggregationQueryBuilder) {
		this.aggregationQueryBuilder = aggregationQueryBuilder;
	}

	public FilterBuilder getFilterBuilder() {
		return filterBuilder;
	}

	public void setFilterBuilder(FilterBuilder filterBuilder) {
		this.filterBuilder = filterBuilder;
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

	public boolean isExplain() {
		return explain;
	}

	public void setExplain(boolean explain) {
		this.explain = explain;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public List<String> getFacet_fields() {
		return facet_fields;
	}

	public void setFacet_fields(List<String> facet_fields) {
		this.facet_fields = facet_fields;
	}

	public List<SortBuilder> getSortBuilderList() {
		return sortBuilderList;
	}

	public void setSortBuilderList(List<SortBuilder> sortBuilderList) {
		this.sortBuilderList = sortBuilderList;
	}
	   /**
		 * @return the maxStart
		 */
		public int getMaxStart() {
			return maxStart;
		}

		/**
		 * @param maxStart the maxStart to set
		 */
		public void setMaxStart(int maxStart) {
			this.maxStart = maxStart;
		}

		/**
		 * @return the maxCount
		 */
		public int getMaxCount() {
			return maxCount;
		}

		/**
		 * @param maxCount the maxCount to set
		 */
		public void setMaxCount(int maxCount) {
			this.maxCount = maxCount;
		}

}
