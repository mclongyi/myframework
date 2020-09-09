package com.odianyun.search.whale.es.request;
import java.util.List;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;
/**
 * Created by ody on 2016/8/2.
 */
public class ESAggSearchRequest {




        private String[] indexNames;
        private String type;
        private SearchType searchType = SearchType.DFS_QUERY_THEN_FETCH;
        private QueryBuilder queryBuilder;
        private QueryBuilder aggregationQueryBuilder;
        private List<SortBuilder> sortBuilders;
        private FilterBuilder filterBuilder;
        private int start = 0;

        private int count = 10;

        private boolean explain = false;
        private List<String> fields;
        private List<String> facet_fields;
        private String[] includeFields;
        private List<AbstractAggregationBuilder> aggrBuilders;

        public List<AbstractAggregationBuilder> getAggrBuilders()
        {
            return this.aggrBuilders;
        }

        public void setAggrBuilders(List<AbstractAggregationBuilder> aggrBuilders) {
            this.aggrBuilders = aggrBuilders;
        }

        public String getType()
        {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public SearchType getSearchType() {
            return this.searchType;
        }

        public void setSearchType(SearchType searchType) {
            this.searchType = searchType;
        }

        public QueryBuilder getQueryBuilder() {
            return this.queryBuilder;
        }

        public void setQueryBuilder(QueryBuilder queryBuilder) {
            this.queryBuilder = queryBuilder;
        }

        public QueryBuilder getAggregationQueryBuilder() {
            return this.aggregationQueryBuilder;
        }

        public void setAggregationQueryBuilder(QueryBuilder aggregationQueryBuilder) {
            this.aggregationQueryBuilder = aggregationQueryBuilder;
        }

        public List<SortBuilder> getSortBuilders() {
            return this.sortBuilders;
        }

        public void setSortBuilders(List<SortBuilder> sortBuilders) {
            this.sortBuilders = sortBuilders;
        }

        public FilterBuilder getFilterBuilder() {
            return this.filterBuilder;
        }

        public void setFilterBuilder(FilterBuilder filterBuilder) {
            this.filterBuilder = filterBuilder;
        }

        public int getStart() {
            return this.start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getCount() {
            return this.count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isExplain() {
            return this.explain;
        }

        public void setExplain(boolean explain) {
            this.explain = explain;
        }

        public List<String> getFields() {
            return this.fields;
        }

        public void setFields(List<String> fields) {
            this.fields = fields;
        }

        public List<String> getFacet_fields() {
            return this.facet_fields;
        }

        public void setFacet_fields(List<String> facet_fields) {
            this.facet_fields = facet_fields;
        }

        public String[] getIncludeFields() {
            return this.includeFields;
        }

        public void setIncludeFields(String[] includeFields) {
            this.includeFields = includeFields;
        }

        public ESAggSearchRequest(String[] indexNames, String type) {
            this(indexNames, type, (QueryBuilder)null);
        }

        public ESAggSearchRequest(String[] indexNames, String type, QueryBuilder queryBuilder) {
            this.searchType = SearchType.DFS_QUERY_THEN_FETCH;
            this.start = 0;
            this.count = 10;
            this.explain = false;
            this.indexNames = indexNames;
            this.type = type;
            this.queryBuilder = queryBuilder;
        }

        public String[] getIndexNames() {
            return this.indexNames;
        }

        public void setIndexNames(String[] indexNames) {
            this.indexNames = indexNames;
        }
    }

