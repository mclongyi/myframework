package com.odianyun.search.whale.index.common;

/**
 * Created by cuikai on 16/6/21.
 */
public class SparkJobConf {

    private int companyId;
    private String indexName;
    private String indexVersion;
    private String indexType;
    
    public SparkJobConf() {
    }

    public SparkJobConf(int companyId, String indexName, String indexType) {
        this.companyId = companyId;
        this.indexName = indexName;
        this.indexType = indexType;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexVersion() {
        return indexVersion;
    }

    public void setIndexVersion(String indexVersion) {
        this.indexVersion = indexVersion;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

}
